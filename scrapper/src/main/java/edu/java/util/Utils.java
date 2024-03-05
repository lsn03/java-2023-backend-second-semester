package edu.java.util;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public final class Utils {
    public static final String GITHUB_BASE_URL = "github-base-url";
    public static final String SOF_BASE_URL = "sof-base-url";

    private Utils() {
    }

    public static OffsetDateTime convertLongToOffsetDayTime(Long time) {

        return time == null ? null : Instant.ofEpochSecond(time).atOffset(ZoneOffset.UTC);
    }
}
