package domomoufle;

import domomoufle.arduino.ArduinoReader;
import domomoufle.database.Database;
import domomoufle.gui.Frame;
import domomoufle.gui.StartDialog;
import java.awt.EventQueue;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import jssc.SerialPortException;

public class GantGUI {

    private static final Database DATABASE = new Database("jdbc:mysql://PC-TP-MYSQL:3306/G221_A_BD1", "G221_A", "G221_A");
    private static String port;
    private static ArduinoReader reader;

    public static Database getDatabase() {
        return DATABASE;
    }

    public static String getPort() {
        return port;
    }

    public static ArduinoReader getReader() {
        return reader;
    }

    public static void diapo() {
        try {
            reader = new ArduinoReader(port);
        } catch (IOException | SerialPortException ex) {
            JOptionPane.showMessageDialog(null,
                    "Impossible d'ouvrir le port : " + port,
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Diapo diapo = new Diapo();
        reader.addListener(diapo);
        reader.startReading();
    }

    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                StartDialog dialog = new StartDialog(null, true);
                dialog.setVisible(true);
                port = dialog.getPort();

                if (dialog.getChoice() == StartDialog.START_ACQUISITION) {
                    ProjetGantAnalyse p = new ProjetGantAnalyse();
                    p.listenArduino();
                } else if (dialog.getChoice() == StartDialog.START_MANAGE) {
                    try {
                        reader = new ArduinoReader(port);
                    } catch (IOException | SerialPortException ex) {
                        JOptionPane.showMessageDialog(null,
                                "Impossible d'ouvrir le port : " + port,
                                "Erreur",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    new Frame().setVisible(true);
                } else if (dialog.getChoice() == StartDialog.START_DIAPO) {
                    diapo();
                }
            }
        });
    }

}
