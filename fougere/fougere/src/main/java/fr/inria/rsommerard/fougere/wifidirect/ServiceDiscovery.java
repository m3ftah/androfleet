package fr.inria.rsommerard.fougere.wifidirect;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.util.Log;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import fr.inria.rsommerard.fougere.Fougere;
import fr.inria.rsommerard.fougere.FougereDistance;

/**
 * Created by Romain on 01/08/16.
 */
public class ServiceDiscovery {

    private static final String SERVICE_NAME = "_fougere";
    private static final String SERVICE_TYPE = "_tcp";

    private static final int DISCOVERY_INTERVAL = 180000; //180000;//17000
    private static final int DELAY = 11000;//11000
    private final ConnectionHandler connectionHandler;
    private final ScheduledExecutorService executor;
    private final FougereDistance fougereDistance;

    private WifiP2pDnsSdServiceInfo wiFiP2pDnsSdServiceInfo;
    private FougereActionListener addLocalServiceActionListener;
    private final WifiP2pManager manager;
    private final Channel channel;
    private FougereActionListener discoverServicesActionListener;

    private FougereDnsSdTxtRecordListener dnsSdTxtRecordListener;
    private WifiP2pDnsSdServiceRequest wiFiP2pDnsSdServiceRequest;
    private FougereActionListener addServiceRequestActionListener;
    private FougereActionListener clearLocalServicesActionListener;
    private FougereActionListener clearServiceRequestsActionListener;
    private FougereActionListener stopPeerDiscoveryActionListener;
    private boolean isStarted;
    private final Runnable discover;

    public ServiceDiscovery(final WifiP2pManager manager, final Channel channel,
                            final ConnectionHandler connectionHandler,
                            final FougereDistance fougereDistance) {
        this.manager = manager;
        this.channel = channel;

        this.fougereDistance = fougereDistance;
        this.connectionHandler = connectionHandler;

        this.executor = Executors.newSingleThreadScheduledExecutor();

        this.discover = new Runnable() {
            @Override
            public void run() {
                ServiceDiscovery.this.discover();
            }
        };

        this.initialize();
    }

    private void initialize() {
        this.wiFiP2pDnsSdServiceInfo = WifiP2pDnsSdServiceInfo.newInstance(SERVICE_NAME,
                SERVICE_TYPE, null);
        this.addLocalServiceActionListener = new FougereActionListener(null,
                "Add local service failed: ");
        this.dnsSdTxtRecordListener = new FougereDnsSdTxtRecordListener(SERVICE_NAME, SERVICE_TYPE);

        this.wiFiP2pDnsSdServiceRequest = WifiP2pDnsSdServiceRequest.newInstance();
        this.addServiceRequestActionListener = new FougereActionListener(null,
                "Add service request failed: ");

        this.clearLocalServicesActionListener = new FougereActionListener(null,
                "Clear local service failed: ");
        this.clearServiceRequestsActionListener = new FougereActionListener(null,
                "Clear service requests failed: ");

        this.discoverServicesActionListener = new FougereActionListener("Discover call succeeded",
                "Discovery failed: ") {
            @Override
            public void onFailure(int reason) {
                super.onFailure(reason);
                ServiceDiscovery.this.restart();
            }
        };

        this.stopPeerDiscoveryActionListener = new FougereActionListener(null,
                "Stop peer discovery failed: ");
    }

    private void startDiscovery() {
        if (this.isStarted) {
            Log.d(Fougere.TAG, "[ServiceDiscovery] Service discovery already started");
            return;
        }

        this.isStarted = true;

        // https://developer.android.com/reference/android/net/wifi/p2p/WifiP2pManager.html
        this.manager.clearLocalServices(this.channel, this.clearLocalServicesActionListener);
        this.manager.clearServiceRequests(this.channel, this.clearServiceRequestsActionListener);

        this.manager.stopPeerDiscovery(this.channel, this.stopPeerDiscoveryActionListener);

        this.manager.addLocalService(this.channel, this.wiFiP2pDnsSdServiceInfo,
                this.addLocalServiceActionListener);
        this.manager.setDnsSdResponseListeners(this.channel, null, this.dnsSdTxtRecordListener);
        this.manager.addServiceRequest(this.channel, this.wiFiP2pDnsSdServiceRequest,
                this.addServiceRequestActionListener);

        this.executor.scheduleAtFixedRate(this.discover, DELAY, DISCOVERY_INTERVAL,
                TimeUnit.MILLISECONDS);
    }

    private void discover() {
        this.manager.discoverServices(this.channel, this.discoverServicesActionListener);
    }

    private void stopDiscovery() {
        // https://developer.android.com/reference/android/net/wifi/p2p/WifiP2pManager.html
        this.manager.clearLocalServices(this.channel, this.clearLocalServicesActionListener);
        this.manager.clearServiceRequests(this.channel, this.clearServiceRequestsActionListener);

        this.manager.stopPeerDiscovery(this.channel, this.stopPeerDiscoveryActionListener);

        this.isStarted = false;
    }

    private void restart() {
        this.stopDiscovery();
        this.initialize();
        this.startDiscovery();
    }

    public void stop() {
        this.stopDiscovery();
    }

    public void start() {
        this.startDiscovery();
    }

    private class FougereDnsSdTxtRecordListener implements WifiP2pManager.DnsSdTxtRecordListener {

        private final String serviceName;
        private final String serviceType;

        public FougereDnsSdTxtRecordListener(final String serviceName, final String serviceType) {
            this.serviceName = serviceName;
            this.serviceType = serviceType;
        }

        @Override
        public void onDnsSdTxtRecordAvailable(final String fullDomainName,
                                              final Map<String, String> txtRecordMap,
                                              final WifiP2pDevice srcDevice) {

            if (isValidDnsSdTxtRecord(fullDomainName, srcDevice)) {
                Log.d(Fougere.TAG, "[FougereDnsSdTxtRecordListener] " + srcDevice.deviceName +
                        " discovered");

                if (srcDevice.status == WifiP2pDevice.AVAILABLE) {
                    if ( ! ServiceDiscovery.this.fougereDistance.containsUser(srcDevice.deviceAddress)) {
                        ServiceDiscovery.this.fougereDistance.addUser(srcDevice.deviceAddress);
                    }

                    if (ServiceDiscovery.this.fougereDistance.canSendTo(srcDevice.deviceAddress)) {
                        ServiceDiscovery.this.connectionHandler.connect(srcDevice);
                    }
                }
            } else {
                Log.d(Fougere.TAG, "[FougereDnsSdTxtRecordListener] DnsSdTxtRecord not valid");
            }
        }

        private boolean isValidDnsSdTxtRecord(final String fullDomainName,
                                              final WifiP2pDevice srcDevice) {
            if (fullDomainName == null ||
                    !fullDomainName.contains(this.serviceName + "." + this.serviceType)) {
                return false;
            }

            if (srcDevice.deviceAddress == null ||
                    srcDevice.deviceAddress.isEmpty()) {
                return false;
            }

            if (srcDevice.deviceName == null ||
                    srcDevice.deviceName.isEmpty()) {
                return false;
            }

            return true;
        }
    }
}
