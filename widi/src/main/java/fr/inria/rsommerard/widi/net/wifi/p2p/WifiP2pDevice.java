package fr.inria.rsommerard.widi.net.wifi.p2p;

public class WifiP2pDevice {

    public String deviceName = "";
    public String deviceAddress = "";
    public String primaryDeviceType = "";
    public String secondaryDeviceType = "";

    public int wpsConfigMethodsSupported = 0;
    public int deviceCapability = 0;
    public int groupCapability = 0;

    public static final int AVAILABLE = 3;

    private static final int GROUP_CAPAB_GROUP_OWNER = 1;

    public int status = AVAILABLE;

    public WifiP2pWfdInfo wfdInfo = new WifiP2pWfdInfo();

    public boolean isGroupOwner() {
        return (groupCapability & GROUP_CAPAB_GROUP_OWNER) != 0;
    }

    public String toString() {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append("Device: ").append(deviceName);
        sbuf.append("\n deviceAddress: ").append(deviceAddress);
        sbuf.append("\n primary type: ").append(primaryDeviceType);
        sbuf.append("\n secondary type: ").append(secondaryDeviceType);
        sbuf.append("\n wps: ").append(wpsConfigMethodsSupported);
        sbuf.append("\n grpcapab: ").append(groupCapability);
        sbuf.append("\n devcapab: ").append(deviceCapability);
        sbuf.append("\n status: ").append(status);
        sbuf.append("\n wfdInfo: ").append(wfdInfo);
        return sbuf.toString();
    }
}
