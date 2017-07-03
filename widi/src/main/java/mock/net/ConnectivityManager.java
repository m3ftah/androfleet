package mock.net;


/**
 * Created by lakhdar on 7/3/17.
 */

public class ConnectivityManager {
    public ConnectivityManager(){}
    public NetworkInfo getActiveNetworkInfo(){
        return new NetworkInfo(null);
    }
}
