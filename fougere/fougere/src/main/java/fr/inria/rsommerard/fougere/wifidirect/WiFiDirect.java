package fr.inria.rsommerard.fougere.wifidirect;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import fr.inria.rsommerard.fougere.Fougere;
import fr.inria.rsommerard.fougere.FougereDistance;
import fr.inria.rsommerard.fougere.FougereModule;
import fr.inria.rsommerard.fougere.data.Data;
import fr.inria.rsommerard.fougere.data.DataPool;
import fr.inria.rsommerard.fougere.data.wifidirect.WiFiDirectData;
import fr.inria.rsommerard.fougere.data.wifidirect.WiFiDirectDataPool;

/**
 * Created by Romain on 01/08/16.
 */
public class WiFiDirect implements FougereModule {

    public static final String NAME = "WiFiDirect";

    private int ratio;

    private final WifiP2pManager manager;
    private final Channel channel;

    private final ServiceDiscovery serviceDiscovery;
    private final ConnectionHandler connectionHandler;
    private final WiFiDirectDataPool wiFiDirectDataPool;

    public WiFiDirect(final Activity activity, final DataPool dataPool,
                      final FougereDistance fougereDistance) {
        this.ratio = 100;

        this.manager = (WifiP2pManager) activity.getSystemService(Context.WIFI_P2P_SERVICE);
        this.channel = this.manager.initialize(activity, activity.getMainLooper(),
                new FougereChannelListener());

        this.wiFiDirectDataPool = new WiFiDirectDataPool(activity);

        this.connectionHandler = new ConnectionHandler(activity, this.manager, this.channel,
                dataPool, this.wiFiDirectDataPool, fougereDistance);

        this.serviceDiscovery = new ServiceDiscovery(this.manager, this.channel,
                this.connectionHandler, fougereDistance);
    }

    @Override
    public void addData(final Data data) {
        this.wiFiDirectDataPool.insert(WiFiDirectData.fromData(data));
    }

    @Override
    public List<Data> getAllData() {
        List<WiFiDirectData> wiFiDirectData = this.wiFiDirectDataPool.getAll();
        List<Data> data = new ArrayList<>();

        for (WiFiDirectData dt : wiFiDirectData) {
            data.add(WiFiDirectData.toData(dt));
        }

        return data;
    }

    @Override
    public void removeData(final Data data) {
        this.wiFiDirectDataPool.delete(WiFiDirectData.fromData(data));
    }

    @Override
    public void start() {
        this.cleanAllGroupsRegistered();

        this.connectionHandler.start();

        this.serviceDiscovery.start();
    }

    @Override
    public void stop() {
        this.serviceDiscovery.stop();

        this.connectionHandler.stop();
    }

    @Override
    public int getRatio() {
        return this.ratio;
    }

    @Override
    public void setRatio(int ratio) {
        this.ratio = ratio;
    }

    @Override
    public String getName() {
        return WiFiDirect.NAME;
    }

    private void cleanAllGroupsRegistered() {
        try {
            Method deletePersistentGroupMethod =
                    WifiP2pManager.class.getMethod("deletePersistentGroup",
                            WifiP2pManager.Channel.class,
                            int.class,
                            WifiP2pManager.ActionListener.class);

            for (int netid = 0; netid < 32; netid++) {
                deletePersistentGroupMethod.invoke(this.manager, this.channel, netid, null);
            }

            Log.d(Fougere.TAG, "[WiFiDirect] Groups are successfully removed");
        } catch (NoSuchMethodException e) {
            //e.printStackTrace();
            Log.e(Fougere.TAG, "[WiFiDirect] Failed to deletePersistentGroup: method NOT found");
        } catch (InvocationTargetException e) {
            //e.printStackTrace();
            Log.e(Fougere.TAG, "[WiFiDirect] Failed to deletePersistentGroup");
        } catch (IllegalAccessException e) {
            //e.printStackTrace();
            Log.e(Fougere.TAG, "[WiFiDirect] Failed to deletePersistentGroup");
        }
    }

    private class FougereChannelListener implements WifiP2pManager.ChannelListener {

        @Override
        public void onChannelDisconnected() {
            Log.d(Fougere.TAG, "[FougereChannelListener] Channel disconnected");
        }
    }
}
