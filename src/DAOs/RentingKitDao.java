package DAOs;

import DomainModel.RentingKit;

public interface RentingKitDao extends Dao {
    RentingKit getRentingKit(String type);
}
