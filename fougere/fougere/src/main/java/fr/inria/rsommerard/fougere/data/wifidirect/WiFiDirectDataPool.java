package fr.inria.rsommerard.fougere.data.wifidirect;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.List;

import fr.inria.rsommerard.fougere.Fougere;
import fr.inria.rsommerard.fougere.data.DaoMaster;
import fr.inria.rsommerard.fougere.data.DaoSession;
import fr.inria.rsommerard.fougere.data.WiFiDirectDataDao;
import fr.inria.rsommerard.fougere.wifidirect.DeviceInfo;

/**
 * Created by Romain on 14/08/2016.
 */
public class WiFiDirectDataPool {

    private final WiFiDirectDataDao wiFiDirectDataDao;

    public WiFiDirectDataPool(final Context context) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "fougere-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        this.wiFiDirectDataDao = daoSession.getWiFiDirectDataDao();

        // TODO: to remove
        this.wiFiDirectDataDao.deleteAll();
    }

    public void insert(final WiFiDirectData data) {
        if (data.getIdentifier() == null || data.getContent() == null || data.getTtl() < 0 ||
                data.getDisseminate() < 0 || data.getSent() < 0) {
            return;
        }

        WiFiDirectData found = this.wiFiDirectDataDao.queryBuilder()
                .where(WiFiDirectDataDao.Properties.Identifier.eq(data.getIdentifier()))
                .unique();

        if (found != null) {
            Log.d(Fougere.TAG,
                    "[WiFiDirectDataPool] A data with the same identifier already exists");
            return;
        }

        String timestamp = ( (Long) (System.currentTimeMillis()/1000)).toString();
        Log.d(Fougere.TAG, "[" +timestamp + "]" + "[" + DeviceInfo.deviceName + "]" + "[WiFiDirectDataPool][Insert]: " + data.toString());

        this.wiFiDirectDataDao.insert(data);
    }

    public void update(final WiFiDirectData data) {
        if (data.getIdentifier() == null || data.getContent() == null || data.getTtl() < 0 ||
                data.getDisseminate() < 0 || data.getSent() < 0) {
            return;
        }

        WiFiDirectData found = this.wiFiDirectDataDao.queryBuilder()
                .where(WiFiDirectDataDao.Properties.Identifier.eq(data.getIdentifier()))
                .unique();

        if (found == null) {
            Log.d(Fougere.TAG, "[WiFiDirectDataPool] The data does not found");
            return;
        }

        Log.d(Fougere.TAG, "[WiFiDirectDataPool] To: " + data.toString());

        this.wiFiDirectDataDao.update(data);
    }

    public void delete(final WiFiDirectData data) {
        if (data.getIdentifier() == null) {
            return;
        }

        WiFiDirectData found = this.wiFiDirectDataDao.queryBuilder()
                .where(WiFiDirectDataDao.Properties.Identifier.eq(data.getIdentifier()))
                .unique();

        if (found == null) {
            Log.d(Fougere.TAG, "[WiFiDirectDataPool] The data does not found");
            return;
        }

        Log.d(Fougere.TAG, "[WiFiDirectDataPool] Delete: " + found.toString());

        this.wiFiDirectDataDao.delete(found);
    }

    public List<WiFiDirectData> getAll() {
        return this.wiFiDirectDataDao.loadAll();
    }
}
