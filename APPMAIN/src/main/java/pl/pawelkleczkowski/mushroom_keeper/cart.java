package pl.pawelkleczkowski.mushroom_keeper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.Objects;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class cart extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_cart);

        Button info_btt = findViewById(R.id.info_btt);
        TextView header = findViewById(R.id.header_shop);
        Button button = findViewById(R.id.btt);
        button.setOnClickListener((View v) -> finish() );

        info_btt.setOnClickListener((View v) -> {
            OkHttpClient client = new OkHttpClient();

            HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse("https://notify-api.line.me/api/notify")).newBuilder();
            urlBuilder.addQueryParameter("message", "from android !");
            String URL = urlBuilder.build().toString();

            Request request = new Request.Builder()
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("Authorization","Bearer TkAybobysN1Ow0bJzbsMGAEDRKwYCb1zrr1hBJflAjr")
                    .url(URL)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                assert response.body() != null;
                header.setText(response.body().toString());
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }
}
