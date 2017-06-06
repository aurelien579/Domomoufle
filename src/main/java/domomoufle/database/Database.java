package domomoufle.database;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {

    private Connection connection;

    public Database(String url, String user, String pass) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, pass);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int executeUpdate(String sql, Object... args) throws MySQLIntegrityConstraintViolationException {
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                stmt.setObject(i + 1, args[i]);
            }

            return stmt.executeUpdate();
        } catch (SQLException ex) {
            if (ex instanceof MySQLIntegrityConstraintViolationException) {
                throw (MySQLIntegrityConstraintViolationException) ex;
            } else {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return 0;
    }

    public ResultSet executeQuery(String sql, Object... args) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(sql);
        for (int i = 0; i < args.length; i++) {
            stmt.setObject(i + 1, args[i]);
        }

        return stmt.executeQuery();
    }

    public ArrayList<Action> getAllActions() {
        ArrayList<Action> actions = new ArrayList<>();

        try {
            ResultSet r = executeQuery("SELECT * FROM Action;");
            while (r.next()) {
                actions.add(Action.map(r));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }

        return actions;
    }

    public void addAction(Action a) {
        try {
            executeUpdate("INSERT INTO Action (descAction, adresseScript) VALUES (?, ?)", a.description, a.command);
        } catch (MySQLIntegrityConstraintViolationException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteAction(Action a) {
        try {
            executeUpdate("DELETE FROM Action WHERE descAction = ? AND adresseScript = ?", a.description, a.command);
        } catch (MySQLIntegrityConstraintViolationException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<Modele> getAllModeles() {
        ArrayList<Modele> modeles = new ArrayList<>();

        try {
            ResultSet r = executeQuery("SELECT nomGeste, descAction FROM gestes, Action WHERE Action.idAction = gestes.idAction;");

            while (r.next()) {
                modeles.add(Modele.map(r));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }

        return modeles;
    }

    public void updateModeleAction(String modeleDesc, String actionDesc) {
        try {
            executeUpdate("UPDATE gestes SET idAction = (SELECT idAction FROM Action WHERE descAction = ?) WHERE nomGeste = ?;", actionDesc, modeleDesc);
        } catch (MySQLIntegrityConstraintViolationException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getModeleAction(String modeleDesc) {
        try {
            ResultSet r = executeQuery("SELECT actionDesc FROM modeles, Action WHERE Action.actionId = modeles.actionId AND descModele = ?", modeleDesc);
            return r.getString("actionDesc");
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public int insertGeste(String gesteDescription) throws MySQLIntegrityConstraintViolationException {
        try {
            executeUpdate("INSERT INTO gestes (nomGeste) VALUES (?);", gesteDescription);
            ResultSet r = executeQuery("SELECT idGeste FROM gestes WHERE nomGeste = ?;", gesteDescription);
            r.next();
            return r.getInt("idGeste");
        } catch (SQLException ex) {
            if (ex instanceof MySQLIntegrityConstraintViolationException) {
                throw (MySQLIntegrityConstraintViolationException) ex;
            } else {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
            
        return -1;
    }
    
    public void deleteGeste(int gesteId) {
        try {
            executeUpdate("DELETE FROM modeles2 WHERE idGeste = ?;", gesteId);
            executeUpdate("DELETE FROM gestes WHERE idGeste = ?;", gesteId);
        } catch (MySQLIntegrityConstraintViolationException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void deleteGeste(String geste) {
        try {
            executeUpdate("DELETE FROM modeles2 WHERE idGeste = (SELECT idGeste FROM gestes WHERE nomGeste = ?);", geste);
            executeUpdate("DELETE FROM gestes WHERE nomGeste = ?;", geste);
        } catch (MySQLIntegrityConstraintViolationException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
