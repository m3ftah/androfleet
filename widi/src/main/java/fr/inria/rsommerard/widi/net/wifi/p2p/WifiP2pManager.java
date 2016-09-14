package fr.inria.rsommerard.widi.net.wifi.p2p;

import android.content.Context;
import android.os.Looper;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import fr.inria.rsommerard.widi.core.WiDiHandler;
import fr.inria.rsommerard.widi.core.event.CancelConnectEvent;
import fr.inria.rsommerard.widi.core.event.ConnectEvent;
import fr.inria.rsommerard.widi.core.event.DiscoverPeersEvent;
import fr.inria.rsommerard.widi.core.event.DiscoverServicesEvent;
import fr.inria.rsommerard.widi.core.event.HelloEvent;
import fr.inria.rsommerard.widi.core.event.RequestPeersEvent;
import fr.inria.rsommerard.widi.core.event.StopDiscoveryEvent;
import fr.inria.rsommerard.widi.core.thread.CancelConnectThread;
import fr.inria.rsommerard.widi.core.thread.ConnectThread;
import fr.inria.rsommerard.widi.core.thread.DiscoverServicesThread;
import fr.inria.rsommerard.widi.net.wifi.p2p.nsd.WifiP2pServiceInfo;
import fr.inria.rsommerard.widi.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;

public class WifiP2pManager {

    public static final String WIFI_P2P_STATE_CHANGED_ACTION =
            "fr.inria.rsommerard.widi.STATE_CHANGED";

    public static final String WIFI_P2P_PEERS_CHANGED_ACTION =
            "fr.inria.rsommerard.widi.PEERS_CHANGED";

    public static final String WIFI_P2P_CONNECTION_CHANGED_ACTION =
            "fr.inria.rsommerard.widi.CONNECTION_STATE_CHANGE";

    public static final String WIFI_P2P_THIS_DEVICE_CHANGED_ACTION =
            "fr.inria.rsommerard.widi.THIS_DEVICE_CHANGED";

    public static final int WIFI_P2P_STATE_ENABLED = 2;

    public static final String EXTRA_WIFI_P2P_DEVICE = "wifiP2pDevice";

    public static final int ERROR = 0;

    public static final String EXTRA_WIFI_STATE = "wifi_p2p_state";

    public static final String EXTRA_NETWORK_INFO = "networkInfo";

    public static final String EXTRA_WIFI_P2P_INFO = "wifiP2pInfo";
    private final WiDiHandler mWiDiHandler;

    private WifiP2pServiceInfo mWifiP2pServiceInfo;
    private DnsSdServiceResponseListener mDnsSdServiceResponseListener;
    private DnsSdTxtRecordListener mDnsSdTxtRecordListener;
    private WifiP2pDnsSdServiceRequest mWifiP2pDnsSdServiceRequest;

    public WifiP2pManager() {
        mWiDiHandler = new WiDiHandler();
        EventBus.getDefault().post(new HelloEvent());
    }

    public Channel initialize(final Context context, final Looper looper,
                              final ChannelListener channelListener) {
        return new Channel();
    }

    public void discoverPeers(final Channel channel, final ActionListener actionListener) {
        EventBus.getDefault().post(new DiscoverPeersEvent(actionListener));
    }

    public void stopPeerDiscovery(final Channel channel, final ActionListener actionListener) {
        EventBus.getDefault().post(new StopDiscoveryEvent(actionListener));
    }

    public void requestPeers(final WifiP2pManager.Channel channel, final WifiP2pManager.PeerListListener peerListListener) {
        EventBus.getDefault().post(new RequestPeersEvent(peerListListener));
    }

    public void addLocalService(final Channel channel, final WifiP2pServiceInfo wifiP2pServiceInfo, final ActionListener actionListener) {
        mWifiP2pServiceInfo = wifiP2pServiceInfo;

        if(actionListener != null)
            actionListener.onSuccess();
    }

    public void clearLocalServices(final Channel channel, final ActionListener actionListener) {
        mWifiP2pServiceInfo = null;

        if(actionListener != null)
            actionListener.onSuccess();
    }

    public void setDnsSdResponseListeners(
            final Channel channel,
            final DnsSdServiceResponseListener dnsSdServiceResponseListener,
            final DnsSdTxtRecordListener dnsSdTxtRecordListener) {

        mDnsSdServiceResponseListener = dnsSdServiceResponseListener;
        mDnsSdTxtRecordListener = dnsSdTxtRecordListener;
    }

    public void addServiceRequest(final Channel channel,
                                  final WifiP2pDnsSdServiceRequest wifiP2pDnsSdServiceRequest,
                                  final ActionListener actionListener) {

        mWifiP2pDnsSdServiceRequest = wifiP2pDnsSdServiceRequest;

        if(actionListener != null)
            actionListener.onSuccess();
    }

    public void clearServiceRequests(final Channel channel, final ActionListener actionListener) {
        mDnsSdServiceResponseListener = null;
        mDnsSdTxtRecordListener = null;
        mWifiP2pDnsSdServiceRequest = null;

        if(actionListener != null)
            actionListener.onSuccess();
    }

    public void discoverServices(final Channel channel, final ActionListener actionListener) {
        EventBus.getDefault().post(new DiscoverServicesEvent(mWifiP2pServiceInfo,
                mDnsSdServiceResponseListener, mDnsSdTxtRecordListener, actionListener));
    }

    public void connect(final Channel channel, final WifiP2pConfig wifiP2pConfig, final ActionListener actionListener) {
        EventBus.getDefault().post(new ConnectEvent(wifiP2pConfig, actionListener));
    }

    public void cancelConnect(final Channel channel, final ActionListener actionListener) {
        EventBus.getDefault().post(new CancelConnectEvent(actionListener));
    }

    public void removeGroup(Channel channel, ActionListener actionListener) {
        // Nothing to do
        actionListener.onSuccess();
    }

    public interface DnsSdTxtRecordListener {
        void onDnsSdTxtRecordAvailable(String fullDomainName, Map<String, String> txtRecordMap, WifiP2pDevice srcDevice);
    }

    public class Channel {}

    public interface PeerListListener {
        void onPeersAvailable(WifiP2pDeviceList peers);
    }

    public interface ChannelListener {
        void onChannelDisconnected();
    }

    public interface ActionListener {
        void onSuccess();
        void onFailure(int reason);
    }

    public interface DnsSdServiceResponseListener {
        void onDnsSdServiceAvailable(String instanceName, String registrationType, WifiP2pDevice srcDevice);
    }
}
