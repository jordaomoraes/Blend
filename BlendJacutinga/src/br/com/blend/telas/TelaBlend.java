package br.com.blend.telas;

import java.sql.*;
import br.com.blend.dal.ModuloConexao;
import static br.com.blend.telas.TelaSilos.check_float;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;
import java.text.DateFormat;
import java.util.Date;
import javax.swing.JTable;

public class TelaBlend extends javax.swing.JFrame {
    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    String NomeBlend, QtdSilo1, QtdSilo2, QtdSilo3, QtdSilo4;
    String Metodo;
    
    public TelaBlend() {
        initComponents();
        conexao = ModuloConexao.conector();
    }
    
    
    
    
    private void buscar_blend(){
        String sql = "select * from tb_blend";
        
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            
            tbBlend.setModel(DbUtils.resultSetToTableModel(rs));
            //Remove campos desnecessários da table
            remove_colunas();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    
    private void pesquisar_blend(){
        
        String sql = "select * from tb_blend where nome like ?";
        
        try {
            pst = conexao.prepareStatement(sql);
            
            pst.setString(1,txtBlendPesq.getText()+'%');
            rs = pst.executeQuery();
            
            tbBlend.setModel(DbUtils.resultSetToTableModel(rs));
            remove_colunas();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        
    }

    
    private void set_campos_blend(){
        int setar = tbBlend.getSelectedRow();
        txtIdBlend.setText(tbBlend.getModel().getValueAt(setar,0).toString());
        txtNomeBlend.setText(tbBlend.getModel().getValueAt(setar,1).toString());
        txtQtdSilo1.setText(tbBlend.getModel().getValueAt(setar,3).toString());
        txtQtdSilo2.setText(tbBlend.getModel().getValueAt(setar,5).toString());
        txtQtdSilo3.setText(tbBlend.getModel().getValueAt(setar,7).toString());
        txtQtdSilo4.setText(tbBlend.getModel().getValueAt(setar,9).toString());
    }
    
    
    private void remove_colunas(){
        tbBlend.removeColumn(tbBlend.getColumnModel().getColumn(0));
        tbBlend.removeColumn(tbBlend.getColumnModel().getColumn(1));
        tbBlend.removeColumn(tbBlend.getColumnModel().getColumn(2));
        tbBlend.removeColumn(tbBlend.getColumnModel().getColumn(3));
        tbBlend.removeColumn(tbBlend.getColumnModel().getColumn(4));
    }
    
    
    private void block_campos(){
        txtNomeBlend.setEditable(false);
        txtQtdSilo1.setEditable(false);
        txtQtdSilo2.setEditable(false);
        txtQtdSilo3.setEditable(false);
        txtQtdSilo4.setEditable(false);
    }
    
    
    private void unlock_campos(){
        txtNomeBlend.setEditable(true);
        txtQtdSilo1.setEditable(true);
        txtQtdSilo2.setEditable(true);
        txtQtdSilo3.setEditable(true);
        txtQtdSilo4.setEditable(true);
    }
    
    
    private void add_blend(){
        
        String sql = "insert into tb_blend(nome,fk_silo1, qtd_silo1,fk_silo2, qtd_silo2, fk_silo3, qtd_silo3, fk_silo4, qtd_silo4) values (?,1,?,2,?,3,?,4,?)";
        
        try {
            pst = conexao.prepareStatement(sql);
            
            NomeBlend = txtNomeBlend.getText();
            QtdSilo1 = txtQtdSilo1.getText();
            QtdSilo2 = txtQtdSilo2.getText();
            QtdSilo3 = txtQtdSilo3.getText();
            QtdSilo4 = txtQtdSilo4.getText();
            
            pst.setString(1,NomeBlend);
            pst.setString(2,QtdSilo1);
            pst.setString(3,QtdSilo2);
            pst.setString(4,QtdSilo3);
            pst.setString(5,QtdSilo4);
            
            
            if((NomeBlend.isEmpty() || QtdSilo2.isEmpty() || QtdSilo3.isEmpty() || QtdSilo4.isEmpty())){
                JOptionPane.showMessageDialog(null, "Preencha todos os campos!");
            }
            else if((check_int(QtdSilo1) == false || check_int(QtdSilo2) == false || check_int(QtdSilo3) == false || check_int(QtdSilo4) == false)){
                JOptionPane.showMessageDialog(null, "Insira um valorválido para a Quantidade!");
            }
            else{
                int adicionado = pst.executeUpdate();
                if(adicionado > 0 ){
                    JOptionPane.showMessageDialog(null, "Blend cadastrado com sucesso!");
                    txtNomeBlend.setText(null);
                    txtQtdSilo1.setText(null);
                    txtQtdSilo2.setText(null);
                    txtQtdSilo3.setText(null);
                    txtQtdSilo4.setText(null);
                }
                else{
                    JOptionPane.showMessageDialog(null, "Erro inesperado!");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    
    private void editar_blend(){
       String sql = "update tb_blend set nome=?, qtd_silo1=?, qtd_silo2=?, qtd_silo3=?, qtd_silo4=? where id_blend=?";
        
        try {
            pst = conexao.prepareStatement(sql);
            
            NomeBlend = txtNomeBlend.getText();
            QtdSilo1 = txtQtdSilo1.getText();
            QtdSilo2 = txtQtdSilo2.getText();
            QtdSilo3 = txtQtdSilo3.getText();
            QtdSilo4 = txtQtdSilo4.getText();
            
            pst.setString(1,NomeBlend);
            pst.setString(2,QtdSilo1);
            pst.setString(3,QtdSilo2);
            pst.setString(4,QtdSilo3);
            pst.setString(5,QtdSilo4);
            pst.setString(6, txtIdBlend.getText());

            
            if((NomeBlend.isEmpty() || QtdSilo2.isEmpty() || QtdSilo3.isEmpty() || QtdSilo4.isEmpty())){
                JOptionPane.showMessageDialog(null, "Preencha todos os campos!");
            }
            else if((check_int(QtdSilo1) == false || check_int(QtdSilo2) == false || check_int(QtdSilo3) == false || check_int(QtdSilo4) == false)){
                JOptionPane.showMessageDialog(null, "Insira um valorválido para a Quantidade!");
            }
            else{
                int adicionado = pst.executeUpdate();
                if(adicionado > 0 ){
                    JOptionPane.showMessageDialog(null, "Blend editado com sucesso!");
                    txtNomeBlend.setText(null);
                    txtQtdSilo1.setText(null);
                    txtQtdSilo2.setText(null);
                    txtQtdSilo3.setText(null);
                    txtQtdSilo4.setText(null);
                }
                else{
                    JOptionPane.showMessageDialog(null, "Erro inesperado!");
                }
                
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    
    private void remover_blend(){
        String sql = "delete from tb_blend where id_blend=?";
        
        int confirma = JOptionPane.showConfirmDialog(null,"Tem certeza de que quer deletar o Blend?","Atenção",JOptionPane.YES_NO_OPTION);
        if(confirma == JOptionPane.YES_OPTION){
            
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1,txtIdBlend.getText());
                int apagado = pst.executeUpdate();
                
                if(apagado>0){
                    JOptionPane.showMessageDialog(null, "Blend deletado com sucesso!");
                    txtNomeBlend.setText(null);
                    txtQtdSilo1.setText(null);
                    txtQtdSilo2.setText(null);
                    txtQtdSilo3.setText(null);
                    txtQtdSilo4.setText(null);
                }
                else{
                    JOptionPane.showMessageDialog(null, "Erro inesperado!");
                }
                                 
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
    
    
     public static boolean check_int(String text) {
        try {
            Integer.parseInt(text);
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
        lblData = new javax.swing.JLabel();
        txtQtdSilo1 = new javax.swing.JTextField();
        txtQtdSilo2 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtQtdSilo3 = new javax.swing.JTextField();
        txtQtdSilo4 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbBlend = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        txtNomeBlend = new javax.swing.JTextField();
        btnBlendEditar = new javax.swing.JButton();
        txtIdBlend = new javax.swing.JTextField();
        btnBlendDeletar = new javax.swing.JButton();
        btnBlendSalvar = new javax.swing.JButton();
        btnBlendAdd = new javax.swing.JButton();
        txtBlendPesq = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Blend");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        jLabel1.setText("CRIAR BLEND");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setText("SILO 1");

        lblData.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblData.setText("Data - Observação");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("Quantidade");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel7.setText("SILO 2");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel11.setText("SILO 3");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel12.setText("Quantidade");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel14.setText("Quantidade");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel16.setText("SILO 4");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setText("Quantidade");

        tbBlend.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tbBlend.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbBlendMouseClicked(evt);
            }
        });
        tbBlend.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbBlendKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tbBlend);

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setText("Nome do Blend:");

        btnBlendEditar.setText("Editar Blend");
        btnBlendEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBlendEditarActionPerformed(evt);
            }
        });

        txtIdBlend.setEditable(false);

        btnBlendDeletar.setText("Deletar Blend");
        btnBlendDeletar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBlendDeletarActionPerformed(evt);
            }
        });

        btnBlendSalvar.setText("Salvar");
        btnBlendSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBlendSalvarActionPerformed(evt);
            }
        });

        btnBlendAdd.setText("Novo Blend");
        btnBlendAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBlendAddActionPerformed(evt);
            }
        });

        txtBlendPesq.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBlendPesqKeyReleased(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/blend/icones/proc.png"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblData, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 112, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(18, 18, 18)
                        .addComponent(txtNomeBlend, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35)
                        .addComponent(txtIdBlend, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 621, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(txtQtdSilo1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addGap(50, 50, 50)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel6)
                            .addComponent(txtQtdSilo2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(50, 50, 50)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(jLabel12)
                            .addComponent(txtQtdSilo3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(50, 50, 50)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addComponent(jLabel14)
                            .addComponent(txtQtdSilo4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(133, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(350, 350, 350)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnBlendDeletar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnBlendSalvar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnBlendEditar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnBlendAdd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(126, 126, 126)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtBlendPesq, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(368, 368, 368)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtBlendPesq, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtNomeBlend, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtIdBlend, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel16)
                    .addComponent(jLabel11)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtQtdSilo1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel14)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtQtdSilo4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel12)
                            .addGap(9, 9, 9)
                            .addComponent(txtQtdSilo3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel6)
                            .addGap(9, 9, 9)
                            .addComponent(txtQtdSilo2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(71, 71, 71)
                .addComponent(btnBlendAdd)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnBlendEditar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnBlendSalvar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnBlendDeletar)
                .addGap(23, 23, 23)
                .addComponent(lblData)
                .addContainerGap())
        );

        setSize(new java.awt.Dimension(882, 689));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        //Mudanças ao abrir janela
        buscar_blend();
        block_campos();
        txtIdBlend.setVisible(false);
        //Data
        Date data = new Date();
        DateFormat formatador = DateFormat.getDateInstance(DateFormat.SHORT);
        lblData.setText(formatador.format(data)+" - Crie seu blend selecionando as quantidades de café vindas de cada silo");
    }//GEN-LAST:event_formWindowActivated

    private void tbBlendKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbBlendKeyReleased
        
    }//GEN-LAST:event_tbBlendKeyReleased

    private void tbBlendMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbBlendMouseClicked
        // Chama função para preencher os campos de texto e muda o metodo
        Metodo = "pesquisar";
        set_campos_blend();
    }//GEN-LAST:event_tbBlendMouseClicked

    private void btnBlendDeletarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBlendDeletarActionPerformed
        // Chama função para deletar blend
        remover_blend();
    }//GEN-LAST:event_btnBlendDeletarActionPerformed

    private void btnBlendSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBlendSalvarActionPerformed
        // Checa o metodo e chama a função correspondente
        if(Metodo=="pesquisar"){
            return;
        }
        else{
            if(Metodo == "adicionar"){
                add_blend();
                
            }
            else if(Metodo == "editar"){
                editar_blend();
            }
            else if(Metodo == null){
                return;
            }
            else{
                JOptionPane.showMessageDialog(null, "Erro inesperado!");
            }
        }
    }//GEN-LAST:event_btnBlendSalvarActionPerformed

    private void btnBlendEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBlendEditarActionPerformed
        //Muda metodo para editar e desbloqueia campos
        Metodo="editar";
        
        unlock_campos();
    }//GEN-LAST:event_btnBlendEditarActionPerformed

    private void btnBlendAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBlendAddActionPerformed
        //Muda metodo para adicionar e desbloqueia campos
        Metodo="adicionar";
        
        txtNomeBlend.setText(null);
        txtQtdSilo1.setText(null);
        txtQtdSilo2.setText(null);
        txtQtdSilo3.setText(null);
        txtQtdSilo4.setText(null);
      
        unlock_campos();
        
    }//GEN-LAST:event_btnBlendAddActionPerformed

    private void txtBlendPesqKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBlendPesqKeyReleased
        // Chama função para pesquisar blend criado
        pesquisar_blend();
    }//GEN-LAST:event_txtBlendPesqKeyReleased
    
    public static void main(String args[]) {
       
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaBlend().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBlendAdd;
    private javax.swing.JButton btnBlendDeletar;
    private javax.swing.JButton btnBlendEditar;
    private javax.swing.JButton btnBlendSalvar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblData;
    private javax.swing.JTable tbBlend;
    private javax.swing.JTextField txtBlendPesq;
    private javax.swing.JTextField txtIdBlend;
    private javax.swing.JTextField txtNomeBlend;
    private javax.swing.JTextField txtQtdSilo1;
    private javax.swing.JTextField txtQtdSilo2;
    private javax.swing.JTextField txtQtdSilo3;
    private javax.swing.JTextField txtQtdSilo4;
    // End of variables declaration//GEN-END:variables
}
