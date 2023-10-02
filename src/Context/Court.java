package Context;

public abstract class Court {
    public Court(int id, float price) {
        this.id = id;
        this.price = price;
    }

    public float getPrice() {
        return price;
    }

    protected int id;
    protected float price;
}

