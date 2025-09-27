package org.alexdev.http.util.housekeeping;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class MessageEncoderUtil {
    public static String encodeMessage(String message) {
        return URLEncoder.encode(message, StandardCharsets.UTF_8);
    }
}