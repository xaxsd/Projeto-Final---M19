package util;

public class Validator {
    public static boolean validateEmail(String email) {
        return email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }
    
    public static boolean validateNIF(String nif) {
        return nif.matches("^[0-9]{9}$");
    }
    
    public static boolean validateRequired(String input) {
        return input != null && !input.trim().isEmpty();
    }
    
    public static boolean validateLength(String input, int min, int max) {
        return input.length() >= min && input.length() <= max;
    }
}