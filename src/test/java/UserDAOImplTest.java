import entities.UserDTO;
import impls.UserDAOImpl;
import interfaces.IUserDAO;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class UserDAOImplTest {
    UserDAOImpl userDAO = null;

    @Before
    public void beforeEach(){
        userDAO = new UserDAOImpl();
    }

    @Test
    public void gevenSetRolesForTheRoleTable_returnRolesWereSetInTheDatabase(){
        List<String> roles = new ArrayList<String>(){{add("Admin");add("Pharmacist");
                                                      add("Foreman");add("Operator");}};
        userDAO.addRolesToRoleTable(roles);
        List<String> rolesRetrived = userDAO.queryAllRoles();
        for(String role : roles){
            assertEquals(true, rolesRetrived.contains(role));
        }
    }
    @Test
    public void deleteUser() throws IUserDAO.DALException {
        userDAO.deleteUser(3);
    }

    @Test
    public void givenCreateNewUser_returnNewUserWasCreated() throws IUserDAO.DALException {
        UserDTO user = new UserDTO();
        user.setUserName("Bob");
        user.setIni("bap");
        user.setUserId(Integer.MAX_VALUE-1);
        user.addRole("Admin");
        user.addRole("Pharmacist");
        user.setCpr("1234567890");
        user.setPassword("ABab1!");
        userDAO.createUser(user);

        UserDTO returnedUser = userDAO.getUser(user.getUserId());

        assertEquals(user.toString(), returnedUser.toString());
        assertEquals(user.getCpr(), returnedUser.getCpr());
        assertEquals(user.getPassword(), returnedUser.getPassword());

        userDAO.deleteUser(user.getUserId());
    }

        /*
        Adgangskoden skal indeholde mindst 6 og højst 50 tegn
        af mindst tre af de følgende fire kategorier:
        - små bogstaver (’a’ til ’z’),
        - store bogstaver (’A’ til ’Z’),
        - cifre (’0’ til ’9’)
        - specialtegn (som defineret herunder).
        {'.', '-', '_', '+', '!', '?', '='}
        Undgå at bruge dit fornavn, efternavn eller bruger-ID som en del af din adgangskode,
        da dette vil medføre problemer med at logge ind på nogle systemer og tjenester på DTU, især Windows-tjenester.
        Anvend blot følgende special-tegn: {'.', '-', '_', '+', '!', '?', '='}
    */
        private String createPassword() {
            String pass = "";
            String upperStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            int upperIndex = (int)(upperStr.length() * Math.random());
            pass += upperStr.charAt(upperIndex);
            upperIndex = (int)(upperStr.length() * Math.random());
            pass += upperStr.charAt(upperIndex);

            String lowerStr = "abcdefghijklmnopqrstuvxyz";
            int lowerIndex = (int)(lowerStr.length() * Math.random());
            pass += lowerStr.charAt(lowerIndex);
            lowerIndex = (int)(lowerStr.length() * Math.random());
            pass += lowerStr.charAt(lowerIndex);

            String digitStr = "0123456789";
            int digitIndex = (int)(digitStr.length() * Math.random());
            pass += digitStr.charAt(digitIndex);

            String specialStr = ".-_+!?=";
            int spacialIndex = (int)(specialStr.length() * Math.random());
            pass += specialStr.charAt(spacialIndex);

            return pass;
        }

    @Test
    public void givenCreateNewPassword_returnStringBetween6and50Chars() throws IUserDAO.DALException {
       assertEquals(true, pass.length() >= 6 && pass.length() <= 50 );
    }
    String pass = createPassword();
    @Test
    public void givenCreateNewPassword_returnStringWithMinOneSmallOneBigAndOneNumberChar() throws IUserDAO.DALException {
        System.out.println(pass);
        int countUpper = getUpperCount(pass);
        int countLower = getLowerCount(pass);
        int countDigit = getDigitsCount(pass);
        boolean specialChars = areThereSpacialChars(pass);
        assertEquals(true,
                countLower >= 1 && countUpper >= 1 && countDigit >= 1 && specialChars ||
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
