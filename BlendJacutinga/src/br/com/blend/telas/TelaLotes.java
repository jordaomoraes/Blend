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
import java.awt.Toolkit;
import org.json.JSONArray;
import org.json.JSONObject;

public class TelaLotes extends javax.swing.JFrame {

    Connection conexao = null;
    Connection nuvem = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    PreparedStatement pstString = null;
    ResultSet rsString = null;
    PreparedStatement pstNuvem = null;
    ResultSet rsNuvem = null;

    //Variaveis de timer(loop)
    final private Timer timer = new Timer();
    private TimerTask timer_internet, timer_tipo_cafe;
    int tempo_internet = (1000), tempo_tipo_cafe = (1000);
    String ip_servidor, string_conexao, user, password;

    //Variaveis JSON
    JSONObject Jlotetorra = new JSONObject();
    JSONArray Jtorras = new JSONArray();

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

        set_tipos_cafe();
         buscar_string_conexao();

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
    private void checa_conexao_internet() {
        if (timer_internet != null) {
            return;
        }
        timer_internet = new TimerTask() {
            @Override
            public void run() {
                //Checa conexao com INTERNET
                if (testa_url(ip_servidor)) {
                    temos_internet = true;
                } else if (!testa_url(ip_servidor)) {
                    temos_internet = false;
                }
            }
        };
        timer.scheduleAtFixedRate(timer_internet, 1, tempo_internet);
    }

