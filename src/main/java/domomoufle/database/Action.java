package domomoufle.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Action {
    
    public final String description;
    public final String command;
    
    public Action(String d, String c) {
        description = d;
        command = c;
    }
    
    public static Action map(ResultSet r) throws SQLException {
        return new Action(r.getString("descAction"),
                r.getString("adresseScript"));
    }
    
}
