package in.creationdevs.aqi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.anastr.speedviewlib.ImageSpeedometer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.font.TextAttribute;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TodayActivity extends AppCompatActivity {

    public static final String PREFERENCES = "VALUES";
    TextView textViewSO2,textViewNO2,textViewPM25,textViewPM10,textViewAQI;
    TextView textViewSO2Desp,textViewNO2Desp,textViewPM25Desp,textViewPM10Desp,textViewAQIDesp;
    String TodayDate;
    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    Date date;
    String dateString;
    Calendar cal;
    TextView textviewtodaysdate;
    ImageSpeedometer imageSpeedometer;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_today: {
                    return true;
                }
                case R.id.navigation_legend: {
                    Intent intent = new Intent(TodayActivity.this, MainActivity.class);
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
        setContentView(R.layout.activity_today);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.getMenu().getItem(0).setChecked(true);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        textviewtodaysdate = findViewById(R.id.textview_todaysdate);

        //Gauge All
        imageSpeedometer = findViewById(R.id.imageSpeedometer);
        imageSpeedometer.setMaxSpeed(500);
        imageSpeedometer.setMinSpeed(0);
        imageSpeedometer.setWithTremble(false);

        //Find All Text Views Value
        textViewSO2 = findViewById(R.id.textview_so2);
        textViewNO2 = findViewById(R.id.textview_no2);
        textViewPM25 = findViewById(R.id.textview_pm25);
        textViewPM10 = findViewById(R.id.textview_pm10);
        textViewAQI = findViewById(R.id.textview_aqi);

        //Find All Text View Description
        textViewSO2Desp = findViewById(R.id.textview_so2_desp);
        textViewNO2Desp = findViewById(R.id.textview_no2_desp);
        textViewPM25Desp = findViewById(R.id.textview_pm25_desp);
        textViewPM10Desp = findViewById(R.id.textview_pm10_desp);
        textViewAQIDesp = findViewById(R.id.textview_aqi_desp);


        //Write code to get today date from the android device

        //TodayDate = "30-01-2015";


        cal = Calendar.getInstance();
        date = cal.getTime();
        dateString = dateFormat.format(date);
        textviewtodaysdate.setText("Today :  "+dateString);
        //Toast.makeText(TodayActivity.this, dateFormat.format(date), Toast.LENGTH_SHORT).show();
        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES,MODE_PRIVATE);
        if(sharedPreferences.getString("AQI",null)!=null){

            String so2 = sharedPreferences.getString("SO2",null);
            textViewSO2.setText(so2);
            String valso2 = GetDesp(so2);
            textViewSO2Desp.setText(valso2);
            String Colso2 = GetColor(valso2);
            textViewSO2Desp.setTextColor(Color.parseColor("#FFFFFF"));
            textViewSO2Desp.setBackgroundColor(Color.parseColor(Colso2));


            String no2 = sharedPreferences.getString("NO2",null);
            textViewNO2.setText(no2);
            String valno2 = GetDesp(no2);
            textViewNO2Desp.setText(valno2);
            String Colno2 = GetColor(valno2);
            textViewNO2Desp.setTextColor(Color.parseColor("#FFFFFF"));
            textViewNO2Desp.setBackgroundColor(Color.parseColor(Colno2));


            String pm25 = sharedPreferences.getString("PM25",null);
            textViewPM25.setText(pm25);
            String valpm25 = GetDesp(pm25);
            textViewPM25Desp.setText(valpm25);
            String Colpm25 = GetColor(valpm25);
            textViewPM25Desp.setTextColor(Color.parseColor("#FFFFFF"));
            textViewPM25Desp.setBackgroundColor(Color.parseColor(Colpm25));


            String pm10 = sharedPreferences.getString("PM10",null);
            textViewPM10.setText(pm10);
            String valpm10 = GetDesp(pm10);
            textViewPM10Desp.setText(valpm10);
            String Colpm10 = GetColor(valpm10);
            textViewPM10Desp.setTextColor(Color.parseColor("#FFFFFF"));
            textViewPM10Desp.setBackgroundColor(Color.parseColor(Colpm10));


            String aqi = sharedPreferences.getString("AQI",null);
            textViewAQI.setText(aqi);
            String valaqi = GetDesp(aqi);
            textViewAQIDesp.setText(valaqi);
            String Colaqi = GetColor(valaqi);
            textViewAQIDesp.setTextColor(Color.parseColor("#FFFFFF"));
            textViewAQIDesp.setBackgroundColor(Color.parseColor(Colaqi));

            imageSpeedometer.speedTo(Integer.parseInt(aqi));


        }
        //Volley Start
        if (isNetworkAvailable()) {

            RequestQueue queue = Volley.newRequestQueue(TodayActivity.this);
            String url = "http://creationdevs.in/AirIndex/fetch.php";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    //Function Parse JSON
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        int length = jsonArray.length();
                        String[] s = new String[length];
                        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE);
                        for (int i = 0; i < length; i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String dateget = jsonObject.getString("COL2");

                            if (dateget.equals(dateString)) {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                String so2 = jsonObject.getString("COL8");
                                textViewSO2.setText(so2);
                                editor.putString("SO2", so2);
                                String valso2 = GetDesp(so2);
                                textViewSO2Desp.setText(valso2);
                                String Colso2 = GetColor(valso2);
                                textViewSO2Desp.setTextColor(Color.parseColor("#FFFFFF"));
                                textViewSO2Desp.setBackgroundColor(Color.parseColor(Colso2));


                                String no2 = jsonObject.getString("COL9");
                                textViewNO2.setText(no2);
                                editor.putString("NO2", no2);
                                String valno2 = GetDesp(no2);
                                textViewNO2Desp.setText(valno2);
                                String Colno2 = GetColor(valno2);
                                textViewNO2Desp.setTextColor(Color.parseColor("#FFFFFF"));
                                textViewNO2Desp.setBackgroundColor(Color.parseColor(Colno2));


                                String pm25 = jsonObject.getString("COL10");
                                textViewPM25.setText(pm25);
                                editor.putString("PM25", pm25);
                                String valpm25 = GetDesp(pm25);
                                textViewPM25Desp.setText(valpm25);
                                String Colpm25 = GetColor(valpm25);
                                textViewPM25Desp.setTextColor(Color.parseColor("#FFFFFF"));
                                textViewPM25Desp.setBackgroundColor(Color.parseColor(Colpm25));


                                String pm10 = jsonObject.getString("COL11");
                                textViewPM10.setText(pm10);
                                editor.putString("PM10", pm10);
                                String valpm10 = GetDesp(pm10);
                                textViewPM10Desp.setText(valpm10);
                                String Colpm10 = GetColor(valpm10);
                                textViewPM10Desp.setTextColor(Color.parseColor("#FFFFFF"));
                                textViewPM10Desp.setBackgroundColor(Color.parseColor(Colpm10));


                                String aqi = jsonObject.getString("COL12");
                                textViewAQI.setText(aqi);
                                editor.putString("AQI", aqi);
                                String valaqi = GetDesp(aqi);
                                textViewAQIDesp.setText(valaqi);
                                String Colaqi = GetColor(valaqi);
                                textViewAQIDesp.setTextColor(Color.parseColor("#FFFFFF"));
                                textViewAQIDesp.setBackgroundColor(Color.parseColor(Colaqi));

                                imageSpeedometer.speedTo(Integer.parseInt(aqi));

                                editor.commit();
                            }

                        }
                    } catch (Exception exe) {
                        Toast.makeText(TodayActivity.this, "JSON Error" + exe.toString(), Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(TodayActivity.this, "Internet / DataBase Offline", Toast.LENGTH_LONG).show();

                }
            }
            );
            queue.add(stringRequest);
            //End Volley
        }
            else{
                Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
            }


        //Notification
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel =
                    new NotificationChannel("MyNotification","MyNotification",NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        FirebaseMessaging.getInstance().subscribeToTopic("general")
                .addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        String msg = "Successful";
                        if (!task.isSuccessful()) {
                            msg ="Failed";
                        }
                        // Log.d(TAG, msg);
                       // Toast.makeText(TodayActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });


        }



    public String GetDesp(String Value)
    {
        if(Value.equals("NA")) //NA is Hardcoded check database for nil,null etc
        {
            Value="-1";
        }
        int value = Integer.valueOf(Value);

        if(value >= 0 && value <= 50)
        {
            return "Good";
        }
        else if(value >=51  && value <= 100)
        {
            return "Satisfactory";
        }
        else if(value >=101  && value <= 200)
        {
            return "Moderate";
        }
        else if(value >=201  && value <= 300)
        {
            return "Poor";
        }
        else if(value >=301  && value <= 400)
        {
            return "Very Poor";
        }
        else if(value >=401  && value <= 500)
        {
            return "Severe";
        }

        return "No Data";
    }

    public String GetColor(String Value)
    {
        if(Value.equals("Good"))
        {
            return "#2ECC71";
        }
        else if(Value.equals("Satisfactory"))
        {
            return "#196F3D";
        }
        else if(Value.equals("Moderate"))
        {
            return "#D4AC0D";
        }
        else if(Value.equals("Poor"))
        {
            return "#F1C40F";
        }
        else if(Value.equals("Very Poor"))
        {
            return "#E74C3C";
        }
        else if(Value.equals("Severe"))
        {
            return "#C0392B";
        }

        return "#FFFFFF";
    }

    public void previousdate(View view)
    {

        cal.add(Calendar.DAY_OF_MONTH, -1); //Goes to previous day
        date = cal.getTime();
        Toast.makeText(TodayActivity.this, dateFormat.format(date), Toast.LENGTH_SHORT).show();
    }

    public void nextdate(View view)
    {

        cal.add(Calendar.DAY_OF_MONTH, 1); //Goes to previous day
        date = cal.getTime();
        Toast.makeText(TodayActivity.this, dateFormat.format(date), Toast.LENGTH_SHORT).show();
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}