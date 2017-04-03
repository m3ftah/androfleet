package fr.inria.rsommerard.fougere.wifidirect;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import fr.inria.rsommerard.fougere.Fougere;
import fr.inria.rsommerard.fougere.FougereDistance;
import fr.inria.rsommerard.fougere.data.Data;
import fr.inria.rsommerard.fougere.data.DataPool;
import fr.inria.rsommerard.fougere.data.wifidirect.WiFiDirectData;
import fr.inria.rsommerard.fougere.data.wifidirect.WiFiDirectDataPool;

/**
 * Created by Romain on 10/08/16.
 */
public class Active implements Runnable {

    private static final int SOCKET_TIMEOUT = 7000;
    private static final int NB_ATTEMPTS = 1000;

    private final InetAddress groupOwnerAddress;
    private final WiFiDirectDataPool wiFiDirectDataPool;
    private final DataPool dataPool;
    private final WifiP2pDevice me;
    private final FougereDistance fougereDistance;
    private Socket socket;
    private int failCounter;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    public Active(final WifiP2pDevice me, final InetAddress groupOwnerAddress,
                  final DataPool dataPool, final WiFiDirectDataPool wiFiDirectDataPool,
                  final FougereDistance fougereDistance) {
        this.groupOwnerAddress = groupOwnerAddress;
        this.wiFiDirectDataPool = wiFiDirectDataPool;
        this.dataPool = dataPool;
        this.me = me;
        this.fougereDistance = fougereDistance;
    }

    @Override
    public void run() {
        Log.d(Fougere.TAG, "[Active] Started");

        while ( this.socket == null || ! this.socket.isConnected()) {
            // Warning: This is important to recreate a fresh object after a connect attempt!
            this.socket = new Socket();

            try {
                this.socket.connect(new InetSocketAddress(this.groupOwnerAddress, 11131), SOCKET_TIMEOUT);
                // Warning: Order is important! First create output for the header!
                this.output = new ObjectOutputStream(this.socket.getOutputStream());
                this.input = new ObjectInputStream(this.socket.getInputStream());
            } catch (IOException e) {
                this.failCounter++;
                Log.e(Fougere.TAG, "[Active] failCounter: " + this.failCounter);
            }

            if (this.failCounter >= NB_ATTEMPTS) {
                Log.e(Fougere.TAG, "[Active] Cannot open socket with the groupOwner after " +
                        this.failCounter + " attempts");
                return;
            }
        }

        Log.d(Fougere.TAG, "[Active] OK");

        try {
            this.process();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            Log.e(Fougere.TAG, "[Active] KO : " + e.getMessage());
        } finally {
            this.release();
        }
    }

