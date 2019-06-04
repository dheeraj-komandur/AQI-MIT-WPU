package in.creationdevs.aqi;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {
    Handler handler;
    TextView load,know,quote;
    ImageView image,earth,hub;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_today: {
                    Intent intent = new Intent(SplashActivity.this, TodayActivity.class);
                    startActivity(intent);
                    return true;
                }
                case R.id.navigation_legend: {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    return true;
                }
                case R.id.navigation_history: {
                    Intent intent = new Intent(SplashActivity.this, Graph.class);
                    startActivity(intent);
                    return true;
                }
                case R.id.navigation_aboutus: {
                    Intent intent = new Intent(SplashActivity.this, AboutUs.class);
                    startActivity(intent);
                    return true;
                }
            }
            return false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        load = findViewById(R.id.textview_load);
        image = findViewById(R.id.image);
        know = findViewById(R.id.know);
        earth = findViewById(R.id.earth);
        hub = findViewById(R.id.hub);
        quote = findViewById(R.id.textquote);

        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.mytransition);
        load.startAnimation(myanim);
        image.startAnimation(myanim);
        know.startAnimation(myanim);
        earth.startAnimation(myanim);
        hub.startAnimation(myanim);
        quote.startAnimation(myanim);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.getMenu().getItem(0).setChecked(false);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        /*
        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashActivity.this,TodayActivity.class);
                startActivity(intent);
                finish();
            }
        },5000);*/



    }
}