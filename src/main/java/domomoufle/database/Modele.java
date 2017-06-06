package domomoufle.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Modele {
    
    public final String description;
    public final String action;
    
    public Modele(String description, String action) {
        this.description = description;
        this.action = action;
    }
    
    public static Modele map(ResultSet r) throws SQLException {
        return new Modele(r.getString("nomGeste"), r.getString("descAction"));
    }
}
