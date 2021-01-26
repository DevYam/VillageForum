package com.example.villageforum;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Posts extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
        ListView lv = (ListView) findViewById(R.id.list);
        List<String> your_array_list = new ArrayList<String>();



        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://api.ncertguruji.com/getJson.php";
        JsonObjectRequest request = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (null != response) {
                            //handle your response
                            System.out.println("respo is not null");
                            System.out.println(response.toString());
                            for (int i=0; i<response.length(); i++){
                                try {
                                    String name = response.getJSONObject(i+"").getString("content");
                                    System.out.println("My string is"+name);
                                    your_array_list.add(name);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                                    Posts.this,
                                    android.R.layout.simple_list_item_1,
                                    your_array_list );
                            lv.setAdapter(arrayAdapter);
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });
        queue.add(request);


    }
}