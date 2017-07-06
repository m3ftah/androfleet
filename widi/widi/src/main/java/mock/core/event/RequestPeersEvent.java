package mock.core.event;

import mock.net.wifi.p2p.WifiP2pManager;

public class RequestPeersEvent {

    public final WifiP2pManager.PeerListListener peerListListener;

    public RequestPeersEvent(final WifiP2pManager.PeerListListener peerListListener) {
        this.peerListListener = peerListListener;
    }
}
