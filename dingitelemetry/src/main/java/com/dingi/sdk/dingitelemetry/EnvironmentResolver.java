package com.dingi.sdk.dingitelemetry;

import android.os.Bundle;

interface EnvironmentResolver {
    void nextChain(EnvironmentResolver chain);

    ServerInformation obtainServerInformation(Bundle appMetaData);
}