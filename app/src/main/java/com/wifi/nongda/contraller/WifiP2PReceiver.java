package com.wifi.nongda.contraller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;

import com.wifi.nongda.MainActivity;

/**
 * Created by 段碧伟 on 2016/2/17.
 */
public class WifiP2PReceiver extends BroadcastReceiver {
    private final String TAG = "WiFiP2PReceiver";
    private WifiP2pManager.Channel channel;
    private WifiP2pManager manager;
    private MainActivity activity;
    public WifiP2PReceiver(WifiP2pManager.Channel channel, WifiP2pManager manager,
                           MainActivity activity) {
        super();
        this.channel = channel;
        this.manager = manager;
        this.activity = activity;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)) {
            // 确定Wi-Fi Direct模式是否已经启用
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                manager.requestPeers(channel, (WifiP2pManager.PeerListListener) activity);
            } else {
                // ...
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            // 对等点设备列表改变
            if (manager != null) {
                manager.requestPeers(channel, (WifiP2pManager.PeerListListener) activity);
            } else {
                // ...
            }
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION
                .equals(action)) {
            // 连接状态改变
            if (manager != null) {
                NetworkInfo networkInfo = (NetworkInfo) intent
                        .getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
                if (networkInfo.isConnected()) {
                    manager.requestConnectionInfo(channel,
                            (WifiP2pManager.ConnectionInfoListener) activity);
                } else {
                    // ...
                }
            } else {
                // ...
            }
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION
                .equals(action)) {
            //连接设备信息改变
            // ...
        }
    }

}
