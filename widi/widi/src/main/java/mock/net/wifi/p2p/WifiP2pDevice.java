package mock.net.wifi.p2p;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class WifiP2pDevice implements Parcelable{

    public static final int CONNECTED = 0;
    public static final int INVITED = 1;
    public static final int FAILED = 2;
    public static final int AVAILABLE = 3;
    public static final int UNAVAILABLE = 4;

    public String deviceName = "";
    public String deviceAddress = "";
    public String primaryDeviceType = "";
    public String secondaryDeviceType = "";

    public int wpsConfigMethodsSupported = 0;
    public int deviceCapability = 0;
    public int groupCapability = 0;


    private static final int GROUP_CAPAB_GROUP_OWNER = 1;

    public int status = AVAILABLE;

    public WifiP2pWfdInfo wfdInfo = new WifiP2pWfdInfo();

    public WifiP2pDevice(){}
    protected WifiP2pDevice(Parcel in) {
        deviceName = in.readString();
        deviceAddress = in.readString();
        primaryDeviceType = in.readString();
        secondaryDeviceType = in.readString();
        wpsConfigMethodsSupported = in.readInt();
        deviceCapability = in.readInt();
        groupCapability = in.readInt();
        status = in.readInt();
    }
    public static final Creator<WifiP2pDevice> CREATOR =
            new Creator<WifiP2pDevice>() {
                public WifiP2pDevice createFromParcel(Parcel in) {
                    Log.d("WifiP2pDevice","Creating parcel" + in.toString());
                    return new WifiP2pDevice(in);
                }

                public WifiP2pDevice[] newArray(int size) {
                    return new WifiP2pDevice[size];
                }
            };

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
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(deviceName);
        dest.writeString(deviceAddress);
        dest.writeString(primaryDeviceType);
        dest.writeString(secondaryDeviceType);
        dest.writeInt(wpsConfigMethodsSupported);
        dest.writeInt(deviceCapability);
        dest.writeInt(groupCapability);
        dest.writeInt(status);
        if (wfdInfo != null) {
            dest.writeInt(1);
            //wfdInfo.writeToParcel(dest, flags);
        } else {
            dest.writeInt(0);
        }
    }
}
