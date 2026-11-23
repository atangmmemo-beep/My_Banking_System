package controller;

// Small helper methods that check what the user typed.
// They are static, so we call them like this:  Validator.isEmpty(text)
public class Validator {

    // true if nothing was typed (or only spaces)
    public static boolean isEmpty(String text) {
        return text == null || text.trim().equals("");

    }

    // true if the text is a real number, like 250 or 99.5
    public static boolean isNumber(String text) {
        if (text == null) {
            return false;
        }
        try {
            double value = Double.parseDouble(text.trim());
            return !Double.isNaN(value) && !Double.isInfinite(value);
        } catch (NumberFormatException e) {
            return false;           // the text was not a number, e.g. "abc"
        }
    }

    // true if the text is a number bigger than zero
    public static boolean isPositiveNumber(String text) {
        return isNumber(text) && toNumber(text) > 0;
    }

    // turns text into a number (only call this after isNumber says it is OK)
    public static double toNumber(String text) {
        return Double.parseDouble(text.trim());

    }

    public static boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }

    // digits only, 7 to 15 of them
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.length() < 7 || phone.length() > 15) {
            return false;
        }
        for (int i = 0; i < phone.length(); i++) {
            char c = phone.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }
}
