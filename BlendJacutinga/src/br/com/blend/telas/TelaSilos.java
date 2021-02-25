package br.com.blend.telas;

import java.sql.*;
import br.com.blend.dal.ModuloConexao;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;
import java.text.DateFormat;
import java.util.Date;
import javax.swing.JTable;


public class TelaSilos extends javax.swing.JFrame {
    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    String NomeSilo, QtdAtuSilo, QtdMaxSilo, DivSilo;
    
    
    public TelaSilos() {
        initComponents();
        conexao = ModuloConexao.conector();
    }
    

    private void buscar_silos(){
        String sql = "select * from tb_silos";
        
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            
            tbSilos.setModel(DbUtils.resultSetToTableModel(rs));
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    
    private void pesquisar_silos(){
        String sql = "select * from tb_silos where nome like ?";
        
        try {
            pst = conexao.prepareStatement(sql);
            
            pst.setString(1,txtPesqSilo.getText()+'%');
            rs = pst.executeQuery();
            
            tbSilos.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    
    private void set_campos_silos(){
        int setar = tbSilos.getSelectedRow();
        txtIdSilo.setText(tbSilos.getModel().getValueAt(setar,0).toString());
        txtNmSilo.setText(tbSilos.getModel().getValueAt(setar,1).toString());
        txtQtdAtuSilo.setText(tbSilos.getModel().getValueAt(setar,2).toString());
        txtQtdMaxSilo.setText(tbSilos.getModel().getValueAt(setar,3).toString());
        txtDivSilo.setText(tbSilos.getModel().getValueAt(setar,4).toString());
    }
    
    
    private void add_silos(){
        String sql = "insert into tb_silos(nome,qtd_atual,qtd_maxima,divisao) values (?,?,?,?)";
        
        try {
            
            NomeSilo = txtNmSilo.getText();
            QtdAtuSilo= txtQtdAtuSilo.getText();
            QtdMaxSilo= txtQtdMaxSilo.getText();
            DivSilo = txtDivSilo.getText();
            
            pst = conexao.prepareStatement(sql);
            pst.setString(1,NomeSilo);
            pst.setString(2,QtdAtuSilo);
            pst.setString(3,QtdMaxSilo);
            pst.setString(4,DivSilo);
            
            
            
            if((NomeSilo.isEmpty() || QtdAtuSilo.isEmpty() || QtdMaxSilo.isEmpty() || DivSilo.isEmpty())){
                JOptionPane.showMessageDialog(null, "Preencha todos os campos necessários!");
            }
            else if((check_float(QtdAtuSilo) == false || check_float(QtdMaxSilo) == false)){
                JOptionPane.showMessageDialog(null, "Insira um valor válido para Quantidade!");
            }
            else if(check_float(DivSilo) == false){
                JOptionPane.showMessageDialog(null, "Insira um valor válido para Divisão!");
            }
            else{
                int adicionado = pst.executeUpdate();
                if(adicionado>0){
                    JOptionPane.showMessageDialog(null, "Silo cadastrado com sucesso!");
                    txtNmSilo.setText(null);
                    txtQtdAtuSilo.setText(null);
                    txtQtdMaxSilo.setText(null);
                    txtDivSilo.setText(null);
                }
                else{
                    JOptionPane.showMessageDialog(null, "Erro inesperado!");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    
    private void editar_silos(){
        String sql = "update tb_silos set nome=?, qtd_atual=?, qtd_maxima=?, divisao=? where id_silo=?";
        
        int confirma = JOptionPane.showConfirmDialog(null,"Confirmar mudanças?","Atenção",JOptionPane.YES_NO_OPTION);
        if(confirma == JOptionPane.YES_OPTION)
        {
            try {
                
            NomeSilo = txtNmSilo.getText();
            QtdAtuSilo= txtQtdAtuSilo.getText();
            QtdMaxSilo= txtQtdMaxSilo.getText();
            DivSilo= txtDivSilo.getText();
            
            pst = conexao.prepareStatement(sql);
            pst.setString(1,NomeSilo);
            pst.setString(2,QtdAtuSilo);
            pst.setString(3,QtdMaxSilo);
            pst.setString(4,DivSilo);
            pst.setString(5, txtIdSilo.getText());
            
            
            if((txtNmSilo.getText().isEmpty() || txtQtdAtuSilo.getText().isEmpty() || txtQtdMaxSilo.getText().isEmpty() || txtDivSilo.getText().isEmpty())){
                JOptionPane.showMessageDialog(null, "Preencha todos os campos necessários!");
            }
            else if((check_float(QtdAtuSilo) == false || check_float(QtdMaxSilo) == false)){
                JOptionPane.showMessageDialog(null, "Insira um valor válido para Quantidade!");
            }
            else if(check_float(DivSilo) == false){
                JOptionPane.showMessageDialog(null, "Insira um valor válido para Divisão!");
            }
            else{
                int adicionado = pst.executeUpdate();
                if(adicionado>0){
                    JOptionPane.showMessageDialog(null, "Silo editado com sucesso!");
                    txtIdSilo.setText(null);
                    txtNmSilo.setText(null);
                    txtQtdAtuSilo.setText(null);
                    txtQtdMaxSilo.setText(null);
                    txtDivSilo.setText(null);
                }
                else{
                    JOptionPane.showMessageDialog(null, "Erro inesperado!");
                }
            }
             
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
    
    
    private void remover_silo(){
        String sql = "delete from tb_silos where id_silo=?";
        
        int confirma = JOptionPane.showConfirmDialog(null,"Tem certeza de que quer deletar o Silo?","Atenção",JOptionPane.YES_NO_OPTION);
        if(confirma == JOptionPane.YES_OPTION){
            
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1,txtIdSilo.getText());
                int apagado = pst.executeUpdate();
                
                if(apagado>0){
                    JOptionPane.showMessageDialog(null, "Silo removido com sucesso!");
                    txtIdSilo.setText(null);
                    txtNmSilo.setText(null);
                    txtQtdAtuSilo.setText(null);
                    txtQtdMaxSilo.setText(null);
                    txtDivSilo.setText(null);
                }
                else{
                    JOptionPane.showMessageDialog(null, "Erro inesperado!");
                }
                                 
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
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
        jLabel3 = new javax.swing.JLabel();
        txtNmSilo = new javax.swing.JTextField();
        txtQtdAtuSilo = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtQtdMaxSilo = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtDivSilo = new javax.swing.JTextField();
        lblData = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbSilos = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        btnSilosDeletar = new javax.swing.JButton();
        btnSilosEditar = new javax.swing.JButton();
        btnSilosAdd = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        txtIdSilo = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtPesqSilo = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Cadastro de Silos");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setText("Dados do Silo");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Nome do Silo");

        txtNmSilo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        txtQtdAtuSilo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("Quantidade Atual");

        txtQtdMaxSilo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setText("Quantidade Maxima");

        txtDivSilo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        lblData.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblData.setText("Data");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setText("Divisão");

        tbSilos.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tbSilos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Nome", "Quantidade Atual", "Quantidade Máxima", "Divisão"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbSilos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbSilosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbSilos);
        if (tbSilos.getColumnModel().getColumnCount() > 0) {
            tbSilos.getColumnModel().getColumn(0).setMinWidth(25);
            tbSilos.getColumnModel().getColumn(0).setPreferredWidth(25);
            tbSilos.getColumnModel().getColumn(2).setMinWidth(30);
            tbSilos.getColumnModel().getColumn(2).setPreferredWidth(30);
            tbSilos.getColumnModel().getColumn(3).setMinWidth(30);
            tbSilos.getColumnModel().getColumn(3).setPreferredWidth(30);
            tbSilos.getColumnModel().getColumn(4).setResizable(false);
            tbSilos.getColumnModel().getColumn(4).setPreferredWidth(50);
        }

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        jLabel2.setText("Silos Cadastrados");

        btnSilosDeletar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/blend/icones/delete.png"))); // NOI18N
        btnSilosDeletar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSilosDeletar.setPreferredSize(new java.awt.Dimension(80, 80));
        btnSilosDeletar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSilosDeletarActionPerformed(evt);
            }
        });

        btnSilosEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/blend/icones/update.png"))); // NOI18N
        btnSilosEditar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSilosEditar.setPreferredSize(new java.awt.Dimension(80, 80));
        btnSilosEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSilosEditarActionPerformed(evt);
            }
        });

        btnSilosAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/blend/icones/add.png"))); // NOI18N
        btnSilosAdd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSilosAdd.setPreferredSize(new java.awt.Dimension(80, 80));
        btnSilosAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSilosAddActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(btnSilosAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(btnSilosEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addComponent(btnSilosDeletar, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSilosAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSilosEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSilosDeletar, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnSilosDeletar.getAccessibleContext().setAccessibleName("Deletar");
        btnSilosDeletar.getAccessibleContext().setAccessibleDescription("Deletar");
        btnSilosEditar.getAccessibleContext().setAccessibleName("Editar");
        btnSilosEditar.getAccessibleContext().setAccessibleDescription("Editar");
        btnSilosAdd.getAccessibleContext().setAccessibleName("Adicionar");
        btnSilosAdd.getAccessibleContext().setAccessibleDescription("Adicionar");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("ID");

        txtIdSilo.setEditable(false);
        txtIdSilo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/blend/icones/proc.png"))); // NOI18N

        txtPesqSilo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtPesqSilo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesqSiloKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblData)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(99, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPesqSilo, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(jLabel10)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtDivSilo, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel3)
                                .addComponent(jLabel7)
                                .addComponent(jLabel4))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtQtdAtuSilo, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel1)
                                .addComponent(jLabel8))
                            .addGap(18, 18, 18)
                            .addComponent(txtQtdMaxSilo, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 596, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(142, 142, 142)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtIdSilo, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNmSilo, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(99, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel2)
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPesqSilo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtIdSilo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtNmSilo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtQtdAtuSilo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtQtdMaxSilo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDivSilo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addGap(31, 31, 31)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(lblData)
                .addContainerGap())
        );

        setSize(new java.awt.Dimension(810, 689));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        buscar_silos();
        
        //Configurações da table
        tbSilos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tbSilos.getColumnModel().getColumn(0).setPreferredWidth(50);
        tbSilos.getColumnModel().getColumn(1).setPreferredWidth(180);
        tbSilos.getColumnModel().getColumn(2).setPreferredWidth(130);
        tbSilos.getColumnModel().getColumn(3).setPreferredWidth(130);
        tbSilos.getColumnModel().getColumn(4).setPreferredWidth(103);
        
        //Modulos para pegar e formatar a data
        Date data = new Date();
        DateFormat formatador = DateFormat.getDateInstance(DateFormat.SHORT);
        lblData.setText(formatador.format(data));
    }//GEN-LAST:event_formWindowActivated

    private void btnSilosAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSilosAddActionPerformed
        //Chama função para adicionar silos
        add_silos();
    }//GEN-LAST:event_btnSilosAddActionPerformed

