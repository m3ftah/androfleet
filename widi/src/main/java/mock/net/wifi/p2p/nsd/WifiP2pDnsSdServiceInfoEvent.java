package mock.net.wifi.p2p.nsd;

import java.util.Map;

public class WifiP2pDnsSdServiceInfoEvent {

    public final String serviceName;
    public final String serviceType;
    public final Map<String, String> record;

    public WifiP2pDnsSdServiceInfoEvent(final String serviceName, final String serviceType, final Map<String, String> record) {
        this.serviceName = serviceName;
        this.serviceType = serviceType;
        this.record = record;
    }
}
