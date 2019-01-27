package com.dingi.sdk.dingitelemetry;


class EnvironmentChain {

    EnvironmentResolver setup() {
        EnvironmentResolver com = new ComServerInformation();
        return com;

        /*EnvironmentResolver staging = new StagingServerInformation();
        staging.nextChain(com);
        EnvironmentResolver rootOfTheChain = new ChinaServerInformation();
        rootOfTheChain.nextChain(staging);

        return rootOfTheChain;*/
    }
}
