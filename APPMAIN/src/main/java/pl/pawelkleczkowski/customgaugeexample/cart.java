package pl.pawelkleczkowski.customgaugeexample;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.TextView;

public class cart extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_cart);

        TextView header_shop = findViewById(R.id.header_shop);
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/ZCOOL-Regular.ttf");
        header_shop.setTypeface(typeface);

    }
}
