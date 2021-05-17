package br.com.blend.telas;

import java.sql.*;
import br.com.blend.dal.ModuloConexao;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;
import java.text.DateFormat;
import java.util.Date;
import javax.swing.JTable;
import java.awt.Toolkit;


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

        jPanel1 = new javax.swing.JPanel();
        lblData = new javax.swing.JLabel();
        btnSilosDeletar = new javax.swing.JButton();
        btnSilosEditar = new javax.swing.JButton();
        btnSilosAdd = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbSilos = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        txtDivSilo = new javax.swing.JTextField();
        txtNmSilo = new javax.swing.JTextField();
        txtIdSilo = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        lblData1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtPesqSilo = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        txtQtdMaxSilo = new javax.swing.JTextField();
        txtQtdAtuSilo = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jPanel43 = new javax.swing.JPanel();
        jPanel44 = new javax.swing.JPanel();
        jPanel45 = new javax.swing.JPanel();
        jPanel46 = new javax.swing.JPanel();
        jPanel47 = new javax.swing.JPanel();
        jPanel48 = new javax.swing.JPanel();
        jPanel49 = new javax.swing.JPanel();
        jPanel50 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Cadastro de Silos");
        setIconImage(Toolkit.getDefaultToolkit().getImage(TelaSilos.class.getResource("/br/com/blend/icones/silo (1).png")));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(25, 42, 86));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblData.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblData.setText("Data");
        jPanel1.add(lblData, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 670, -1, -1));

        btnSilosDeletar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/blend/icones/delete_bin_48px.png"))); // NOI18N
        btnSilosDeletar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSilosDeletar.setPreferredSize(new java.awt.Dimension(80, 80));
        btnSilosDeletar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSilosDeletarActionPerformed(evt);
            }
        });
        jPanel1.add(btnSilosDeletar, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 190, 80, -1));
        btnSilosDeletar.getAccessibleContext().setAccessibleName("Deletar");
        btnSilosDeletar.getAccessibleContext().setAccessibleDescription("Deletar");

        btnSilosEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/blend/icones/edit_48px.png"))); // NOI18N
        btnSilosEditar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSilosEditar.setPreferredSize(new java.awt.Dimension(80, 80));
        btnSilosEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSilosEditarActionPerformed(evt);
            }
        });
        jPanel1.add(btnSilosEditar, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 100, 80, 80));
        btnSilosEditar.getAccessibleContext().setAccessibleName("Editar");
        btnSilosEditar.getAccessibleContext().setAccessibleDescription("Editar");

        btnSilosAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/blend/icones/add_48px.png"))); // NOI18N
        btnSilosAdd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSilosAdd.setPreferredSize(new java.awt.Dimension(80, 80));
        btnSilosAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSilosAddActionPerformed(evt);
            }
        });
        jPanel1.add(btnSilosAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, 80, 80));
        btnSilosAdd.getAccessibleContext().setAccessibleName("Adicionar");
        btnSilosAdd.getAccessibleContext().setAccessibleDescription("Adicionar");

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 90, 160, 290));

        jPanel3.setBackground(new java.awt.Color(25, 42, 86));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(250, 250, 250));
        jLabel2.setText("CONTROLE DE SILOS");
        jPanel3.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 112, -1, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(250, 250, 250));
        jLabel6.setText("CONTROLE DE SILOS");
        jPanel3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, -1, 60));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 780, 80));

        jPanel2.setBackground(new java.awt.Color(72, 126, 176));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(250, 250, 250));
        jLabel11.setText("SILOS CADASTRADOS");
        jPanel2.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 210, -1, -1));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(250, 250, 250));
        jLabel12.setText("SILOS CADASTRADOS");
        jPanel2.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, -1));

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

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, 596, 113));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 390, 730, 190));

        jPanel4.setBackground(new java.awt.Color(72, 126, 176));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtDivSilo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jPanel4.add(txtDivSilo, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 50, 100, 31));

        txtNmSilo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jPanel4.add(txtNmSilo, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 50, 170, 31));

        txtIdSilo.setEditable(false);
        txtIdSilo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jPanel4.add(txtIdSilo, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 50, 40, 31));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(250, 250, 250));
        jLabel9.setText("NOME DO SILO");
        jPanel4.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 10, -1, -1));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(250, 250, 250));
        jLabel14.setText("DIVISÃO");
        jPanel4.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 10, 80, -1));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(250, 250, 250));
        jLabel7.setText("ID");
        jPanel4.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 10, -1, -1));

        getContentPane().add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 540, 110));

        jPanel7.setBackground(new java.awt.Color(72, 126, 176));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblData1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblData1.setText("Data");
        jPanel7.add(lblData1, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 360, -1, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/blend/icones/proc.png"))); // NOI18N
        jPanel7.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, 20, 30));

        txtPesqSilo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtPesqSilo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesqSiloKeyReleased(evt);
            }
        });
        jPanel7.add(txtPesqSilo, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, 258, 31));

        getContentPane().add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 330, 540, 50));

        jPanel6.setBackground(new java.awt.Color(72, 126, 176));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(250, 250, 250));
        jLabel13.setText("QUANTIDADE MÁXIMA");
        jPanel6.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 10, -1, -1));

        txtQtdMaxSilo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jPanel6.add(txtQtdMaxSilo, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 50, 100, 31));

        txtQtdAtuSilo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jPanel6.add(txtQtdAtuSilo, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 50, 100, 31));

        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(250, 250, 250));
        jLabel15.setText("QUANTIDADE ATUAL");
        jPanel6.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, -1, -1));

        jPanel43.setBackground(new java.awt.Color(255, 255, 255));
        jPanel43.setPreferredSize(new java.awt.Dimension(2, 100));

        jPanel44.setBackground(new java.awt.Color(255, 255, 255));
        jPanel44.setPreferredSize(new java.awt.Dimension(2, 100));

        javax.swing.GroupLayout jPanel44Layout = new javax.swing.GroupLayout(jPanel44);
        jPanel44.setLayout(jPanel44Layout);
        jPanel44Layout.setHorizontalGroup(
            jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );
        jPanel44Layout.setVerticalGroup(
            jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 68, Short.MAX_VALUE)
        );

        jPanel45.setBackground(new java.awt.Color(255, 255, 255));
        jPanel45.setPreferredSize(new java.awt.Dimension(2, 100));

        jPanel46.setBackground(new java.awt.Color(255, 255, 255));
        jPanel46.setPreferredSize(new java.awt.Dimension(2, 100));

        javax.swing.GroupLayout jPanel46Layout = new javax.swing.GroupLayout(jPanel46);
        jPanel46.setLayout(jPanel46Layout);
        jPanel46Layout.setHorizontalGroup(
            jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );
        jPanel46Layout.setVerticalGroup(
            jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel45Layout = new javax.swing.GroupLayout(jPanel45);
        jPanel45.setLayout(jPanel45Layout);
        jPanel45Layout.setHorizontalGroup(
            jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel45Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel46, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel45Layout.setVerticalGroup(
            jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 74, Short.MAX_VALUE)
            .addGroup(jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel45Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel46, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        jPanel47.setBackground(new java.awt.Color(255, 255, 255));
        jPanel47.setPreferredSize(new java.awt.Dimension(2, 100));

        jPanel48.setBackground(new java.awt.Color(255, 255, 255));
        jPanel48.setPreferredSize(new java.awt.Dimension(2, 100));

        javax.swing.GroupLayout jPanel48Layout = new javax.swing.GroupLayout(jPanel48);
        jPanel48.setLayout(jPanel48Layout);
        jPanel48Layout.setHorizontalGroup(
            jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );
        jPanel48Layout.setVerticalGroup(
            jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 68, Short.MAX_VALUE)
        );

        jPanel49.setBackground(new java.awt.Color(255, 255, 255));
        jPanel49.setPreferredSize(new java.awt.Dimension(2, 100));

        jPanel50.setBackground(new java.awt.Color(255, 255, 255));
        jPanel50.setPreferredSize(new java.awt.Dimension(2, 100));

        javax.swing.GroupLayout jPanel50Layout = new javax.swing.GroupLayout(jPanel50);
        jPanel50.setLayout(jPanel50Layout);
        jPanel50Layout.setHorizontalGroup(
            jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );
        jPanel50Layout.setVerticalGroup(
            jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel49Layout = new javax.swing.GroupLayout(jPanel49);
        jPanel49.setLayout(jPanel49Layout);
        jPanel49Layout.setHorizontalGroup(
            jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel49Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel50, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel49Layout.setVerticalGroup(
            jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 74, Short.MAX_VALUE)
            .addGroup(jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel49Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel50, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        javax.swing.GroupLayout jPanel47Layout = new javax.swing.GroupLayout(jPanel47);
        jPanel47.setLayout(jPanel47Layout);
        jPanel47Layout.setHorizontalGroup(
            jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel47Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel48, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel47Layout.createSequentialGroup()
                    .addGap(0, 10, Short.MAX_VALUE)
                    .addComponent(jPanel49, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 10, Short.MAX_VALUE)))
        );
        jPanel47Layout.setVerticalGroup(
            jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 90, Short.MAX_VALUE)
            .addGroup(jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel47Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel48, javax.swing.GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE)
                    .addContainerGap()))
            .addGroup(jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel47Layout.createSequentialGroup()
                    .addGap(0, 8, Short.MAX_VALUE)
                    .addComponent(jPanel49, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 8, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout jPanel43Layout = new javax.swing.GroupLayout(jPanel43);
        jPanel43.setLayout(jPanel43Layout);
        jPanel43Layout.setHorizontalGroup(
            jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
            .addGroup(jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel43Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel44, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel43Layout.createSequentialGroup()
                    .addGap(0, 10, Short.MAX_VALUE)
                    .addComponent(jPanel45, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 10, Short.MAX_VALUE)))
            .addGroup(jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel43Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel47, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel43Layout.setVerticalGroup(
            jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 90, Short.MAX_VALUE)
            .addGroup(jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel43Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel44, javax.swing.GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE)
                    .addContainerGap()))
            .addGroup(jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel43Layout.createSequentialGroup()
                    .addGap(0, 8, Short.MAX_VALUE)
                    .addComponent(jPanel45, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 8, Short.MAX_VALUE)))
            .addGroup(jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel43Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel47, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jPanel6.add(jPanel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 10, 2, 90));

        getContentPane().add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, 540, 110));

        setSize(new java.awt.Dimension(789, 638));
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
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel43;
    private javax.swing.JPanel jPanel44;
    private javax.swing.JPanel jPanel45;
    private javax.swing.JPanel jPanel46;
    private javax.swing.JPanel jPanel47;
    private javax.swing.JPanel jPanel48;
    private javax.swing.JPanel jPanel49;
    private javax.swing.JPanel jPanel50;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblData;
    private javax.swing.JLabel lblData1;
    private javax.swing.JTable tbSilos;
    private javax.swing.JTextField txtDivSilo;
    private javax.swing.JTextField txtIdSilo;
    private javax.swing.JTextField txtNmSilo;
    private javax.swing.JTextField txtPesqSilo;
    private javax.swing.JTextField txtQtdAtuSilo;
    private javax.swing.JTextField txtQtdMaxSilo;
    // End of variables declaration//GEN-END:variables
}
