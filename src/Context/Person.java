package Context;

public abstract class Person {
    protected int id;
    protected String name;
    protected String surname;
    protected String email;
    protected String password;
    protected int telephoneNumber;

    public Person(int id, String name, String surname, String email, String password, int telephoneNumber) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.telephoneNumber = telephoneNumber;
    }

    public Person(int id, String name, String surname, String email, int telephoneNumber) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.telephoneNumber = telephoneNumber;
    }

    public Person() {
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public void setEmail(String email) { this.email = email; }

    public void setTelephoneNumber(int telephoneNumber) { this.telephoneNumber = telephoneNumber; }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getTelephoneNumber() {
        return telephoneNumber;
    }

}
