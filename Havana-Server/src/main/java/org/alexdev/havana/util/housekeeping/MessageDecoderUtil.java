package org.alexdev.havana.util.housekeeping;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class MessageDecoderUtil {
    public static String decodeMessage(String message) {
        return URLDecoder.decode(message, StandardCharsets.ISO_8859_1);
    }
}
