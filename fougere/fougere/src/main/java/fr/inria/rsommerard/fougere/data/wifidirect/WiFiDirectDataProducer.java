package fr.inria.rsommerard.fougere.data.wifidirect;

import android.util.Log;

import java.util.Random;
import java.util.UUID;

import fr.inria.rsommerard.fougere.Fougere;
import fr.inria.rsommerard.fougere.data.Data;
import fr.inria.rsommerard.fougere.data.DataProducer;

/**
 * Created by Romain on 14/08/2016.
 */
public class WiFiDirectDataProducer {

    public static WiFiDirectData produce() {
        Data dt = DataProducer.produce();

        WiFiDirectData data = new WiFiDirectData(null, dt.getIdentifier(), dt.getContent(),
                dt.getTtl(), dt.getDisseminate(), dt.getSent());

        Log.d(Fougere.TAG, "[WiFiDirectDataProducer] WiFiDirectData produced: " + data);

        return data;
    }
}
