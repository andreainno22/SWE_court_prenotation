package Context;

public class Wallet {
    static int globalId = 0;
    private int id;
    private float balance;

    public int getId() {
        return id;
    }

    public Wallet() {
        this.balance = 0;
        this.id = globalId++;
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
