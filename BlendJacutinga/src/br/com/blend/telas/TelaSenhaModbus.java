package br.com.blend.telas;

import java.util.Arrays;
import javax.swing.JOptionPane;


public class TelaSenhaModbus extends javax.swing.JFrame {

    public TelaSenhaModbus() {
        initComponents();
    }

    public void logar(){
        String senha = "nextlevel";
        
        if(Arrays.equals( senha.toCharArray(), txtModbusSenha.getPassword() )){
            TelaModbus modbus = new TelaModbus();
            modbus.setVisible(true);
            dispose();
        }
        else{
            JOptionPane.showMessageDialog(null, "Senha inválida");
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel1 = new java.awt.Panel();
        btnLogarModbus = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtModbusSenha = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Endereçamento do PLC");
        setPreferredSize(new java.awt.Dimension(429, 319));

        panel1.setBackground(new java.awt.Color(25, 42, 86));
        panel1.setMinimumSize(new java.awt.Dimension(424, 340));
        panel1.setPreferredSize(new java.awt.Dimension(424, 340));
        panel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnLogarModbus.setBackground(new java.awt.Color(255, 51, 51));
        btnLogarModbus.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnLogarModbus.setForeground(new java.awt.Color(250, 250, 250));
        btnLogarModbus.setText("Acessar");
        btnLogarModbus.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLogarModbus.setFocusPainted(false);
        btnLogarModbus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogarModbusActionPerformed(evt);
            }
        });
        panel1.add(btnLogarModbus, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 180, 190, 66));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(250, 250, 250));
        jLabel1.setText("SENHA:");
        panel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 80, -1, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(250, 250, 250));
        jLabel2.setText("ENDEREÇAMENTO DO CLP");
        panel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 10, -1, -1));

        jPanel1.setBackground(new java.awt.Color(68, 141, 41));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(250, 250, 250));
        jLabel3.setText("Insira a senha para acessar essa página");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addContainerGap(107, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        panel1.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 280, 424, -1));
        panel1.add(txtModbusSenha, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 120, 190, 31));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panel1, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        setSize(new java.awt.Dimension(440, 368));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnLogarModbusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogarModbusActionPerformed
        // Logar
        logar();
    }//GEN-LAST:event_btnLogarModbusActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TelaSenhaModbus.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaSenhaModbus.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaSenhaModbus.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaSenhaModbus.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaSenhaModbus().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLogarModbus;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private java.awt.Panel panel1;
    private javax.swing.JPasswordField txtModbusSenha;
    // End of variables declaration//GEN-END:variables
}
