package fr.inria.rsommerard.fougere;

/**
 * Created by romain on 05/09/16.
 */
public class FougereUser {

    private String identifier;
    private double distance;

    public FougereUser(final String identifier) {
        this.identifier = identifier;
        this.distance = 0.5;
    }

    public void setDistance(final double distance) {
        this.distance = distance;
    }

    public double getDistance() {
        return this.distance;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public void updateDistance() {
        this.distance /= 2;
    }
}
