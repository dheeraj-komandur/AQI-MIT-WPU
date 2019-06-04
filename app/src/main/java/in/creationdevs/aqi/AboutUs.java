package in.creationdevs.aqi;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class AboutUs extends AppCompatActivity {
    int i=0;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_today: {
                    Intent intent = new Intent(AboutUs.this, TodayActivity.class);
                    startActivity(intent);
                    return true;
                }
                case R.id.navigation_legend: {
                    Intent intent = new Intent(AboutUs.this, MainActivity.class);
                    startActivity(intent);
                    return true;
                }
               /* case R.id.navigation_history: {
                    Intent intent = new Intent(AboutUs.this, Graph.class);
                    startActivity(intent);
                    return true;
                }*/
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
        setContentView(R.layout.activity_about_us);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.getMenu().getItem(2).setChecked(true);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public void kokate(View view)
    {
        i++;
        if(i>20)
        {
            Toast.makeText(AboutUs.this, "Gauge By : Shubham Kokate \nTested BY : Amey Konde \nTested Futher By : Devesh Bhogre", Toast.LENGTH_SHORT).show();
        }
    }
}
