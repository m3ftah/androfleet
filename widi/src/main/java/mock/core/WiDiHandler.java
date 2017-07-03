package mock.core;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import mock.core.event.CancelConnectEvent;
import mock.core.event.ConnectEvent;
import mock.core.event.DiscoverPeersEvent;
import mock.core.event.DiscoverServicesEvent;
import mock.core.event.HelloEvent;
import mock.core.event.RequestConnectionInfoEvent;
import mock.core.event.RequestPeersEvent;
import mock.core.event.StopDiscoveryEvent;
import mock.core.thread.CancelConnectThread;
import mock.core.thread.ConnectThread;
import mock.core.thread.DiscoverPeersThread;
import mock.core.thread.DiscoverServicesThread;
import mock.core.thread.HelloThread;
import mock.core.thread.RequestConnectionInfoThread;
import mock.core.thread.RequestPeersThread;
import mock.core.thread.StopDiscoveryThread;

public class WiDiHandler {

    public WiDiHandler() {
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void onHelloEvent(final HelloEvent event) {
        new HelloThread().start();
    }

    @Subscribe
    public void onDiscoverPeersEvent(final DiscoverPeersEvent event) {
        new DiscoverPeersThread(event.actionListener).start();
    }

    @Subscribe
    public void onStopDiscoveryEvent(final StopDiscoveryEvent event) {
        new StopDiscoveryThread(event.actionListener).start();
    }

    @Subscribe
    public void onRequestPeersEvent(final RequestPeersEvent event) {
        new RequestPeersThread(event.peerListListener).start();
    }
    @Subscribe
    public void onRequestConnectionInfoEvent(final RequestConnectionInfoEvent event) {
        new RequestConnectionInfoThread(event.connectionInfoListener).start();
    }

    @Subscribe
    public void onDiscoverServicesEvent(final DiscoverServicesEvent event) {
        new DiscoverServicesThread(event.wifiP2pServiceInfo, event.dnsSdServiceResponseListener,
                event.dnsSdTxtRecordListener, event.actionListener).start();
    }

    @Subscribe
    public void onConnectEvent(final ConnectEvent event) {
        new ConnectThread(event.wifiP2pConfig, event.actionListener).start();
    }

    @Subscribe
    public void onCancelConnectEvent(final CancelConnectEvent event) {
        new CancelConnectThread(event.actionListener).start();
    }
}
