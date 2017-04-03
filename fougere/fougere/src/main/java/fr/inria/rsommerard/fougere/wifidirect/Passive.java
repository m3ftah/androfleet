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
import java.net.ServerSocket;
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
public class Passive implements Runnable {

    private final WiFiDirectDataPool wiFiDirectDataPool;
    private final DataPool dataPool;
    private final WifiP2pDevice me;
    private final FougereDistance fougereDistance;
    private ServerSocket serverSocket;
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    public Passive(final WifiP2pDevice me, final DataPool dataPool,
                   final WiFiDirectDataPool wiFiDirectDataPool,
                   final FougereDistance fougereDistance) {
        this.wiFiDirectDataPool = wiFiDirectDataPool;
        this.dataPool = dataPool;
        this.me = me;
        this.fougereDistance = fougereDistance;
    }

    @Override
    public void run() {
        Log.d(Fougere.TAG, "[Passive] Started");

        try {
            this.serverSocket = new ServerSocket(11131);
            Log.e(Fougere.TAG, "[Passive] ServerSocket instance created");
        } catch (IOException e) {
            Log.e(Fougere.TAG, "[Passive] Error on ServerSocket initialization");
        }

        if (this.serverSocket == null) {
            Log.e(Fougere.TAG, "[Passive] ServerSocket is null");
            return;
        }

            Log.e(Fougere.TAG, "[Passive] ServerSocket is not null");

        try {
            this.socket = this.serverSocket.accept();
            Log.e(Fougere.TAG, "[Passive] ServerSocket in accept mode");
            // Warning: Order is important! First create output for the header!
            this.output = new ObjectOutputStream(this.socket.getOutputStream());
            this.input = new ObjectInputStream(this.socket.getInputStream());
            Log.d(Fougere.TAG, "[Passive] Socket OK");
            this.process();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            Log.e(Fougere.TAG, "[Passive] KO");
        } finally {
            this.release();
        }
    }

    private void process() throws IOException, ClassNotFoundException {
        if ( ! Protocol.HELLO.equals(this.receive())) {
            Log.e(Fougere.TAG, "[Passive] " + Protocol.HELLO + " not received");
            return;
        }

        this.send(Protocol.HELLO);

        String deviceAddress = this.receive();
        this.send(Protocol.ACK);

        if ( ! this.fougereDistance.containsUser(deviceAddress)) {
            this.fougereDistance.addUser(deviceAddress);
        }

        this.send(me.deviceAddress);

        if ( ! Protocol.ACK.equals(this.receive())) {
            Log.e(Fougere.TAG, "[Active] " + Protocol.ACK + " not received");
            return;
        }

        this.send(Protocol.SEND);

        String json = this.receive();

        List<WiFiDirectData> dataReceived = WiFiDirectData.deGsonify(json);
        for (WiFiDirectData dt : dataReceived) {
            String timestamp = ( (Long) (System.currentTimeMillis()/1000)).toString();
            Log.d(Fougere.TAG,  "[" +timestamp + "]" +  "[" + getlocation() + "]" + "[" +DeviceInfo.deviceName + "]" +"[Passive][Received]: " + dt.toString());
            this.dataPool.insert(Data.reset(WiFiDirectData.toData(dt)));
            if (dt.getTtl() > 0) this.wiFiDirectDataPool.insert(dt);
            Fougere.fougereListener.onDataReceived(WiFiDirectData.toData(dt));
        }

        this.send(Protocol.ACK);

        if ( ! Protocol.SEND.equals(this.receive())) {
            Log.e(Fougere.TAG, "[Passive] " + Protocol.SEND + " not received");
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
            Log.d(Fougere.TAG, "[" +timestamp + "]" +  "[" + getlocation() + "]" +  "[" +DeviceInfo.deviceName + "]" +"[Passive][Sent]: " + dt.toString());
        }

        this.send(WiFiDirectData.gsonify(dataToSend));

        if ( ! Protocol.ACK.equals(this.receive())) {
            Log.e(Fougere.TAG, "[Passive] " + Protocol.ACK + " not received");
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

        Log.d(Fougere.TAG, "[Passive] Process done");

        this.fougereDistance.updateDistance(deviceAddress);

        this.send(Protocol.OUT);

        if ( ! Protocol.OUT.equals(this.receive())) {
            Log.e(Fougere.TAG, "[Passive] " + Protocol.OUT + " not received");
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
        Message msg = new Message(content);

        this.output.writeObject(msg);
        this.output.flush();

        String timestamp = ( (Long) (System.currentTimeMillis()/1000)).toString();
        Log.d(Fougere.TAG, "[" +timestamp + "]" +  "[" +getlocation() + "]" +  "[" +DeviceInfo.deviceName + "]" +"[Passive] Sent: " + content);
    }

    private String receive() throws IOException, ClassNotFoundException {
        Message received = (Message) this.input.readObject();

        String timestamp = ( (Long) (System.currentTimeMillis()/1000)).toString();
        String content = received.getContent();
        Log.d(Fougere.TAG, "[" +timestamp + "]" +  "[" + getlocation() + "]" + "[" +DeviceInfo.deviceName + "]" +"[Passive] Received: " + content);

        return content;
    }

    private void release() {
        Log.d(Fougere.TAG, "[Passive] Release resources");

        this.closeInputStream();
        this.closeOutputStream();
        this.closeSocket();
        this.closeServerSocket();
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

    private void closeServerSocket() {
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            // Nothing
        }
    }
}
