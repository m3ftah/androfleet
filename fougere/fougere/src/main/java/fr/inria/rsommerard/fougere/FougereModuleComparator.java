package fr.inria.rsommerard.fougere;

import java.util.Comparator;

/**
 * Created by Romain on 23/08/16.
 */
public class FougereModuleComparator implements Comparator<FougereModule> {

    @Override
    public int compare(FougereModule m1, FougereModule m2) {
        if (m1.getRatio() < m2.getRatio()) {
            return -1;
        } else if (m1.getRatio() > m2.getRatio()) {
            return 1;
        } else {
            return 0;
        }
    }
}
