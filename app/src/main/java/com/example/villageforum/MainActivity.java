package com.example.villageforum;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    String post = "";
    String name = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onBtnClick(View view){
        EditText editText = (EditText)findViewById(R.id.name);
        name = editText.getText().toString();

        EditText editText1 = (EditText)findViewById(R.id.edit_text);
        post = editText1.getText().toString();
//        Toast.makeText(this, post, Toast.LENGTH_SHORT).show();

        JSONObject postData = new JSONObject();

        try {
            postData.put("name", name);
            postData.put("post", post);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("Post data is "+postData);

        new SendDeviceDetails().execute("http://api.ncertguruji.com/postjson.php", postData.toString());
    }

    public void navigateToForum(View view){
        Intent intent = new Intent(MainActivity.this,Posts.class);
        startActivity(intent);
    }


     class SendDeviceDetails extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String data = "";

            HttpURLConnection httpURLConnection = null;
            try {

                httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
                httpURLConnection.setRequestMethod("POST");

                httpURLConnection.setDoOutput(true);

                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.writeBytes(params[1]);
                wr.flush();
                wr.close();

                InputStream in = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(in);

                int inputStreamData = inputStreamReader.read();
                while (inputStreamData != -1) {
                    char current = (char) inputStreamData;
                    inputStreamData = inputStreamReader.read();
                    data += current;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }

            return data;
        }
        private ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result.equals("New record created successfully")){
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                Intent intent = new Intent(MainActivity.this, Posts.class);
                startActivity(intent);
            }
            Log.e("TAG", result); // this is expecting a response code to be sent from your server upon receiving the POST data
        }


    }


}