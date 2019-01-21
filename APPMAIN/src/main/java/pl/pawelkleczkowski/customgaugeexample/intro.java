package pl.pawelkleczkowski.customgaugeexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class intro extends AppCompatActivity {

    Timer timer;
    int Screen_delay = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_intro);

        ImageView imageView =
                findViewById(R.id.imageView);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.scale);
        imageView.startAnimation(animation);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(intro.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        },Screen_delay);


    }

    public void onStop() {
        super.onStop();
        finish();
    }


}
