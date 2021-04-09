package br.com.blend.telas;

import br.com.blend.dal.ModuloConexao;
import br.com.blend.dal.ModuloConexaoNuvem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;


public class TelaLotes extends javax.swing.JFrame {
    Connection conexao = null;
    Connection nuvem = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    PreparedStatement pstNuvem = null;
    ResultSet rsNuvem = null;
    
    //Variaveis de lote
    
    String num_lote;
    String nome_lote;
    String tipo_cafe;
    String obs;
       
    
    public TelaLotes() {
        initComponents();
        conexao = ModuloConexao.conector();
    }
    
    
    private void novo_lote(){
        String sql = "insert into tb_lotes(nome_lote, num_lote, tipo_cafe, obs) values(?, ?, ?, ?)";
        
        try {
            nome_lote = txtNomeLote.getText();
            num_lote = txtNumLote.getText();
            tipo_cafe = cbLoteTipoCafe.getSelectedItem().toString();
            obs = txtObservacao.getText();
            
            
            
           if((nome_lote.isEmpty() || num_lote.isEmpty())){
                JOptionPane.showMessageDialog(null, "Preencha todos os campos corretamente");
            }
            else if(check_float(num_lote) == false){
                JOptionPane.showMessageDialog(null, "Insira um número válido para o lote");
            }
            else if(tipo_cafe == "TIPO DO CAFÉ..."){
                JOptionPane.showMessageDialog(null, "Escolha o tipo do café");
            }
            else{ 
                pst = conexao.prepareStatement(sql);
                pst.setString(1, nome_lote);
                pst.setInt(2, Integer.parseInt(num_lote));
                pst.setString(3,tipo_cafe);
                pst.setString(4, obs);
                
                int lote_adicionado = pst.executeUpdate();
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
    
    private void novo_lote_nuvem(){
        String sql = "insert into tb_lotes(nome_lote, num_lote, tipo_cafe, obs) values(?, ?, ?, ?)";
        
        try {
            nuvem = ModuloConexaoNuvem.conector();
            
            nome_lote = txtNomeLote.getText();
            num_lote = txtNumLote.getText();
            tipo_cafe = cbLoteTipoCafe.getSelectedItem().toString();
            obs = txtObservacao.getText();
            
            
            
           if((nome_lote.isEmpty() || num_lote.isEmpty())){
                JOptionPane.showMessageDialog(null, "Preencha todos os campos corretamente");
            }
            else if(check_float(num_lote) == false){
                JOptionPane.showMessageDialog(null, "Insira um número válido para o lote");
            }
            else if(tipo_cafe == "TIPO DO CAFÉ..."){
                JOptionPane.showMessageDialog(null, "Escolha o tipo do café");
            }
            else{ 
                pstNuvem = nuvem.prepareStatement(sql);
                pstNuvem.setString(1, nome_lote);
                pstNuvem.setInt(2, Integer.parseInt(num_lote));
                pstNuvem.setString(3,tipo_cafe);
                pstNuvem.setString(4, obs);
                
                int lote_adicionado = pstNuvem.executeUpdate();
                if(lote_adicionado > 0){
                    System.out.println("Lote cadastrado na nuvem");
                }
                else{
                        JOptionPane.showMessageDialog(null, "Falha ao cadastrar lote (nuvem)");
                }
            }
        } catch (Exception e) {
            System.out.println(e + "Falha ao cadastrar lote");
        }
    }
    
    public static boolean check_float(String text) {
        try {
            Float.parseFloat(text);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtNomeLote = new javax.swing.JTextField();
        txtObservacao = new javax.swing.JTextField();
        btnLoteNovo = new javax.swing.JButton();
        txtNumLote = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        cbLoteTipoCafe = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(450, 300));
        setPreferredSize(new java.awt.Dimension(450, 400));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("CRIAR LOTE");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(213, 24, -1, -1));

        jLabel2.setText("Nome lote");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 100, -1, -1));

        jLabel3.setText("Tipo Cafe");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 180, -1, -1));

        jLabel5.setText("Observação");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 220, -1, -1));
        getContentPane().add(txtNomeLote, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 100, 110, -1));
        getContentPane().add(txtObservacao, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 220, 110, -1));

        btnLoteNovo.setText("CRIAR LOTE");
        btnLoteNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoteNovoActionPerformed(evt);
            }
        });
        getContentPane().add(btnLoteNovo, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 320, -1, -1));
        getContentPane().add(txtNumLote, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 140, 110, -1));

        jLabel6.setText("Numero Lote");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 140, -1, -1));

        cbLoteTipoCafe.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "TIPO DO CAFÉ...", "Supremo", "Tradicional ", "Blah blah" }));
        getContentPane().add(cbLoteTipoCafe, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 180, 110, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLoteNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoteNovoActionPerformed
        // Cria novo lote
        novo_lote();
        novo_lote_nuvem();
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
    private javax.swing.JComboBox<String> cbLoteTipoCafe;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JTextField txtNomeLote;
    private javax.swing.JTextField txtNumLote;
    private javax.swing.JTextField txtObservacao;
    // End of variables declaration//GEN-END:variables
}
