package DomainModel;

import java.util.Formatter;

public class Court {
    public Court(int id) {
        this.id = id;
    }

    public Court(int id, float price) {
        this.id = id;
        this.price = price;
    }

    public float getPrice() {
        return price;
    }


    public void setType(String type) {
        this.type = type;
    }

    public void setTerrain_type(String terrain_type) {
        this.terrain_type = terrain_type;
    }

    public String getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public void printAllCourts(Formatter fmt, boolean showDiscount) {
        if (this.type.equals("padel"))
            if (showDiscount)
                fmt.format("%-15d%-15s%-15.2f%-15.2f\n", id, type, price, price * 0.9);
            else
                fmt.format("%-15d%-15s%-15.2f\n", id, type, price);
        else if (showDiscount) {
            fmt.format("%-15d%-15s%-15.2f%-15.2f\n", id, terrain_type, price, price * 0.9);
        } else
            fmt.format("%-15d%-15s%-15.2f\n", id, terrain_type, price);
    }

    private final int id;
    private String type;
    private String terrain_type;
    private float price;
}

