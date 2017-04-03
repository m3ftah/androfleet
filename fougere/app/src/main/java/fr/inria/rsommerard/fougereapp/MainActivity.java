package fr.inria.rsommerard.fougereapp;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import fr.inria.rsommerard.fougere.Fougere;
import fr.inria.rsommerard.fougere.data.Data;
import fr.inria.rsommerard.fougere.wifidirect.DeviceInfo;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "APP";
    private Fougere fougere;
    private GPSLocation gpslocation;
    private Location location;
    private float pingResult;
    private RestServer restServer;
    private final int SENDING_TIMES = 300;
    private final int[] sentTimes = {0};
    private final int PING_TIMES = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initiateServer();
        gps();

        this.fougere = new Fougere(this, new Fougere.FougereListener() {
            @Override
            public void onDataReceived(Data data) {
                Log.d(TAG,"Received in App"  + data.toString());
            }
        });
        this.fougere.start();
    }

    private void initiateServer() {
        this.restServer = new RestServer(new RestServer.RestServerListener() {
            @Override
            public void onUploadFinished(String response) {
                Log.d(TAG,"upload finished, response :" + response);

            }
            @Override
            public void onError(String error) {
                Log.d(TAG,"upload error : " + error);
            }
        });
    }

    private void gps() {
        gpslocation = new GPSLocation(this,new GPSLocation.GPSLocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                sentTimes[0]++;
                Log.v(MainActivity.TAG,"onLocationChanged times : " + sentTimes[0]);
                //if (sentTimes[0] > SENDING_TIMES) return;
                MainActivity.this.location = location;
                ping(PING_TIMES);
            }
        });
    }
    void ping(int times){
        new PingTest(times,new PingTest.PingListener() {
            @Override
            public void callback(float result) {
                Log.d(MainActivity.TAG,"result : " + result);
                MainActivity.this.pingResult = result;
            }
            @Override
            public void finished() {
                Log.d(MainActivity.TAG,"finished");
                String timestamp = ( (Long) (System.currentTimeMillis()/1000)).toString();
                MainActivity.this.fougere.sendData("{ping:"+MainActivity.this.pingResult + ", lon:" + MainActivity.this.location.getLongitude() + ", lat:" + MainActivity.this.location.getLatitude() + ", timestamp:" + timestamp + "}");
                //restServer.send(DeviceInfo.deviceName,MainActivity.this.pingResult,MainActivity.this.location.getLongitude(),MainActivity.this.location.getLatitude());
            }
        });
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        gpslocation.getGPSLocation();

                } else {
                    Log.e(MainActivity.TAG,"User denied permission");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onDestroy() {
        this.fougere.stop();
        super.onDestroy();
    }
}
