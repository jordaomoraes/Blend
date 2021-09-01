package br.com.blend.telas;

import java.sql.*;
import br.com.blend.dal.ModuloConexao;
import java.awt.*;
import java.awt.Toolkit;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class TelaModbus extends javax.swing.JFrame {
    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public TelaModbus() {
        initComponents();
        conexao = ModuloConexao.conector();
    }
    
    private void carregar_campos(){
        String sql="select * from tb_modbus_blend";
        
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            
            if(rs.next()){
                txtValorSilo1.setText(rs.getString(2));
                txtValorSilo2.setText(rs.getString(3));
                txtValorSilo3.setText(rs.getString(4));
                txtValorSilo4.setText(rs.getString(5));
                txtAccMexedor.setText(rs.getString(6));
                txtFator.setText(rs.getString(7));
                txtLastroSecur.setText(rs.getString(8));
                txtPeso.setText(rs.getString(9));
                txtLiga.setText(rs.getString(10));
                txtSilo1.setText(rs.getString(11));
                txtSilo2.setText(rs.getString(12));
                txtSilo3.setText(rs.getString(13));
                txtSilo4.setText(rs.getString(14));
                txtEsteira.setText(rs.getString(15));
                txtSenCafeMist.setText(rs.getString(16));
                txtMexedor.setText(rs.getString(17));
                txtPistMex.setText(rs.getString(18));
                txtEmergencia.setText(rs.getString(19));
                txtBlend.setText(rs.getString(20));
                txtManual1.setText(rs.getString(21));
                txtManual2.setText(rs.getString(22));
                txtManual3.setText(rs.getString(23));
                txtManual4.setText(rs.getString(24));
                txtTarar.setText(rs.getString(25));
                txtBlendado.setText(rs.getString(26));
                
            }
            else{
                JOptionPane.showMessageDialog(null, "Erro ao exibir dados");
            }
            
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    
    private void editar(){
         int confirma = JOptionPane.showConfirmDialog(null,"Tem certeza de que quer atualizar os dados do ModBus?","Atenção",JOptionPane.YES_NO_OPTION);
         
        if(confirma == JOptionPane.YES_OPTION){
             String sql = "update tb_modbus set VALOR_SILO_1=?,VALOR_SILO_2=?,VALOR_SILO_3=?,VALOR_SILO_4=?,ACC_MEXEDOR=?, FATOR=?,LASTRO_SECUR=?,PESO=?,LIGA=?,SILO_1=?,SILO_2=?,SILO_3=?,SILO_4=?,ESTEIRA=?,SEN_CAFE_MIST=?, MEXEDOR=?,PISTAO_MEXEDOR=?,EMERGENCIA=?,BLEND=?,MANUAL_01=?,MANUAL_02=?,MANUAL_03=?,MANUAL_04=?,TARAR=?,BLENDADO=? where ID_MODBUS=1";
        
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1,txtValorSilo1.getText());
                pst.setString(2,txtValorSilo2.getText());
                pst.setString(3,txtValorSilo3.getText());
                pst.setString(4,txtValorSilo4.getText());
                pst.setString(5,txtAccMexedor.getText());
                pst.setString(6,txtFator.getText());
                pst.setString(7,txtLastroSecur.getText());
                pst.setString(8,txtPeso.getText());
                pst.setString(9,txtLiga.getText());
                pst.setString(10,txtSilo1.getText());
                pst.setString(11,txtSilo2.getText());
                pst.setString(12,txtSilo3.getText());
                pst.setString(13,txtSilo4.getText());
                pst.setString(14,txtEsteira.getText());
                pst.setString(15,txtSenCafeMist.getText());
                pst.setString(16,txtMexedor.getText());
                pst.setString(17,txtPistMex.getText());
                pst.setString(18,txtEmergencia.getText());
                pst.setString(19,txtBlend.getText());
                pst.setString(20,txtManual1.getText());
                pst.setString(21,txtManual2.getText());
                pst.setString(22,txtManual3.getText());
                pst.setString(23,txtManual4.getText());
                pst.setString(24,txtTarar.getText());
                pst.setString(25,txtBlendado.getText());

                if(txtValorSilo1.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios");
                }
                else{
                    int adicionado = pst.executeUpdate();

                    if(adicionado > 0){
                        JOptionPane.showMessageDialog(null, "Dados atualizados com sucesso!");
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "Erro ao atualizar ModBus!");
                    }
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
       }
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel5 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtValorSilo2 = new javax.swing.JTextField();
        txtValorSilo1 = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        txtValorSilo3 = new javax.swing.JTextField();
        txtValorSilo4 = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        txtLiga = new javax.swing.JTextField();
        txtPeso = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        txtLastroSecur = new javax.swing.JTextField();
        txtFator = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        txtAccMexedor = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        txtSenCafeMist = new javax.swing.JTextField();
        txtEsteira = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        txtBlendado = new javax.swing.JTextField();
        txtTarar = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        txtMexedor = new javax.swing.JTextField();
        txtPistMex = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        txtEmergencia = new javax.swing.JTextField();
        txtBlend = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        txtSilo1 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        txtSilo2 = new javax.swing.JTextField();
        txtSilo3 = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        txtSilo4 = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        txtManual4 = new javax.swing.JTextField();
        txtManual3 = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        txtManual2 = new javax.swing.JTextField();
        txtManual1 = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        btnModbusEditar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Modbus");
        setBackground(new java.awt.Color(17, 29, 48));
        setIconImage(Toolkit.getDefaultToolkit().getImage(TelaModbus.class.getResource("/br/com/blend/icones/modbus.png"))
        );
        setMinimumSize(new java.awt.Dimension(1360, 710));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 102, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(2031, 414, -1, -1));

        jPanel8.setBackground(new java.awt.Color(25, 42, 86));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(250, 250, 250));
        jLabel2.setText("CONTROLE DE SILOS");
        jPanel8.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 112, -1, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(250, 250, 250));
        jLabel3.setText("CONTROLE DE SILOS");
        jPanel8.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 112, -1, -1));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(250, 250, 250));
        jLabel1.setText("ENDEREÇAMENTO DO CLP");
        jPanel8.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        getContentPane().add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1410, 80));

        jPanel1.setBackground(new java.awt.Color(72, 126, 176));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(250, 250, 250), 3), "VALOR RECEITA SILOS", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 18), new java.awt.Color(250, 250, 250))); // NOI18N
        jPanel1.setForeground(new java.awt.Color(250, 250, 250));
        jPanel1.setPreferredSize(new java.awt.Dimension(360, 260));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(250, 250, 250));
        jLabel13.setText("VALOR SILO 1");
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 50, -1, 30));

        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(250, 250, 250));
        jLabel16.setText("VALOR SILO 2");
        jPanel1.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 100, -1, 30));
        jPanel1.add(txtValorSilo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 100, 140, 31));
        jPanel1.add(txtValorSilo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 50, 140, 31));

        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(250, 250, 250));
        jLabel17.setText("VALOR SILO 3");
        jPanel1.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 150, -1, 30));
        jPanel1.add(txtValorSilo3, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 150, 140, 31));
        jPanel1.add(txtValorSilo4, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 200, 140, 31));

        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(250, 250, 250));
        jLabel18.setText("VALOR SILO 4");
        jPanel1.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 200, -1, 30));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 381, 300));

        jPanel14.setBackground(new java.awt.Color(72, 126, 176));
        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(250, 250, 250), 3), "OUTROS", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 18), new java.awt.Color(250, 250, 250))); // NOI18N
        jPanel14.setForeground(new java.awt.Color(250, 250, 250));
        jPanel14.setPreferredSize(new java.awt.Dimension(360, 260));
        jPanel14.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel15.setBackground(new java.awt.Color(72, 126, 176));
        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(250, 250, 250), 3), "MANUAL", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 18), new java.awt.Color(250, 250, 250))); // NOI18N
        jPanel15.setForeground(new java.awt.Color(250, 250, 250));
        jPanel15.setPreferredSize(new java.awt.Dimension(360, 260));
        jPanel15.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel14.add(jPanel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 470, 381, 220));

        jPanel16.setBackground(new java.awt.Color(72, 126, 176));
        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(250, 250, 250), 3), "MANUAL", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 18), new java.awt.Color(250, 250, 250))); // NOI18N
        jPanel16.setForeground(new java.awt.Color(250, 250, 250));
        jPanel16.setPreferredSize(new java.awt.Dimension(360, 260));
        jPanel16.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel17.setBackground(new java.awt.Color(72, 126, 176));
        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(250, 250, 250), 3), "MANUAL", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 18), new java.awt.Color(250, 250, 250))); // NOI18N
        jPanel17.setForeground(new java.awt.Color(250, 250, 250));
        jPanel17.setPreferredSize(new java.awt.Dimension(360, 260));
        jPanel17.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel16.add(jPanel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 470, 381, 220));

        jPanel14.add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 470, 381, 220));

        jLabel28.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(250, 250, 250));
        jLabel28.setText("LIGA");
        jPanel14.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 240, -1, 30));
        jPanel14.add(txtLiga, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 240, 140, 31));
        jPanel14.add(txtPeso, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 190, 140, 31));

        jLabel27.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(250, 250, 250));
        jLabel27.setText("PESO");
        jPanel14.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 190, -1, 30));

        jLabel26.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(250, 250, 250));
        jLabel26.setText("LASTRO_SECUR");
        jPanel14.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, -1, 30));
        jPanel14.add(txtLastroSecur, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 140, 140, 31));
        jPanel14.add(txtFator, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 90, 140, 31));

        jLabel25.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(250, 250, 250));
        jLabel25.setText("FATOR");
        jPanel14.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 90, -1, 30));

        jLabel20.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(250, 250, 250));
        jLabel20.setText("ACC MEXEDOR");
        jPanel14.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, -1, 30));
        jPanel14.add(txtAccMexedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 40, 140, 31));

        jLabel36.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(250, 250, 250));
        jLabel36.setText("SEN CAFÉ MIST");
        jPanel14.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 190, -1, 30));
        jPanel14.add(txtSenCafeMist, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 190, 140, 31));
        jPanel14.add(txtEsteira, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 140, 140, 31));

        jLabel35.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(250, 250, 250));
        jLabel35.setText("ESTEIRA");
        jPanel14.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 140, -1, 30));

        jLabel34.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(250, 250, 250));
        jLabel34.setText("BLENDADO");
        jPanel14.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 90, -1, 30));
        jPanel14.add(txtBlendado, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 90, 140, 31));
        jPanel14.add(txtTarar, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 40, 140, 31));

        jLabel33.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(250, 250, 250));
        jLabel33.setText("TARAR");
        jPanel14.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 40, -1, 30));

        jLabel37.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(250, 250, 250));
        jLabel37.setText("MISTURADOR");
        jPanel14.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 190, -1, 30));
        jPanel14.add(txtMexedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 190, 140, 31));
        jPanel14.add(txtPistMex, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 240, 140, 31));

        jLabel38.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(250, 250, 250));
        jLabel38.setText("SAÍDA MISTURADOR");
        jPanel14.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 240, -1, 30));

        jLabel39.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(250, 250, 250));
        jLabel39.setText("EMERGÊNCIA");
        jPanel14.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 90, -1, 30));
        jPanel14.add(txtEmergencia, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 90, 140, 31));
        jPanel14.add(txtBlend, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 140, 140, 31));

        jLabel40.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(250, 250, 250));
        jLabel40.setText("BLEND");
        jPanel14.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 140, -1, 30));

        getContentPane().add(jPanel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 100, 950, 300));

        jPanel9.setBackground(new java.awt.Color(72, 126, 176));
        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(250, 250, 250), 3), "SILOS ABRIR MANUAL", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 18), new java.awt.Color(250, 250, 250))); // NOI18N
        jPanel9.setForeground(new java.awt.Color(250, 250, 250));
        jPanel9.setPreferredSize(new java.awt.Dimension(360, 260));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel9.add(txtSilo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 50, 140, 31));

        jLabel19.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(250, 250, 250));
        jLabel19.setText("SILO 1");
        jPanel9.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 50, -1, 30));

        jLabel21.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(250, 250, 250));
        jLabel21.setText("SILO 2");
        jPanel9.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 100, -1, 30));
        jPanel9.add(txtSilo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 100, 140, 31));
        jPanel9.add(txtSilo3, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 150, 140, 31));

        jLabel22.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(250, 250, 250));
        jLabel22.setText("SILO 3");
        jPanel9.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 150, -1, 30));

        jLabel23.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(250, 250, 250));
        jLabel23.setText("SILO 4");
        jPanel9.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 200, -1, 30));
        jPanel9.add(txtSilo4, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 200, 140, 31));

        getContentPane().add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 420, 380, 272));

        jPanel10.setBackground(new java.awt.Color(72, 126, 176));
        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(250, 250, 250), 3), "MANUAL", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 18), new java.awt.Color(250, 250, 250))); // NOI18N
        jPanel10.setForeground(new java.awt.Color(250, 250, 250));
        jPanel10.setPreferredSize(new java.awt.Dimension(360, 260));
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel11.setBackground(new java.awt.Color(72, 126, 176));
        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(250, 250, 250), 3), "MANUAL", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 18), new java.awt.Color(250, 250, 250))); // NOI18N
        jPanel11.setForeground(new java.awt.Color(250, 250, 250));
        jPanel11.setPreferredSize(new java.awt.Dimension(360, 260));
        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel10.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 470, 381, 220));

        jPanel12.setBackground(new java.awt.Color(72, 126, 176));
        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(250, 250, 250), 3), "MANUAL", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 18), new java.awt.Color(250, 250, 250))); // NOI18N
        jPanel12.setForeground(new java.awt.Color(250, 250, 250));
        jPanel12.setPreferredSize(new java.awt.Dimension(360, 260));
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel13.setBackground(new java.awt.Color(72, 126, 176));
        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(250, 250, 250), 3), "MANUAL", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 18), new java.awt.Color(250, 250, 250))); // NOI18N
        jPanel13.setForeground(new java.awt.Color(250, 250, 250));
        jPanel13.setPreferredSize(new java.awt.Dimension(360, 260));
        jPanel13.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel12.add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 470, 381, 220));

        jPanel10.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 470, 381, 220));

        jLabel32.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(250, 250, 250));
        jLabel32.setText("MANUAL 4");
        jPanel10.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 200, -1, 30));
        jPanel10.add(txtManual4, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 200, 140, 31));
        jPanel10.add(txtManual3, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 150, 140, 31));

        jLabel31.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(250, 250, 250));
        jLabel31.setText("MANUAL 3");
        jPanel10.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 150, -1, 30));

        jLabel30.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(250, 250, 250));
        jLabel30.setText("MANUAL 2");
        jPanel10.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 100, -1, 30));
        jPanel10.add(txtManual2, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 100, 140, 31));
        jPanel10.add(txtManual1, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 50, 140, 31));

        jLabel29.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(250, 250, 250));
        jLabel29.setText("MANUAL 1");
        jPanel10.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 50, -1, 30));

        getContentPane().add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 420, 381, 270));

        btnModbusEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/blend/icones/save_48px.png"))); // NOI18N
        btnModbusEditar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnModbusEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModbusEditarActionPerformed(evt);
            }
        });
        getContentPane().add(btnModbusEditar, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 420, 100, 100));
        btnModbusEditar.getAccessibleContext().setAccessibleName("Editar");
        btnModbusEditar.getAccessibleContext().setAccessibleDescription("Editar");

        setSize(new java.awt.Dimension(1429, 811));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // Chama função de carregar campos
        carregar_campos();
    }//GEN-LAST:event_formWindowActivated

    private void btnModbusEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModbusEditarActionPerformed
        // Chama função de editar
        editar();
    }//GEN-LAST:event_btnModbusEditarActionPerformed

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
            java.util.logging.Logger.getLogger(TelaModbus.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaModbus.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaModbus.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaModbus.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaModbus().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnModbusEditar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JTextField txtAccMexedor;
    private javax.swing.JTextField txtBlend;
    private javax.swing.JTextField txtBlendado;
    private javax.swing.JTextField txtEmergencia;
    private javax.swing.JTextField txtEsteira;
    private javax.swing.JTextField txtFator;
    private javax.swing.JTextField txtLastroSecur;
    private javax.swing.JTextField txtLiga;
    private javax.swing.JTextField txtManual1;
    private javax.swing.JTextField txtManual2;
    private javax.swing.JTextField txtManual3;
    private javax.swing.JTextField txtManual4;
    private javax.swing.JTextField txtMexedor;
    private javax.swing.JTextField txtPeso;
    private javax.swing.JTextField txtPistMex;
    private javax.swing.JTextField txtSenCafeMist;
    private javax.swing.JTextField txtSilo1;
    private javax.swing.JTextField txtSilo2;
    private javax.swing.JTextField txtSilo3;
    private javax.swing.JTextField txtSilo4;
    private javax.swing.JTextField txtTarar;
    private javax.swing.JTextField txtValorSilo1;
    private javax.swing.JTextField txtValorSilo2;
    private javax.swing.JTextField txtValorSilo3;
    private javax.swing.JTextField txtValorSilo4;
    // End of variables declaration//GEN-END:variables
}
