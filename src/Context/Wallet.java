package Context;

public class Wallet {
    private float balance;

    public Wallet() {
        this.balance = 0;
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
