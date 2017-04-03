package fr.inria.rsommerard.fougere;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.security.SecureRandom;

/**
 * Created by romain on 05/09/16.
 */
public class FougereDistanceTest {

    private SecureRandom random;
    private FougereDistance fougereDistance;

    @Before
    public void setup() {
        this.random = Mockito.mock(SecureRandom.class);
        this.fougereDistance = new FougereDistance(this.random);
    }

    @Test
    public void addANewUser() {
        Mockito.when(this.random.nextDouble()).thenReturn(0.3);

        this.fougereDistance.addUser("IDENTIFIER");

        Assert.assertTrue(this.fougereDistance.canSendTo("IDENTIFIER"));
    }

    @Test
    public void addAnExistingUser() {
        Mockito.when(this.random.nextDouble()).thenReturn(0.3);

        this.fougereDistance.addUser("IDENTIFIER");

        Assert.assertTrue(this.fougereDistance.canSendTo("IDENTIFIER"));
    }

    @Test
    public void canSendToANewUser() {
        Mockito.when(this.random.nextDouble()).thenReturn(0.3);
        Assert.assertFalse(this.fougereDistance.canSendTo("IDENTIFIER"));
    }

    @Test
    public void cannotSendToANewUser() {
        Mockito.when(this.random.nextDouble()).thenReturn(0.6);
        Assert.assertFalse(this.fougereDistance.canSendTo("IDENTIFIER"));
    }

    @Test
    public void canSendToAnExistingUser() {
        Mockito.when(this.random.nextDouble()).thenReturn(0.3);
        this.fougereDistance.addUser("IDENTIFIER");
        Assert.assertTrue(this.fougereDistance.canSendTo("IDENTIFIER"));
    }

    @Test
    public void cannotSendToAnExistingUser() {
        Mockito.when(this.random.nextDouble()).thenReturn(0.6);
        this.fougereDistance.addUser("IDENTIFIER");
        Assert.assertFalse(this.fougereDistance.canSendTo("IDENTIFIER"));
    }

    @Test
    public void updateDistanceForANewUser() {
        Mockito.when(this.random.nextDouble()).thenReturn(0.2);
        this.fougereDistance.addUser("IDENTIFIER");
        this.fougereDistance.updateDistance("IDENTIFIER");
        Assert.assertTrue(this.fougereDistance.canSendTo("IDENTIFIER"));
    }

    @Test
    public void updateDistanceForAnExistingUser() {
        Mockito.when(this.random.nextDouble()).thenReturn(0.2);
        this.fougereDistance.addUser("IDENTIFIER");
        this.fougereDistance.updateDistance("IDENTIFIER");

        this.fougereDistance.updateDistance("IDENTIFIER");
        Assert.assertFalse(this.fougereDistance.canSendTo("IDENTIFIER"));
    }
}
