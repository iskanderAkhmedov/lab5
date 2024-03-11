package org.example.utils;

import lombok.experimental.UtilityClass;

import java.sql.Timestamp;
import java.time.LocalDate;

@UtilityClass
public class DateUtils {
    public static Timestamp localDateToTimestamp(LocalDate localDate) {
        return localDate == null ? null : Timestamp.valueOf(localDate.atStartOfDay());
    }
}
