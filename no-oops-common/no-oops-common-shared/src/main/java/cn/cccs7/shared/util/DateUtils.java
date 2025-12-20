package cn.cccs7.shared.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Date utility class with common date operations
 */
public class DateUtils {
    
    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
    public static final String DEFAULT_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    
    /**
     * Format LocalDate to string
     *
     * @param date date to format
     * @param pattern date pattern
     * @return formatted date string
     */
    public static String format(LocalDate date, String pattern) {
        if (date == null) {
            return null;
        }
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }
    
    /**
     * Format LocalDateTime to string
     *
     * @param dateTime date time to format
     * @param pattern date time pattern
     * @return formatted date time string
     */
    public static String format(LocalDateTime dateTime, String pattern) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }
    
    /**
     * Calculate days between two dates
     *
     * @param startDate start date
     * @param endDate end date
     * @return days between dates
     */
    public static long daysBetween(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return 0;
        }
        return ChronoUnit.DAYS.between(startDate, endDate);
    }
    
    /**
     * Add days to a date
     *
     * @param date base date
     * @param days days to add
     * @return new date
     */
    public static LocalDate addDays(LocalDate date, long days) {
        if (date == null) {
            return null;
        }
        return date.plusDays(days);
    }
}