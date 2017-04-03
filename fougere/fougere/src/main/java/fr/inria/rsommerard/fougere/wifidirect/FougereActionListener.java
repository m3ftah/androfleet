package fr.inria.rsommerard.fougere.wifidirect;

import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import fr.inria.rsommerard.fougere.Fougere;

/**
 * Created by Romain on 08/08/16.
 */
public class FougereActionListener implements WifiP2pManager.ActionListener {

    private String onSuccessMessage;
    private String onFailureMessage;

    public FougereActionListener(final String onSuccessMessage, final String onFailureMessage) {
        this.onSuccessMessage = onSuccessMessage;
        this.onFailureMessage = onFailureMessage;
    }

    @Override
    public void onSuccess() {
        if (this.onSuccessMessage != null) {
            Log.d(Fougere.TAG, "[FougereActionListener] " + this.onSuccessMessage);
        }
    }

    @Override
    public void onFailure(final int reason) {
        if (this.onFailureMessage != null) {
            Log.e(Fougere.TAG, "[FougereActionListener] " + this.onFailureMessage +
                    this.getActionListenerFailureName(reason));
        }
    }

    private String getActionListenerFailureName(int reason) {
        switch (reason) {
            case 0:
                return "The operation failed due to an internal error. (0, ERROR)";
            case 1:
                return "The operation failed because p2p is unsupported on the device. " +
                        "(1, P2P_UNSUPPORTED)";
            case 2:
                return "The operation failed because the framework is busy and unable to service " +
                        "the request. (2, BUSY)";
            case 3:
                return "The discoverServices failed because no service requests are added. " +
                        "Use addServiceRequest to add a service request. (3, NO_SERVICE_REQUESTS)";
            default:
                return "The operation failed due to an unknown error. (" + reason + ", UNKNOWN)";
        }
    }
}
