package cn.cccs7.shared.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * String utility class with common string operations
 */
public class NoOopsStringUtils {
    
    /**
     * Check if string is blank (null, empty, or whitespace only)
     *
     * @param str string to check
     * @return true if blank, false otherwise
     */
    public static boolean isBlank(String str) {
        return StringUtils.isBlank(str);
    }
    
    /**
     * Check if string is not blank
     *
     * @param str string to check
     * @return true if not blank, false otherwise
     */
    public static boolean isNotBlank(String str) {
        return StringUtils.isNotBlank(str);
    }
    
    /**
     * Generate random string with specified length
     *
     * @param length length of random string
     * @return random string
     */
    public static String randomString(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }
    
    /**
     * Truncate string to specified length
     *
     * @param str string to truncate
     * @param maxLength maximum length
     * @return truncated string
     */
    public static String truncate(String str, int maxLength) {
        return StringUtils.truncate(str, maxLength);
    }
    
    /**
     * Check if string is empty (null or empty)
     *
     * @param str string to check
     * @return true if empty, false otherwise
     */
    public static boolean isEmpty(String str) {
        return StringUtils.isEmpty(str);
    }
    
    /**
     * Check if string is not empty
     *
     * @param str string to check
     * @return true if not empty, false otherwise
     */
    public static boolean isNotEmpty(String str) {
        return StringUtils.isNotEmpty(str);
    }
}