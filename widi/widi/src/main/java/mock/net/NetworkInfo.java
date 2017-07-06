package mock.net;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.EnumMap;

public class NetworkInfo implements Parcelable {

    public enum State {
        CONNECTING, CONNECTED, SUSPENDED, DISCONNECTING, DISCONNECTED, UNKNOWN
    }

    public enum DetailedState {
        IDLE,
        SCANNING,
        CONNECTING,
        AUTHENTICATING,
        OBTAINING_IPADDR,
        CONNECTED,
        SUSPENDED,
        DISCONNECTING,
        DISCONNECTED,
        FAILED,
        BLOCKED,
        VERIFYING_POOR_LINK,
        CAPTIVE_PORTAL_CHECK
    }

    private static final EnumMap<DetailedState, State> stateMap =
            new EnumMap<DetailedState, State>(DetailedState.class);

    static {
        stateMap.put(DetailedState.IDLE, State.DISCONNECTED);
        stateMap.put(DetailedState.SCANNING, State.DISCONNECTED);
        stateMap.put(DetailedState.CONNECTING, State.CONNECTING);
        stateMap.put(DetailedState.AUTHENTICATING, State.CONNECTING);
        stateMap.put(DetailedState.OBTAINING_IPADDR, State.CONNECTING);
        stateMap.put(DetailedState.VERIFYING_POOR_LINK, State.CONNECTING);
        stateMap.put(DetailedState.CAPTIVE_PORTAL_CHECK, State.CONNECTING);
        stateMap.put(DetailedState.CONNECTED, State.CONNECTED);
        stateMap.put(DetailedState.SUSPENDED, State.SUSPENDED);
        stateMap.put(DetailedState.DISCONNECTING, State.DISCONNECTING);
        stateMap.put(DetailedState.DISCONNECTED, State.DISCONNECTED);
        stateMap.put(DetailedState.FAILED, State.DISCONNECTED);
        stateMap.put(DetailedState.BLOCKED, State.DISCONNECTED);
    }

    private int mNetworkType;
    private int mSubtype;
    private String mTypeName;
    private String mSubtypeName;
    private State mState;
    private DetailedState mDetailedState;
    private String mReason;
    private String mExtraInfo;
    private boolean mIsFailover;
    private boolean mIsRoaming;

    private boolean mIsAvailable;

    public NetworkInfo(int type, int subtype, String typeName, String subtypeName) {
        mNetworkType = type;
        mSubtype = subtype;
        mTypeName = typeName;
        mSubtypeName = subtypeName;
        setDetailedState(DetailedState.IDLE, null, null);
        mState = State.UNKNOWN;
        mIsAvailable = true;
        mIsRoaming = false;
        mIsFailover = false;
        mDetailedState = NetworkInfo.DetailedState.DISCONNECTED;
    }

    public NetworkInfo(NetworkInfo source) {
        if (source != null) {
            synchronized (source) {
                mNetworkType = source.mNetworkType;
                mSubtype = source.mSubtype;
                mTypeName = source.mTypeName;
                mSubtypeName = source.mSubtypeName;
                mState = source.mState;
                mDetailedState = source.mDetailedState;
                mReason = source.mReason;
                mExtraInfo = source.mExtraInfo;
                mIsFailover = source.mIsFailover;
                mIsRoaming = source.mIsRoaming;
                mIsAvailable = source.mIsAvailable;
            }
        }
    }

    public int getType() {
        synchronized (this) {
            return mNetworkType;
        }
    }

    public void setType(int type) {
        synchronized (this) {
            mNetworkType = type;
        }
    }

    public int getSubtype() {
        synchronized (this) {
            return mSubtype;
        }
    }

    public void setSubtype(int subtype, String subtypeName) {
        synchronized (this) {
            mSubtype = subtype;
            mSubtypeName = subtypeName;
        }
    }

    public String getTypeName() {
        synchronized (this) {
            return mTypeName;
        }
    }

    public String getSubtypeName() {
        synchronized (this) {
            return mSubtypeName;
        }
    }

