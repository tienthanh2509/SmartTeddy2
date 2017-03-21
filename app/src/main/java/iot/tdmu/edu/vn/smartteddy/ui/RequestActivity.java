package iot.tdmu.edu.vn.smartteddy.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.ScanResult;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import iot.tdmu.edu.vn.smartteddy.wifi.WifiHotspot;

public class RequestActivity extends AppCompatActivity {

    ListView lvListHotspot;
    WifiHotspot HU;
    Button btnScan,btnQR;
    String mac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        btnScan = (Button) findViewById(R.id.btnScan);
        lvListHotspot = (ListView) findViewById(R.id.lvListHotspot);

        Intent intent2 = getIntent();
        mac = intent2.getStringExtra("MAC1");
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences(ConectTeddybyQR_Activity.addressBT, Context.MODE_PRIVATE);
                String mac_bt = preferences.getString("address","0");

                if (mac_bt.equals(mac)){
                    HU = new WifiHotspot(RequestActivity.this);

                    HU.showHotspotsList(lvListHotspot);
                }

                Intent intentScanBT = getIntent();
                String mac = intentScanBT.getStringExtra("ADDRESS");
                int maBT = intentScanBT.getIntExtra("MABT",0);
                if (maBT == 20){
                    HU = new WifiHotspot(RequestActivity.this);

                    HU.showHotspotsList(lvListHotspot);
                }

            }
        });
        HU = new WifiHotspot(RequestActivity.this);
        final List<ScanResult> list = HU.getHotspotsList();

        lvListHotspot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String a = list.get(position).SSID;



                Intent intent = new Intent(RequestActivity.this,ConnectTeddyActivity.class);
                intent.putExtra("SSID",a);
                intent.putExtra("MAC2",mac);
                startActivity(intent);
            }
        });

        /*btnQR = (Button) findViewById(R.id.btnQR);
        btnQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RequestActivity.this,ConectTeddybyQR_Activity.class);
                startActivity(intent);
            }
        });*/
    }
}
