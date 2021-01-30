package src.util;

public class ValidationUtil {
    public static boolean checkIfEmailIsValid(String email){
        return email.matches("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
    }

    public static boolean checkLengthPassword(String password){
        return password.length() >= 6;
    }
}
