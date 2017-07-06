package mock.core.event;

import mock.net.wifi.p2p.WifiP2pConfig;
import mock.net.wifi.p2p.WifiP2pManager;

public class ConnectEvent {

    public final WifiP2pConfig wifiP2pConfig;
    public final WifiP2pManager.ActionListener actionListener;

    public ConnectEvent(final WifiP2pConfig wifiP2pConfig, final WifiP2pManager.ActionListener actionListener) {
        this.wifiP2pConfig = wifiP2pConfig;
        this.actionListener = actionListener;
    }
}
