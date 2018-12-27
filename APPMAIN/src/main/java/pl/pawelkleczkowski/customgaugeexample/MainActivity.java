package pl.pawelkleczkowski.customgaugeexample;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Button;
import android.widget.TextView;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import io.netpie.microgear.Microgear;
import io.netpie.microgear.MicrogearEventListener;

import pl.pawelkleczkowski.customgauge.CustomGauge;


public class MainActivity extends AppCompatActivity {


	private CustomGauge humidity;
	private CustomGauge temperature;

	int i;
	private TextView text1;
	protected TextView text2;
	protected TextView text3;
	protected TextView text4;

	private Microgear microgear = new Microgear(this);
	private String appid = "Greenroom"; //APP_ID
	private String key = "OTH365jrDMiPUK0"; //KEY
	protected String secret = "Cu7Wo7r17GAqPBsr1hdWe8Qib"; //SECRET
	protected String alias = "android";

	// i dont know how to remove yellow tag ;w; .. -
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Bundle bundle = msg.getData();
			String string = bundle.getString("OTH365jrDMiPUK0");
			TextView text1 =
					findViewById(R.id.textView1);
            text1.setText(string);
            Log.i("got",string);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_main);
		MicrogearCallBack callback = new MicrogearCallBack();
		microgear.connect(appid,key,secret,alias);
		microgear.setCallback(callback);
		microgear.subscribe("/#"); //listen all

		setContentView(R.layout.activity_main);
        // define and set toolbar
		Toolbar toolbar =
				findViewById(R.id.toolbar);
		toolbar.setTitle("Muchroom Project APP");
		toolbar.setTitleTextColor(Color.WHITE);
		toolbar.setSubtitle("เพาะเห็ดไงละ");
		toolbar.setSubtitleTextColor(Color.YELLOW);
		setSupportActionBar(toolbar);

        //define button & switch
		Button button =
				findViewById(R.id.button);
		final Switch switch1 =
				findViewById(R.id.switch1);
        //defind temp,humi
		humidity = findViewById(R.id.gauge1);
		temperature = findViewById(R.id.gauge2);

        //set GAUGE
		humidity.setStartValue(0);
		humidity.setEndValue(100);
		temperature.setStartValue(0);
		temperature.setEndValue(100);
		// set switch OFF
        switch1.setText(R.string.OFF);
        switch1.setChecked(false);
        microgear.publish("/relaylight","OFF");
		switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(switch1.isChecked()){
                    microgear.publish("/relaylight","ON");
                    switch1.setText(R.string.ON);
                    ShowText("ON");
                }else{
                    microgear.publish("/relaylight","OFF");
                    switch1.setText(R.string.OFF);
                    ShowText("OFF");
                }
                Log.i("Switch", "click");
            }
        });
	}

	// Toast func()
	protected void ShowText(String msg){
        Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT  ).show();
    }

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
	}

	protected void onDestroy() {
		super.onDestroy();
		microgear.disconnect();
	}
	@Override
	protected void onResume() {
		super.onResume();
		microgear.bindServiceResume();
	}

    private void setMessage(String topic, String message){
        text2 =
                findViewById(R.id.textView2);
        text3 =
                findViewById(R.id.textView3);
        text4 =
                findViewById(R.id.textView4);

        if(topic.equals("/" + appid + "/status_relaylight")){
            String status = "status : ";
            text4.setText(status);
            text4.append(message);
        }
        if(topic.equals("/" + appid + "/dht1")){
            String[] DHT = message.split(",");
            text2.setText("Temp "+DHT[0]+"  °C"); //temp
            temperature.setValue(Integer.valueOf(DHT[0]));
            text3.setText("Hum "+DHT[1]+" %RH"); //humi
            humidity.setValue(Integer.valueOf(DHT[1]));
        }
    }

	class MicrogearCallBack implements MicrogearEventListener{
        @Override
        public void onConnect() {
            Message msg = handler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString(key, "Now I'm connected with netpie");
            msg.setData(bundle);
            handler.sendMessage(msg);
            Log.i("Connected","Now I'm connected with netpie");
        }

        @Override
        public void onMessage(String topic, String message) {
            Message msg = handler.obtainMessage();
            Bundle bundle = new Bundle();
            //bundle.putString(key, topic+" : "+message);
            bundle.putString(key, message);
            msg.setData(bundle);
            handler.sendMessage(msg);
            Log.i("Message",topic+" : "+message);
            setMessage(topic,message);
        }

        @Override
        public void onPresent(String token) {
//            Message msg = handler.obtainMessage();
//            Bundle bundle = new Bundle();
//            bundle.putString(key, "New friend Connect :"+token);
//            msg.setData(bundle);
//            handler.sendMessage(msg);
            Log.i("present","New friend Connect :"+token);
        }

        @Override
        public void onAbsent(String token) {
//            Message msg = handler.obtainMessage();
//            Bundle bundle = new Bundle();
//            bundle.putString(key, "Friend lost :"+token);
//            msg.setData(bundle);
//            handler.sendMessage(msg);
            Log.i("absent","Friend lost :"+token);
        }

        @Override
        public void onDisconnect() {
            Message msg = handler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString(key, "Disconnected");
            msg.setData(bundle);
            handler.sendMessage(msg);
            Log.i("disconnect","Disconnected");
        }

        @Override
        public void onError(String error) {
            Message msg = handler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString(key, error);
            msg.setData(bundle);
            handler.sendMessage(msg);
            Log.i("exception","Exception : "+error);
        }

        @Override
        public void onInfo(String info) {
            Message msg = handler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString(key, info);
            msg.setData(bundle);
            handler.sendMessage(msg);
            Log.i("info","Info : "+info);
        }
	}

}
