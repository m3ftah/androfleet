package fr.inria.rsommerard.fougereapp;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by lakhdar on 12/8/16.
 */

public class RestServer {
    public final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private RestServerListener rsl;
    public final String URL = "http://193.51.236.140:9200/geoip/location/";

    OkHttpClient client = new OkHttpClient();

    public RestServer(RestServerListener restServerListener) {
        this.rsl = restServerListener;
    }
    public void send(String id, float ping, double lat, double lon){
        final String json = jsonData(id,String.valueOf(ping),String.valueOf(lat),String.valueOf(lon));
        Log.d(MainActivity.TAG,"sending over Rest:" + json);
        final AsyncTask<Object, Object, Void> task = new AsyncTask<Object, Object, Void>() {
            @Override
            protected Void doInBackground(Object... voids) {
                try {
                    String response = post(URL,json);
                    rsl.onUploadFinished(response);
                } catch (IOException e) {
                    e.printStackTrace();
                    rsl.onError(e.getMessage());
                }
                return null;
            }

        };
        task.execute();
    }

    String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static String jsonData(String id, String ping, String lat, String lon) {
        return "{\"id\":\""+ id +"\","
                + "\"ping\":\"" + ping + "\","
                + "\"location\":"
                + "{\"lon\":\"" + lon + "\","
                + "\"lat\":\"" + lat + "\"}"
                + "}";
    }

    public interface RestServerListener{
        void onUploadFinished(String response);
        void onError(String error);
    }
}
