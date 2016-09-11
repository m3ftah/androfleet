package fr.inria.rsommerard.widi.net.wifi.p2p;

import android.os.Parcel;
import android.os.Parcelable;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class WifiP2pInfo implements Parcelable {

    public boolean groupFormed;

    public boolean isGroupOwner;

    public InetAddress groupOwnerAddress;

    public WifiP2pInfo() {
        groupFormed = true;
    }

    public WifiP2pInfo(InetAddress groupOwnerAddress, boolean isGroupOwner) {
        this.groupOwnerAddress = groupOwnerAddress;
        this.isGroupOwner = isGroupOwner;
    }

    public String toString() {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append("groupFormed: ").append(groupFormed)
                .append(" isGroupOwner: ").append(isGroupOwner)
                .append(" groupOwnerAddress: ").append(groupOwnerAddress);
        return sbuf.toString();
    }

    public WifiP2pInfo(WifiP2pInfo source) {
        if (source != null) {
            groupFormed = source.groupFormed;
            isGroupOwner = source.isGroupOwner;
            groupOwnerAddress = source.groupOwnerAddress;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(groupFormed ? (byte)1 : (byte)0);
        dest.writeByte(isGroupOwner ? (byte)1 : (byte)0);

        if (groupOwnerAddress != null) {
            dest.writeByte((byte)1);
            dest.writeByteArray(groupOwnerAddress.getAddress());
        } else {
            dest.writeByte((byte)0);
        }
    }

    public static final Creator<WifiP2pInfo> CREATOR =
            new Creator<WifiP2pInfo>() {
                public WifiP2pInfo createFromParcel(Parcel in) {
                    WifiP2pInfo info = new WifiP2pInfo();
                    info.groupFormed = (in.readByte() == 1);
                    info.isGroupOwner = (in.readByte() == 1);
                    if (in.readByte() == 1) {
                        try {
                            info.groupOwnerAddress = InetAddress.getByAddress(in.createByteArray());
                        } catch (UnknownHostException e) {}
                    }
                    return info;
                }

                public WifiP2pInfo[] newArray(int size) {
                    return new WifiP2pInfo[size];
                }
            };
}
