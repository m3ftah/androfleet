package mock.core.event;

import mock.net.wifi.p2p.WifiP2pManager;

public class CancelConnectEvent {

    public final WifiP2pManager.ActionListener actionListener;

    public CancelConnectEvent(final WifiP2pManager.ActionListener actionListener) {
        this.actionListener = actionListener;
    }
}
