package pl.pawelkleczkowski.customgaugeexample;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;


public class calendar extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_date);

        Button button = findViewById(R.id.btt);
        TextView calendarText = findViewById(R.id.header_calendar);
        CalendarView calendarView = findViewById(R.id.calendar_date);
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/ZCOOL-Regular.ttf");
        button.setTypeface(typeface);
        calendarText.setTypeface(typeface);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            long millisTime = bundle.getLong("date");
            calendarView.setDate(millisTime,true,true);
        }
    }
}
