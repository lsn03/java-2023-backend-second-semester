package edu.java.util;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public final class Utils {
    private Utils() {
    }

    public static OffsetDateTime convertLongToOffsetDayTime(Long time) {

        return time == null ? null : Instant.ofEpochSecond(time).atOffset(ZoneOffset.UTC);
    }
}
