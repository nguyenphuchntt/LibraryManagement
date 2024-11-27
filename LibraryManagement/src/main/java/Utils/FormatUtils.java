package Utils;

import java.text.SimpleDateFormat;

public class FormatUtils {

    public static int StringToInteger(String value) throws NumberFormatException {
        if (value == null || value.isEmpty()) {
            throw new NumberFormatException();
        }
        int i = 0;
        try {
            i = Integer.parseInt(value);
            if (i < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            throw new NumberFormatException();
        }
        return i;
    }

    public static String extractYear(String publishedDate) {
        if (publishedDate == null || publishedDate.isEmpty()) {
            return "Unknown";
        }
        return publishedDate.split("-")[0];
    }

    public static String refactorNames(String namesString) {
        String[] names = namesString.replaceAll("[\\[\\]\"]", "").split(",\\s*");

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < names.length; i++) {
            String firstName = names[i].split(" ")[0];
            result.append(firstName);

            if (i < names.length - 1) {
                result.append(", ");
            }
        }
        return result.toString();
    }

    public static String getShortDescription(String text, int maxLength) {
        if (text == null || text.isEmpty()) {
            return "No description available";
        }
        return text.length() > maxLength ? text.substring(0, maxLength) + "..." : text;
    }

    public static SimpleDateFormat getDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd");
    }

    public static SimpleDateFormat getDateTimeFormat() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
}
