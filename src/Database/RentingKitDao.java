package Database;

import Context.RentingKit;

public interface RentingKitDao extends Dao {
    RentingKit getRentingKit(String type);
}
