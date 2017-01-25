package de.tudarmstadt.informatik.tk.assistance.sdk.util;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 26.11.2015
 */
public enum StringUtils {
    ;

    /**
     * Returns true if string IS null or IS empty
     * else - otherwise
     *
     * @param str
     * @return
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * Returns true if string NOT null and NOT empty
     * else - otherwise
     *
     * @param str
     * @return
     */
    public static boolean isNotNullAndEmpty(String str) {
        return str != null && !str.isEmpty();
    }
}
