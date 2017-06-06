package domomoufle;

import domomoufle.database.Database;
import domomoufle.gui.Frame;
import domomoufle.gui.StartDialog;
import java.awt.EventQueue;
import javax.swing.UIManager;

public class GantGUI {

    private static final Database DATABASE = new Database("jdbc:mysql://PC-TP-MYSQL:3306/G221_A_BD1", "G221_A", "G221_A");

    public static Database getDatabase() {
        return DATABASE;
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
                if (dialog.getChoice() ==  StartDialog.START_ACQUISITION) {
                    ProjetGantAnalyse p = new ProjetGantAnalyse();
                    p.listenArduino();
                } else if (dialog.getChoice() == StartDialog.START_MANAGE) {
                    new Frame().setVisible(true);                    
                }
            }
        });
    }

}
