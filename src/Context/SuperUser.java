package Context;

public class SuperUser extends Person{
    public SuperUser(int id, String name, String surname, String email, String password, int telephoneNumber) {
        super(id, name, surname, email, password, telephoneNumber);
    }
    private boolean isSuperUser = true;

    @Override
    public boolean getIsSuperUser() {
        return isSuperUser;
    }
}
