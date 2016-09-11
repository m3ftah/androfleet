package fr.inria.rsommerard.widi.core;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import fr.inria.rsommerard.widi.core.event.CancelConnectEvent;
import fr.inria.rsommerard.widi.core.event.ConnectEvent;
import fr.inria.rsommerard.widi.core.event.DiscoverPeersEvent;
import fr.inria.rsommerard.widi.core.event.DiscoverServicesEvent;
import fr.inria.rsommerard.widi.core.event.HelloEvent;
import fr.inria.rsommerard.widi.core.event.RequestPeersEvent;
import fr.inria.rsommerard.widi.core.event.StopDiscoveryEvent;
import fr.inria.rsommerard.widi.core.thread.CancelConnectThread;
import fr.inria.rsommerard.widi.core.thread.ConnectThread;
import fr.inria.rsommerard.widi.core.thread.DiscoverPeersThread;
import fr.inria.rsommerard.widi.core.thread.DiscoverServicesThread;
import fr.inria.rsommerard.widi.core.thread.HelloThread;
import fr.inria.rsommerard.widi.core.thread.RequestPeersThread;
import fr.inria.rsommerard.widi.core.thread.StopDiscoveryThread;

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
