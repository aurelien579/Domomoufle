package domomoufle.gui;

import jssc.SerialPortList;

public class StartDialog extends javax.swing.JDialog {
    
    public static final int START_ACQUISITION = 1;
    public static final int START_MANAGE = 2;
    public static final int START_DIAPO = 3;
    
    private String port;
    private int choice = 0;

    public StartDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
        
        comboBoxPort.removeAllItems();
        String[] portNames = SerialPortList.getPortNames();
        for (String portName : portNames) {
            comboBoxPort.addItem(portName);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        manageButton = new javax.swing.JButton();
        acquisitionButton = new javax.swing.JButton();
        comboBoxPort = new javax.swing.JComboBox<>();
        diapoButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Démarrage");
        setResizable(false);

        manageButton.setText("Gérer système");
        manageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manageButtonActionPerformed(evt);
            }
        });

        acquisitionButton.setText("Acquisition");
        acquisitionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                acquisitionButtonActionPerformed(evt);
            }
        });

        comboBoxPort.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        diapoButton.setText("Diapo");
        diapoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                diapoButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(acquisitionButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(manageButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)
                    .addComponent(comboBoxPort, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(diapoButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(comboBoxPort, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addComponent(manageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(acquisitionButton, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(diapoButton, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void manageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manageButtonActionPerformed
        choice = START_MANAGE;
        port = (String) comboBoxPort.getSelectedItem();
        dispose();
    }//GEN-LAST:event_manageButtonActionPerformed

    private void acquisitionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_acquisitionButtonActionPerformed
        choice = START_ACQUISITION;
        port = (String) comboBoxPort.getSelectedItem();
        dispose();
    }//GEN-LAST:event_acquisitionButtonActionPerformed

    private void diapoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_diapoButtonActionPerformed
        choice = START_DIAPO;
        port = (String) comboBoxPort.getSelectedItem();
        dispose();
    }//GEN-LAST:event_diapoButtonActionPerformed
    
    public int getChoice() {
        return choice;
    }
    
    public String getPort()  {
        return port;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton acquisitionButton;
    private javax.swing.JComboBox<String> comboBoxPort;
    private javax.swing.JButton diapoButton;
    private javax.swing.JButton manageButton;
    // End of variables declaration//GEN-END:variables
}
