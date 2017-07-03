package mock.net.wifi;

public class WpsInfo {

    public static final int PBC     = 0;

    public static final int DISPLAY = 1;

    public static final int KEYPAD  = 2;

    public static final int LABEL   = 3;

    public static final int INVALID = 4;

    public int setup;

    public String BSSID;

    public String pin;

    public WpsInfo() {
        setup = INVALID;
        BSSID = "";
        pin = "";
    }

    public String toString() {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append(" setup: ").append(setup);
        sbuf.append('\n');
        sbuf.append(" BSSID: ").append(BSSID);
        sbuf.append('\n');
        sbuf.append(" pin: ").append(pin);
        sbuf.append('\n');
        return sbuf.toString();
    }

    public WpsInfo(WpsInfo source) {
        if (source != null) {
            setup = source.setup;
            BSSID = source.BSSID;
            pin = source.pin;
        }
    }
}
