package domomoufle;

import fr.insalyon.p2i2.javaarduino.usb.ArduinoUsbChannel;
import fr.insalyon.p2i2.javaarduino.util.Console;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import jssc.SerialPortException;

public class ProjetGant {

    Connection conn = null;
    PreparedStatement mesuresStmt = null;
    PreparedStatement acquisitionStmt = null;
    PreparedStatement closeAcqStmt = null;
    PreparedStatement acqGetStmt = null;
    PreparedStatement modeleGetStmt = null;
    int acquisitionId = 0;
    boolean acquisitionEnCours = false;

    public ProjetGant() {
        final Console console = new Console();
        console.log("DEBUT du programme TestArduino !..");
        String port = "COM6";

        try {
            //Enregistrement de la classe du driver par le driverManager
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver trouvé...");

            //Création d'une connexion sur la base de donnée
            conn = DriverManager.getConnection("jdbc:mysql://PC-TP-MYSQL.insa-lyon.fr:3306/G221_A_BD1", "G221_A", "G221_A");
            System.out.println("Connexion etablie...");

            // Prepared Statement
            mesuresStmt = conn.prepareStatement("INSERT INTO mesures (idAcquisition, flex1, flex2, vitesseX, vitesseY, vitesseZ, dateMesure)"
                    + " VALUES (?,?,?,?,?,?,?);");
            acquisitionStmt = conn.prepareStatement("INSERT INTO acquisitions (dateDebut) VALUES (?);", Statement.RETURN_GENERATED_KEYS);
            closeAcqStmt = conn.prepareStatement("UPDATE acquisitions SET dateFin=? WHERE idAcquisition=?;");
            acqGetStmt = conn.prepareStatement("SELECT avg(flex1), avg(flex2), avg(vitesseX), avg(vitesseZ), avg(vitesseZ)\n"
                    + "FROM mesures WHERE idAcquisition = ?;");
            modeleGetStmt = conn.prepareStatement("SELECT * FROM modeles WHERE flex1 = ? AND flex2 = ? "
                    + "AND ? < vitesseXMax AND vitesseYMin < ? AND ? < vitesseYMax AND vitesseZMin < ? AND ? < vitesseZMax");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace(System.err);
            System.exit(-1);
        } catch (SQLException ex) {
            ex.printStackTrace(System.err);
            System.exit(-1);
        }

        acquisitionId = 1 + getMaxAcquisitionId();

        console.println("Connection au Port " + port);
        try {
            final ArduinoUsbChannel vcpChannel = new ArduinoUsbChannel(port);

            Thread readingThread = new Thread(new Runnable() {
                public void run() {

                    BufferedReader vcpInput = new BufferedReader(new InputStreamReader(vcpChannel.getReader()));

                    String line;
                    try {

                        while ((line = vcpInput.readLine()) != null) {
                            console.println("Data from Arduino: " + line);
                            if ("NEW".equals(line)) {
                                /* if (acquisitionEnCours)
                                    traitementAcquisition(acquisitionId);
                                else
                                    acquisitionEnCours = true;*/
                                acquisitionId = addAcquisition();
                            } else if ("END".equals(line)) {
                                closeAcquisition();
                            } else if (line.split(",").length == 5) {
                                insertData(line, acquisitionId);
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace(System.err);
                    }
                }
            });

            readingThread.start();

            vcpChannel.open();

            boolean exit = false;

            while (!exit) {
                String line = console.readLine("Envoyer une ligne (ou 'fin') > ");

                if (line.length() == 0) {
                    continue;
                }

                if ("fin".equals(line)) {
                    exit = true;
                }
            }

            vcpChannel.close();
            closeAcquisition();

            readingThread.interrupt();
            try {
                readingThread.join(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace(System.err);
            }

        } catch (IOException ex) {            
            ex.printStackTrace(System.err);
        } catch (SerialPortException ex) {
            ex.printStackTrace(System.err);
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
            mesuresStmt.setInt(1, acquisitionId);
            mesuresStmt.setInt(2, flex1);
            mesuresStmt.setInt(3, flex2);
            mesuresStmt.setDouble(4, vitx);
            mesuresStmt.setDouble(5, vity);
            mesuresStmt.setDouble(6, vitz);
            mesuresStmt.setTimestamp(7, new Timestamp(date.getTime()));
            mesuresStmt.executeUpdate();
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.exit(0);
        } catch (SQLException e) {
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
            acqGetStmt.setInt(1, id);
            ResultSet acq = acqGetStmt.executeQuery();
            acq.next();

            modeleGetStmt.setInt(1, acq.getInt("avg(flex1)"));
            modeleGetStmt.setInt(2, acq.getInt("avg(flex2)"));
            modeleGetStmt.setDouble(3, acq.getDouble("avg(vitesseX)"));
            modeleGetStmt.setDouble(4, acq.getDouble("avg(vitesseX)"));
            modeleGetStmt.setDouble(5, acq.getDouble("avg(vitesseY)"));
            modeleGetStmt.setDouble(6, acq.getDouble("avg(vitesseY)"));
            modeleGetStmt.setDouble(7, acq.getDouble("avg(vitesseZ)"));
            modeleGetStmt.setDouble(8, acq.getDouble("avg(vitesseZ)"));
            ResultSet r = modeleGetStmt.executeQuery();

            if (r.next()) {
                System.out.println("Mouvement " + r.getInt("idGeste") + " identifie !");
                playVideo(r.getString("adresseVideo"));
            } else {
                System.out.println("MOUVEMENT NON IDENTIFIE !");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void playVideo(String url) {
        try {
            Desktop.getDesktop().open(new File("H:\\JavaArduino\\target\\" + url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
