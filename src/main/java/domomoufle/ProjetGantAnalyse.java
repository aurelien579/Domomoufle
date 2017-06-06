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
    private PreparedStatement mesuresStmt = null;
    private PreparedStatement acquisitionStmt = null;
    private PreparedStatement closeAcqStmt = null;
    private PreparedStatement acqGetStmt = null;
    private PreparedStatement modeleGetStmt = null;
    private PreparedStatement acqFlexGetStmt = null;
    private PreparedStatement getModeleStmt = null;
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
        port = "COM8";

        try {
            //Enregistrement de la classe du driver par le driverManager
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver trouvé...");

            //Création d'une connexion sur la base de donnée
            conn = DriverManager.getConnection("jdbc:mysql://PC-TP-MYSQL.insa-lyon.fr:3306/G221_A_BD1", "G221_A", "G221_A");
            //conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/p2i?zeroDateTimeBehavior=convertToNull", "root", "root");
            //this.conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/" + bd, compte, motDePasse);
            System.out.println("Connexion etablie...");

            // Prepared Statement
            mesuresStmt = conn.prepareStatement("INSERT INTO mesures (idAcquisition, flex1, flex2, vitesseX, vitesseY, vitesseZ, dateMesure)"
                    + " VALUES (?,?,?,?,?,?,?);");
            acquisitionStmt = conn.prepareStatement("INSERT INTO acquisitions (dateDebut) VALUES (?);", Statement.RETURN_GENERATED_KEYS);
            closeAcqStmt = conn.prepareStatement("UPDATE acquisitions SET dateFin=? WHERE idAcquisition=?;");
            acqGetStmt = conn.prepareStatement("SELECT vitesseX, vitesseY, vitesseZ, flex1, flex2 FROM mesures WHERE idAcquisition = ?;");
            acqFlexGetStmt = conn.prepareStatement("SELECT avg(flex1), avg(flex2) FROM mesures WHERE idAcquisition = ?;");
            getModeleStmt = conn.prepareStatement("SELECT * FROM modeles2;");
            getDescriptionGesteStmt = conn.prepareStatement("SELECT descModele FROM modeles WHERE idGeste = ?");
            insertModeleStmt = conn.prepareStatement("INSERT INTO modeles2 (x1, y1, z1, flex11, flex21, x2, y2, z2, flex12, flex22, x3, y3, z3, "
                    + "flex13, flex23, x4, y4, z4, flex14, flex24, x5, y5, z5, flex15, flex25, x6, y6, z6, flex16, flex26, x7, y7, z7, flex17, "
                    + "flex27, x8, y8, z8, flex18, flex28, x9, y9, z9, flex19, flex29, x10, y10, z10, flex110, flex210, x11, y11, z11, flex111, "
                    + "flex211, x12, y12, z12, flex112, flex212, x13, y13, z13, flex113, flex213, x14, y14, z14, flex114, flex214, x15, y15, z15, "
                    + "flex115, flex215, x16, y16, z16, flex116, flex216, x17, y17, z17, flex117, flex217, x18, y18, z18, flex118, flex218, x19, y19"
                    + ", z19, flex119, flex219, x20, y20, z20, flex120, flex220, x21, y21, z21, flex121, flex221, x22, y22, z22, flex122, flex222, "
                    + "x23, y23, z23, flex123, flex223, x24, y24, z24, flex124, flex224, x25, y25, z25, flex125, flex225, x26, y26, z26, flex126, "
                    + "flex226, x27, y27, z27, flex127, flex227, x28, y28, z28, flex128, flex228, x29, y29, z29, flex129, flex229, x30, y30, z30, "
                    + "flex130, flex230, idGeste) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
            getActionStmt = conn.prepareStatement("SELECT * FROM modeles, Action WHERE "
                    + "Action.idAction = modeles.idAction AND modeles.idGeste = ?;");
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
            Date date = new Date();
//            mesuresStmt.setInt(1, acquisitionId);
//            mesuresStmt.setInt(2, flex1);
//            mesuresStmt.setInt(3, flex2);
//            mesuresStmt.setDouble(4, vitx);
//            mesuresStmt.setDouble(5, vity);
//            mesuresStmt.setDouble(6, vitz);
//            mesuresStmt.setTimestamp(7, new Timestamp(date.getTime()));
//            mesuresStmt.executeUpdate();

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
//            acqGetStmt.setInt(1, id);
//            ResultSet acq = acqGetStmt.executeQuery();
//            ArrayList<Double[]> t = new ArrayList<Double[]>();
//
//            while (acq.next()) {
//                Double[] tab = new Double[5];
//                tab[0] = acq.getDouble("vitesseX");
//                tab[1] = acq.getDouble("vitesseY");
//                tab[2] = acq.getDouble("vitesseZ");
//                tab[3] = (double) acq.getInt("flex1");
//                tab[4] = (double) acq.getInt("flex2");
//                t.add(tab);
//            }

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

    public static String getInsertInModeleRequest(ArrayList t, int geste) {
        String s = "INSERT INTO `modeles2` (`x1`, `y1`, `z1`, `flex11`, `flex21`, `x2`, "
                + "`y2`, `z2`, `flex12`, `flex22`, `x3`, `y3`, `z3`, `flex13`, `flex23`, `x4`,"
                + "`y4`, `z4`, `flex14`, `flex24`, `x5`, `y5`, `z5`, `flex15`, `flex25`, `x6`, "
                + "`y6`, `z6`, `flex16`, `flex26`, `x7`, `y7`, `z7`, `flex17`, `flex27`, `x8`, `y8`,"
                + "`z8`, `flex18`, `flex28`, `x9`, `y9`, `z9`, `flex19`, `flex29`, `x10`, `y10`, `z10`"
                + ", `flex110`, `flex210`, `x11`, `y11`, `z11`, `flex111`, `flex211`, `x12`, `y12`, `z12"
                + "`, `flex112`, `flex212`, `x13`, `y13`, `z13`, `flex113`, `flex213`, `x14`, `y14`, `z14`"
                + ", `flex114`, `flex214`, `x15`, `y15`, `z15`, `flex115`, `flex215`, `x16`, `y16`, `z16`, "
                + "`flex116`, `flex216`, `x17`, `y17`, `z17`, `flex117`, `flex217`, `x18`, `y18`, `z18`, "
                + "`flex118`, `flex218`, `x19`, `y19`, `z19`, `flex119`, `flex219`, `x20`, `y20`, `z20`, "
                + "`flex120`, `flex220`, `x21`, `y21`, `z21`, `flex121`, `flex221`, `x22`, `y22`, `z22`, "
                + "`flex122`, `flex222`, `x23`, `y23`, `z23`, `flex123`, `flex223`, `x24`, `y24`, `z24`, "
                + "`flex124`, `flex224`, `x25`, `y25`, `z25`, `flex125`, `flex225`, `x26`, `y26`, `z26`, "
                + "`flex126`, `flex226`, `x27`, `y27`, `z27`, `flex127`, `flex227`, `x28`, `y28`, `z28`, "
                + "`flex128`, `flex228`, `x29`, `y29`, `z29`, `flex129`, `flex229`, `x30`, `y30`, `z30`, "
                + "`flex130`, `flex230`, idGeste) VALUES (";
        for (int i = 0; i < t.size() - 1; i++) {
            s += t.get(i) + ", ";
        }
        s += t.get(t.size() - 1) + ", " + geste + ");";

        return s;
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
            Runtime.getRuntime().exec("cmd /c start " + cmd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ProjetGantAnalyse p = new ProjetGantAnalyse();
        p.listenArduino();
    }
}
