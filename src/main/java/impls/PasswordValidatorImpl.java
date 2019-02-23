package impls;

import interfaces.PasswordValidator;

public class PasswordValidatorImpl implements PasswordValidator {
    @Override
    public boolean isValid(String pass) {
        int countUpper = getUpperCount(pass);
        int countLower = getLowerCount(pass);
        int countDigit = getDigitsCount(pass);
        boolean specialChars = areThereSpacialChars(pass);
        return         (pass.length() >= 6 && pass.length() <= 50) &&
                (countLower >= 1 && countUpper >= 1 && countDigit >= 1 && specialChars ||
                        countLower >= 1 && countUpper >= 1 && countDigit >= 1 ||
                        countLower >= 1 && countUpper >= 1 && specialChars  ||
                        countLower >= 1 && specialChars && countDigit >= 1 ||
                        specialChars && countUpper >= 1 && countDigit >= 1);
    }

    private boolean areThereSpacialChars(String pass) {
        if(     pass.indexOf('.') >= 0 || pass.indexOf('-') >= 0 ||
                pass.indexOf('_') >= 0 || pass.indexOf('+') >= 0 ||
                pass.indexOf('!') >= 0 || pass.indexOf('?') >= 0 ||
                pass.indexOf('=') >= 0     )
            return true;
        return false;
    }

    private int getUpperCount(String pass) {
        int n = 0;
        for(int i=0; i < pass.length(); i++)
            if(Character.isUpperCase(pass.charAt(i)))
                n++;
        return n;
    }

    private int getDigitsCount(String pass) {
        int n = 0;
        for(int i=0; i < pass.length(); i++)
            if(Character.isDigit(pass.charAt(i)))
                n++;
        return n;
    }

    private int getLowerCount(String pass) {
        int n = 0;
        for(int i=0; i < pass.length(); i++)
            if(Character.isLowerCase(pass.charAt(i)))
                n++;
        return n;
    }
}
