package mock.net.wifi.p2p.nsd;

import java.util.Map;

public class WifiP2pDnsSdServiceInfo extends WifiP2pServiceInfo {

    private final String mServiceName;
    private final String mServiceType;
    private final Map<String, String> mTxtMap;

    private WifiP2pDnsSdServiceInfo(String serviceName,
                                    String serviceType,
                                    Map<String, String> txtMap) {
        mServiceName = serviceName;
        mServiceType = serviceType;
        mTxtMap = txtMap;
    }

    public String getServiceName() {
        return mServiceName;
    }

    public String getServiceType() {
        return mServiceType;
    }

    public Map<String, String> getTxtMap() {
        return mTxtMap;
    }

    public static WifiP2pDnsSdServiceInfo newInstance(String serviceName,
                                                      String serviceType,
                                                      Map<String, String> txtMap) {

        return new WifiP2pDnsSdServiceInfo(serviceName, serviceType, txtMap);
    }
}
