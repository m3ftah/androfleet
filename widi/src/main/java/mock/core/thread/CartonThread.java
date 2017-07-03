package mock.core.thread;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import mock.core.Protocol;
import mock.core.WiDi;
import mock.net.wifi.p2p.WifiP2pManager;

public class CartonThread extends Thread implements Runnable {

    private final Socket mSocket;

    public CartonThread() {
        mSocket = new Socket();
    }

    @Override
    public void run() {
        try {
            mSocket.connect(new InetSocketAddress(WiDi.SERVER_ADDRESS, WiDi.SERVER_PORT), WiDi.SOCKET_TIMEOUT);

            // Warning: Order is important! First create output for the header!
            ObjectOutputStream oOStream = new ObjectOutputStream(mSocket.getOutputStream());
            ObjectInputStream oIStream = new ObjectInputStream(mSocket.getInputStream());
            
            oOStream.writeObject(Protocol.CARTON);
            oOStream.flush();

            String ack = (String) oIStream.readObject();

            if (Protocol.ACK.equals(ack)) {
                return;
            }

            error("ACK not received correctly");

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
