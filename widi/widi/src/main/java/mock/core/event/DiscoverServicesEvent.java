package mock.core.event;

import mock.net.wifi.p2p.WifiP2pManager;
import mock.net.wifi.p2p.nsd.WifiP2pServiceInfo;

public class DiscoverServicesEvent {

    public final WifiP2pServiceInfo wifiP2pServiceInfo;
    public final WifiP2pManager.DnsSdServiceResponseListener dnsSdServiceResponseListener;
    public final WifiP2pManager.DnsSdTxtRecordListener dnsSdTxtRecordListener;
    public final WifiP2pManager.ActionListener actionListener;

    public DiscoverServicesEvent(final WifiP2pServiceInfo wifiP2pServiceInfo,
                                 final WifiP2pManager.DnsSdServiceResponseListener dnsSdServiceResponseListener,
                                 final WifiP2pManager.DnsSdTxtRecordListener dnsSdTxtRecordListener,
                                 final WifiP2pManager.ActionListener actionListener) {

        this.wifiP2pServiceInfo = wifiP2pServiceInfo;
        this.dnsSdServiceResponseListener = dnsSdServiceResponseListener;
        this.dnsSdTxtRecordListener = dnsSdTxtRecordListener;
        this.actionListener = actionListener;
    }
}
