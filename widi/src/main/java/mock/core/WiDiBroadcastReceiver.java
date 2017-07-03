package mock.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.net.InetAddress;
import java.net.UnknownHostException;

import mock.core.thread.RequestConnectionInfoThread;
import mock.net.NetworkInfo;

import mock.net.wifi.p2p.WifiP2pDevice;
import mock.net.wifi.p2p.WifiP2pInfo;
import mock.net.wifi.p2p.WifiP2pManager;

public class WiDiBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        String action = intent.getAction();
        Log.d(WiDi.TAG, action);

        if (WiDiIntent.CONNECT.equals(action)) {
            boolean state = intent.getBooleanExtra(WiDiExtra.EXTRA_CONNECT_STATE, false);

            Log.d(WiDi.TAG, WiDiExtra.EXTRA_CONNECT_STATE + " = " + Boolean.toString(state));

            Intent ntnt = new Intent(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);

            Log.v(WiDi.TAG, "Intent created");

            NetworkInfo networkInfo = new NetworkInfo(13, 0, "WIFI_P2P", "");

            Log.v(WiDi.TAG, "networkInfo instance created");

            if (state) {
                networkInfo.setDetailedState(NetworkInfo.DetailedState.CONNECTED, null, null);
                Log.v(WiDi.TAG, "DetailedState for networkInfo is added as CONNECTED");
            }
            ntnt.putExtra(WifiP2pManager.EXTRA_NETWORK_INFO, networkInfo);
            Log.v(WiDi.TAG, "intent receives networkInfo as EXTRA_NETWORK_INFO");

            WifiP2pInfo wifiP2pInfo = new WifiP2pInfo();

            Log.v(WiDi.TAG, "wifiP2pInfo instance created");

            if (state) {
                String groupOwnerAddress = intent.getStringExtra(WiDiExtra.EXTRA_GROUP_OWNER_ADDRESS);
                boolean isGroupOwner = intent.getBooleanExtra(WiDiExtra.EXTRA_GROUP_OWNER, false);
                try {
                    wifiP2pInfo = new WifiP2pInfo(InetAddress.getByName(groupOwnerAddress), isGroupOwner);

                    Log.v(WiDi.TAG, "groupOwnerAddress is Added to WifiP2pInfo");
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                    Log.e(WiDi.TAG, e.getMessage());
                }
            }

            WifiP2pManager.wifiP2pInfo = wifiP2pInfo;

            ntnt.putExtra(WifiP2pManager.EXTRA_WIFI_P2P_INFO, wifiP2pInfo);
            Log.v(WiDi.TAG, "intent receives wifiP2pInfo as EXTRA_WIFI_P2P_INFO");


            Log.v(WiDi.TAG, "sending Broadcast....");

            context.sendBroadcast(ntnt);

            Log.v(WiDi.TAG, "Broadcast sent.");

        }
        if (WiDiIntent.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION_EMULATOR.equals(action)) {

            Log.d(WiDi.TAG, "Received : " + WiDiIntent.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION_EMULATOR);

            WifiP2pDevice wifiP2pDevice = new WifiP2pDevice();
            wifiP2pDevice.deviceAddress = intent.getStringExtra("wifiP2pDeviceIp");
            wifiP2pDevice.deviceName = intent.getStringExtra("wifiP2pDeviceName");

            Intent ntnt = new Intent(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

            ntnt.putExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE,wifiP2pDevice);

            context.sendBroadcast(ntnt);

        }
    }
}
