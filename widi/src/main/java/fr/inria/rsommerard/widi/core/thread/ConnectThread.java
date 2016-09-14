package fr.inria.rsommerard.widi.core.thread;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import fr.inria.rsommerard.widi.core.Protocol;
import fr.inria.rsommerard.widi.core.WiDi;
import fr.inria.rsommerard.widi.net.wifi.p2p.WifiP2pConfig;
import fr.inria.rsommerard.widi.net.wifi.p2p.WifiP2pManager;

public class ConnectThread extends Thread implements Runnable {

    private final WifiP2pManager.ActionListener mActionListener;
    private final WifiP2pConfig mWifiP2pConfig;
    private final Socket mSocket;

    public ConnectThread(final WifiP2pConfig wifiP2pConfig, final WifiP2pManager.ActionListener actionListener) {
        mWifiP2pConfig = wifiP2pConfig;
        mActionListener = actionListener;
        mSocket = new Socket();
    }

    @Override
    public void run() {
        try {
            mSocket.connect(new InetSocketAddress(WiDi.SERVER_ADDRESS, WiDi.SERVER_PORT), WiDi.SOCKET_TIMEOUT);

            // Warning: Order is important! First create output for the header!
            ObjectOutputStream oOStream = new ObjectOutputStream(mSocket.getOutputStream());
            ObjectInputStream oIStream = new ObjectInputStream(mSocket.getInputStream());
            
            oOStream.writeObject(Protocol.CONNECT);
            oOStream.flush();

            String ack = (String) oIStream.readObject();

            if (!Protocol.ACK.equals(ack)) {
                error("ACK not received correctly");
                return;
            }

            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();

            String jsonWifiP2pConfig = gson.toJson(mWifiP2pConfig);
            Log.d(WiDi.TAG, jsonWifiP2pConfig);
            oOStream.writeObject(jsonWifiP2pConfig);
            oOStream.flush();

            ack = (String) oIStream.readObject();

            if (!Protocol.ACK.equals(ack)) {
                error("ACK received, the device is maybe already connected");
                return;
            }

            if (mActionListener != null) {
                mActionListener.onSuccess();
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            error(e.getMessage());
        }
    }

    private void error(String msg) {
        Log.e(WiDi.TAG, "Error: " + msg);

        if (mActionListener != null) {
            mActionListener.onFailure(WifiP2pManager.ERROR);
        }

        if (!mSocket.isClosed()) {
            try {
                mSocket.close();
            } catch (IOException e) {
                // Nothing
            }
        }
    }
}

