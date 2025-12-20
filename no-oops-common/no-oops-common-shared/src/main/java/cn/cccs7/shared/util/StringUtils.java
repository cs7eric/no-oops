package cn.cccs7.shared.util;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * String utility class with common string operations
 */
public class StringUtils {
    
    /**
     * Check if string is blank (null, empty, or whitespace only)
     *
     * @param str string to check
     * @return true if blank, false otherwise
     */
    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }
    
    /**
     * Check if string is not blank
     *
     * @param str string to check
     * @return true if not blank, false otherwise
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
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
        if (str == null) {
            return null;
        }
        
        if (str.length() <= maxLength) {
            return str;
        }
        
        return str.substring(0, maxLength);
    }
}