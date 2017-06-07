package domomoufle;

import fr.insalyon.p2i2.javaarduino.usb.ArduinoUsbChannel;
import fr.insalyon.p2i2.javaarduino.util.Console;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.SerialPortException;

public class ProjetGantAnalyse {

    private Connection conn = null;
    private PreparedStatement acquisitionStmt = null;
    private PreparedStatement closeAcqStmt = null;
    private PreparedStatement getDescriptionGesteStmt = null;
    private PreparedStatement getActionStmt = null;
    private PreparedStatement insertModeleStmt = null;
    private int acquisitionId = 0;
    private String port;
    
    private ArrayList<Double[]> valeurs = new ArrayList();

    public boolean addingModeles = false;

    public ProjetGantAnalyse() {
        final Console console = new Console();
        console.log("DEBUT du programme TestArduino !..");
        port = GantGUI.getPort();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver trouvé...");

            conn = DriverManager.getConnection("jdbc:mysql://PC-TP-MYSQL.insa-lyon.fr:3306/G221_A_BD1", "G221_A", "G221_A");
            System.out.println("Connexion etablie...");

            acquisitionStmt = conn.prepareStatement("INSERT INTO acquisitions (dateDebut) VALUES (?);", Statement.RETURN_GENERATED_KEYS);
            closeAcqStmt = conn.prepareStatement("UPDATE acquisitions SET dateFin=? WHERE idAcquisition=?;");
            getDescriptionGesteStmt = conn.prepareStatement("SELECT descModele FROM modeles WHERE idGeste = ?");
            
            insertModeleStmt = conn.prepareStatement(getInsertPreparedSQL());
            getActionStmt = conn.prepareStatement("SELECT * FROM gestes, Action WHERE "
                    + "Action.idAction = gestes.idAction AND gestes.idGeste = ?;");
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
            System.exit(-1);
        }

        acquisitionId = 1 + getMaxAcquisitionId();

    }

    public void listenArduino() {
        System.out.println("Connection au Port " + port);
        try {

            final ArduinoUsbChannel vcpChannel = new ArduinoUsbChannel(port);
            vcpChannel.open();
            BufferedReader vcpInput = new BufferedReader(new InputStreamReader(vcpChannel.getReader()));

            String line;
            try {

                while ((line = vcpInput.readLine()) != null) {
                    System.out.println("Data from Arduino: " + line);
                    if ("NEW".equals(line)) {
                        closeAcquisition();
                        acquisitionId = addAcquisition();
                    } else if ("END".equals(line)) {
                        traitementAcquisition(acquisitionId);
                    } else if (line.split(",").length == 5) {
                        insertData(line, acquisitionId);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }


            vcpChannel.close();
            closeAcquisition();
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        } catch (SerialPortException ex) {
            Logger.getLogger(ProjetGantAnalyse.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void insertData(String line, int acquisitionId) {
        try {
            String[] tab = line.split(",");
            double vitx = Double.parseDouble(tab[0]);
            double vity = Double.parseDouble(tab[1]);
            double vitz = Double.parseDouble(tab[2]);
            int flex1 = Integer.parseInt(tab[3]);
            int flex2 = Integer.parseInt(tab[4]);

            Double[] t = new Double[5];
            t[0] = (vitx);
            t[1] = (vity);
            t[2] = (vitz);
            t[3] = (double)(flex1);
            t[4] = (double)(flex2);
            valeurs.add(t);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public int getMaxAcquisitionId() {
        try {
            Statement stmt = conn.createStatement();
            ResultSet r = stmt.executeQuery("SELECT max(idAcquisition) as 'ID' FROM mesures");
            if (r.next()) {
                return r.getInt("ID");
            } else {
                return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    public int addAcquisition() {
        try {
            Date dateDeb = new Date();
            acquisitionStmt.setTimestamp(1, new Timestamp(dateDeb.getTime()));
            acquisitionStmt.executeUpdate();
            ResultSet res = acquisitionStmt.getGeneratedKeys();
            res.next();
            return res.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
            return 0;
        }
    }

    public void closeAcquisition() {
        try {
            Date dateFin = new Date();
            closeAcqStmt.setInt(2, acquisitionId);
            closeAcqStmt.setTimestamp(1, new Timestamp(dateFin.getTime()));
            closeAcqStmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void traitementAcquisition(int id) {
        try {
            ArrayList tuple = Formatage.createTuple(valeurs);
            valeurs.clear();

            if (addingModeles) {
                insertModele(tuple, 8);
            } else {
                actionnement(analyseTuple(tuple));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertModele(ArrayList t, int geste) {
        try {
            for (int i = 1; i < t.size() + 1; i++) {
                insertModeleStmt.setDouble(i, (double) t.get(i - 1));
                i++;
                insertModeleStmt.setDouble(i, (double) t.get(i - 1));
                i++;
                insertModeleStmt.setDouble(i, (double) t.get(i - 1));
                i++;
                insertModeleStmt.setInt(i, (int) t.get(i - 1));
                i++;
                insertModeleStmt.setInt(i, (int) t.get(i - 1));
            }
            insertModeleStmt.setInt(151, geste);
            insertModeleStmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ProjetGantAnalyse.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String getInsertPreparedSQL() {
        StringBuilder b = new StringBuilder();
        
        b.append("INSERT INTO modeles2 (flex1, flex2");
        
        for (int i = 1; i <= 30; i++) {
            b.append(String.format(", x%d, y%d, z%d", i, i, i));
        }
        
        b.append(") VALUES (?, ?");
        
        for (int i = 0; i < 3*30; i++) {
            b.append(", ?");
        }
        
        b.append(");");

        return b.toString();
    }

    public int analyseTuple(ArrayList tuple) {
        writeTuple(tuple);
        System.out.println("Analyse du mouvement en cours ...");
        try {
            final Process process = Runtime.getRuntime().exec("cmd /c runKnime.bat");
            process.waitFor();

            System.out.println("Analyse terminee !");
            BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream("result.csv")));
            int idGeste = Integer.parseInt(r.readLine());
            r.close();
            return idGeste;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static void writeTuple(ArrayList tuple) {
        try {
            PrintWriter w = new PrintWriter(new OutputStreamWriter(new FileOutputStream("output.csv")));
            String s = "";
            w.write(getHeaderTuple());

            for (int i = 0; i < tuple.size(); i++) {
                s += tuple.get(i) + ";";
            }
            s += "0";
            w.write(s);
            w.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getDescriptionGeste(int idGeste) {
        try {
            getDescriptionGesteStmt.setInt(1, idGeste);
            ResultSet r = getDescriptionGesteStmt.executeQuery();
            r.next();
            return r.getString("descModele");
        } catch (SQLException ex) {
            Logger.getLogger(ProjetGantAnalyse.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static String getHeaderTuple() {
        String s = "";
        for (int i = 1; i < 31; i++) {
            s += "x" + i + ";";
            s += "y" + i + ";";
            s += "z" + i + ";";
            s += "flex1" + i + ";";
            s += "flex2" + i + ";";
        }
        s += "idGeste\n";
        return s;
    }

    public void actionnement(int idGeste) {
        try {
            System.out.println("Geste effectue : " + idGeste);
            getActionStmt.setInt(1, idGeste);
            ResultSet r = getActionStmt.executeQuery();
            r.next();
            playScript(r.getString("adresseScript"));
        } catch (SQLException ex) {
            Logger.getLogger(ProjetGantAnalyse.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void playScript(String cmd) {
        try {
            System.out.println("Lancement du script : " + cmd);
            Runtime.getRuntime().exec(cmd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
