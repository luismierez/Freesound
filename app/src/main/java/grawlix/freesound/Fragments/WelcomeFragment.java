package grawlix.freesound.Fragments;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.select.Elements;

import java.io.IOException;

import grawlix.freesound.R;

/**
 * Created by luismierez on 10/6/14.
 */
public class WelcomeFragment extends Fragment implements View.OnClickListener {

    private EditText username;
    private EditText password;
    private Button submit;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.welcome_fragment, container, false);
        username = (EditText) view.findViewById(R.id.username);
        password = (EditText) view.findViewById(R.id.password);
        submit = (Button) view.findViewById(R.id.btn_login);

        submit.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.btn_login) {
            Log.d("Button Clicked", username.getText().toString());
            /*
            new ScrapeLoginPage()
                    .execute("https://www.freesound.org/apiv2/oauth2/authorize/?client_id=8232eb7787f0216a1111&response_type=code&state=xyz",
                            username.getText().toString(),
                            password.getText().toString());
            */
            new FreeSoundLogin().execute("https://www.freesound.org/apiv2/oauth2/authorize/?client_id=8232eb7787f0216a1111&response_type=code&state=xyz",
                    username.getText().toString(),
                    password.getText().toString());
        }
    }

    private class ScrapeLoginPage extends AsyncTask<String, Void, Document> {

        @Override
        protected Document doInBackground(String... strings) {
            try {
                /*
                Connection.Response loginForm = Jsoup.connect(strings[0])
                        .userAgent("Mozilla")
                        .method(Connection.Method.GET)
                        .execute();
                */

                /*
                Document document = Jsoup.connect(strings[0])
                        .userAgent("Mozilla")
                        .data("username", strings[1])
                        .data("password", strings[2])
                        .data("login", "Login")
                        .cookies(loginForm.cookies())
                        .post();
                Log.d("Document", document.toString());
                */
                Document document = Jsoup.connect(strings[0]).get();
                //Log.d("Document", document.toString());
                Elements elements = document.getElementsByTag("form");
                Log.d("Form Element", elements.get(0).toString());


            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class FreeSoundLogin extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient okHttpClient = new OkHttpClient();

            //Log.d("params", params);
            //RequestBody body = RequestBody.create(JSON, params);
            Request request = new Request.Builder()
                    .url(strings[0])
                    .build();

            Call call = okHttpClient.newCall(request);

            try {
                // Get HTML for login page
                Response response = call.execute();
                // Parse the HTML
                Document document = Jsoup.parse(response.body().string());
                // Find all the input tags
                Elements elements = document.getElementsByTag("input");
                // Split the first input
                String[] parsedInput = elements.get(0).toString().split("\"");
                //String params = "{'username':'"+strings[1]+"','password':'"+strings[2]+"'";
                //RequestBody body = RequestBody.create(JSON, params);
                // Build a form for post
                RequestBody formInput = new FormEncodingBuilder()
                        .add("username", strings[1])
                        .add("password", strings[2])
                        //.add("csrfmiddlewaretoken", parsedInput[5])
                        .build();

                Log.d("csrfmiddlewaretoken", parsedInput[5]);
                Request post = new Request.Builder()
                        .header("Referer", strings[0])
                        .url(strings[0])
                        .post(formInput)
                        .build();

                Call postCall = okHttpClient.newCall(post);
                Response postResponse = postCall.execute();
                Log.d("Received call", postResponse.body().string());


            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }
    }
}
