package mock.core.thread;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import mock.core.Protocol;
import mock.core.WiDi;
import mock.net.wifi.p2p.WifiP2pInfo;
import mock.net.wifi.p2p.WifiP2pManager;

public class RequestConnectionInfoThread extends Thread implements Runnable {

    public static WifiP2pInfo wifiP2pInfo;
    private final WifiP2pManager.ConnectionInfoListener mConnectionInfoListener;
    private final Socket mSocket;

    public RequestConnectionInfoThread(WifiP2pManager.ConnectionInfoListener listener) {
        mConnectionInfoListener = listener;
        mSocket = new Socket();
    }

    @Override
    public void run() {
        try {
            mSocket.connect(new InetSocketAddress(WiDi.SERVER_ADDRESS, WiDi.SERVER_PORT), WiDi.SOCKET_TIMEOUT);
            
            // Warning: Order is important! First create output for the header!
            ObjectOutputStream oOStream = new ObjectOutputStream(mSocket.getOutputStream());
            ObjectInputStream oIStream = new ObjectInputStream(mSocket.getInputStream());

            oOStream.writeObject(Protocol.REQUEST_CONNECTION_INFO);
            oOStream.flush();

            String ack = (String) oIStream.readObject();

            if (!Protocol.ACK.equals(ack)) {
                error("ACK not received correctly");
                return;
            }
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    mConnectionInfoListener.onConnectionInfoAvailable(wifiP2pInfo);
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
