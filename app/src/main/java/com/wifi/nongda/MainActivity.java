package com.wifi.nongda;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.wifi.nongda.Datebase.DataDB;
import com.wifi.nongda.contraller.FileTransferService;
import com.wifi.nongda.contraller.PictureOfLiActivity;
import com.wifi.nongda.contraller.WifiP2PReceiver;
import com.wifi.nongda.model.DataInfo;
import com.wifi.nongda.view.DataAdapter;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MainActivity extends AppCompatActivity implements WifiP2pManager.PeerListListener,WifiP2pManager.ConnectionInfoListener {

    private static final int SOCKET_PORT = 0;
    private static final String TAG = "MainActivity";
    private static final String SERVICE_TYPE = "";

    private final IntentFilter intentFilter = new IntentFilter();
    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;
    private WifiP2PReceiver mReceiver;
    PopupMenu popup;
    ArrayList<DataInfo> datas;
    ListView dataList;
    DataDB dataDB;
    public static final String ISLI = "is_li";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
            }
        });
        datas = initList();
        DataAdapter adapter = new DataAdapter(this, datas);
        dataList = (ListView) findViewById(R.id.data_list);
        dataList.setVisibility(View.INVISIBLE);
        dataList.setAdapter(adapter);

        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(MainActivity.this, getMainLooper(), null);
        manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                //...
            }

            @Override
            public void onFailure(int reasonCode) {
                //...
            }
        });


        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        // 表示可用的对等点的列表发生了改变
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        // 表示Wi-Fi对等网络的连接状态发生了改变
        intentFilter
                .addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        // 设备配置信息发生了改变
        intentFilter
                .addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showPopupMenu(View v) {
        popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.shuaxing:
                        dataList.setVisibility(View.VISIBLE);
                        dataDB = new DataDB(MainActivity.this);
                        if (dataDB.query().size() != 0) {
                            dataDB.delete();
                            for (DataInfo d : initList()) {
                                dataDB.insert(d);
                            }
                        }
                        Toast.makeText(MainActivity.this, "刷新", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.li_shendu_tu:
                        Intent i = new Intent(MainActivity.this, PictureOfLiActivity.class);
                        i.putExtra(ISLI, 1);
                        startActivity(i);
                        return true;
                    case R.id.shendu_yaqiang_tu:
                        Intent i2 = new Intent(MainActivity.this, PictureOfLiActivity.class);
                        i2.putExtra(ISLI, 0);
                        startActivity(i2);
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.inflate(R.menu.popup_menu);
        popup.show();
    }

    public ArrayList<DataInfo> initList() {
        ArrayList<DataInfo> dataInfos = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            DataInfo data = new DataInfo();
            data.setLi(i);
            float f1 = i;
            f1 *= 1.2;
            data.setShenDu(f1);
            f1 *= 1.2;
            data.setYaQiang(f1);
            dataInfos.add(data);
        }
        return dataInfos;
    }

    @Override
    public void onPeersAvailable(WifiP2pDeviceList peers) {
        Collection<WifiP2pDevice> collection = peers.getDeviceList();
        if (collection == null || collection.size() == 0) {
            Toast.makeText(MainActivity.this, "未找到网络", Toast.LENGTH_LONG).show();
        } else {
            //更新显示列表
            //...
            Toast.makeText(MainActivity.this, "已成功连接到网络", Toast.LENGTH_LONG).show();
            List peersList = new ArrayList();
            peersList.addAll(collection);
            WifiP2pDevice device = (WifiP2pDevice) peersList.get(0);
            WifiP2pConfig config = new WifiP2pConfig();
            config.deviceAddress = device.deviceAddress;
            config.wps.setup = WpsInfo.PBC;
            manager.connect(channel, config, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    // ...
                }

                @Override
                public void onFailure(int reason) {
                    // ...
                }
            });
        }
        //...
    }

    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo info) {
        WifiP2pInfo connectInfo = info;
        if (info.groupFormed && info.isGroupOwner) {
            Intent i = new Intent(MainActivity.this, FileTransferService.class);
            i.setAction(FileTransferService.ACTION_RECEIVE_FILE);
            i.putExtra("port", SOCKET_PORT);
            startService(i);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mReceiver = new WifiP2PReceiver(channel,
                manager, this);
        registerReceiver(mReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(mReceiver);
        super.onPause();
    }
}