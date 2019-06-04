package in.creationdevs.aqi;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Graph extends AppCompatActivity {
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_today: {
                    Intent intent = new Intent(Graph.this, TodayActivity.class);
                    startActivity(intent);
                    return true;
                }
                case R.id.navigation_legend: {
                    Intent intent = new Intent(Graph.this, MainActivity.class);
                    startActivity(intent);
                    return true;
                }
              /*  case R.id.navigation_history: {
                    return true;
                }
                */
                case R.id.navigation_aboutus: {
                    Intent intent = new Intent(Graph.this, AboutUs.class);
                    startActivity(intent);
                    return true;
                }
            }
            return false;
        }
    };

    Calendar cal;
    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    Date date;
    String dateString;
    BarChart barChart;
    BarDataSet barDataSet;
    LineChart lineChart;
    BarData dataa;
    ArrayList<BarEntry> barEntries = new ArrayList<>(); // Y_AXIS DATA
    ArrayList<Entry> yvalues = new ArrayList<>();


    String[] quarters = new String[7];
    String[] q = new String[32];
    int[] colours = new int[8];
    int colour_count = 0;

    ValueFormatter formatter = new ValueFormatter() {
        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            return quarters[(int) value];
        }
    };
    ValueFormatter formatter_line_chart = new ValueFormatter() {
        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            return q[(int) value];
        }
    };

    public String GetColor(int value)
    {
        if((value < 50))
        {
            return "#2ECC71";
        }
        else if(value < 100)
        {
            return "#196F3D";
        }
        else if(value < 200)
        {
            return "#D4AC0D";
        }
        else if(value < 300)
        {
            return "#F1C40F";
        }
        else if(value < 400)
        {
            return "#E74C3C";
        }
        else if(value < 500)
        {
            return "#C0392B";
        }

        return "#FFFFFF";
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.getMenu().getItem(2).setChecked(true);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        cal = Calendar.getInstance();
        date = cal.getTime();
        dateString = dateFormat.format(date);
        barChart = (BarChart) findViewById(R.id.bar);
        lineChart=(LineChart) findViewById(R.id.linechart);



        lineChart.setHighlightPerDragEnabled(false);
        lineChart.setHighlightPerTapEnabled(false);

        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getAxisRight().setDrawGridLines(false);

        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setTouchEnabled(true);

        lineChart.getXAxis().setTextColor(Color.parseColor("#ffffff"));

        lineChart.getDescription().setEnabled(false);
        lineChart.getDescription().setText("MONTHLY");
        //lineChart.getDescription().setTextColor(Color.parseColor("#ffffff"));
        //lineChart.getDescription().setTextSize(14);
        //lineChart.getDescription().setPosition(200,300);
        //lineChart.setBorderColor(Color.parseColor("#ffffff"));

        lineChart.getAxisLeft().setDrawLabels(false);
        lineChart.getAxisRight().setDrawLabels(false);
        //lineChart.getXAxis().setDrawLabels(false);
        lineChart.getLegend().setEnabled(false);

        //lineChart.setDrawGridBackground(true);
        //lineChart.setGridBackgroundColor(Color.parseColor("#ffffff"));
        //lineChart.setBackgroundColor(Color.parseColor("#778899"));





        RequestQueue queue = Volley.newRequestQueue(Graph.this);
        String url = "http://creationdevs.in/AirIndex/fetch.php";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            float t;

            @Override
            public void onResponse(String response) {
                Handler handler;
                Calendar c = Calendar.getInstance();

                //Function Parse JSON
                try {

                    JSONArray jsonArray = new JSONArray(response);
                    int length = jsonArray.length();
                    String[] s = new String[length];
                    // cal.add(Calendar.DAY_OF_MONTH, ); //Goes to previous day
                    for (int x = 0; x < 7; x++) {
                        date = cal.getTime();
                        dateString = dateFormat.format(date);
                        for (int i = 1; i < length; i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String dateget = jsonObject.getString("COL1");
                            String aqiget = jsonObject.getString("COL4");
                            Log.d(dateget, dateString);
                            if (dateget.equals(dateString)) {
                                if(x == 0){
                                    quarters[x] = "Today";
                                }
                                else if(x == 1) {
                                    quarters[x] = "Yesterday";
                                    cal.add(Calendar.DAY_OF_MONTH, -1);
                                }
                                else {
                                    c.setTime(date);
                                    quarters[x] = getDay(c.get(Calendar.DAY_OF_WEEK));
                                }
                                barEntries.add(new BarEntry((float)x, Float.parseFloat(aqiget)));
                                colours[colour_count] = Color.parseColor(GetColor(Integer.parseInt(aqiget)));
                                colour_count++;
                               // Toast.makeText(Graph.this, dateString + " " + String.valueOf(i), Toast.LENGTH_SHORT).show();
                                break;
                                //t = Float.parseFloat(aqiget);
                            }
                        }
                        cal.add(Calendar.DAY_OF_MONTH, -1); //Goes to previous day

                    }
                    for (int x = 0; x < 31; x++) {
                        date = cal.getTime();
                        dateString = dateFormat.format(date);
                        for (int i = 1; i < length; i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String dateget = jsonObject.getString("COL1");
                            String aqiget = jsonObject.getString("COL4");
                            Log.d(dateget, dateString);
                            if (dateget.equals(dateString)) {


                                yvalues.add(new Entry((float)x, Float.parseFloat(aqiget)));
                                q[x] = dateString;
                               // Toast.makeText(Graph.this, dateString + " " + String.valueOf(i), Toast.LENGTH_SHORT).show();
                                break;
                                //t = Float.parseFloat(aqiget);
                            }
                        }
                        cal.add(Calendar.DAY_OF_MONTH, -1); //Goes to previous day
                    }
                    handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setgraph();
                            barChart.animateXY(500, 500);
                            lineChart.invalidate();
                        }
                    }, 20);
                } catch (Exception exe) {
                    Toast.makeText(Graph.this, "JSON Error" + exe.toString(), Toast.LENGTH_SHORT).show();
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Graph.this, "Internet / DataBase Offline", Toast.LENGTH_LONG).show();

            }
        }
        );
        queue.add(stringRequest);

        // setgraph();
        /*

        barEntries.add(new BarEntry(2f,20f));
        barEntries.add(new BarEntry(3f,35f));
        barEntries.add(new BarEntry(4f,40f));
        barEntries.add(new BarEntry(6f,42f));
        barEntries.add(new BarEntry(7f,38f));




        yvalues.add(new Entry(1f,30f));
        yvalues.add(new Entry(2f,20f));
        yvalues.add(new Entry(3f,35f));
        yvalues.add(new Entry(4f,40f));
        yvalues.add(new Entry(5f,42f));
        yvalues.add(new Entry(6f,38f));


        */
    }

    public void setgraph() {
        barDataSet = new BarDataSet(barEntries, "AQI");
        //barDataSet.setColor(Color.parseColor("#000000"));
        barDataSet.setValueTextColor(Color.parseColor("#ffffff"));
        barDataSet.setColors(colours);
        dataa = new BarData(barDataSet);
        barChart.setData(dataa);
        Legend legend = barChart.getLegend();
        legend.setEnabled(false);
        barChart.setTouchEnabled(true);
        barChart.setDragEnabled(true);
        barChart.setScaleEnabled(true);
        barChart.getDescription().setEnabled(false);
        barChart.getDescription().setText("WEEKLY");
        Description description = barChart.getDescription();
        XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setTextColor(Color.parseColor("#ffffff"));
        xAxis.setValueFormatter(formatter);
        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setTextColor(Color.parseColor("#ffffff"));
        YAxis yright = barChart.getAxisRight();
        yright.setEnabled(false);





        LineDataSet set = new LineDataSet(yvalues,"AQI");
        set.setCircleColor(Color.parseColor("#ffffff"));
        set.setCircleHoleRadius(3);
        set.setLineWidth(2);
        set.setColor(Color.parseColor("#ffffff"));
        set.setValueTextSize(9);
        set.setValueTextColor(Color.parseColor("#ffffff"));
        LineData dataaa =new LineData(set);
        XAxis x = lineChart.getXAxis();
        x.setValueFormatter(formatter_line_chart);
        lineChart.setData(dataaa);
        set.setFillAlpha(110);
        //XAxis x = lineChart.getXAxis();
        //  x.setValueFormatter(formatter_line_chart);

    }

    public String getDay(int day){
        switch (day){
            case 1:
                return "Mon";
            case 2:
                return "Tue";
            case 3:
                return "Wed";
            case 4:
                return "Thur";
            case 5:
                return "Fri";
            case 6:
                return "Sat";
            case 7:
                return "Sun";
        }
        return "Error";
    }

}