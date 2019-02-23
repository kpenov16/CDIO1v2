package interfaces;

import java.util.List;

public interface Functionality {
    String getFormatedUsers();
    void createUser(int id, String cpr, String firstName, String ini, List<String> roles) throws UserCreationException;
    List<String> getAvailableRoles();
    boolean isAvailable(String ini);

    String startUserUpdate(int id) throws UserNotFoundException;

    void updateUserName(String firstName);

    boolean isUserPasswordMatching(String currentPass);

    boolean isPasswordValid(String newPass);

    void updateUserPassword(String newPass);

    void setUserRolles(List<String> roles);

    String applyUserUpdate();

    void clearUserUnderUpdate();

    void deleteUser(int idToBeDeleted);

    String getPassword(int userId) throws UserNotFoundException;

    class  UserDeletionException extends RuntimeException{
        public UserDeletionException(String msg){
            super(msg);
        }
    }

    class  UserUpdateException extends RuntimeException{
        public UserUpdateException(String msg){
            super(msg);
        }
    }
    class  UserNotFoundException extends RuntimeException{
        public UserNotFoundException(String msg){
            super(msg);
        }
    }
    class  UserCreationException extends RuntimeException{
        public UserCreationException(String msg){
            super(msg);
        }
    }

}
