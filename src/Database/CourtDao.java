package Database;

import Context.Court;

import java.util.List;

public interface CourtDao extends GeneralDaoSettings{
    List<Court> getCourts();
}
