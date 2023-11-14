package Context;

public class SuperUser extends Person{
    public SuperUser(int id, String name, String surname, String email, String password, int telephoneNumber) {
        super(id, name, surname, email, password, telephoneNumber);
    }

    public SuperUser(String name, String surname, String email, String password, int telephoneNumber) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.telephoneNumber = telephoneNumber;
    }
    private final boolean isSuperUser = true;

}
