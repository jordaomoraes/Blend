package br.com.blend.telas;

import java.util.Arrays;
import javax.swing.JOptionPane;


public class TelaSenhaSilos extends javax.swing.JFrame {

    public TelaSenhaSilos() {
        initComponents();
    }

    
    
    public void logar(){
        String senha = "nextlevel";
        
        if(Arrays.equals( senha.toCharArray(), txtSilosSenhaa.getPassword())){
            TelaSilos silos = new TelaSilos();
            silos.setVisible(true);
            dispose();
        }
        else{
            JOptionPane.showMessageDialog(null, "Senha inválida");
        }
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel3 = new java.awt.Panel();
        btnLogarModbus2 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        txtSilosSenhaa = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Autenticação Silos");
        setBackground(new java.awt.Color(25, 42, 86));
        setResizable(false);

        panel3.setBackground(new java.awt.Color(25, 42, 86));
        panel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnLogarModbus2.setBackground(new java.awt.Color(255, 51, 51));
        btnLogarModbus2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnLogarModbus2.setForeground(new java.awt.Color(250, 250, 250));
        btnLogarModbus2.setText("Acessar");
        btnLogarModbus2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLogarModbus2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogarModbus2ActionPerformed(evt);
            }
        });
        panel3.add(btnLogarModbus2, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 180, 190, 66));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(250, 250, 250));
        jLabel7.setText("SENHA:");
        panel3.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 80, -1, -1));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(250, 250, 250));
        jLabel8.setText("CONTROLE DE SILOS");
        panel3.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 10, -1, -1));

        jPanel3.setBackground(new java.awt.Color(68, 141, 41));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(250, 250, 250));
        jLabel9.setText("Insira a senha para acessar essa  página");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        panel3.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 270, 440, 60));
        panel3.add(txtSilosSenhaa, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 120, 190, 31));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel3, javax.swing.GroupLayout.PREFERRED_SIZE, 429, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(445, 353));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnLogarModbus2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogarModbus2ActionPerformed
        // Logar
        logar();
    }//GEN-LAST:event_btnLogarModbus2ActionPerformed

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
            java.util.logging.Logger.getLogger(TelaSenhaSilos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaSenhaSilos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaSenhaSilos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaSenhaSilos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaSenhaSilos().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLogarModbus2;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel3;
    private java.awt.Panel panel3;
    private javax.swing.JPasswordField txtSilosSenhaa;
    // End of variables declaration//GEN-END:variables
}
