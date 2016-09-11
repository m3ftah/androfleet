package fr.inria.rsommerard.widi.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.net.InetAddress;
import java.net.UnknownHostException;

import fr.inria.rsommerard.widi.net.NetworkInfo;

import fr.inria.rsommerard.widi.net.wifi.p2p.WifiP2pInfo;
import fr.inria.rsommerard.widi.net.wifi.p2p.WifiP2pManager;

public class WiDiBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        String action = intent.getAction();
        Log.d(WiDi.TAG, action);

        if (WiDiIntent.CONNECT.equals(action)) {
            boolean state = intent.getBooleanExtra(WiDiExtra.EXTRA_CONNECT_STATE, false);

            Log.d(WiDi.TAG, WiDiExtra.EXTRA_CONNECT_STATE + " = " + Boolean.toString(state));

            Intent ntnt = new Intent(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);

            NetworkInfo networkInfo = new NetworkInfo(13, 0, "WIFI_P2P", "");
            if (state) {
                networkInfo.setDetailedState(NetworkInfo.DetailedState.CONNECTED, null, null);
            }
            ntnt.putExtra(WifiP2pManager.EXTRA_NETWORK_INFO, networkInfo);

            WifiP2pInfo wifiP2pInfo = new WifiP2pInfo();

            if (state) {
                String groupOwnerAddress = intent.getStringExtra(WiDiExtra.EXTRA_GROUP_OWNER_ADDRESS);
                boolean isGroupOwner = intent.getBooleanExtra(WiDiExtra.EXTRA_GROUP_OWNER, false);
                try {
                    wifiP2pInfo = new WifiP2pInfo(InetAddress.getByName(groupOwnerAddress), isGroupOwner);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                    Log.e(WiDi.TAG, e.getMessage());
                }
            }

            ntnt.putExtra(WifiP2pManager.EXTRA_WIFI_P2P_INFO, wifiP2pInfo);

            context.sendBroadcast(ntnt);
        }
    }
}
