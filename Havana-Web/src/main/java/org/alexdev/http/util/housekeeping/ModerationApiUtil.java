package org.alexdev.http.util.housekeeping;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ModerationApiUtil {
    public static List<String> splitUsernames(String users) {
        String usersList = users.substring(1, users.length() - 1);

        return Arrays.stream(usersList.split(",,"))
                .filter(s -> !s.isEmpty())
                .map(String::trim)
                .collect(Collectors.toList());
    }

    public static String replaceLineBreaks(String str) {
        return str.replace("\n", ",").replace("\r", ",")/*.replace("\r\n", ",")*/;
    }
}