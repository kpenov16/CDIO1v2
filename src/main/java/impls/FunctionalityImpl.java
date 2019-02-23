package impls;

import entities.UserDTO;
import interfaces.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FunctionalityImpl implements Functionality {
    private IExtendedUserDAO userRepo;
    private PasswordGenerator passwordGenerator;
    private PasswordValidator passwordValidator;
    private UserDTO user = new UserDTO();

    public FunctionalityImpl(PasswordValidator passwordValidator,
                             PasswordGenerator passwordGenerator,
                             IExtendedUserDAO userDAOImplS133967) {
        this.passwordValidator = passwordValidator;
        this.passwordGenerator = passwordGenerator;
        this.userRepo = userDAOImplS133967;
    }

    @Override
    public String getFormatedUsers() {
        List<UserDTO> users = new ArrayList<>();
        String msg = "";
        try {
            users = userRepo.getUserList();
            Collections.sort(users, (u1,u2) -> u1.getUserId() - u2.getUserId());
            for(UserDTO u: users){
                msg += String.format(u.toString() + "\n");
            }
        } catch (IUserDAO.DALException e) {
           msg +=  e.getMessage() + "\nStackTrace: " + e.getStackTrace();
        }
        return msg;
    }

    @Override
    public void createUser(int id, String cpr, String firstName, String ini, List<String> roles) {
        UserDTO user = new UserDTO();
        user.setPassword( passwordGenerator.generatePassword());
        user.setUserId(id);
        user.setCpr(cpr);
        user.setUserName(firstName);
        user.setIni(ini);
        user.setRoles(new ArrayList<>(roles));
        try {
            userRepo.createUser(user);
        } catch (IUserDAO.DALException e) {
            throw new UserCreationException(e.getMessage());
        }
    }

    @Override
    public List<String> getAvailableRoles() {
        return userRepo.queryAllRoles();
    }

    @Override
    public boolean isAvailable(String ini) {
        return !userRepo.isInUse(ini);
    }

    @Override
    public String startUserUpdate(int id) throws UserNotFoundException {
        user = new UserDTO();
        String userDetails = "";
        try {
            user = userRepo.getUser(id);
            userDetails = String.format("%s",user.toString());
        } catch (IUserDAO.DALException e) {
            throw  new UserNotFoundException(e.getMessage());
        }
        return userDetails;
    }

    @Override
    public void updateUserName(String firstName) {
        user.setUserName(firstName);
    }

    @Override
    public boolean isUserPasswordMatching(String currentPass) {
        return user.getPassword().equals(currentPass);
    }

    @Override
    public boolean isPasswordValid(String newPass) {
        return passwordValidator.isValid(newPass) ;
    }

    @Override
    public void updateUserPassword(String newPass) {
        user.setPassword(newPass);
    }

    @Override
    public void setUserRolles(List<String> roles) {
        user.setRoles(roles);
    }

    @Override
    public String applyUserUpdate() {
        String updatedUserDetails = "";
        try {
            userRepo.updateUser(user);
            updatedUserDetails = String.format("%s", user.toString());
        } catch (IUserDAO.DALException e) {
            throw new UserUpdateException(e.getMessage());
        }
        return updatedUserDetails;
    }

    @Override
    public void clearUserUnderUpdate() {
        int id = user.getUserId();
        user = new UserDTO();
        user.setUserId(id);
    }

    @Override
    public void deleteUser(int idToBeDeleted) {
        try {
            userRepo.deleteUser(idToBeDeleted);
        } catch (IUserDAO.DALException e) {
            throw new UserDeletionException(e.getMessage());
        }
    }

    @Override
    public String getPassword(int userId) throws UserNotFoundException {
        UserDTO user;
        try {
            user = userRepo.getUser(userId);
        } catch (IUserDAO.DALException e) {
            throw  new UserNotFoundException(e.getMessage());
        }
        return user.getPassword();
    }
}
