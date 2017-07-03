package mock.core.event;

import mock.net.wifi.p2p.WifiP2pManager;

public class DiscoverPeersEvent {

    public final WifiP2pManager.ActionListener actionListener;

    public DiscoverPeersEvent(final WifiP2pManager.ActionListener actionListener) {
        this.actionListener = actionListener;
    }
}