    private void tbSilosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbSilosMouseClicked
        //Chama função para preencher campos
        set_campos_silos();
    }//GEN-LAST:event_tbSilosMouseClicked

    private void btnSilosEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSilosEditarActionPerformed
        // Chama função para editar
        editar_silos();
    }//GEN-LAST:event_btnSilosEditarActionPerformed

    private void btnSilosDeletarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSilosDeletarActionPerformed
        // Chama função para deletar
        remover_silo();
    }//GEN-LAST:event_btnSilosDeletarActionPerformed

    private void txtPesqSiloKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesqSiloKeyReleased
        // Chamafunçao de procurar
        pesquisar_silos();
    }//GEN-LAST:event_txtPesqSiloKeyReleased


    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaSilos().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSilosAdd;
    private javax.swing.JButton btnSilosDeletar;
    private javax.swing.JButton btnSilosEditar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblData;
    private javax.swing.JTable tbSilos;
    private javax.swing.JTextField txtDivSilo;
    private javax.swing.JTextField txtIdSilo;
    private javax.swing.JTextField txtNmSilo;
    private javax.swing.JTextField txtPesqSilo;
    private javax.swing.JTextField txtQtdAtuSilo;
    private javax.swing.JTextField txtQtdMaxSilo;
    // End of variables declaration//GEN-END:variables
}
