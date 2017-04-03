package fr.inria.rsommerard.fougereapp;

import android.os.AsyncTask;
import android.util.Log;

import com.stealthcopter.networktools.Ping;
import com.stealthcopter.networktools.ping.PingResult;

import java.net.UnknownHostException;

/**
 * Created by lakhdar on 12/7/16.
 */

public class PingTest {
    private final int times;
    PingListener pl;

    PingTest(int times, PingListener pl){
        this.pl = pl;
        this.times = times;
        ping();
    }
    private void ping(){
        final AsyncTask<Object, Object, Void> task = new AsyncTask<Object, Object, Void>() {
            @Override
            protected Void doInBackground(Object... voids) {
                try {
                    Ping.onAddress("127.0.0.01").setTimeOutMillis(1000).setTimes(PingTest.this.times).doPing(new Ping.PingListener() {
                        @Override
                        public void onResult(PingResult pingResult) {
                            Log.d(MainActivity.TAG,"pingResult" + pingResult.getTimeTaken());
                            PingTest.this.pl.callback(pingResult.getTimeTaken());
                        }
                        @Override
                        public void onFinished() {
                            PingTest.this.pl.finished();
                        }
                    });
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                    Log.e(MainActivity.TAG, "ping result error");
                }
                return null;
            }

        };
        task.execute();
    }
    interface PingListener{
        void callback(float result);
        void finished();
    }
}
