package br.com.blend.telas;

import br.com.blend.dal.ModuloConexao;
import br.com.blend.dal.ModuloConexaoNuvem;
import static br.com.blend.telas.TelaPrincipal.conexaoURL;
import java.awt.TextArea;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JOptionPane;


public class TelaLotes extends javax.swing.JFrame {
    Connection conexao = null;
    Connection nuvem = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    PreparedStatement pstNuvem = null;
    ResultSet rsNuvem = null;
    
    //Variaveis de timer(loop)
    final private Timer timer = new Timer();
    private TimerTask timer_internet;
    int tempo_internet = (1000);
    
    
    //Variáveis de internet
    static URL conexaoURL;
    static URLConnection conn;
    boolean temos_internet = false;
    
    //Variaveis de lote
    String num_lote;
    String nome_lote;
    String tipo_cafe;
    String obs;
       
    
    public TelaLotes() {
        initComponents();
        conexao = ModuloConexao.conector();
        txtObservacao.setLineWrap(true);
        //Loops de checagem
        checa_conexao_internet();
        
    }
    
    
    //Metodo para checar se há internet
    public boolean testa_url(String endereco) {
        try {
            conexaoURL = new URL(endereco);
            conn = conexaoURL.openConnection();
            conn.setConnectTimeout(1000);
            conn.connect();
            temos_internet = true;
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    //Loop para testar conexao com internet
    private void checa_conexao_internet(){
        if (timer_internet != null) {
            return;
        }
        timer_internet = new TimerTask() {
            @Override
            public void run() {
                //Checa conexao com INTERNET
                if(testa_url("http://192.169.80.2")){
                    temos_internet = true;
                }
                
                else if(!testa_url("http://192.169.80.2")){
                    temos_internet = false;
                }
            }};
        timer.scheduleAtFixedRate(timer_internet, 1, tempo_internet);
    }
    
    //Salva no banco que há novo lote disponível, para depois notificar o usuário e sincronizar
    private void set_novo_lote_1(){
        String sql = "update tb_lote_atual set novo_lote = 1";
        
        try {
            pst = conexao.prepareStatement(sql);
            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println("Falha ao setar novo lote para 1");
            System.out.println(e);
        }
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
                set_novo_lote_1();
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
    
    //Metodo para avisar que precisa sincronizar
    private void set_sincronizar_1(){
        String sql = "update tb_modbus set SINCRONIZADO = 1 where id_modbus";
        
        try {
            pst = conexao.prepareStatement(sql);
            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println("Falha ao alterar variavel sincronizado para 1");
            System.out.println(e);
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

        jLabel14 = new javax.swing.JLabel();
        btnLoteNovo = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtObservacao = new javax.swing.JTextArea();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        cbLoteTipoCafe = new javax.swing.JComboBox<>();
        jPanel9 = new javax.swing.JPanel();
        txtNomeLote = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jPanel43 = new javax.swing.JPanel();
        jPanel44 = new javax.swing.JPanel();
        jPanel45 = new javax.swing.JPanel();
        jPanel46 = new javax.swing.JPanel();
        jPanel47 = new javax.swing.JPanel();
        jPanel48 = new javax.swing.JPanel();
        jPanel49 = new javax.swing.JPanel();
        jPanel50 = new javax.swing.JPanel();
        txtNumLote = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(250, 250, 250));
        jLabel14.setText("DIVISÃO");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(562, 427));
        setPreferredSize(new java.awt.Dimension(562, 427));
        setSize(new java.awt.Dimension(562, 427));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnLoteNovo.setBackground(new java.awt.Color(68, 141, 41));
        btnLoteNovo.setForeground(new java.awt.Color(250, 250, 250));
        btnLoteNovo.setText("CRIAR LOTE");
        btnLoteNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoteNovoActionPerformed(evt);
            }
        });
        getContentPane().add(btnLoteNovo, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 320, 270, 60));

        jPanel3.setBackground(new java.awt.Color(25, 42, 86));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(250, 250, 250));
        jLabel4.setText("CONTROLE DE SILOS");
        jPanel3.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 112, -1, -1));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(250, 250, 250));
        jLabel7.setText("CRIAÇÃO DE LOTES");
        jPanel3.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, -1, 60));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 560, 80));

        jPanel5.setBackground(new java.awt.Color(72, 126, 176));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(250, 250, 250), 2), "OBSERVAÇÃO", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(250, 250, 250))); // NOI18N
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel6.setBackground(new java.awt.Color(72, 126, 176));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel5.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 210, 210, 100));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(250, 250, 250));
        jLabel9.setText("OBSERVAÇÃO");
        jPanel5.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, -1, -1));

        txtObservacao.setColumns(20);
        txtObservacao.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtObservacao.setRows(4);
        txtObservacao.setTabSize(10);
        txtObservacao.setMinimumSize(new java.awt.Dimension(162, 94));
        jScrollPane1.setViewportView(txtObservacao);

        jPanel5.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 220, 110));

        getContentPane().add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, 260, 180));

        jPanel7.setBackground(new java.awt.Color(72, 126, 176));
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(250, 250, 250), 2), "TIPO DO CAFÉ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(250, 250, 250))); // NOI18N
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel8.setBackground(new java.awt.Color(72, 126, 176));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel7.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 210, 210, 100));

        cbLoteTipoCafe.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbLoteTipoCafe.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "TIPO DO CAFÉ...", "Supremo", "Tradicional ", "Blah blah" }));
        jPanel7.add(cbLoteTipoCafe, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, 150, 31));

        getContentPane().add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 200, 270, 110));

        jPanel9.setBackground(new java.awt.Color(72, 126, 176));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtNomeLote.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jPanel9.add(txtNomeLote, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, 160, 31));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(250, 250, 250));
        jLabel10.setText("NOME DO LOTE");
        jPanel9.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, -1, -1));

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
                    .addGap(0, 15, Short.MAX_VALUE)
                    .addComponent(jPanel47, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 15, Short.MAX_VALUE)))
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

        jPanel9.add(jPanel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 3, -1, 90));

        txtNumLote.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jPanel9.add(txtNumLote, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 50, 130, 31));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(250, 250, 250));
        jLabel8.setText("NÚEMRO DO LOTE");
        jPanel9.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 10, -1, -1));

        getContentPane().add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 540, 100));

        setSize(new java.awt.Dimension(577, 435));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnLoteNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoteNovoActionPerformed
        // Cria novo lote
        if(temos_internet == true){
            novo_lote();
            novo_lote_nuvem();
        }
        else if(temos_internet == false){
            novo_lote();
            set_sincronizar_1();
        }
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
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel43;
    private javax.swing.JPanel jPanel44;
    private javax.swing.JPanel jPanel45;
    private javax.swing.JPanel jPanel46;
    private javax.swing.JPanel jPanel47;
    private javax.swing.JPanel jPanel48;
    private javax.swing.JPanel jPanel49;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel50;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txtNomeLote;
    private javax.swing.JTextField txtNumLote;
    private javax.swing.JTextArea txtObservacao;
    // End of variables declaration//GEN-END:variables
}
