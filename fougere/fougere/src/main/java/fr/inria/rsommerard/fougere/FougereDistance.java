package fr.inria.rsommerard.fougere;

import android.util.Log;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by romain on 05/09/16.
 */
public class FougereDistance {

    private final Map<String, FougereUser> users;
    private final SecureRandom random;

    public FougereDistance(final SecureRandom random) {
        this.users = new HashMap<>();
        this.random = random;
    }

    public boolean canSendTo(final String identifier) {
        FougereUser user = this.users.get(identifier);

        if (user == null) {
            Log.e(Fougere.TAG, "[FougereDistance] User not found");
            return false;
        }

        double draw = this.random.nextDouble();
        boolean canSend = draw < user.getDistance();

        //Log.d(Fougere.TAG, "[FougereDistance] Distance: " + Double.toString(user.getDistance()));
        //Log.d(Fougere.TAG, "[FougereDistance] Drawn: " + Double.toString(draw));
        //Log.d(Fougere.TAG, "[FougereDistance] Sending: " + Boolean.toString(canSend));

        return true;//return canSend;
    }

    public void updateDistance(final String identifier) {
        FougereUser user = this.users.get(identifier);

        if (user == null) {
            Log.e(Fougere.TAG, "[FougereDistance] User not found");
            return;
        }

        Log.d(Fougere.TAG, "[FougereDistance] Old distance: " + Double.toString(user.getDistance()));

        user.updateDistance();

        Log.d(Fougere.TAG, "[FougereDistance] New distance: " + Double.toString(user.getDistance()));
    }

    public boolean containsUser(final String identifier) {
        return this.users.containsKey(identifier);
    }

    public void addUser(final String identifier) {
        Log.d(Fougere.TAG, "[FougereDistance] Adding user to the Map");

        if (this.users.containsKey(identifier)) {
            Log.e(Fougere.TAG, "[FougereDistance] User already exists");
            return;
        }

        FougereUser user = new FougereUser(identifier);
        this.users.put(identifier, user);
    }
}
