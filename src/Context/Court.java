package Context;

import java.util.Formatter;

public class Court {
    public Court(int id) {
        this.id = id;
    }

    public Court(int id, String type, String terrain_type, int price) {
        this.id = id;
        this.type = type;
        this.terrain_type = terrain_type;
        this.price = price;
    }



    public float getPrice() {
        return price;
    }

    public Court(int id, float price) {
        this.id = id;
        this.price = price;
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

    public void printAllCourt(Formatter fmt) {
        if (this.type.equals("padel"))
            fmt.format("%-15d%-15s%-15.2f\n", id, type, price);
        else
            fmt.format("%-15d%-15s%-15.2f\n", id, terrain_type, price);
        //System.out.println(id + " - " + type + " - " + price+" €");
    }

    protected int id;
    protected String type;
    protected String terrain_type;
    protected float price;
}

