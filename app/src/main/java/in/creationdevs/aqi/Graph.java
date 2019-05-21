package in.creationdevs.aqi;

import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Graph extends AppCompatActivity {
        Calendar cal;
    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    Date date;
    String dateString;
    BarChart barChart;
    BarDataSet barDataSet;
    LineChart lineChart;
    BarData dataa;
    ArrayList<BarEntry> barEntries = new ArrayList<>(); // Y_AXIS DATA

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        float t;
        cal = Calendar.getInstance();
        date = cal.getTime();
        dateString = dateFormat.format(date);
        barChart = (BarChart) findViewById(R.id.bar);


        RequestQueue queue = Volley.newRequestQueue(Graph.this);
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

                            cal.add(Calendar.DAY_OF_MONTH, -i); //Goes to previous day
                            date = cal.getTime();
                            dateString = dateFormat.format(date);

                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String dateget = jsonObject.getString("COL 2");
                            String aqiget = jsonObject.getString("COL 12");

                            if(i==9) {
                                Toast.makeText(Graph.this, dateString + " " + String.valueOf(i), Toast.LENGTH_SHORT).show();
                                //t = Float.parseFloat(aqiget);
                            }
                        }
                }
                    catch (Exception exe) {
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
        /*
        lineChart=(LineChart) findViewById(R.id.linechart);

        barEntries.add(new BarEntry(2f,20f));
        barEntries.add(new BarEntry(3f,35f));
        barEntries.add(new BarEntry(4f,40f));
        barEntries.add(new BarEntry(6f,42f));
        barEntries.add(new BarEntry(7f,38f));



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
        //lineChart.getDescription().setText("DAILY");
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

        ArrayList<Entry> yvalues = new ArrayList<>();

        yvalues.add(new Entry(1f,30f));
        yvalues.add(new Entry(2f,20f));
        yvalues.add(new Entry(3f,35f));
        yvalues.add(new Entry(4f,40f));
        yvalues.add(new Entry(5f,42f));
        yvalues.add(new Entry(6f,38f));

        LineDataSet set = new LineDataSet(yvalues,"AQI");
        set.setCircleColor(Color.parseColor("#ffffff"));
        set.setCircleHoleRadius(3);
        set.setLineWidth(2);
        set.setColor(Color.parseColor("#ffffff"));
        set.setValueTextSize(9);
        set.setValueTextColor(Color.parseColor("#ffffff"));
        LineData dataaa =new LineData(set);
        lineChart.setData(dataaa);
        set.setFillAlpha(110);
        */
    }

    public void setgraph()
    {
        barEntries.add(new BarEntry(1f,20f));

        barDataSet  = new BarDataSet(barEntries,"AQI");
        dataa = new BarData(barDataSet);
        barChart.setData(dataa);

        barChart.setTouchEnabled(true);
        barChart.setDragEnabled(true);
        barChart.setScaleEnabled(true);

    }
}
