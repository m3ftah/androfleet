package mock.net.wifi.p2p;

import android.content.Context;
import android.os.Looper;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import mock.core.WiDiHandler;
import mock.core.event.CancelConnectEvent;
import mock.core.event.ConnectEvent;
import mock.core.event.DiscoverPeersEvent;
import mock.core.event.DiscoverServicesEvent;
import mock.core.event.HelloEvent;
import mock.core.event.RequestConnectionInfoEvent;
import mock.core.event.RequestPeersEvent;
import mock.core.event.StopDiscoveryEvent;
import mock.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import mock.net.wifi.p2p.nsd.WifiP2pServiceInfo;

public class WifiP2pManager {

    public static final String TAG =
            "WifiP2pManager";
    public static final String WIFI_P2P_STATE_CHANGED_ACTION =
            "mock.STATE_CHANGED";

    public static final String WIFI_P2P_PEERS_CHANGED_ACTION =
            "mock.PEERS_CHANGED";

    public static final String WIFI_P2P_CONNECTION_CHANGED_ACTION =
            "mock.CONNECTION_STATE_CHANGE";

    public static final String WIFI_P2P_THIS_DEVICE_CHANGED_ACTION =
            "mock.THIS_DEVICE_CHANGED";

    public static final int WIFI_P2P_STATE_ENABLED = 2;

    public static final String EXTRA_WIFI_P2P_DEVICE = "wifiP2pDevice";

    public static final int ERROR = 0;

    public static final String EXTRA_WIFI_STATE = "wifi_p2p_state";

    public static final String EXTRA_NETWORK_INFO = "networkInfo";

    public static final String EXTRA_WIFI_P2P_INFO = "wifiP2pInfo";

    public static WifiP2pInfo wifiP2pInfo;

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
        Log.d(TAG,"discoverPeers Thread");
        EventBus.getDefault().post(new DiscoverPeersEvent(actionListener));
    }

    public void stopPeerDiscovery(final Channel channel, final ActionListener actionListener) {
        Log.d(TAG,"stopPeerDiscovery Thread");
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
        //mDnsSdServiceResponseListener = null;
        mDnsSdTxtRecordListener = null;
        mWifiP2pDnsSdServiceRequest = null;

        if(actionListener != null)
            actionListener.onSuccess();
    }

    public void discoverServices(final Channel channel, final ActionListener actionListener) {
        Log.d(TAG,"discoverServices Thread");
        EventBus.getDefault().post(new DiscoverServicesEvent(mWifiP2pServiceInfo,
                mDnsSdServiceResponseListener, mDnsSdTxtRecordListener, actionListener));
    }

    public void connect(final Channel channel, final WifiP2pConfig wifiP2pConfig, final ActionListener actionListener) {
        Log.d(TAG,"connect Thread");
        EventBus.getDefault().post(new ConnectEvent(wifiP2pConfig, actionListener));
    }

    public void cancelConnect(final Channel channel, final ActionListener actionListener) {
        Log.d(TAG,"cancelConnect Thread");
        EventBus.getDefault().post(new CancelConnectEvent(actionListener));
    }

    public void removeGroup(Channel channel, ActionListener actionListener) {
        // Nothing to do
        EventBus.getDefault().post(new CancelConnectEvent(actionListener));
        //actionListener.onSuccess();
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
    /**
     * Request device connection info.
     *
     * @param c is the channel created at {@link #initialize}
     * @param listener for callback when connection info is available. Can be null.
     */
    public void requestConnectionInfo(Channel c, ConnectionInfoListener listener) {
        //Log.d(TAG,"requestConnectionInfo Thread");
        //EventBus.getDefault().post(new RequestConnectionInfoEvent(listener));
        listener.onConnectionInfoAvailable(wifiP2pInfo);
    }
    public interface ConnectionInfoListener {
        /**
         * The requested connection info is available
         * @param info Wi-Fi p2p connection info
         */
        public void onConnectionInfoAvailable(WifiP2pInfo info);
    }
}
