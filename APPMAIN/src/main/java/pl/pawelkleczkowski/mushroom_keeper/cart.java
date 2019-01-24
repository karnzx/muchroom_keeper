package pl.pawelkleczkowski.mushroom_keeper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;

public class cart extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_cart);

        Button info_btt = findViewById(R.id.info_btt);
        Button button = findViewById(R.id.btt);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        info_btt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebView myWebView = (WebView) findViewById(R.id.webview);
                myWebView.loadUrl("https://hug-hed.blogspot.com/2015/07/factors-affect-mushroom-growth.html");
            }
        });
    }
}
