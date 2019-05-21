package in.creationdevs.aqi;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {
    Handler handler;
    TextView load,know,quote;
    ImageView image,earth,hub;
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


        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashActivity.this,TodayActivity.class);
                startActivity(intent);
                finish();
            }
        },5000);
    }
}