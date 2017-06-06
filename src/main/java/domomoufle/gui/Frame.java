package domomoufle.gui;

import domomoufle.GantGUI;
import domomoufle.database.Action;
import domomoufle.database.Modele;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Frame extends javax.swing.JFrame {

    private final MyTableModel tableActionsModel;
    private final MyTableModel tableGestesModel;
    private final DefaultComboBoxModel<String> comboBoxModel;
    private final DefaultComboBoxModel<String> comboBoxModelesModel;

    public Frame() {
        initComponents();
        setLocationRelativeTo(null);

        comboBoxModel = new DefaultComboBoxModel();
        comboBoxActions.setModel(comboBoxModel);

        tableGestesModel = new MyTableModel(new String[]{"Description modele", "Description action"}, 0);
        tableModeles.setModel(tableGestesModel);

        tableActionsModel = new MyTableModel(new String[]{"Description", "Commande"}, 0);
        tableActions.setModel(tableActionsModel);

        comboBoxModelesModel = new DefaultComboBoxModel();
        comboBoxModeles.setModel(comboBoxModelesModel);

        updateActionTable();
        updateListModeles();
    }

    private void updateActionTable() {
        comboBoxModel.removeAllElements();

        while (tableActionsModel.getRowCount() > 0) {
            tableActionsModel.removeRow(0);
        }

        ArrayList<Action> actions = GantGUI.getDatabase().getAllActions();
        for (Action a : actions) {
            tableActionsModel.addRow(new String[]{a.description, a.command});
            comboBoxModel.addElement(a.description);
        }
    }

    private void updateListModeles() {
        comboBoxModelesModel.removeAllElements();

        while (tableGestesModel.getRowCount() > 0) {
            tableGestesModel.removeRow(0);
        }

        ArrayList<Modele> modeles = GantGUI.getDatabase().getAllModeles();
        for (Modele modele : modeles) {
            comboBoxModelesModel.addElement(modele.description);
            tableGestesModel.addRow(new String[]{modele.description, modele.action});
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableActions = new javax.swing.JTable();
        buttonNewAction = new javax.swing.JButton();
        buttonDelete = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        newGesteButton = new javax.swing.JButton();
        deleteGesteButton = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        comboBoxActions = new javax.swing.JComboBox<>();
        jButton3 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        comboBoxModeles = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        panelTableModeles = new javax.swing.JPanel();
        scrollPaneModeles = new javax.swing.JScrollPane();
        tableModeles = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Domomoufle");
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        tableActions.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tableActions);

        buttonNewAction.setText("Ajouter action");
        buttonNewAction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonNewActionActionPerformed(evt);
            }
        });

        buttonDelete.setText("Supprimer actions selectionnées");
        buttonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDeleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(buttonNewAction, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonDelete)
                .addContainerGap())
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 710, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonNewAction)
                    .addComponent(buttonDelete))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Gestion des actions", jPanel1);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        newGesteButton.setText("Ajouter geste");
        newGesteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newGesteButtonActionPerformed(evt);
            }
        });

        deleteGesteButton.setText("Supprimer gestes selectionnés");
        deleteGesteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteGesteButtonActionPerformed(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Association"));

        comboBoxActions.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jButton3.setText("Associer");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel1.setText("Action : ");

        comboBoxModeles.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel2.setText("Modele : ");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton3))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(comboBoxActions, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(comboBoxModeles, 0, 597, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(comboBoxModeles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(comboBoxActions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelTableModeles.setBackground(new java.awt.Color(255, 255, 255));
        panelTableModeles.setBorder(javax.swing.BorderFactory.createTitledBorder("Vue des modeles"));

        scrollPaneModeles.setBackground(new java.awt.Color(255, 255, 255));
        scrollPaneModeles.setBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.light")));

        tableModeles.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        scrollPaneModeles.setViewportView(tableModeles);

        javax.swing.GroupLayout panelTableModelesLayout = new javax.swing.GroupLayout(panelTableModeles);
        panelTableModeles.setLayout(panelTableModelesLayout);
        panelTableModelesLayout.setHorizontalGroup(
            panelTableModelesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPaneModeles)
        );
        panelTableModelesLayout.setVerticalGroup(
            panelTableModelesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPaneModeles, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelTableModeles, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(newGesteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 92, Short.MAX_VALUE)
                        .addComponent(deleteGesteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(9, 9, 9))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(panelTableModeles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(newGesteButton)
                    .addComponent(deleteGesteButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 137, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Gestion des gestes", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonNewActionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonNewActionActionPerformed
        NewActionDialog actionDialog = new NewActionDialog(this, true);
        actionDialog.setVisible(true);

        Action action = actionDialog.getAction();

        if (action != null) {
            GantGUI.getDatabase().addAction(action);
            updateActionTable();
        }
    }//GEN-LAST:event_buttonNewActionActionPerformed

    private void buttonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDeleteActionPerformed
        for (int i : tableActions.getSelectedRows()) {
            GantGUI.getDatabase().deleteAction(new Action(
                    (String) tableActions.getValueAt(i, 0),
                    (String) tableActions.getValueAt(i, 1)
            ));
        }

        updateActionTable();
    }//GEN-LAST:event_buttonDeleteActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        String action = (String) comboBoxActions.getSelectedItem();
        String modele = (String) comboBoxModeles.getSelectedItem();
        if (action == null || modele == null) {
            action = "";
            modele = "";
        }

        if (action.isEmpty() || modele.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Les champs ne sont pas censés être vides",
                    "Attention !",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        GantGUI.getDatabase().updateModeleAction(modele, action);
        updateListModeles();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void newGesteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newGesteButtonActionPerformed
        NewGesteDialog dialog = new NewGesteDialog(this, true);
        dialog.setVisible(true);
        
        updateListModeles();
    }//GEN-LAST:event_newGesteButtonActionPerformed

    private void deleteGesteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteGesteButtonActionPerformed
        for (int row : tableModeles.getSelectedRows()) {
            String desc = (String) this.tableGestesModel.getValueAt(row, 0);
            GantGUI.getDatabase().deleteGeste(desc);
        }
        
        updateListModeles();
    }//GEN-LAST:event_deleteGesteButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonDelete;
    private javax.swing.JButton buttonNewAction;
    private javax.swing.JComboBox<String> comboBoxActions;
    private javax.swing.JComboBox<String> comboBoxModeles;
    private javax.swing.JButton deleteGesteButton;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton newGesteButton;
    private javax.swing.JPanel panelTableModeles;
    private javax.swing.JScrollPane scrollPaneModeles;
    private javax.swing.JTable tableActions;
    private javax.swing.JTable tableModeles;
    // End of variables declaration//GEN-END:variables
}

class MyTableModel extends DefaultTableModel {

    MyTableModel(String[] string, int i) {
        super(string, i);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
