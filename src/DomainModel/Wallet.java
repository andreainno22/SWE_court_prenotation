package DomainModel;

public class Wallet {
    private final int id;
    private float balance;

    public int getId() {
        return id;
    }


    public Wallet(int id, float balance) {
        this.balance = balance;
        this.id = id;
    }

    public void addMoney(float amount) {
        this.balance += amount;
    }

    public boolean removeMoney(float amount) {
        if (this.balance - amount < 0) {
            return false;
        }
        this.balance -= amount;
        return true;
    }

    public float getBalance() {
        return this.balance;
    }
}
