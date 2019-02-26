package pl.pawelkleczkowski.mushroom_keeper;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.KeyListener;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class option extends AppCompatActivity {
    boolean CantEdit = true;
    private static final String MY_PREFS = "my_prefs";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_option);

        EditText IDline = findViewById(R.id.idline);
        EditText username = findViewById(R.id.name);
        Editable(false);  // editable = false

        Button SaveInfo = findViewById(R.id.SaveInformation);
        Button button = findViewById(R.id.btt);

        SharedPreferences shared = getSharedPreferences(MY_PREFS,Context.MODE_PRIVATE); //point to my_prefs
        IDline.setText(shared.getString("IDLINE", ""));     // set IDLINE if exist
        username.setText(shared.getString("USERNAME", "")); // set USERNAME if exist

        SaveInfo.setOnClickListener((View v) -> {
            if(CantEdit){
                CantEdit = false;
                Editable(true);
                SaveInfo.setText(getString(R.string.save));
            }else {
                CantEdit = true;
                Editable(false);
                SaveInfo.setText(getString(R.string.Edit));
                SharedPreferences.Editor editor = shared.edit();
                editor.putString("IDLINE", IDline.getText().toString());
                editor.putString("USERNAME", username.getText().toString());
                editor.apply();
                Toast.makeText(option.this, "บันทึกเรียบร้อย", Toast.LENGTH_SHORT).show();
            }
        });

        button.setOnClickListener((View v) -> finish() );       //for go back
    }

    private void Editable(boolean b){
        EditText IDline = findViewById(R.id.idline);
        EditText username = findViewById(R.id.name);
        if(b){ //True
            username.setKeyListener((KeyListener)username.getTag());    // set on
            IDline.setKeyListener((KeyListener)IDline.getTag());
        }else{  //False
            username.setTag(username.getKeyListener()); //remember default Tag(state)
            username.setKeyListener(null);      // set off
            IDline.setTag(IDline.getKeyListener());
            IDline.setKeyListener(null);
        }
    }
}
