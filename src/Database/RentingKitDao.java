package Database;

import Context.RentingKit;

public interface RentingKitDao extends GeneralDaoSettings{
    RentingKit getRentingKit(String type);
}