    //Loop para checar se há novos tipos de café
    private void buscar_string_conexao() {
        //Busca blend atual, ultimo enviado ao plc
        String sql = "select string_conexao, ip_servidor,user,password from tb_config_torra";
        try {
            pstString = conexao.prepareStatement(sql);
            rsString = pstString.executeQuery();

            if (rsString.next()) {
                string_conexao = (rsString.getString(1));
                ip_servidor = (rsString.getString(2));
                user = (rsString.getString(3));
                password = (rsString.getString(4));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Falha ao buscar blend atual");
            System.out.println(e);
        }
    }

    private void set_tipos_cafe() {
        //Popula combobox com tipos de café
        String sql = "select marca from tb_embalagem";
        nuvem = ModuloConexaoNuvem.conector(string_conexao, user, password);
        try {
            cbLoteTipoCafe.removeAllItems();
            cbLoteTipoCafe.addItem("TIPO DO CAFÉ...");
            pstNuvem = nuvem.prepareStatement(sql);
            rsNuvem = pstNuvem.executeQuery();

            while (rsNuvem.next()) {
                String nome_tipo_cafe = rsNuvem.getString(1);
                cbLoteTipoCafe.addItem(nome_tipo_cafe);
            }
        } catch (Exception e) {
            System.out.println("Falha ao popular combobox com tipos de café da numvem " + e);
            set_tipos_cafe_off();
        }
    }

    private void set_tipos_cafe_off() {
        String sql = "select marca from tb_embalagem";

        try {
            cbLoteTipoCafe.removeAllItems();
            cbLoteTipoCafe.addItem("TIPO DO CAFÉ...");
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                String nome_tipo_cafe = rs.getString(1);
                cbLoteTipoCafe.addItem(nome_tipo_cafe);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Falha ao buscar tipos de café");
            this.dispose();
        }
    }

    //Salva no banco que há novo lote disponível, para depois notificar o usuário e sincronizar
    private void set_novo_lote_1() {
        String sql = "update tb_lote_atual set novo_lote = 1";

        try {
            pst = conexao.prepareStatement(sql);
            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println("Falha ao setar novo lote para 1");
            System.out.println(e);
        }
    }

    private void novo_lote() {
        String sql = "insert into tb_lotes_grao(nome_lote_grao, num_lote_grao, tipo_cafe_grao, obs) values(?, ?, ?, ?)";

        try {
            nome_lote = txtNomeLote.getText();
            num_lote = txtNumLote.getText();
            tipo_cafe = cbLoteTipoCafe.getSelectedItem().toString();
            obs = txtObservacao.getText();

            if ((nome_lote.isEmpty() || num_lote.isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos corretamente");
            } else if (check_float(num_lote) == false) {
                JOptionPane.showMessageDialog(null, "Insira um número válido para o lote");
            } else if (tipo_cafe == "TIPO DO CAFÉ...") {
                JOptionPane.showMessageDialog(null, "Escolha o tipo do café");
            } else {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, nome_lote);
                pst.setInt(2, Integer.parseInt(num_lote));
                pst.setString(3, tipo_cafe);
                pst.setString(4, obs);

                int lote_adicionado = pst.executeUpdate();
                set_lote_atual_grao(Integer.parseInt(num_lote), nome_lote);
                set_novo_lote_1();
                if (lote_adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Lote cadastrado com sucesso");
                } else {
                    JOptionPane.showMessageDialog(null, "Falha ao cadastrar lote");
                }
            }

        } catch (Exception e) {
            System.out.println(e + "Falha ao cadastrar lote");
        }
    }

    //Atualiza lote atal com dados do último criado
    private void set_lote_atual_grao(int num_lote, String nome_lote) {
        String sql = "update tb_lote_atual set num_lote_atual = ?, nome_lote_atual = ? where id_lote_atual = 2";

        try {
            pst = conexao.prepareStatement(sql);
            pst.setFloat(1, num_lote);
            pst.setString(2, nome_lote);
            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println("Falha ao configurar lote atual_grao " + e);
        }
    }

    private void set_lote_atual_grao_nuvem(int num_lote, String nome_lote) {
        String sql = "update tb_lote_atual set num_lote_atual = ?, nome_lote_atual = ? where id_lote_atual = 2";

        try {
            pstNuvem = nuvem.prepareStatement(sql);
            pstNuvem.setFloat(1, num_lote);
            pstNuvem.setString(2, nome_lote);
            pstNuvem.executeUpdate();
        } catch (Exception e) {
            System.out.println("Falha ao configurar lote atual_grao (nuvem) " + e);
        }
    }

    private void novo_lote_nuvem() {
        String sql = "insert into tb_lotes_grao(nome_lote_grao, num_lote_grao, tipo_cafe_grao, obs) values(?, ?, ?, ?)";

        try {
        nuvem = ModuloConexaoNuvem.conector(string_conexao,user,password);

            nome_lote = txtNomeLote.getText();
            num_lote = txtNumLote.getText();
            tipo_cafe = cbLoteTipoCafe.getSelectedItem().toString();
            obs = txtObservacao.getText();

            if ((nome_lote.isEmpty() || num_lote.isEmpty())) {
                //JOptionPane.showMessageDialog(null, "Preencha todos os campos corretamente");
            } else if (check_float(num_lote) == false) {
                //JOptionPane.showMessageDialog(null, "Insira um número válido para o lote");
            } else if (tipo_cafe == "TIPO DO CAFÉ...") {
                //JOptionPane.showMessageDialog(null, "Escolha o tipo do café");
            } else {
                pstNuvem = nuvem.prepareStatement(sql);
                pstNuvem.setString(1, nome_lote);
                pstNuvem.setInt(2, Integer.parseInt(num_lote));
                pstNuvem.setString(3, tipo_cafe);
                pstNuvem.setString(4, obs);

                int lote_adicionado = pstNuvem.executeUpdate();
                set_lote_atual_grao_nuvem(Integer.parseInt(num_lote), nome_lote);
                if (lote_adicionado > 0) {
                    System.out.println("Lote cadastrado na nuvem");
                } else {
                    JOptionPane.showMessageDialog(null, "Falha ao cadastrar lote (nuvem)");
                }
            }
        } catch (Exception e) {
            System.out.println(e + "Falha ao cadastrar lote");
        }
    }

    private void gerar_json_lote_torras_local() {
        String sql = "select idTorra from tb_torras_lote_grao";

        Jlotetorra = new JSONObject();
        Jtorras = new JSONArray();

        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                Jtorras.put(rs.getInt(1));
            }
            Jlotetorra.put("Torras", Jtorras);
            System.out.println(Jlotetorra.toString());
        } catch (Exception e) {
            System.out.println("Falha ao gerar json (local)! " + e);
        }
    }

    private void incrementa_lote_com_torras_local() {
        gerar_json_lote_torras_local();
        String sql_update = "update tb_lotes_grao set torras = ? order by id_lote_grao desc limit 1";

        try {
            pst = conexao.prepareStatement(sql_update);
            pst.setString(1, Jlotetorra.toString());
            pst.executeUpdate();

            System.out.println("DEU CERTO");
        } catch (Exception e) {
            System.out.println(e + " Falha ao incrementar ultimo lote com torras (local)");
        }
    }

    private void gerar_json_lote_torras_nuvem() {
        String sqlNuvem = "select idTorra from tb_torras_lote_grao";
      nuvem = ModuloConexaoNuvem.conector(string_conexao,user,password);

        try {
            pstNuvem = nuvem.prepareStatement(sqlNuvem);
            rsNuvem = pstNuvem.executeQuery();

            Jlotetorra = new JSONObject();
            Jtorras = new JSONArray();

            while (rsNuvem.next()) {
                Jtorras.put(rsNuvem.getInt(1));
            }
            Jlotetorra.put("Torras", Jtorras);
        } catch (Exception e) {
            System.out.println("Falha ao gerar json! " + e);
        }
    }

    private void incrementa_lote_com_torras_nuvem() {
        gerar_json_lote_torras_nuvem();
        String sql_update_nuvem = "update tb_lotes_grao set torras = ? order by id_lote_grao desc limit 1";

        try {
            pstNuvem = nuvem.prepareStatement(sql_update_nuvem);
            pstNuvem.setString(1, Jlotetorra.toString());
            pstNuvem.executeUpdate();
        } catch (Exception e) {
            System.out.println(e + " Falha ao incrementar ultimo lote com torras");
        }
    }

    //Metodo para avisar que precisa sincronizar
    private void set_sincronizar_1() {
        String sql = "update tb_modbus set SINCRONIZADO = 1 where id_modbus";

        try {
            pst = conexao.prepareStatement(sql);
            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println("Falha ao alterar variavel sincronizado para 1");
            System.out.println(e);
        }
    }

    private void truncate_torras_lote_local() {
        String sql = "delete from tb_torras_lote_grao";

        try {
            pst = conexao.prepareStatement(sql);
            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println("Falha ao truncar torras lote local " + e);
        }
    }

    private void truncate_torras_lote_nuvem() {
        String sql = "delete from tb_torras_lote_grao";

        try {
            pstNuvem = nuvem.prepareStatement(sql);
            pstNuvem.executeUpdate();
        } catch (Exception e) {
            System.out.println("Falha ao truncar torras lote local " + e);
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
        jPanel11 = new javax.swing.JPanel();
        txtNomeLote = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        cbLoteTipoCafe = new javax.swing.JComboBox<>();
        jPanel12 = new javax.swing.JPanel();
        txtNumLote = new javax.swing.JTextField();
        jPanel13 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtObservacao = new javax.swing.JTextArea();

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(250, 250, 250));
        jLabel14.setText("DIVISÃO");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Criação de lotes - Café inteiro");
        setIconImage(Toolkit.getDefaultToolkit().getImage(TelaLotes.class.getResource("/br/com/blend/icones/box.png"))
        );
        setMinimumSize(new java.awt.Dimension(562, 427));
        setPreferredSize(new java.awt.Dimension(562, 427));
        setResizable(false);
        setSize(new java.awt.Dimension(562, 427));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnLoteNovo.setBackground(new java.awt.Color(68, 141, 41));
        btnLoteNovo.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        btnLoteNovo.setForeground(new java.awt.Color(250, 250, 250));
        btnLoteNovo.setText("CRIAR LOTE");
        btnLoteNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoteNovoActionPerformed(evt);
            }
        });
        getContentPane().add(btnLoteNovo, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 370, 260, 80));

        jPanel3.setBackground(new java.awt.Color(25, 42, 86));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(250, 250, 250));
        jLabel4.setText("CONTROLE DE SILOS");
        jPanel3.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 112, -1, -1));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(250, 250, 250));
        jLabel7.setText("CRIAR LOTE PARA CAFÉ INTEIRO");
        jPanel3.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, 60));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 570, 80));

        jPanel11.setBackground(new java.awt.Color(72, 126, 176));
        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(250, 250, 250), 3), "NOME DO LOTE", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 20), new java.awt.Color(250, 250, 250))); // NOI18N
        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtNomeLote.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jPanel11.add(txtNomeLote, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, 200, 30));

        getContentPane().add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 260, 130));

        jPanel10.setBackground(new java.awt.Color(72, 126, 176));
        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(250, 250, 250), 3), "TIPO DO CAFÉ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 20), new java.awt.Color(250, 250, 250))); // NOI18N
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cbLoteTipoCafe.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbLoteTipoCafe.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "TIPO DO CAFÉ...", "Supremo", "Tradicional ", "Blah blah" }));
        jPanel10.add(cbLoteTipoCafe, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, 200, 40));

        getContentPane().add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 240, 260, 120));

        jPanel12.setBackground(new java.awt.Color(72, 126, 176));
        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(250, 250, 250), 3), "NÚMERO DO LOTE", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 20), new java.awt.Color(250, 250, 250))); // NOI18N
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtNumLote.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jPanel12.add(txtNumLote, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 60, 130, 30));

        getContentPane().add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 90, 260, 130));

        jPanel13.setBackground(new java.awt.Color(72, 126, 176));
        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(250, 250, 250), 3), "OBSERVAÇÃO", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 20), new java.awt.Color(250, 250, 250))); // NOI18N
        jPanel13.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtObservacao.setColumns(20);
        txtObservacao.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtObservacao.setRows(4);
        txtObservacao.setTabSize(10);
        txtObservacao.setMinimumSize(new java.awt.Dimension(162, 94));
        jScrollPane1.setViewportView(txtObservacao);

        jPanel13.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 220, 130));

        getContentPane().add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 240, 260, 210));

        setSize(new java.awt.Dimension(584, 507));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnLoteNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoteNovoActionPerformed
        // Cria novo lote
        int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza de que quer encerrar o lote atual?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            if (temos_internet == true) {
                incrementa_lote_com_torras_local();
                incrementa_lote_com_torras_nuvem();
                novo_lote();
                novo_lote_nuvem();
                truncate_torras_lote_local();
                truncate_torras_lote_nuvem();
            } else if (temos_internet == false) {
                incrementa_lote_com_torras_local();
                novo_lote();
                set_sincronizar_1();
                truncate_torras_lote_local();
            }
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
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txtNomeLote;
    private javax.swing.JTextField txtNumLote;
    private javax.swing.JTextArea txtObservacao;
    // End of variables declaration//GEN-END:variables
}
