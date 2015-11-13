package vickypatel.ca.shiftmanager.Activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import vickypatel.ca.shiftmanager.R;
import vickypatel.ca.shiftmanager.extras.Constants;

public class ContactUsActivity extends AppCompatActivity {

    EditText userNameEditText, userEmailEditText, subjectEditText, commentsEditText;
    String userName, userEmail, subject, comments;
    Boolean error = false;

    private final String USER_AGENT = "Mozilla/5.0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // INIT
        userNameEditText = (EditText) findViewById(R.id.input_user_name);
        userEmailEditText = (EditText) findViewById(R.id.input_user_email);
        subjectEditText = (EditText) findViewById(R.id.input_subject);
        commentsEditText = (EditText) findViewById(R.id.input_comments);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                collectData();
                if (!error) {
                    new PostDataAsyncTask().execute();
                }

            }
        });


    }

    public class PostDataAsyncTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            // do stuff before posting data
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                postData();

            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String lenghtOfFile) {
            // do stuff after posting data
        }
    }

    private void postData() {

        // url where the data will be posted
        String url = "http://vickypatel.ca/shift_send_email.php";

        URL obj = null;
        HttpURLConnection conn = null;
        OutputStream os = null;
        InputStream is = null;
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.NAME, userName);
            jsonObject.put(Constants.EMAIL, userEmail);
            jsonObject.put(Constants.SUBJECT, subject);
            jsonObject.put(Constants.COMMENTS, comments);
            String message = jsonObject.toString();


            obj = new URL(url);
            conn = (HttpURLConnection) obj.openConnection();
            conn.setReadTimeout(10000 /*milliseconds*/);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setFixedLengthStreamingMode(message.getBytes().length);

            //make some HTTP header nicety
            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");

            //open
            conn.connect();

            //setup send
            os = new BufferedOutputStream(conn.getOutputStream());
            os.write(message.getBytes());
            //clean up
            os.flush();

            System.out.println("data to server");
            System.out.println(os.toString());





            //do somehting with response
            is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder out = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }
            System.out.println("data from server");
            System.out.println(out.toString());   //Prints the string content read from input stream
            reader.close();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            //clean up
            try {
                os.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            conn.disconnect();
        }


    }

    public void collectData() {
        userName = (userNameEditText.getText().toString());
        userEmail = (userEmailEditText.getText().toString());
        subject = (subjectEditText.getText().toString());
        comments = (commentsEditText.getText().toString());
        error = false;

        if (userName.equals("")) {
            error = true;
            userNameEditText.setError("This field is required");
        }
        if (userEmail.equals("")) {
            error = true;
            userEmailEditText.setError("This field is required");
        }
        if (subject.equals("")) {
            error = true;
            subjectEditText.setError("This field is required");
        }
        if (comments.equals("")) {
            error = true;
            commentsEditText.setError("This field is required");
        }

    }
}
