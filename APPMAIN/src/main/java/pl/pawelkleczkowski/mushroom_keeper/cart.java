package pl.pawelkleczkowski.mushroom_keeper;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class cart extends AppCompatActivity {
    private static final String MY_PREFS = "my_prefs";
    private ArrayList<String> amount = new ArrayList<>();
    private ArrayList<String> ArrInfoName = new ArrayList<>();
    private ArrayList<Integer> ArrInfoPic = new ArrayList<>();
    private ArrayList<String> ArrInfoDescrib = new ArrayList<>();
    private ArrayList<Integer> ArrInfoPrice = new ArrayList<>();
    private int Position;
    private int IndexName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_cart);

        ImageButton buy_btt1 = findViewById(R.id.buy_btt1);
        Button button = findViewById(R.id.btt);
        TextView IDline = findViewById(R.id.idline);
        TextView username = findViewById(R.id.name);
        ImageView infoPic = findViewById(R.id.infoPic);
        TextView infoName = findViewById(R.id.infoName);
        TextView infoDescrib = findViewById(R.id.infoDescrib);
        TextView infoPrice = findViewById(R.id.infoPrice);

        SharedPreferences shared = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE); //point to my_prefs
        IDline.setText(getString(R.string.IDline));
        IDline.append(" : ");
        IDline.append(shared.getString("IDLINE", "-"));     // set IDLINE if exist
        username.setText(getString(R.string.username));
        username.append(" : ");
        username.append(shared.getString("USERNAME", "-")); // set USERNAME if exist

        //Spinner
        Spinner Amount1 = findViewById(R.id.Amount1);
        Spinner Select = findViewById(R.id.select);
        setValueAmount();
        setValueName();
        setValuePic();
        setValueDescrib();
        setValuePrice();

        // Adapter
        ArrayAdapter<String> adapterAmount = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, amount);
        ArrayAdapter<String> adapterSelect = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, ArrInfoName);
        Amount1.setAdapter(adapterAmount);
        Select.setAdapter(adapterSelect);

        Amount1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Position = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Select.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                IndexName = position;
                infoName.setText(ArrInfoName.get(position));
                infoPic.setImageResource(ArrInfoPic.get(position));
                infoDescrib.setText(ArrInfoDescrib.get(position));
                infoPrice.setText(String.format(Locale.getDefault(),"%d",ArrInfoPrice.get(position)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        buy_btt1.setOnClickListener((View v) -> {
            String IDLINE = shared.getString("IDLINE","");
            String USERNAME = shared.getString("USERNAME","");
            if(Position != 0 && IndexName != 0){
                if(!IDLINE.equals("") && !USERNAME.equals("")) {
                    run(setMessage(IDLINE,USERNAME));
                }else{
                    Toast.makeText(cart.this,"กรุณาตั้งชื่อก่อน",Toast.LENGTH_SHORT).show();      //please set your INFORMATION first
                }
            }else{
                Toast.makeText(cart.this,"โปรดเลือกจำนวน หรือ ชนิดของเห็ด",Toast.LENGTH_SHORT).show();   //please select amount
            }
        });

        button.setOnClickListener((View v) -> finish() );
    }

    private void setValueAmount() {

        amount.add("ชิ้น");
        amount.add("1 ชิ้น");
        amount.add("2 ชิ้น");
        amount.add("3 ชิ้น");
        amount.add("4 ชิ้น");
        amount.add("5 ชิ้น");

    }
    private void setValueName() {

        ArrInfoName.add(getString(R.string.please_select_kind));
        ArrInfoName.add(getString(R.string.mr_sell1));
        ArrInfoName.add(getString(R.string.mr_sell2));

    }
    private void setValuePic() {

        ArrInfoPic.add(R.drawable.mushroom);
        ArrInfoPic.add(R.drawable.forsell);
        ArrInfoPic.add(R.drawable.forsell2);

    }
    private void setValueDescrib() {

        ArrInfoDescrib.add("");
        ArrInfoDescrib.add(getString(R.string.info_sell1));
        ArrInfoDescrib.add(getString(R.string.info_sell2));

    }
    private void setValuePrice() {

        ArrInfoPrice.add(0);
        ArrInfoPrice.add(120);
        ArrInfoPrice.add(190);

    }

    private String setMessage(String IDLINE,String USERNAME){
        int price = (ArrInfoPrice.get(IndexName)*Position);
        String msg = "\n";  //break line first for space!
        msg += getString(R.string.IDline) + " : " + IDLINE + "\n";
        msg += getString(R.string.username) + " : " + USERNAME + "\n";
        msg += "order : " + ArrInfoName.get(IndexName) + "\n";
        msg += "ชิ้นละ : " + ArrInfoPrice.get(IndexName) + "\n";
        msg += "จำนวน : " + amount.get(Position) + "\n";
        msg += "เป็นเงิน : " + String.format(Locale.getDefault(),"%d",price) + "บาท";
        return msg;
    }
    private void run(String message) {
        OkHttpClient client = new OkHttpClient();
        String URL = "https://notify-api.line.me/api/notify";
        RequestBody body = new FormBody.Builder()
                .add("message", message)
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
