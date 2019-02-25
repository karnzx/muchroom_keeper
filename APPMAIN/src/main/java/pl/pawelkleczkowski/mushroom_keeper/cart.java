package pl.pawelkleczkowski.mushroom_keeper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
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
        Button button = findViewById(R.id.btt);
        button.setOnClickListener((View v) -> finish() );

        info_btt.setOnClickListener((View v) -> {
            run("ฉันพูดภาษาไทยได้ด้ด้ด้ด้ด้ด้ด้ด้");
        });
    }

    private void run(String message) {
        String msg = message;
        OkHttpClient client = new OkHttpClient();
        String URL = "https://notify-api.line.me/api/notify";
        RequestBody body = new FormBody.Builder()
                .add("message", msg)
                .build();

        Request request = new Request.Builder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Bearer TkAybobysN1Ow0bJzbsMGAEDRKwYCb1zrr1hBJflAjr")
                .url(URL)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Log.i("POSTrequest","failure") );
            }

            @Override
            public void onResponse(Call call,final Response response){
                runOnUiThread(() -> {
                    //No need to use
//                        try{
//                            Time.setText(response.body().toString());
//                        }catch (Exception e){
//                            e.printStackTrace();
//                            Time.setText("error during get body");
//                        }
                });
            }
        });
    }
}
