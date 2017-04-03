package fr.inria.rsommerard.fougere.data;

import android.util.Log;

import java.util.Random;
import java.util.UUID;

import fr.inria.rsommerard.fougere.Fougere;
import fr.inria.rsommerard.fougere.wifidirect.DeviceInfo;

/**
 * Created by Romain on 17/08/16.
 */
public class DataProducer {

    public static Data produce() {
        Random rand = new Random();
        String key = Integer.toString(rand.nextInt());

        return DataProducer.produce(key);
    }

    public static Data produce(String key) {
        int ttl = 1;
        int disseminate = 4;
        int sent = 0;

        Data data = new Data(null, UUID.randomUUID().toString(),key, ttl, disseminate,
                sent);
        String timestamp = ( (Long) (System.currentTimeMillis()/1000)).toString();
        Log.d(Fougere.TAG, "[" +timestamp + "]" + "[" + DeviceInfo.deviceName + "]" + "[DataProducer][Data produced]: " + data);

        return data;
    }
}
