package iot.tdmu.edu.vn.smartteddy.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.vision.barcode.Barcode;

public class ConectTeddybyQR_Activity extends AppCompatActivity {

    Button scanbtn,btnConnectQR;
    TextView result;
    public static final int REQUEST_CODE = 100;
    public static final int PERMISSION_REQUEST = 200;

    String stringWifi;
    final public static String addressBT = "addressBT" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conect_teddyby_qr_);

        scanbtn = (Button) findViewById(R.id.scanbtn);
        result = (TextView) findViewById(R.id.result);
        btnConnectQR = (Button) findViewById(R.id.btnConnectQR);
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, PERMISSION_REQUEST);
        }
        scanbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConectTeddybyQR_Activity.this, ScanQR_Activity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            if(data != null){
                final Barcode barcode = data.getParcelableExtra("barcode");
                result.post(new Runnable() {
                    @Override
                    public void run() {
                        result.setText(barcode.displayValue);
                        stringWifi = barcode.displayValue;
                    }
                });
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


        btnConnectQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int begin = stringWifi.indexOf("|") + 1;
                int end = stringWifi.length();
                String mac = stringWifi.substring(begin,end);
                Log.e("TAG",mac);
                SharedPreferences preferences = getSharedPreferences(addressBT,Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                editor.putString("address",mac);
                editor.apply();
                Intent intent = new Intent(ConectTeddybyQR_Activity.this,RequestActivity.class);
                intent.putExtra("MAC1",mac);
                startActivity(intent);

            }
        });

    }
}
