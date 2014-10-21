package grawlix.freesound.Fragments;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import grawlix.freesound.FreesoundAPI.FreesoundClient;
import grawlix.freesound.R;
import grawlix.freesound.Resources.Result;
import grawlix.freesound.Resources.SearchText;
import grawlix.freesound.Services.MusicService;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by luismierez on 10/11/14.
 */
public class LocationSoundFragment extends Fragment implements
                                                    GooglePlayServicesClient.ConnectionCallbacks,
                                                    GooglePlayServicesClient.OnConnectionFailedListener,
                                                    GoogleMap.OnMarkerDragListener{

    MapView mapView;
    GoogleMap googleMap;

    LocationClient locationClient;
    Location currentLocation;

    MusicService musicService;
    Intent playIntent;
    String soundUrl = "";
    private Map<Marker, Result> markerResultHashMap;

    private Circle circle;

    // Connect to the service
    private ServiceConnection musicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            musicService = binder.getService();

            musicService.setSongUrl(soundUrl);

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationClient = new LocationClient(getActivity(), this, this);
        markerResultHashMap = new HashMap<Marker, Result>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.sound_location_fragment, container, false);

        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) v.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);

        // Gets the GoogleMap from the MapView and does initialization stuff
        googleMap = mapView.getMap();
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.setMyLocationEnabled(true);
        googleMap.setInfoWindowAdapter(new MarkerInfoWindowAdapter());

        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        MapsInitializer.initialize(this.getActivity());


        return v;
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        if (musicService!=null) {
            musicService.pausePlayer();
            getActivity().stopService(new Intent(getActivity(), MusicService.class));
            getActivity().unbindService(musicConnection);

        }
        //musicService.unbindService(musicConnection);
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Connect the client
        locationClient.connect();
        if (playIntent==null) {
            playIntent = new Intent(getActivity(), MusicService.class);
            getActivity().bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            getActivity().startService(playIntent);
        }
    }

    @Override
    public void onStop() {
        // Disconnecting the client invalidates it.
        locationClient.disconnect();
        musicService.pausePlayer();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {
        // Display the connection status
        Toast.makeText(getActivity(), "Connected", Toast.LENGTH_SHORT).show();
        currentLocation = locationClient.getLastLocation();
        LatLng current = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        // Updates the location and zoom of the MapView
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(current, 10);
        googleMap.animateCamera(cameraUpdate);

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //Log.d("Clicked Marker", marker.getTitle());
                if (!marker.getTitle().equals("You")) {
                    Result result = markerResultHashMap.get(marker);
                    musicService.setSongUrl(result.getPreviews().getPreviewHqMp3());
                    musicService.playSong();
                }
                return false;
            }
        });

        final MarkerOptions currentMarkerOptions = new MarkerOptions().position(current).draggable(true)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        googleMap.addMarker(currentMarkerOptions).setTitle("You");

        CircleOptions circleOptions = new CircleOptions().center(current).radius(1000000);
        circle = googleMap.addCircle(circleOptions);
        geoSearch(currentLocation);

        googleMap.setOnMarkerDragListener(this);

    }

    void consumeApi(SearchText searchText) {
        List<Result> resultList = searchText.getResults();
        if (resultList != null) {
            for (Result result : resultList) {
                String geotag = result.getGeoTag();
                String[] latlng = geotag.split(" ");
                Marker currentMarker = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.valueOf(latlng[0]), Double.valueOf(latlng[1])))
                .title(result.getName()));

                markerResultHashMap.put(currentMarker, result);
            }
        }
    }

    private void geoSearch(Location location) {
        String query = "{!geofilt sfield=geotag pt=" +
                location.getLatitude() +"," + location.getLongitude() + " d=" +
                1000 + "}";

        FreesoundClient.getFreesoundApiClient().geoSearch(query, new Callback<SearchText>() {
            @Override
            public void success(SearchText searchText, Response response) {
                consumeApi(searchText);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    @Override
    public void onDisconnected() {
        // Display the connection status
        Toast.makeText(getActivity(), "Disconnected. Please re-connect", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        Iterator<Map.Entry<Marker, Result>> iterator = markerResultHashMap.entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry<Marker, Result> entry = iterator.next();
            if (entry != null) {
                entry.getKey().remove();
            }
            iterator.remove();
        }


    }

    @Override
    public void onMarkerDrag(Marker marker) {
        circle.setCenter(marker.getPosition());
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        Location loc = new Location("Test");
        loc.setLatitude(marker.getPosition().latitude);
        loc.setLongitude(marker.getPosition().longitude);
        geoSearch(loc);
    }

    public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(final Marker marker) {
            View v = getActivity().getLayoutInflater().inflate(R.layout.infowindow_layout, null);
            ImageView markerIcon = (ImageView) v.findViewById(R.id.marker_icon);
            TextView markerText = (TextView) v.findViewById(R.id.marker_label);

            if (!marker.getTitle().equals("You")) {
                Result result = markerResultHashMap.get(marker);
                markerText.setText(result.getName());
                Picasso.with(getActivity()).load(result.getImages().getWaveformM()).fit().into(markerIcon, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        marker.showInfoWindow();
                    }

                    @Override
                    public void onError() {

                    }
                });
            } else {
                markerText.setText("You");
            }
            return v;
        }
    }

    private double Distance(double start_lat, double start_long, double end_lat, double end_long) {
        double R = 6371; // Km
        double start_rad_lat = Math.toRadians(start_lat);
        double end_rad_lat = Math.toRadians(end_lat);
        double delta_long_rad = Math.toRadians(end_long - start_long);

        double distance = Math.acos( Math.sin(start_rad_lat)*Math.sin(end_rad_lat) + Math.cos(start_rad_lat)*Math.cos(end_rad_lat) * Math.cos(delta_long_rad)) * R;

        return distance;
    }

    private LatLng DistanceGPS (double start_lat, double start_lng, double distance, double bearing) {
        double dR = distance/6371;
        start_lat = Math.toRadians(start_lat);
        start_lng = Math.toRadians(start_lng);
        bearing = Math.toRadians(bearing);
        double end_lat = Math.asin(Math.sin(start_lat)*Math.cos(dR) +
                                   Math.cos(start_lat)*Math.sin(dR)*Math.cos(bearing));

        double end_lng = start_lng + Math.atan2(Math.sin(bearing)*Math.sin(dR)*Math.cos(start_lat),
                                                Math.cos(dR)-Math.sin(start_lat)*Math.sin(end_lat));
        return new LatLng(Math.toDegrees(end_lng), Math.toDegrees(end_lat));
    }

}
