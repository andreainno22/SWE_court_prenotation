package DAOs;

import DomainModel.SuperUser;

public interface SuperUserDao extends Dao {
    SuperUser getSuperUser(String email, String password);

    boolean checkSuperUser(String email);

    void insertSuperUser(SuperUser superUser);

    void deleteSuperUser(String email);
}
