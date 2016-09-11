package fr.inria.rsommerard.widi.net.wifi;

public class WifiManager {

    public static final String SUPPLICANT_CONNECTION_CHANGE_ACTION =
        "android.net.wifi.supplicant.CONNECTION_CHANGE";

    public static final String EXTRA_SUPPLICANT_CONNECTED = "connected";

    public boolean isWifiEnabled() {
        return true;
    }
}