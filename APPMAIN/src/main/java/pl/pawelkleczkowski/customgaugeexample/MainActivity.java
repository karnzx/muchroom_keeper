package pl.pawelkleczkowski.customgaugeexample;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.pm.ActivityInfo;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Button;
import android.widget.TextView;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

import io.netpie.microgear.Microgear;
import io.netpie.microgear.MicrogearEventListener;

import pl.pawelkleczkowski.customgauge.CustomGauge;

import static pl.pawelkleczkowski.customgaugeexample.nortify.channel_id;


public class MainActivity extends AppCompatActivity {


	private CustomGauge humidity;
	private CustomGauge temperature;

	protected TextView text1;
	protected TextView text2;
	protected TextView text3;
	protected TextView text4;
	protected TextView Time;

	private Microgear microgear = new Microgear(this);
	private String appid = "MUSHROOM21"; //APP_ID
	private String key = "pKbbMlgMoIDDubp"; //KEY
	protected String secret = "00issaBXLFQXVn0A4qbCIBg1U"; //SECRET
	protected String alias = "android";

    protected String FN_D = ""; //final date
    protected String FN_M = ""; //final month
    protected String last_msg = ""; // last message of harvest
	private NotificationManagerCompat notificationManager;
    String SOffline = "Offline",SOnline = "Online";


	// i dont know how to remove yellow tag ;w; so i use this @  -
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Bundle bundle = msg.getData();
			String string = bundle.getString(key);
			//text1 = findViewById(R.id.textView1);
            //text1.setText(string);
            //ShowText(string);
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

        //define button & switch & imageButton & animation
		Button button =
				findViewById(R.id.button);
        ImageButton imageButtonCart =
                findViewById(R.id.cart);
        final ImageButton  imageButtonCalendar =
                findViewById(R.id.calendar);
		final Switch switch1 =
				findViewById(R.id.switch1);
        ImageView imageView =
                findViewById(R.id.imageView);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.scale);

		//nortification
        notificationManager = NotificationManagerCompat.from(this);

		//animation
        imageView.startAnimation(animation);

        text1 = findViewById(R.id.textView1);
        text2 = findViewById(R.id.textView2);
        text3 = findViewById(R.id.textView3);
        text4 = findViewById(R.id.textView4);
        Time =  findViewById(R.id.Time);
        text1.setText(SOffline);
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/ZCOOL-Regular.ttf");
        button.setTypeface(typeface);
        switch1.setTypeface(typeface);

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
        //microgear.publish("/gearname/Node","OFF");
        microgear.chat("Node","OFF");
		switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(text1.getText() == SOffline) return; // if Node is offline
                if(switch1.isChecked()){
                    //microgear.publish("/gearname/Node","ON");
                    microgear.chat("Node","ON");
                    switch1.setText(R.string.ON);
                    ShowText("ON");
                }else{
                    //microgear.publish("/gearname/Node","OFF");
                    microgear.chat("Node","OFF");
                    switch1.setText(R.string.OFF);
                    ShowText("OFF");
                }
                Log.i("Switch", "click");
            }
        });

		button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //microgear.publish("/Node","REFESH");
                microgear.chat("Node","REFRESH");
            }
        });

		imageButtonCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this,cart.class); //start intent here
                startActivity(myIntent);
            }
        });

		imageButtonCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                //calendar.get(Calendar.YEAR);  //get year
                calendar.set(Calendar.MONTH, (Integer.parseInt(FN_M)-1));
                calendar.set(Calendar.DAY_OF_MONTH,Integer.parseInt(FN_D));
                long milliTime = calendar.getTimeInMillis();
                Intent myIntent = new Intent(MainActivity.this,calendar.class); //start intent here
                myIntent.putExtra("date", milliTime); //Long var
                startActivity(myIntent);

            }
        });

	}

	// Toast func()
	private void ShowText(String msg){
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

    @Override
    protected void onPause() {
        super.onPause();
        microgear.bindServiceResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        microgear.bindServiceResume();
}

    private void setMessage(String topic, String message){

        if(topic.equals("/" + appid + "/harvest")){
            if(!message.equals(last_msg) || message.equals("can harvest now")){
                last_msg = message;
                Time.setText(message.substring(message.indexOf('/')+1));    // ignore 1 space
                //Time.setText(message);    // show all
                if(message.equals("can harvest now")){
                    showNotification("Harvest Time");
                    Log.i("harvest","yes");
                }else{
                    FN_M = (message.substring(0,message.indexOf('-')));
                    FN_D = (message.substring(message.indexOf('-')+1,message.indexOf('/')));  // ignore '-' its self
                    showNotification(message.substring(message.indexOf('/')+1));
                    Log.i("harvest","no");
                }
            }
        }
        else if(topic.equals("/" + appid + "/NETstatus_Node")){
            if(message.equals("Online")){
                text1.setText(SOnline);
            }else{
                text1.setText(SOffline);
            }

        }
        else if(topic.equals("/" + appid + "/status_Node")){
            String status = "status : ";
            text4.setText(status);
            text4.append(message);
        }
        else if(topic.equals("/" + appid + "/DHT")){
            String[] DHT = message.split(",");
            text2.setText(DHT[0]);
            text2.append(" °C");
            temperature.setValue(Integer.valueOf(DHT[0]));
            text3.setText(DHT[1]);
            text3.append(" %");
            humidity.setValue(Integer.valueOf(DHT[1]));
        }
    }

    public void showNotification(String text) {
        Notification notification = new NotificationCompat.Builder(this, channel_id)
                .setSmallIcon(R.drawable.mr3)
                .setContentTitle("mush room")
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setAutoCancel(true)
                .setColor(Color.parseColor("#536DFE"))
                .build();

        notificationManager.notify(1000, notification);
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
            bundle.putString(key, topic+" : "+message);
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
            //ShowText(msg.getData().getString(key));
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
