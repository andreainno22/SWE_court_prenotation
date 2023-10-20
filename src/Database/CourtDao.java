package Database;

import Context.Court;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public interface CourtDao extends GeneralDaoSettings{
    List<Court> getCourt();
}
