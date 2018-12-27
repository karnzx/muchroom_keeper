package pl.pawelkleczkowski.customgaugeexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;

import java.util.Timer;
import java.util.TimerTask;

public class intro extends AppCompatActivity {

    Timer timer;
    int Screen_delay = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_intro);
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

    }


}
