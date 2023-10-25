package Database;

import Context.SuperUser;

public interface SuperUserDao extends GeneralDaoSettings {
    SuperUser getSuperUser(String email, String password);

    boolean checkSuperUser(String email);

    void insertSuperUser(SuperUser superUser);

    void deleteSuperUser(String email);
}