    public boolean isConnectedOrConnecting() {
        synchronized (this) {
            return mState == State.CONNECTED || mState == State.CONNECTING;
        }
    }

    public boolean isConnected() {
        synchronized (this) {
            return mState == State.CONNECTED;
        }
    }

    public boolean isAvailable() {
        synchronized (this) {
            return mIsAvailable;
        }
    }

    public void setIsAvailable(boolean isAvailable) {
        synchronized (this) {
            mIsAvailable = isAvailable;
        }
    }

    public boolean isFailover() {
        synchronized (this) {
            return mIsFailover;
        }
    }

    public void setFailover(boolean isFailover) {
        synchronized (this) {
            mIsFailover = isFailover;
        }
    }

    public boolean isRoaming() {
        synchronized (this) {
            return mIsRoaming;
        }
    }

    public void setRoaming(boolean isRoaming) {
        synchronized (this) {
            mIsRoaming = isRoaming;
        }
    }

    public State getState() {
        synchronized (this) {
            return mState;
        }
    }

    public DetailedState getDetailedState() {
        synchronized (this) {
            return mDetailedState;
        }
    }

    public void setDetailedState(DetailedState detailedState, String reason, String extraInfo) {
        synchronized (this) {
            this.mDetailedState = detailedState;
            this.mState = stateMap.get(detailedState);
            this.mReason = reason;
            this.mExtraInfo = extraInfo;
        }
    }

    public void setExtraInfo(String extraInfo) {
        synchronized (this) {
            this.mExtraInfo = extraInfo;
        }
    }

    public String getReason() {
        synchronized (this) {
            return mReason;
        }
    }

    public String getExtraInfo() {
        synchronized (this) {
            return mExtraInfo;
        }
    }

    @Override
    public String toString() {
        synchronized (this) {
            StringBuilder builder = new StringBuilder("[");
            builder.append("type: ").append(getTypeName()).append("[").append(getSubtypeName()).
                    append("], state: ").append(mState).append("/").append(mDetailedState).
                    append(", reason: ").append(mReason == null ? "(unspecified)" : mReason).
                    append(", extra: ").append(mExtraInfo == null ? "(none)" : mExtraInfo).
                    append(", roaming: ").append(mIsRoaming).
                    append(", failover: ").append(mIsFailover).
                    append(", isAvailable: ").append(mIsAvailable).
                    append("]");
            return builder.toString();
        }
    }

    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        synchronized (this) {
            dest.writeInt(mNetworkType);
            dest.writeInt(mSubtype);
            dest.writeString(mTypeName);
            dest.writeString(mSubtypeName);
            dest.writeString(mState.name());
            dest.writeString(mDetailedState.name());
            dest.writeInt(mIsFailover ? 1 : 0);
            dest.writeInt(mIsAvailable ? 1 : 0);
            dest.writeInt(mIsRoaming ? 1 : 0);
            dest.writeString(mReason);
            dest.writeString(mExtraInfo);
        }
    }

    public static final Creator<NetworkInfo> CREATOR =
            new Creator<NetworkInfo>() {
                public NetworkInfo createFromParcel(Parcel in) {
                    int netType = in.readInt();
                    int subtype = in.readInt();
                    String typeName = in.readString();
                    String subtypeName = in.readString();
                    NetworkInfo netInfo = new NetworkInfo(netType, subtype, typeName, subtypeName);
                    netInfo.mState = State.valueOf(in.readString());
                    netInfo.mDetailedState = DetailedState.valueOf(in.readString());
                    netInfo.mIsFailover = in.readInt() != 0;
                    netInfo.mIsAvailable = in.readInt() != 0;
                    netInfo.mIsRoaming = in.readInt() != 0;
                    netInfo.mReason = in.readString();
                    netInfo.mExtraInfo = in.readString();
                    return netInfo;
                }

                public NetworkInfo[] newArray(int size) {
                    return new NetworkInfo[size];
                }
            };
}