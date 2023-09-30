package Database;

import java.util.Formatter;

public class Court_type_price {
    private final int id;
    private final float price;
    private final String type;

    public Court_type_price(int id, String type, float price) {
        this.type = type;
        this.id = id;
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public float getPrice() {
        return price;
    }

    public void printAllCourt(Formatter fmt) {
        fmt.format("%-15d%-15s%-15.2f\n", id, type, price);
        //System.out.println(id + " - " + type + " - " + price+" €");
    }
}
