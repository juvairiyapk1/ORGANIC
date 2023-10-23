package com.timeco.application.Configuration;

import java.util.Formatter;

public class ByteUtils {
    public static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        try (Formatter formatter = new Formatter(result)) {
            for (byte b : bytes) {
                formatter.format("%02x", b);
            }
        }
        return result.toString();
    }
}
