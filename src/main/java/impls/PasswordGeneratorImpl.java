package impls;

import interfaces.PasswordGenerator;

public class PasswordGeneratorImpl implements PasswordGenerator {
    @Override
    public String generatePassword() {
        String pass = "";
        String upperStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int upperIndex = (int)(upperStr.length() * Math.random());
        pass += upperStr.charAt(upperIndex);

        upperIndex = (int)(upperStr.length() * Math.random());
        pass += upperStr.charAt(upperIndex);

        String lowerStr = "abcdefghijklmnopqrstuvxyz";
        int lowerIndex = (int)(lowerStr.length() * Math.random());
        pass += lowerStr.charAt(lowerIndex);

        String digitStr = "0123456789";
        int digitIndex = (int)(digitStr.length() * Math.random());
        pass += digitStr.charAt(digitIndex);

        String specialStr = ".-_+!?=";
        int spacialIndex = (int)(specialStr.length() * Math.random());
        pass += specialStr.charAt(spacialIndex);

        return pass;
    }

}
