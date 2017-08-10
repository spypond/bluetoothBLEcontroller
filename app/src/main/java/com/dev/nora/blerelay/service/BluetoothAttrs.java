package com.dev.nora.blerelay.service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nguoi on 8/20/2016.
 */
public class BluetoothAttrs {
    private static Map<String, String> attributes = new HashMap<>();

    public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";
    public static final String UUID_NOTIFY = "0000ffe1-0000-1000-8000-00805f9b34fb";
    public static final String UUID_SERVICE = "0000ffe0-0000-1000-8000-00805f9b34fb";
    public static String UUID_WRITE = "0000ffe1-0000-1000-8000-00805f9b34fb";

    static {
        // Sample Services.
        attributes.put(UUID_NOTIFY, "RELAY NOTIFY");
        attributes.put(UUID_SERVICE, "Device Information Service");
    }

    public static String lookup(String uuid, String defaultName) {
        String name = attributes.get(uuid);
        return name == null ? defaultName : name;
    }

}
