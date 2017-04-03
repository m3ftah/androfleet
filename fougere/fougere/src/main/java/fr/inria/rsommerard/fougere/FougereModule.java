package fr.inria.rsommerard.fougere;

import java.util.List;

import fr.inria.rsommerard.fougere.data.Data;

/**
 * Created by Romain on 23/08/16.
 */
public interface FougereModule {

    List<Data> getAllData();

    void addData(final Data data);
    void removeData(final Data data);

    void start();
    void stop();

    int getRatio();
    void setRatio(int ratio);

    String getName();
}
