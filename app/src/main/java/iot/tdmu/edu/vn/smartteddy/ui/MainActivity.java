package iot.tdmu.edu.vn.smartteddy.ui;


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import iot.tdmu.edu.vn.smartteddy.audio.AudioRecordHelper;

public class MainActivity extends AppCompatActivity {

    //Button btnNhacVN,btnMusicEN,btnStory;
    ImageButton btnNhacVN,btnMusicEN,btnStory,btnConversation;
    Switch aSwitch;
    Intent intent;
    //TextView txtTest;
    int machuyendoi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dung);
        final SharedPreferences preferences = getSharedPreferences("machuyendoi",MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();
        machuyendoi = preferences.getInt("trangthai",1);
        editor.putInt("trangthai",machuyendoi);
        editor.apply();
        aSwitch = (Switch) findViewById(R.id.mySwitch);
        aSwitch.setSelected(machuyendoi == 1);

        //txtTest = (TextView) findViewById(R.id.txtTest);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    //txtTest.setText("Teddy");
                    machuyendoi = 1;
                }else {
                    //txtTest.setText("App");
                    machuyendoi = 0;
                }

                editor.putInt("trangthai",machuyendoi);
                editor.apply();
            }
        });

        btnNhacVN = (ImageButton) findViewById(R.id.btnMusicVN);
        btnMusicEN = (ImageButton) findViewById(R.id.btnMusicEN);
        btnStory = (ImageButton) findViewById(R.id.btnStory);
        btnNhacVN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this,MusicActivity.class);
                intent.putExtra("MA",1);
                intent.putExtra("MACHUYENDOI",machuyendoi);
                startActivity(intent);
            }
        });

        btnMusicEN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this,MusicActivity.class);
                intent.putExtra("MA",2);
                intent.putExtra("MACHUYENDOI",machuyendoi);
                startActivity(intent);
            }
        });

        btnStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this,MusicActivity.class);
                intent.putExtra("MA",3);
                intent.putExtra("MACHUYENDOI",machuyendoi);
                startActivity(intent);
            }
        });

        btnConversation = (ImageButton) findViewById(R.id.btnConversation);
        btnConversation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRecod = new Intent(MainActivity.this,Audio2Activity.class);
                startActivity(intentRecod);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.connectQR:
                Intent intent1 = new Intent(MainActivity.this,ConectTeddybyQR_Activity.class);
                startActivity(intent1);
                return true;
            case R.id.connectBT:
                Intent intent2 = new Intent(MainActivity.this,Scan_BlutoothActivity.class);
                startActivity(intent2);
                return true;
            case R.id.logout:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