    private void process() throws IOException, ClassNotFoundException {
        this.send(Protocol.HELLO);

        if ( ! Protocol.HELLO.equals(this.receive())) {
            Log.e(Fougere.TAG, "[Active] " + Protocol.HELLO + " not received");
            return;
        }

        this.send(me.deviceAddress);

        if ( ! Protocol.ACK.equals(this.receive())) {
            Log.e(Fougere.TAG, "[Active] " + Protocol.ACK + " not received");
            return;
        }

        String deviceAddress = this.receive();
        this.send(Protocol.ACK);

        if ( ! this.fougereDistance.containsUser(deviceAddress)) {
            this.fougereDistance.addUser(deviceAddress);
        }

        if ( ! Protocol.SEND.equals(this.receive())) {
            Log.e(Fougere.TAG, "[Active] " + Protocol.SEND + " not received");
            return;
        }

        List<WiFiDirectData> dataTemp = this.wiFiDirectDataPool.getAll(); // TODO: select data
        List<WiFiDirectData> dataToSend = new ArrayList<>();

        for (WiFiDirectData dt : dataTemp) {
            Log.d(Fougere.TAG, "[WiFiDirectDataPool] update: " + dt.toString());
            WiFiDirectData dtr = WiFiDirectData.reset(dt);

            dtr.setTtl(dtr.getTtl() - 1);
            dataToSend.add(dtr);
            String timestamp = ( (Long) (System.currentTimeMillis()/1000)).toString();
            Log.d(Fougere.TAG, "[" +timestamp + "]" + "[" + getlocation() + "]" + "[" +DeviceInfo.deviceName + "]" +"[Active][Sent]: " + dt.toString());
        }

        this.send(WiFiDirectData.gsonify(dataToSend));

        if ( ! Protocol.ACK.equals(this.receive())) {
            Log.e(Fougere.TAG, "[Active] " + Protocol.ACK + " not received");
            return;
        }

        for (WiFiDirectData dt : dataTemp) {
            int newSent = dt.getSent() + 1;

            if (newSent >= dt.getDisseminate()) {
                this.wiFiDirectDataPool.delete(dt);
                continue;
            }

            dt.setSent(newSent);


            this.wiFiDirectDataPool.update(dt);
        }

        this.send(Protocol.SEND);

        String json = receive();

        List<WiFiDirectData> dataReceived = WiFiDirectData.deGsonify(json);
        for (WiFiDirectData dt : dataReceived) {
            String timestamp = ( (Long) (System.currentTimeMillis()/1000)).toString();
            Log.d(Fougere.TAG, "[" +timestamp + "]" + "[" + getlocation() + "]" + "[" +DeviceInfo.deviceName + "]" +"[Active][Received]: " + dt.toString());
            this.dataPool.insert(Data.reset(WiFiDirectData.toData(dt)));
            Fougere.fougereListener.onDataReceived(WiFiDirectData.toData(dt));
            if (dt.getTtl() > 0) this.wiFiDirectDataPool.insert(dt);

        }

        this.send(Protocol.ACK);

        Log.d(Fougere.TAG, "[Active] Process done");

        this.fougereDistance.updateDistance(deviceAddress);

        if ( ! Protocol.OUT.equals(this.receive())) {
            Log.e(Fougere.TAG, "[Active] " + Protocol.OUT + " not received");
            return;
        }

        this.send(Protocol.OUT);
    }

    private void release() {
        Log.d(Fougere.TAG, "[Active] Release resources");
        if (this.input != null)
            this.closeInputStream();
        if (this.output != null)
            this.closeOutputStream();
        if (this.socket != null)
            this.closeSocket();
    }

    private void closeInputStream() {
        try {
            this.input.close();
        } catch (IOException e) {
            // Nothing
        }
    }

    private void closeOutputStream() {
        try {
            this.output.close();
        } catch (IOException e) {
            // Nothing
        }
    }

    private void closeSocket() {
        try {
            this.socket.close();
        } catch (IOException e) {
            // Nothing
        }
    }
    private String getlocation(){
        LocationManager locationManager = (LocationManager) Fougere.activity.getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(new Criteria(), true);
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null)
            return  location.getLatitude() + " " + location.getLongitude();
        else return "0 0";
    }
    private void send(final String content) throws IOException {
        getlocation();
        Message msg = new Message(content);

        this.output.writeObject(msg);
        this.output.flush();
        String timestamp = ( (Long) (System.currentTimeMillis()/1000)).toString();
        Log.d(Fougere.TAG, "[" +timestamp + "]" + "[" +getlocation() + "]" + "[" +DeviceInfo.deviceName + "]" +"[Active] Sent: " + content);
    }

    private String receive() throws IOException, ClassNotFoundException {
        Message received = (Message) this.input.readObject();

        String content = received.getContent();
        String timestamp = ( (Long) (System.currentTimeMillis()/1000)).toString();
        Log.d(Fougere.TAG,  "[" +timestamp + "]" + "[" +getlocation() + "]" + "[" +DeviceInfo.deviceName + "]" +"[Active] Received : " + content);

        return content;
    }
}
