package com.dingi.sdk.dingitelemetry;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import timber.log.Timber;

public class TelemetryClient {


    private static final String HTTPS_SCHEME = "https";
    private static final String COM_EVENTS_HOST = "tiles.dingi.live";
    private static final String EVENTS_ENDPOINT = "/auth/events/v2";
    private static final String ACCESS_TOKEN_QUERY_PARAMETER = "access_token";
    private static final String USER_AGENT_REQUEST_HEADER = "User-Agent";


    private String dingiMapAccessToken;
    private String userAgent;
    private TelemetryClientSettings setting;
    private final Logger logger;
    private CertificateBlacklist certificateBlacklist;

    TelemetryClient( String dingiMapAccessToken, String userAgent, TelemetryClientSettings setting, Logger logger,
                     CertificateBlacklist certificateBlacklist) {
        this.dingiMapAccessToken = dingiMapAccessToken;
        this.userAgent = userAgent;
        this.setting = setting;
        this.logger = logger;
        this.certificateBlacklist = certificateBlacklist;
    }


    void sendEvents(List<Event> events) {
        ArrayList<Event> batch = new ArrayList<>();


        //only for turnstile
        //i filtered out only the turnstile events here

        for(int i=0;i<events.size() ; i++){
            if(Event.Type.TURNSTILE.equals(events.get(i).obtainType())){
                batch.add(events.get(i));
            }
            if(Event.Type.LOCATION.equals(events.get(i).obtainType())){
                Timber.tag("Dingi").d("found a location event");
                batch.add(events.get(i));
            }
            Timber.tag("Dingi").d("found a event and the type is %s", events.get(i).obtainType());

        }
        batch.addAll(events);
        if(batch.size()>0)sendBatch(batch);
    }



    public void sendBatch(List<Event> batch) {

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        GsonBuilder gsonBuilder = configureGsonBuilder();
        Gson gson = gsonBuilder.create();
        String payload = gson.toJson(batch);
        // Timber.tag("Dingi").d("the payload is being sent and the payload is %s", payload);

        RequestBody body = RequestBody.create(JSON, payload);


        HttpUrl.Builder builder = new HttpUrl.Builder()
                .scheme(HTTPS_SCHEME);
        builder.host(COM_EVENTS_HOST);
        HttpUrl baseUrl = builder.build();
        //String dingiMapAccessToken = "EjFUMTUMKFcnJ2VzRnL39Cd2ixtHScJ2p0C1vhP2";
        HttpUrl url = baseUrl.newBuilder(EVENTS_ENDPOINT)
                .addQueryParameter(ACCESS_TOKEN_QUERY_PARAMETER, dingiMapAccessToken).build();


        Log.d("Dingi" , "the payload is being sent and the url is "+ url.toString());
        Request request = new Request.Builder()
                .url(url)
                .header(USER_AGENT_REQUEST_HEADER, "normal user agent")
                .post(body)
                .build();
        Log.d("Dingi" , "the payload is being sent and the body is "+ bodyToString(request));

        //for using https
        OkHttpClient client = setting.getClient(certificateBlacklist);


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("Dingi" , "dingi telemetry request has returned and the error    is  "+ e.toString());
                Timber.tag("Dingi").d("dingi telemetry request has returned and the error    is  %s", e.toString());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.d("Dingi" , "dingi telemetry request has returned and the response    is  "+response.toString());
                Timber.tag("Dingi").d("dingi telemetry request has returned and the response is  %s", response.toString());
            }
        });
    }


    private GsonBuilder configureGsonBuilder() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder;
    }

    private static String bodyToString(final Request request){

        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }
}
