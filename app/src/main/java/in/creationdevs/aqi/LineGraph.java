package in.creationdevs.aqi;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LineGraph extends AppCompatActivity {
    ListView listviewdata;
    List<String> list_data = new ArrayList<String>();
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_today: {
                    Intent intent = new Intent(LineGraph.this, TodayActivity.class);
                    startActivity(intent);
                    return true;
                }
                case R.id.navigation_legend: {
                    Intent intent = new Intent(LineGraph.this, MainActivity.class);
                    startActivity(intent);
                    return true;
                }
                case R.id.navigation_aboutus: {
                    return true;
                }
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_graph);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.getMenu().getItem(0).setChecked(true);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        listviewdata= findViewById(R.id.listview_data);


        //temp list called list_email

        //Volley Start
        RequestQueue queue = Volley.newRequestQueue(LineGraph.this);
        String url = "http://creationdevs.in/AirIndex/fetch.php";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Function Parse JSON
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    int length = jsonArray.length();
                    String[] s = new String[length];
                    for (int i = 1; i < length; i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String dateget = jsonObject.getString("COL 2");
                        String aqiget = jsonObject.getString("COL 12");

                        if (!dateget.equals("30-05-2019")) {

                            list_data.add("     "+dateget+"                    "+aqiget);

                        }
                        else
                        {
                            break;
                        }

                    }

                    ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(LineGraph.this,android.R.layout.simple_list_item_1,list_data);
                    listviewdata.setAdapter(arrayAdapter1);

                } catch (Exception exe) {
                    Toast.makeText(LineGraph.this, "JSON Error" + exe.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LineGraph.this, "Internet / DataBase Offline", Toast.LENGTH_LONG).show();

            }
        }
        );
        queue.add(stringRequest);
        //End Volley

        //sets list as list_email


    }
}