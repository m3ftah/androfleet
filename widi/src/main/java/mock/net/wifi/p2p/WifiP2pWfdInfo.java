package mock.net.wifi.p2p;

public class WifiP2pWfdInfo {

    private boolean mWfdEnabled = false;
    private int mDeviceInfo = 0;
    private int mCtrlPort = 0;
    private int mMaxThroughput = 0;

    public String toString() {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append("WFD enabled: ").append(mWfdEnabled);
        sbuf.append("WFD DeviceInfo: ").append(mDeviceInfo);
        sbuf.append("\n WFD CtrlPort: ").append(mCtrlPort);
        sbuf.append("\n WFD MaxThroughput: ").append(mMaxThroughput);
        return sbuf.toString();
    }
}
