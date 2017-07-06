package mock.core.event;

import mock.net.wifi.p2p.WifiP2pManager;

public class StopDiscoveryEvent {

    public final WifiP2pManager.ActionListener actionListener;

    public StopDiscoveryEvent(final WifiP2pManager.ActionListener actionListener) {
        this.actionListener = actionListener;
    }
}
