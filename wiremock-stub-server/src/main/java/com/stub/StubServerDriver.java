package com.stub;

import com.github.tomakehurst.wiremock.WireMockServer;

import java.util.HashMap;
import java.util.Map;

public class StubServerDriver {

    public static Map<String, WireMockServer> mockServers = new HashMap<>();

    public static void registerMockServer(String serverName, WireMockServer mockServer) {
        mockServers.put(serverName, mockServer);
    }

    public static WireMockServer getServer(String serverName) {
        return mockServers.get(serverName);
    }


}
