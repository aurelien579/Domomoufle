package domomoufle.gui;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import domomoufle.Formatage;
import domomoufle.GantGUI;
import domomoufle.ProjetGantAnalyse;
import domomoufle.arduino.ArduinoReader;
import domomoufle.arduino.ArduinoReaderListener;
import domomoufle.arduino.Mesure;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import jssc.SerialPortException;
import jssc.SerialPortList;

public class NewGesteDialog extends javax.swing.JDialog implements ArduinoReaderListener, WindowListener {

    private final AnimatedIcon[] loadingAnimatedIcons;
    private final ImageIcon validIcon;
    private int currentAcquisition;
    private final ArrayList<Mesure> currentAcquisitonValues;
    private final ProjetGantAnalyse projetGant;
    private int gesteId;
    private boolean acquisitionsFinished = false;

    public NewGesteDialog(JFrame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
        addWindowListener(this);

        projetGant = new ProjetGantAnalyse();

        currentAcquisitonValues = new ArrayList();

        validIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/Ok.png")));

        labelIcons = new JLabel[]{labelIcon1, labelIcon2, labelIcon3, labelIcon4, labelIcon5};

        ImageIcon loadingIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/Loading.png")));
        loadingAnimatedIcons = new AnimatedIcon[5];

        for (int i = 0; i < loadingAnimatedIcons.length; i++) {
            loadingAnimatedIcons[i] = new AnimatedIcon(labelIcons[i], 70, 300000);

            loadingAnimatedIcons[i].addIcon(loadingIcon);
            for (int angle = 15; angle < 360; angle += 15) {
                loadingAnimatedIcons[i].addIcon(new RotatedIcon(loadingIcon, angle));
            }
        }

        validButtons = new JButton[]{validButton1, validButton2, validButton3, validButton4, validButton5};
        restartButtons = new JButton[]{restartButton1, restartButton2, restartButton3, restartButton4, restartButton5};
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        startButton = new javax.swing.JButton();
        nameLabel = new javax.swing.JLabel();
        nameTextField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        restartButton1 = new javax.swing.JButton();
        validButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        restartButton2 = new javax.swing.JButton();
        validButton2 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        restartButton3 = new javax.swing.JButton();
        validButton3 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        restartButton4 = new javax.swing.JButton();
        validButton4 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        restartButton5 = new javax.swing.JButton();
        validButton5 = new javax.swing.JButton();
        labelIcon2 = new javax.swing.JLabel();
        labelIcon1 = new javax.swing.JLabel();
        labelIcon3 = new javax.swing.JLabel();
        labelIcon4 = new javax.swing.JLabel();
        labelIcon5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Nouveau geste");
        setResizable(false);

        startButton.setText("Commencer acquisitions");
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });

        nameLabel.setText("Nom du geste : ");

        jLabel1.setText("Acquisition 1 :");

        restartButton1.setText("Recommencer");
        restartButton1.setEnabled(false);
        restartButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                restartButton1ActionPerformed(evt);
            }
        });

        validButton1.setText("Valider");
        validButton1.setEnabled(false);
        validButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                validButton1ActionPerformed(evt);
            }
        });

        jLabel2.setText("Acquisition 2 :");

        restartButton2.setText("Recommencer");
        restartButton2.setEnabled(false);
        restartButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                restartButton2ActionPerformed(evt);
            }
        });

        validButton2.setText("Valider");
        validButton2.setEnabled(false);
        validButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                validButton2ActionPerformed(evt);
            }
        });

        jLabel3.setText("Acquisition 3 :");

        restartButton3.setText("Recommencer");
        restartButton3.setEnabled(false);
        restartButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                restartButton3ActionPerformed(evt);
            }
        });

        validButton3.setText("Valider");
        validButton3.setEnabled(false);
        validButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                validButton3ActionPerformed(evt);
            }
        });

        jLabel4.setText("Acquisition 4 :");

        restartButton4.setText("Recommencer");
        restartButton4.setEnabled(false);
        restartButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                restartButton4ActionPerformed(evt);
            }
        });

        validButton4.setText("Valider");
        validButton4.setEnabled(false);
        validButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                validButton4ActionPerformed(evt);
            }
        });

        jLabel5.setText("Acquisition 5 :");

        restartButton5.setText("Recommencer");
        restartButton5.setEnabled(false);
        restartButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                restartButton5ActionPerformed(evt);
            }
        });

        validButton5.setText("Valider");
        validButton5.setEnabled(false);
        validButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                validButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(labelIcon5, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel5))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(labelIcon4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel4))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(labelIcon3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel3))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(labelIcon1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel1))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(labelIcon2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel2)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(restartButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(validButton1))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(restartButton2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(validButton2))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(restartButton3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(validButton3))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(restartButton4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(validButton4))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(restartButton5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(validButton5))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(nameLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(startButton)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(startButton)
                            .addComponent(nameLabel)
                            .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel1)
                                .addComponent(restartButton1)
                                .addComponent(validButton1))
                            .addComponent(labelIcon1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(restartButton2)
                                        .addComponent(validButton2))
                                    .addComponent(labelIcon2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel3)
                                    .addComponent(restartButton3)
                                    .addComponent(validButton3)))
                            .addComponent(labelIcon3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(restartButton4)
                            .addComponent(validButton4)))
                    .addComponent(labelIcon4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(restartButton5)
                        .addComponent(validButton5))
                    .addComponent(labelIcon5, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void validButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_validButton1ActionPerformed
        if (sendAcquisition()) {
            doAcquisition(1);
        }
    }//GEN-LAST:event_validButton1ActionPerformed

    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtonActionPerformed
        String name = nameTextField.getText();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Le nom ne peut être vide.", "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String port = GantGUI.getPort();
        
        try {
            gesteId = GantGUI.getDatabase().insertGeste(name);
        } catch (MySQLIntegrityConstraintViolationException ex) {
            JOptionPane.showMessageDialog(this,
                    "Un geste porte déjà ce nom.", "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        GantGUI.getReader().addListener(this);

        doAcquisition(0);
    }//GEN-LAST:event_startButtonActionPerformed

    private void restartButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_restartButton1ActionPerformed
        doAcquisition(0);
    }//GEN-LAST:event_restartButton1ActionPerformed

    private void restartButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_restartButton2ActionPerformed
        doAcquisition(1);
    }//GEN-LAST:event_restartButton2ActionPerformed

    private void validButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_validButton2ActionPerformed
        if (sendAcquisition()) {
            doAcquisition(2);
        }
    }//GEN-LAST:event_validButton2ActionPerformed

    private void restartButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_restartButton3ActionPerformed
        doAcquisition(2);
    }//GEN-LAST:event_restartButton3ActionPerformed

    private void validButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_validButton3ActionPerformed
        if (sendAcquisition()) {
            doAcquisition(3);
        }
    }//GEN-LAST:event_validButton3ActionPerformed

    private void restartButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_restartButton4ActionPerformed
        doAcquisition(3);
    }//GEN-LAST:event_restartButton4ActionPerformed

    private void validButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_validButton4ActionPerformed
        if (sendAcquisition()) {
            doAcquisition(4);
        }
    }//GEN-LAST:event_validButton4ActionPerformed

    private void restartButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_restartButton5ActionPerformed
        doAcquisition(4);
    }//GEN-LAST:event_restartButton5ActionPerformed

    private void validButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_validButton5ActionPerformed
        if (sendAcquisition()) {
            acquisitionsFinished = true;
            dispose();
        }
    }//GEN-LAST:event_validButton5ActionPerformed

    private void doAcquisition(int num) {
        currentAcquisition = num;
        labelIcons[num].setIcon(loadingAnimatedIcons[num]);
        loadingAnimatedIcons[num].restart();
        for (int i = 0; i < validButtons.length; i++) {
            validButtons[i].setEnabled(false);
            restartButtons[i].setEnabled(false);
        }

        GantGUI.getReader().startReading();
    }

    private void endAcquisition() {
        labelIcons[currentAcquisition].setIcon(validIcon);
        validButtons[currentAcquisition].setEnabled(true);
        restartButtons[currentAcquisition].setEnabled(true);

        GantGUI.getReader().stopReading();
    }

    private boolean sendAcquisition() {
        ArrayList<Double[]> mesuresDoubles = new ArrayList();
        int flex1Sum = 0;
        int flex2Sum = 0;
        int flex1, flex2;
        
        for (Mesure m : currentAcquisitonValues) {
            flex1Sum += m.flex1;
            flex2Sum += m.flex2;
        }
        
        if (flex1Sum <= 3) {
            flex1 = 0;
        } else {
            flex1 = 1;
        }
        
        if (flex2Sum <= 3) {
            flex2 = 0;
        } else {
            flex2 = 1;
        }
        
        for (Mesure m : this.currentAcquisitonValues) {
            mesuresDoubles.add(new Double[]{(double) m.x,
                (double) m.y,
                (double) m.z,
                (double) flex1,
                (double) flex2}
            );
        }

        try {
            projetGant.insertModele(Formatage.createTuple(mesuresDoubles), gesteId);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);

            doAcquisition(currentAcquisition);
            return false;
        }

        return true;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel labelIcon1;
    private javax.swing.JLabel labelIcon2;
    private javax.swing.JLabel labelIcon3;
    private javax.swing.JLabel labelIcon4;
    private javax.swing.JLabel labelIcon5;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JButton restartButton1;
    private javax.swing.JButton restartButton2;
    private javax.swing.JButton restartButton3;
    private javax.swing.JButton restartButton4;
    private javax.swing.JButton restartButton5;
    private javax.swing.JButton startButton;
    private javax.swing.JButton validButton1;
    private javax.swing.JButton validButton2;
    private javax.swing.JButton validButton3;
    private javax.swing.JButton validButton4;
    private javax.swing.JButton validButton5;
    // End of variables declaration//GEN-END:variables

    private final JLabel[] labelIcons;
    private final JButton[] validButtons;
    private final JButton[] restartButtons;

    @Override
    public void onMesureReceived(Mesure a) {
        currentAcquisitonValues.add(a);
        System.out.println("mesure");
    }

    @Override
    public void onNewAcquisition() {
        currentAcquisitonValues.clear();
        System.out.println("onNewAcquisition");

    }

    @Override
    public void onEndAcquisition() {
        endAcquisition();
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        if (!acquisitionsFinished) {
            if (gesteId > 0) {
                GantGUI.getDatabase().deleteGeste(gesteId);
            }
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }
}
