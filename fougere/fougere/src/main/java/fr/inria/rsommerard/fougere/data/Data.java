package fr.inria.rsommerard.fougere.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Romain on 17/08/16.
 */
@Entity
public class Data {

    @Id(autoincrement = true)
    private Long id;

    @Property
    @NotNull
    private String identifier;

    @Property
    @NotNull
    private String content;

    @Property
    @NotNull
    private int ttl; // Live value of the data

    @Property
    @NotNull
    private int disseminate; // How many times a data need to be sended to different users

    @Property
    @NotNull
    private int sent; // How many times the data had been sent to different users

    @Generated(hash = 1933346554)
    public Data(Long id, @NotNull String identifier, @NotNull String content, int ttl,
                int disseminate, int sent) {
        this.id = id;
        this.identifier = identifier;
        this.content = content;
        this.ttl = ttl;
        this.disseminate = disseminate;
        this.sent = sent;
    }

    @Generated(hash = 2135787902)
    public Data() {
    }

    public static List<Data> deGsonify(final String json) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        Type type = new TypeToken<ArrayList<Data>>() {}.getType();

        return gson.fromJson(json, type);
    }

    public static String gsonify(final List<Data> data) {
        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();
        Gson gson = builder.create();

        Type type = new TypeToken<ArrayList<Data>>() {}.getType();

        return gson.toJson(data, type);
    }

    public static Data reset(final Data data) {
        return new Data(null, data.getIdentifier(), data.getContent(), data.getTtl(),
                data.getDisseminate(), 0);
    }

    @Override
    public String toString() {
        return "{\"id\":\"" + this.id + "\",\"identifier\":\"" + this.identifier +
                "\",\"content\":" + this.content + ",\"ttl\":\"" + this.ttl +
                "\",\"disseminate\":\"" + this.disseminate + "\",\"sent\":\"" + this.sent + "\"}";
    }

    public int getSent() {
        return this.sent;
    }

    public void setSent(int sent) {
        this.sent = sent;
    }

    public int getDisseminate() {
        return this.disseminate;
    }

    public void setDisseminate(int disseminate) {
        this.disseminate = disseminate;
    }

    public int getTtl() {
        return this.ttl;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
