package iot.tdmu.edu.vn.smartteddy.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class ChatActivity extends AppCompatActivity {

    ImageButton btnGui;
    TextView txtNoiDung;
    EditText txtTinNhan;
    private io.socket.client.Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        btnGui = (ImageButton) findViewById(R.id.btnGui);
        txtNoiDung = (TextView) findViewById(R.id.txtNoiDung);
        txtTinNhan = (EditText) findViewById(R.id.txtTinNhan);
        try {
            socket = IO.socket("http://103.27.236.133:3000/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        socket.connect();
        btnGui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                txtNoiDung.setText(txtTinNhan.getText());
                final String msg = txtNoiDung.getText().toString();
                Log.e("TAG",msg);

                try {
                    socket.emit("speak",msg);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
