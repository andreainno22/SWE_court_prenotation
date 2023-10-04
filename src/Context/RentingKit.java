package Context;

public class RentingKit {

    public RentingKit(int numberOfRents){
        this.numOfRents = numberOfRents;
    }

    public RentingKit(int id, String type, float unitPrice){
        this.id = id;
        this.type = type;
        this.unitPrice = unitPrice;
    }

    public float getUnitPrice() {
        return unitPrice;
    }

    public void setNumOfRents(int numOfRents) {
        this.numOfRents = numOfRents;
    }

    public float getTotalPrice(){
        return unitPrice* numOfRents;
    }
    public void modifyNumOfRents(int numberOfRents){
        this.numOfRents = numberOfRents;
    }
    private float unitPrice = 10;
    private int numOfRents;
    private int id;
    private String type;

}