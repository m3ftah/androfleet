package fr.inria.rsommerard.fougere;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import fr.inria.rsommerard.fougere.data.Data;
import fr.inria.rsommerard.fougere.data.DataPool;
import fr.inria.rsommerard.fougere.data.DataProducer;
import fr.inria.rsommerard.fougere.wifidirect.WiFiDirect;

/**
 * Created by Romain on 01/08/16.
 */
public class Fougere {

    public static final String TAG = "Fougere";

    private final DataPool dataPool;
    private final SecureRandom random;
    private final HashMap<String, FougereModule> modules;
    private final FougereDistance fougereDistance;
    public static FougereListener fougereListener;
    private IntentFilter intentFilter;
    private WiFiReceiver wiFiReceiver;
    private WiFiDirect wiFiDirect;

    private boolean isStarted;

    public static Activity activity;

    public Fougere(final Activity activity, FougereListener fougereListener) {
        Fougere.activity = activity;
        Fougere.fougereListener = fougereListener;

        this.dataPool = new DataPool(this.activity);
        this.modules = new HashMap<>();

        this.random = new SecureRandom();
        this.fougereDistance = new FougereDistance(this.random);

        this.initializeModules();

        /*if (this.dataPool.getAll().size() == 0) {
            this.experimentationInit();
        }*/

        this.wiFiReceiver = new WiFiReceiver();

        this.intentFilter = new IntentFilter();
        this.intentFilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);

    }

    public void start() {
        this.isStarted = true;

        this.regroupData();
        this.allocateData();

        this.activity.registerReceiver(wiFiReceiver, intentFilter);

        for (FougereModule module : this.modules.values()) {
            if (WiFiDirect.NAME.equals(module.getName())) {
                WifiManager manager = (WifiManager) this.activity.getSystemService(Context.WIFI_SERVICE);
                if (manager.isWifiEnabled()) {
                    this.modules.get(WiFiDirect.NAME).start();
                }
            } else {
                module.start();
            }
        }
    }

    private void initializeModules() {
        this.wiFiDirect = new WiFiDirect(this.activity, this.dataPool, this.fougereDistance);
        //wiFiDirect.setRatio(60);
        this.modules.put(WiFiDirect.NAME, this.wiFiDirect);
    }

    public void stop() {
        this.isStarted = false;

        for (FougereModule module : this.modules.values()) {
            module.stop();
        }
        this.activity.unregisterReceiver(this.wiFiReceiver);
    }

    // TODO: to delete
    private void experimentationInit() {
        Random rand = new Random();
        int key = rand.nextInt(10000);

        for (int i = 0; i < 5; i++) {
            this.dataPool.insert(DataProducer.produce(Integer.toString(key)));
        }
    }
    public void sendData(String data){
        this.wiFiDirect.addData(DataProducer.produce(data));
    }

    public void addModule(final FougereModule module) {
        if (this.isStarted) {
            Log.e(Fougere.TAG, "Error: module cannot be added when Fougere is started");
            return;
        }

        this.modules.put(module.getName(), module);
    }

    public void removeModule(final FougereModule module) {
        if (this.isStarted) {
            Log.e(Fougere.TAG, "Error: module cannot be removed when Fougere is started");
            return;
        }

        if (this.modules.containsKey(module.getName())) {
            this.modules.remove(module.getName());
        }
    }

    private void allocateData() {
        int totalRatio = 0;
        for (FougereModule module : this.modules.values()) {
            totalRatio += module.getRatio();
        }

        if (totalRatio != 100) {
            Log.e(Fougere.TAG, "Error: total ratio must be equals to 100 (actual: " + totalRatio + ")");
            return;
        }

        List<Data> data = this.dataPool.getAll();

        List<FougereModule> mdls = new ArrayList<>(this.modules.values());
        Collections.sort(mdls, new FougereModuleComparator());

        for (Data dt : data) {
            int rnd = this.random.nextInt(100);
            int base = 0;

            for (FougereModule mdl : mdls) {
                if (rnd < base + mdl.getRatio()) {
                    this.modules.get(mdl.getName()).addData(dt);
                    break;
                }

                base = base + mdl.getRatio();
            }

            this.dataPool.delete(dt);
        }
    }

    private void regroupData() {
        Log.d(Fougere.TAG, "[Fougere] " + this.dataPool.getAll().size() +
                " data in the DataPool");

        for (FougereModule module : this.modules.values()) {
            List<Data> moduleData = module.getAllData();
            Log.d(Fougere.TAG, "[Fougere] Regroup " + moduleData.size() +
                    " data from the " + module.getName() + " module");
            for (Data data : moduleData) {
                this.dataPool.insert(data);
                module.removeData(data);
            }
        }
    }

    public void addData(final Data data) {
        this.dataPool.insert(data);
    }

    private class WiFiReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {
            final String action = intent.getAction();

            if (action.equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)) {
                if (intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false)){
                    Fougere.this.modules.get(WiFiDirect.NAME).start();
                } else {
                    Fougere.this.modules.get(WiFiDirect.NAME).stop();
                }
            }
        }
    }
    public interface FougereListener{
        void onDataReceived(Data data);
    }
}
