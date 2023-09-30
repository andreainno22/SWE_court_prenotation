package Context;

public class TennisCourt extends Court{
    public TennisCourt(int id, float price, TennisCourtType type){
        super(id, price);
        this.type = type;
    }
    enum TennisCourtType {
        grass, clay, hard
    }
    private TennisCourtType type;
}
