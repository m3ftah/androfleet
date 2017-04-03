package fr.inria.rsommerard.fougere.wifidirect;

import java.io.Serializable;

/**
 * Created by Romain on 14/08/2016.
 */
public class Message implements Serializable {

    private final String content;

    public Message(final String content) {
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }
}
