package fr.inria.rsommerard.widi.net.wifi.p2p;

import android.text.TextUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import fr.inria.rsommerard.widi.net.wifi.p2p.WifiP2pDevice;

public class WifiP2pDeviceList {

    private final HashMap<String, WifiP2pDevice> mDevices = new HashMap<String, WifiP2pDevice>();

    public void update(WifiP2pDevice device) {
        updateSupplicantDetails(device);
        mDevices.get(device.deviceAddress).status = device.status;
    }

    public Collection<WifiP2pDevice> getDeviceList() {
        return Collections.unmodifiableCollection(mDevices.values());
    }

    /** Only updates details fetched from the supplicant @hide */
    public void updateSupplicantDetails(WifiP2pDevice device) {
        validateDevice(device);
        WifiP2pDevice d = mDevices.get(device.deviceAddress);
        if (d != null) {
            d.deviceName = device.deviceName;
            d.primaryDeviceType = device.primaryDeviceType;
            d.secondaryDeviceType = device.secondaryDeviceType;
            d.wpsConfigMethodsSupported = device.wpsConfigMethodsSupported;
            d.deviceCapability = device.deviceCapability;
            d.groupCapability = device.groupCapability;
            d.wfdInfo = device.wfdInfo;
            return;
        }
        //Not found, add a new one
        mDevices.put(device.deviceAddress, device);
    }

    private void validateDevice(WifiP2pDevice device) {
        if (device == null) throw new IllegalArgumentException("Null device");
        if (TextUtils.isEmpty(device.deviceAddress)) {
            throw new IllegalArgumentException("Empty deviceAddress");
        }
    }

    public String toString() {
        StringBuffer sbuf = new StringBuffer();
        for (WifiP2pDevice device : mDevices.values()) {
            sbuf.append("\n").append(device);
        }
        return sbuf.toString();
    }
}
