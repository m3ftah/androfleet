package mock.core.thread;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import mock.core.Device;
import mock.core.Protocol;
import mock.core.WiDi;
import mock.net.wifi.p2p.WifiP2pDevice;
import mock.net.wifi.p2p.WifiP2pDeviceList;
import mock.net.wifi.p2p.WifiP2pManager;

public class RequestPeersThread extends Thread implements Runnable {

    private final WifiP2pManager.PeerListListener mPeerListListener;
    private final Socket mSocket;

    public RequestPeersThread(WifiP2pManager.PeerListListener listener) {
        mPeerListListener = listener;
        mSocket = new Socket();
    }

    @Override
    public void run() {
        try {
            mSocket.connect(new InetSocketAddress(WiDi.SERVER_ADDRESS, WiDi.SERVER_PORT), WiDi.SOCKET_TIMEOUT);
            
            // Warning: Order is important! First create output for the header!
            ObjectOutputStream oOStream = new ObjectOutputStream(mSocket.getOutputStream());
            ObjectInputStream oIStream = new ObjectInputStream(mSocket.getInputStream());

            oOStream.writeObject(Protocol.REQUEST_PEERS);
            oOStream.flush();

            String ack = (String) oIStream.readObject();

            if (!Protocol.ACK.equals(ack)) {
                error("ACK not received correctly");
                return;
            }

            String json = (String) oIStream.readObject();

            Log.d(WiDi.TAG, json);

            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();

            Type arrayListType = new TypeToken<ArrayList<Device>>() {}.getType();

            List<Device> deviceList = gson.fromJson(json, arrayListType);

            final WifiP2pDeviceList peers = new WifiP2pDeviceList();

            for (Device device : deviceList) {
                WifiP2pDevice d = new WifiP2pDevice();
                d.deviceName = device.deviceName;
                d.deviceAddress = device.deviceAddress;
                peers.update(d);
            }

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    mPeerListListener.onPeersAvailable(peers);
                }
            });


        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            error(e.getMessage());
        }
    }

    private void error(String msg) {
        Log.e(WiDi.TAG, "Error: " + msg);

        if (!mSocket.isClosed()) {
            try {
                mSocket.close();
            } catch (IOException e) {
                // Nothing
            }
        }
    }
}
