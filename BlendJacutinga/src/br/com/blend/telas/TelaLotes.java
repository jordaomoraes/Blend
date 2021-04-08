package br.com.blend.telas;

import br.com.blend.dal.ModuloConexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;


public class TelaLotes extends javax.swing.JFrame {
    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    
    //Variaveis de lote
    
    int num_lote = 0;
    String nome_lote;
    String tipo_cafe;
    String obs;
    float qtd_torrado = 0;
       
    
    public TelaLotes() {
        initComponents();
        conexao = ModuloConexao.conector();
    }
    
    
    private void novo_lote(){
        String sql = "insert into tb_lotes(nome_lote, num_lote, tipo_cafe, qtd_torrado, obs) values(?, ?, ?, ?, ?)";
        
        num_lote = Integer.parseInt(txtNumLote.getText());
        nome_lote = txtNomeLote.getText();
        tipo_cafe = txtTipoCafe.getText();
        qtd_torrado = Float.parseFloat(txtQtdTorrado.getText());
        obs = txtObservacao.getText();
        
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, nome_lote);
            pst.setInt(2, num_lote);
            pst.setString(3,tipo_cafe);
            pst.setFloat(4, qtd_torrado);
            pst.setString(5, obs);
            
            int confirma = JOptionPane.showConfirmDialog(null,"Tem certeza de que quer criar um novo lote? O lote anterior será encerrado","Atenção",JOptionPane.YES_NO_OPTION);
            if(confirma == JOptionPane.YES_OPTION ){
                int lote_adicionado = pst.executeUpdate();
                set_lote_atual(num_lote, nome_lote);
                if(lote_adicionado > 0){
                    JOptionPane.showMessageDialog(null, "Lote cadastrado com sucesso");
                }
                else{
                    JOptionPane.showMessageDialog(null, "Falha ao cadastrar lote");
                }
            }
        } catch (Exception e) {
            System.out.println(e + "Falha ao cadastrar lote");
        }
    }
    
    
    private void set_lote_atual(int num_lote, String nome_lote){
        String sql = "update tb_lote_atual set num_lote_atual = ?, nome_lote_atual = ? where id_lote_atual";
        int id = 0;
        try {
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, num_lote);
            pst.setString(2, nome_lote);

            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println("Falha ao setar lote atual, "+e);
        }
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtNomeLote = new javax.swing.JTextField();
        txtTipoCafe = new javax.swing.JTextField();
        txtObservacao = new javax.swing.JTextField();
        txtQtdTorrado = new javax.swing.JTextField();
        btnLoteNovo = new javax.swing.JButton();
        txtNumLote = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(450, 300));
        setPreferredSize(new java.awt.Dimension(450, 400));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("CRIAR LOTE");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(213, 24, -1, -1));

        jLabel2.setText("Nome lote");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(84, 112, -1, -1));

        jLabel3.setText("Tipo Cafe");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 180, -1, -1));

        jLabel4.setText("Quantidade Torrado");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 220, -1, -1));

        jLabel5.setText("Observação");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 260, -1, -1));
        getContentPane().add(txtNomeLote, new org.netbeans.lib.awtextra.AbsoluteConstraints(173, 109, 110, -1));
        getContentPane().add(txtTipoCafe, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 180, 110, -1));
        getContentPane().add(txtObservacao, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 250, 110, -1));
        getContentPane().add(txtQtdTorrado, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 210, 110, -1));

        btnLoteNovo.setText("CRIAR LOTE");
        btnLoteNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoteNovoActionPerformed(evt);
            }
        });
        getContentPane().add(btnLoteNovo, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 320, -1, -1));
        getContentPane().add(txtNumLote, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 140, 110, -1));

        jLabel6.setText("Numero Lote");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 140, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLoteNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoteNovoActionPerformed
        // Cria novo lote
        novo_lote();
    }//GEN-LAST:event_btnLoteNovoActionPerformed
    
    
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
            java.util.logging.Logger.getLogger(TelaLotes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaLotes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaLotes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaLotes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaLotes().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLoteNovo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JTextField txtNomeLote;
    private javax.swing.JTextField txtNumLote;
    private javax.swing.JTextField txtObservacao;
    private javax.swing.JTextField txtQtdTorrado;
    private javax.swing.JTextField txtTipoCafe;
    // End of variables declaration//GEN-END:variables
}
