package Context;

public class Wallet {
    private int id;
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

    public void removeMoney(float amount) {
        this.balance -= amount;
    }

    public float getBalance() {
        return this.balance;
    }
}
