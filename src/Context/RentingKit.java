package Context;

public class RentingKit {

    public RentingKit(int numberOfRents){
        this.num = numberOfRents;
    }

    public float getPrice(){
        return unitPrice*num;
    }

    public void modifyNumOfRents(int numberOfRents){
        this.num = numberOfRents;
    }
    private float unitPrice = 10;
    private int num;


}
