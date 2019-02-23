package interfaces;

import java.util.List;

public interface IExtendedUserDAO extends IUserDAO {
    boolean isInUse(String ini);
    List<String> queryAllRoles();
}
