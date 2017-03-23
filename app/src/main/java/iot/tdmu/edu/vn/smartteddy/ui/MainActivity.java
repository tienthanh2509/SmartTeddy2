package iot.tdmu.edu.vn.smartteddy.ui;


import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Calendar;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import iot.tdmu.edu.vn.smartteddy.audio.AudioRecordHelper;

public class MainActivity extends AppCompatActivity {

    //Button btnNhacVN,btnMusicEN,btnStory;
    ImageButton btnNhacVN,btnMusicEN,btnStory,btnConversation,btnAlarms,btnChat;
    Switch aSwitch;
    Intent intent;
    //TextView txtTest;
    int machuyendoi;
    private int  mHour, mMinute;
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
        btnAlarms = (ImageButton) findViewById(R.id.btnAlarms);
        btnChat = (ImageButton) findViewById(R.id.btnChat);
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

        btnAlarms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                          final String time = hourOfDay + "|" + minute;
                        try {
                            final io.socket.client.Socket socket = IO.socket("http://103.27.236.133:3000/");
                            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                                @Override
                                public void call(Object... args) {
                                    try {


                                        socket.emit("alarm",time);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            socket.connect();


                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }
                },mHour,mMinute,false);
                timePickerDialog.show();


            }

        });
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainActivity.this,ChatActivity.class);
                startActivity(intent1);
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
            case R.id.info_TG:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                builder1.setMessage("SmartTeddy v1.0.1 \n Tác Giả: \n - Phạm Tiến Thành \n - Nguyễn Vũ Linh \n - Trịnh Văn Dũng \n - Nguyễn Hoàng Duy");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
