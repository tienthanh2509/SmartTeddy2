package iot.tdmu.edu.vn.smartteddy.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import iot.tdmu.edu.vn.smartteddy.audio.AudioRecordHelper;


public class Audio2Activity extends AppCompatActivity {

    Button stopRecord,playRecord,sendRecord;
    AudioRecordHelper audioRecordHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio2);
        sendRecord = (Button) findViewById(R.id.sendRecord);
        playRecord = (Button) findViewById(R.id.playRecord);
        sendRecord.setVisibility(View.GONE);
        audioRecordHelper  = new AudioRecordHelper(Audio2Activity.this);

        setButtonHandlers();
        enableButtons(false);

        /*playRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (audioRecordHelper.isRecording()){
                    audioRecordHelper.startRecording();
                    playRecord.setText("DỪNG");
                }else {
                    audioRecordHelper.stopRecording();
                    playRecord.setText("GHI");
                }
            }
        });*/


        sendRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final byte[] data_record = audioRecordHelper.getByteArray();
                Log.e("TAG",data_record.length + "");
                try {
                    final io.socket.client.Socket socket = IO.socket("http://103.27.236.133:3000/");
                    socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                        @Override
                        public void call(Object... args) {
                            try {
                                JSONObject obj = new JSONObject();
                                obj.put("data_record",data_record);

                                socket.emit("recordBuffer",obj);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    socket.connect();


                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void setButtonHandlers() {
        ((Button) findViewById(R.id.playRecord)).setOnClickListener(btnClick);
        ((Button) findViewById(R.id.stopRecord)).setOnClickListener(btnClick);
    }

    private void enableButton(int id, boolean isEnable) {
        ((Button) findViewById(id)).setEnabled(isEnable);
    }


    private void enableButtons(boolean isRecording) {
        enableButton(R.id.playRecord, !isRecording);
        enableButton(R.id.stopRecord, isRecording);
    }
    private View.OnClickListener btnClick = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.playRecord: {
                    enableButtons(true);
                    audioRecordHelper.startRecording();
                    break;
                }
                case R.id.stopRecord: {
                    enableButtons(false);
                    audioRecordHelper.stopRecording();
                    sendRecord.setVisibility(View.VISIBLE);
                    break;
                }
            }
        }
    };
}
