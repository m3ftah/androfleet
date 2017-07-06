package mock.core.event;

import mock.net.wifi.p2p.WifiP2pManager;

public class RequestConnectionInfoEvent {

    public final WifiP2pManager.ConnectionInfoListener connectionInfoListener;

    public RequestConnectionInfoEvent(final WifiP2pManager.ConnectionInfoListener connectionInfoListener) {
        this.connectionInfoListener = connectionInfoListener;
    }
}
