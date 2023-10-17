package Context;

public class RentingKit {

    public RentingKit(int id, String type, float unitPrice) {
        this.id = id;
        this.type = type;
        this.unitPrice = unitPrice;
    }

    public int getNumOfRents() {
        return numOfRents;
    }

    public String getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public float getUnitPrice() {
        return unitPrice;
    }

    public float getTotPrice() {
        return unitPrice * numOfRents;
    }

    public void setNumOfRents(int numOfRents) {
        this.numOfRents = numOfRents;
    }

    private final float unitPrice;
    private int numOfRents;
    private final int id;
    private final String type;

}
