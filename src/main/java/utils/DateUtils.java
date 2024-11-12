package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class DateUtils {

    /**
     * Checks if the given date string is in a valid date format.
     *
     * @param date the date string to check
     * @return true if the date string is in a valid date format, false otherwise
     */
    public static boolean isValidDate(String date) {
        String format = "yyyy-MM-dd'T'HH:mm:ssXXX";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setLenient(false);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            sdf.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * Checks if the given date string is in a valid Unix date format.
     *
     * @param date the date string to check
     * @return true if the date string is in a valid Unix date format, false otherwise
     */
    public static boolean isValidUnixDateFormat(String date) {
        try {
            long unixTime = Long.parseLong(date);
            return unixTime >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if the given date string is in a valid UTC date format.
     *
     * @param date the date string to check
     * @return true if the date string is in a valid UTC date format, false otherwise
     */
    public static boolean isValidUTCDateFormat(String date) {
        String format = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setLenient(false);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            sdf.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

}
