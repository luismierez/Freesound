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
            new ScrapeLoginPage()
                    .execute("https://www.freesound.org/apiv2/oauth2/authorize/?client_id=8232eb7787f0216a1111&response_type=code&state=xyz",
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
}
