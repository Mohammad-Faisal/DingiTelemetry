package com.dingi.sdk.dingitelemetry;


import android.support.annotation.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.ConnectionSpec;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

import static com.dingi.sdk.dingitelemetry.Environment.CHINA;
import static com.dingi.sdk.dingitelemetry.Environment.COM;
import static com.dingi.sdk.dingitelemetry.Environment.STAGING;

class TelemetryClientSettings {

    private static final String STAGING_EVENTS_HOST = "18.236.247.77";
    private static final String COM_EVENTS_HOST = "tiles.dingi.live";
    private static final String CHINA_EVENTS_HOST = "tiles.dingi.live";
    private static final Map<Environment, String> HOSTS = new HashMap<Environment, String>() {
        {
            put(STAGING, STAGING_EVENTS_HOST);
            put(COM, COM_EVENTS_HOST);
            put(CHINA, CHINA_EVENTS_HOST);
        }
    };


    //changed into http from https for testing purpose
    private static final String HTTPS_SCHEME = "https";
    private static final String HTTP_SCHEME = "http";
    private Environment environment;
    private final OkHttpClient client;
    private final HttpUrl baseUrl;
    private final SSLSocketFactory sslSocketFactory;
    private final X509TrustManager x509TrustManager;
    private final HostnameVerifier hostnameVerifier;
    private boolean debugLoggingEnabled;

    private TelemetryClientSettings(TelemetryClientSettings.Builder builder) {
        this.environment = builder.environment;
        this.client = builder.client;
        this.baseUrl = builder.baseUrl;
        this.sslSocketFactory = builder.sslSocketFactory;
        this.x509TrustManager = builder.x509TrustManager;
        this.hostnameVerifier = builder.hostnameVerifier;
        this.debugLoggingEnabled = builder.debugLoggingEnabled;
    }

    Environment getEnvironment() {
        return environment;
    }


    //changed
    OkHttpClient getClient(CertificateBlacklist certificateBlacklist) {
        return configureHttpClient(certificateBlacklist, new GzipRequestInterceptor());
        //return configureHttpClient(certificateBlacklist,null);
    }

    OkHttpClient getAttachmentClient(CertificateBlacklist certificateBlacklist) {
        return configureHttpClient(certificateBlacklist, null);
    }

    HttpUrl getBaseUrl() {
        return baseUrl;
    }

    boolean isDebugLoggingEnabled() {
        return debugLoggingEnabled;
    }

    TelemetryClientSettings.Builder toBuilder() {
        return new TelemetryClientSettings.Builder()
                .environment(environment)
                .client(client)
                .baseUrl(baseUrl)
                .sslSocketFactory(sslSocketFactory)
                .x509TrustManager(x509TrustManager)
                .hostnameVerifier(hostnameVerifier)
                .debugLoggingEnabled(debugLoggingEnabled);
    }

    static HttpUrl configureUrlHostname(String eventsHost) {


        HttpUrl.Builder builder = new HttpUrl.Builder()
                .scheme(HTTPS_SCHEME);
        builder.host(eventsHost);
        //builder.port(8001); //need if use http
        builder.addPathSegment("auth");
        builder.addPathSegment("events");
        builder.addPathSegment("v2");
        return builder.build();


    }

    static final class Builder {
        Environment environment = COM;
        OkHttpClient client = new OkHttpClient();
        HttpUrl baseUrl = null;
        SSLSocketFactory sslSocketFactory = null;
        X509TrustManager x509TrustManager = null;
        HostnameVerifier hostnameVerifier = null;
        boolean debugLoggingEnabled = false;

        Builder() {
        }

        TelemetryClientSettings.Builder environment(Environment environment) {
            this.environment = environment;
            return this;
        }

        TelemetryClientSettings.Builder client(OkHttpClient client) {
            if (client != null) {
                this.client = client;
            }
            return this;
        }

        TelemetryClientSettings.Builder baseUrl(HttpUrl baseUrl) {
            if (baseUrl != null) {
                this.baseUrl = baseUrl;
            }
            return this;
        }

        TelemetryClientSettings.Builder sslSocketFactory(SSLSocketFactory sslSocketFactory) {
            this.sslSocketFactory = sslSocketFactory;
            return this;
        }

        TelemetryClientSettings.Builder x509TrustManager(X509TrustManager x509TrustManager) {
            this.x509TrustManager = x509TrustManager;
            return this;
        }

        TelemetryClientSettings.Builder hostnameVerifier(HostnameVerifier hostnameVerifier) {
            this.hostnameVerifier = hostnameVerifier;
            return this;
        }

        TelemetryClientSettings.Builder debugLoggingEnabled(boolean debugLoggingEnabled) {
            this.debugLoggingEnabled = debugLoggingEnabled;
            return this;
        }

        TelemetryClientSettings build() {
            if (baseUrl == null) {
                String eventsHost = HOSTS.get(environment);
                this.baseUrl = configureUrlHostname(eventsHost);
            }
            return new TelemetryClientSettings(this);
        }
    }

    private OkHttpClient configureHttpClient(CertificateBlacklist certificateBlacklist,
                                             @Nullable Interceptor interceptor) {
        CertificatePinnerFactory factory = new CertificatePinnerFactory();
        OkHttpClient.Builder builder = client.newBuilder()
                .retryOnConnectionFailure(true)
                .certificatePinner(factory.provideCertificatePinnerFor(environment, certificateBlacklist))
                .connectionSpecs(Arrays.asList(ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS));

        if (interceptor != null) {
            builder.addInterceptor(interceptor);
        }

        if (isSocketFactoryUnset(sslSocketFactory, x509TrustManager)) {
            builder.sslSocketFactory(sslSocketFactory, x509TrustManager);
            builder.hostnameVerifier(hostnameVerifier);
        }

        return builder.build();
    }





    private boolean isSocketFactoryUnset(SSLSocketFactory sslSocketFactory, X509TrustManager x509TrustManager) {
        return sslSocketFactory != null && x509TrustManager != null;
    }
}
