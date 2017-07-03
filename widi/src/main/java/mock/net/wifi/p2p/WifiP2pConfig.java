package mock.net.wifi.p2p;

import mock.net.wifi.WpsInfo;

public class WifiP2pConfig {

    public String deviceAddress = "";

    public WpsInfo wps;

    public int netId = 0;

    public int groupOwnerIntent = -1;

    public WifiP2pConfig() {
        //set defaults
        wps = new WpsInfo();
        wps.setup = WpsInfo.PBC;
    }

    public String toString() {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append("\n address: ").append(deviceAddress);
        sbuf.append("\n wps: ").append(wps);
        sbuf.append("\n groupOwnerIntent: ").append(groupOwnerIntent);
        sbuf.append("\n persist: ").append(netId);
        return sbuf.toString();
    }
}