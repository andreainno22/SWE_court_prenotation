package DomainModel;

public class RentingKit {

    public RentingKit(int id, String type, float unitPrice) {
        this.id = id;
        this.type = type;
        this.unitPrice = unitPrice;
    }

    public RentingKit(int numOfRents) {
        this.numOfRents = numOfRents;
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

    private float unitPrice;
    private int numOfRents;
    private int id;
    private String type;

}
