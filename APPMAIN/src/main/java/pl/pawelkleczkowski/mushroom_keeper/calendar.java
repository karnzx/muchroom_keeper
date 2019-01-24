package pl.pawelkleczkowski.mushroom_keeper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;


public class calendar extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_date);

        Button button = findViewById(R.id.btt);
        CalendarView calendarView = findViewById(R.id.calendar_date);
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
