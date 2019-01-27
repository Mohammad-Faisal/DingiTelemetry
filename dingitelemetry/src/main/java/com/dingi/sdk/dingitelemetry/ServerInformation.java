package com.dingi.sdk.dingitelemetry;

class ServerInformation {
    private Environment environment;
    private String hostname;
    private String dingiMapAccessToken;


    ServerInformation(Environment environment) {
        this.environment = environment;
    }

    Environment getEnvironment() {
        return environment;
    }

    String getHostname() {
        return hostname;
    }

    void setHostname(String hostname) {
        this.hostname = hostname;
    }

    String getAccessToken() {
        return dingiMapAccessToken;
    }

    void setAccessToken(String dingiMapAccessToken) {
        this.dingiMapAccessToken = dingiMapAccessToken;
    }


}
