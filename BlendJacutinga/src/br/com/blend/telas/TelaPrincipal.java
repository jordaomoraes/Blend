package br.com.blend.telas;

import br.com.blend.dal.ModuloConexao;
import br.com.blend.dal.modbus;
import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusProtocolException;
import com.intelligt.modbus.jlibmodbus.master.ModbusMasterFactory;
import com.intelligt.modbus.jlibmodbus.master.ModbusMaster;
import com.intelligt.modbus.jlibmodbus.slave.ModbusSlave;
import com.intelligt.modbus.jlibmodbus.slave.ModbusSlaveFactory;
import com.intelligt.modbus.jlibmodbus.tcp.TcpParameters;
import java.awt.*;
import java.io.PrintStream;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.net.InetAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JList;
import java.io.IOException;
import java.io.OutputStream;
import javax.swing.JTextArea;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import net.proteanit.sql.DbUtils;


public class TelaPrincipal extends javax.swing.JFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    String Metodo;
    
    int i = 0;
    
    //Variaveis Blend
    String IdBlend, NomeBlend, QtdSilo1, QtdSilo2, QtdSilo3, QtdSilo4;
    float QtdTotal = 0;
    
    //Variaveis estoque
    float Qtd_estoque_s1 = 0, Qtd_estoque_s2 = 0, Qtd_estoque_s3 = 0, Qtd_estoque_s4 = 0;
    float Qtd_estoque = 0;
    float Qtd_estoque_att = 0;
    float []array_silos;
    
    //Variaveis status (Caixa mensagens)
    StyledDocument caixa_mensagens;
    SimpleAttributeSet cor_mensagem_erro = new SimpleAttributeSet();
    SimpleAttributeSet cor_tentando_conectar = new SimpleAttributeSet();
    SimpleAttributeSet cor_conectado = new SimpleAttributeSet();
    SimpleAttributeSet cor_novo_processo = new SimpleAttributeSet();
    SimpleAttributeSet cor_sincronizar = new SimpleAttributeSet();

    //Variaveis ModBus
    String VALOR_SILO_1, VALOR_SILO_2, VALOR_SILO_3, VALOR_SILO_4, ACC_MEXEDOR, FATOR, LASTRO_SECUR, PESO, LIGA, SILO_1, SILO_2, SILO_3, SILO_4, ESTEIRA, SEN_CAFE_MIST, MEXEDOR, PISTAO_MEXEDOR, EMERGENCIA, BLEND, MANUAL_01, MANUAL_02, MANUAL_03, MANUAL_04, TARAR, BLENDADO;
    
    //Variaveis para conectar com PLC
    String host = "127.0.0.1";
    int escravo = 1;
    boolean clp_conectado = false;

    Boolean status_conexao;
    
    TcpParameters tcpParameters = new TcpParameters();
    ModbusMaster m = ModbusMasterFactory.createModbusMasterTCP(tcpParameters);
    ModbusSlave slave = ModbusSlaveFactory.createModbusSlaveTCP(tcpParameters);

    public TelaPrincipal() {
        initComponents();
        conexao = ModuloConexao.conector();
        buscar_blend_atual();
        caixa_mensagens = txtStatus.getStyledDocument();
        define_cores_mensagens();
        
        if(clp_conectado == false){
            try {
            //Conecta com clp
            conecta_com_clp();
            clp_conectado = true;
            } catch (ModbusProtocolException ex) {
                Logger.getLogger(TelaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ModbusNumberException ex) {
                Logger.getLogger(TelaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else{
            return;
        }
    }
    
    
    private void conecta_com_clp() throws ModbusProtocolException, ModbusNumberException {
        try {
            tcpParameters.setHost(InetAddress.getByName(host));
            tcpParameters.setPort(Modbus.TCP_PORT);

            Modbus.setAutoIncrementTransactionId(true);

            try {
                if (!m.isConnected()) {
                    m.connect();
                }
                try {
                    System.out.println("conectou");
                    caixa_mensagens.insertString(caixa_mensagens.getLength(), "Conexão com CLP iniciada! " , cor_conectado);
                } catch (Exception e) {
                     System.out.println("Erro ao exibir mensagem" + e);
                }
            } catch (ModbusIOException e) {
                clp_conectado = false;
                System.out.println("Erro ao se conectar com clp" + e);
            } finally {
                try {
                    m.disconnect();
                } catch (ModbusIOException e) {
                    System.out.println("Erro ao se desconectar com clp" + e);
                }
            }
        } catch (RuntimeException e) {
            clp_conectado = false;
            throw (e);
        } catch (UnknownHostException e) {
            clp_conectado = false;
            System.out.println("Erro "+e);;
        }
    }
    
    
    private void buscar_modbus() {
        String sql = "select * from tb_modbus";

        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();

            if (rs.next()) {
                VALOR_SILO_1 = rs.getString(2);
                VALOR_SILO_2 = rs.getString(3);
                VALOR_SILO_3 = rs.getString(4);
                VALOR_SILO_4 = rs.getString(5);
                ACC_MEXEDOR = rs.getString(6);
                FATOR = rs.getString(7);
                LASTRO_SECUR = rs.getString(8);
                PESO = rs.getString(9);
                LIGA = rs.getString(10);
                SILO_1 = rs.getString(11);
                SILO_2 = rs.getString(12);
                SILO_3 = rs.getString(13);
                SILO_4 = rs.getString(14);
                ESTEIRA = rs.getString(15);
                SEN_CAFE_MIST = rs.getString(16);
                MEXEDOR = rs.getString(17);
                PISTAO_MEXEDOR = rs.getString(18);
                EMERGENCIA = rs.getString(19);
                BLEND = rs.getString(20);
                MANUAL_01 = rs.getString(21);
                MANUAL_02 = rs.getString(22);
                MANUAL_03 = rs.getString(23);
                TARAR = rs.getString(24);
                BLENDADO = rs.getString(25);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void define_cores_mensagens() {
        StyleConstants.setForeground(cor_mensagem_erro, new Color(205, 0, 0));

        StyleConstants.setForeground(cor_tentando_conectar, new Color(255, 140, 0));

        StyleConstants.setForeground(cor_conectado, new Color(34, 139, 34));

        StyleConstants.setForeground(cor_novo_processo, new Color(25, 25, 112));

        StyleConstants.setForeground(cor_sincronizar, new Color(148, 0, 211));

        StyleConstants.setBold(cor_conectado, true);
    }
    
    
    private void limpa_mensagens(){
        try {
            for(int i=0; i<=50; i++){
                caixa_mensagens.remove(i, caixa_mensagens.getLength());
            }
        } catch (BadLocationException ex) {
            System.out.println("Falha ao limpar console!");;
        }
    }
    
    //Funções referentes ao Blend
    
    private void buscar_blend_atual(){
        //Busca blend atual, ultimo enviado ao plc
        String sql = "select id_blend_atual, nome, qtd_silo1, qtd_silo2, qtd_silo3, qtd_silo4 from tb_blend_atual";
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            
            if(rs.next()){
                txtIdBlend.setText(rs.getString(1));
                txtNomeBlend.setText(rs.getString(2));
                txtQtdSilo1.setText(rs.getString(3));
                txtQtdSilo2.setText(rs.getString(4));
                txtQtdSilo3.setText(rs.getString(5));
                txtQtdSilo4.setText(rs.getString(6));
            }
            else{
                caixa_mensagens.insertString(caixa_mensagens.getLength(), "Blend Atual ainda não cadastrado!" , cor_mensagem_erro);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
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
        //lblNomeBlend.setText(tbBlend.getModel().getValueAt(setar,1).toString());
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
        
        tbBlend.getColumnModel().getColumn(0).setHeaderValue("Nome");
        tbBlend.getColumnModel().getColumn(1).setHeaderValue("Quantidade Silo 1");
        tbBlend.getColumnModel().getColumn(2).setHeaderValue("Quantidade Silo 2");
        tbBlend.getColumnModel().getColumn(3).setHeaderValue("Quantidade Silo 3");
        tbBlend.getColumnModel().getColumn(4).setHeaderValue("Quantidade Silo 4");
        
        tbBlend.getTableHeader().repaint();
    }
    
    
    private void block_campos(){
        Metodo = null;
        btnBlendAdd.setEnabled(true);
        btnBlendAdd.setBackground(new Color(255,255,255));
        btnBlendEditar.setBackground(new Color(255,255,255));
        btnBlendEditar.setEnabled(true);
        btnBlendDeletar.setEnabled(false);
        btnBlendAtual.setEnabled(true);
        cbBlendOperacao.setEnabled(true);
        btnBlendEnviar.setEnabled(true);
        txtStatus.setEditable(false);
        txtIdBlend.setVisible(true);
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
    
    private void limpa_campos(){
        txtIdBlend.setText(null);
        txtNomeBlend.setText(null);
        txtQtdSilo1.setText(null);
        txtQtdSilo2.setText(null);
        txtQtdSilo3.setText(null);
        txtQtdSilo4.setText(null);
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
                buscar_blend_atual();
            }
            else if((check_float(QtdSilo1) == false || check_float(QtdSilo2) == false || check_float(QtdSilo3) == false || check_float(QtdSilo4) == false)){
                JOptionPane.showMessageDialog(null, "Insira um valorválido para a Quantidade!");
                buscar_blend_atual();
            }
            else{
                int adicionado = pst.executeUpdate();
                if(adicionado > 0 ){
                    JOptionPane.showMessageDialog(null, "Blend cadastrado com sucesso!");
                    //limpa_campos();
                    buscar_blend_atual();
                }
                else{
                    JOptionPane.showMessageDialog(null, "Falha ao cadastrar Blend!");
                    buscar_blend_atual();
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
                //limpa_campos();
                buscar_blend_atual();
            }
            else if((check_float(QtdSilo1) == false || check_float(QtdSilo2) == false || check_float(QtdSilo3) == false || check_float(QtdSilo4) == false)){
                JOptionPane.showMessageDialog(null, "Insira um valorválido para a Quantidade!");
                buscar_blend_atual();
            }
            else{
                int adicionado = pst.executeUpdate();
                if(adicionado > 0 ){
                    JOptionPane.showMessageDialog(null, "Blend editado com sucesso!");
                    buscar_blend();
                }
                else{
                    JOptionPane.showMessageDialog(null, "Falha ao editar blend!");
                    buscar_blend_atual();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    
    private void remover_blend(){
        String sql = "delete from tb_blend where id_blend=?";

        int confirma = JOptionPane.showConfirmDialog(null,"Tem certeza de que quer deletar o Blend?","Atenção",JOptionPane.YES_NO_OPTION);

        if(confirma == JOptionPane.YES_OPTION ){
            
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1,txtIdBlend.getText());
            int apagado = pst.executeUpdate();
                
            if(apagado>0){
                JOptionPane.showMessageDialog(null, "Blend deletado com sucesso!");
                //limpa_campos();
                buscar_blend_atual();
            }
            else{
                JOptionPane.showMessageDialog(null, "Falha ao deletar Blend!");
            }
                                 
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
    
    
    private void set_blend_atual(){
        //Atualiza/sobrescreve dados da tabela blend atual, para manter o sistema sincronizado com os dados enviados ao PLC
        
        String sql = "update tb_blend_atual set id_blend_atual=?, nome=?, qtd_silo1=?, qtd_silo2=?, qtd_silo3=?, qtd_silo4=? where id_blend_atual";
        
        try {
            pst = conexao.prepareStatement(sql);
            
            IdBlend = txtIdBlend.getText();
            NomeBlend = txtNomeBlend.getText();
            QtdSilo1 = txtQtdSilo1.getText();
            QtdSilo2 = txtQtdSilo2.getText();
            QtdSilo3 = txtQtdSilo3.getText();
            QtdSilo4 = txtQtdSilo4.getText();
            
            pst.setString(1,IdBlend);
            pst.setString(2,NomeBlend);
            pst.setFloat(3,Float.parseFloat(QtdSilo1));
            pst.setFloat(4,Float.parseFloat(QtdSilo2));
            pst.setFloat(5,Float.parseFloat(QtdSilo3));
            pst.setFloat(6,Float.parseFloat(QtdSilo4));
            
            pst.executeUpdate();
            
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    
    private void salvar_registro_blend(){
        String sql="insert into tb_blend_registros(nome, fk_silo1, qtd_silo1, fk_silo2, qtd_silo2, fk_silo3, qtd_silo3, fk_silo4, qtd_silo4, operacao) values (?,1,?,2,?,3,?,4,?, ?)";
        
        try {
                pst = conexao.prepareStatement(sql);
            
                NomeBlend = txtNomeBlend.getText();
                QtdSilo1 = txtQtdSilo1.getText();
                QtdSilo2 = txtQtdSilo2.getText();
                QtdSilo3 = txtQtdSilo3.getText();
                QtdSilo4 = txtQtdSilo4.getText();
                String Moer = "moer e empacotar";
                String Grao = "empacotar inteiro";

                pst.setString(1,NomeBlend);
                pst.setFloat(2,Float.parseFloat(QtdSilo1));
                pst.setFloat(3,Float.parseFloat(QtdSilo2));
                pst.setFloat(4,Float.parseFloat(QtdSilo3));
                pst.setFloat(5,Float.parseFloat(QtdSilo4));
            
                if(cbBlendOperacao.getSelectedIndex() == 1){
                    pst.setString(6,Moer);
                }
                else if(cbBlendOperacao.getSelectedIndex() == 2){
                    pst.setString(6,Grao);
                }
                
                pst.executeUpdate();
                
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    private void consultar_estoque_silos(){
        String sql = "select qtd_atual from tb_silos";
        array_silos = new float[5];       
        int i = 0;
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            
            while (rs.next()) {                
                array_silos[i] = (rs.getFloat(1));
                i++;
            }
            //System.out.println(array_silos[1]);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    
    private void atualizar_estoque_silos(float[] qtd_silos){
        consultar_estoque_silos();
        String sql = "update tb_silos set qtd_atual=? where id_silo=?";
        float value = 0;
        for (int i = 0; i <= 3; i++) {
            try {
                value = array_silos[i];
                pst = conexao.prepareStatement(sql);
                
                pst.setFloat(1, array_silos[i] - qtd_silos[i]);
                pst.setInt(2, i + 1);
                
                //System.out.println(value);
                pst.executeUpdate();
            } catch (Exception e) {
                System.out.println(e);         
            }
        }
    }
    
    
    private void consultar_estoque_grao(){
          String sql = "select quantidade from tb_cafe_grao";
          
          try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            
            if(rs.next()){
                Qtd_estoque = rs.getFloat(1);
            }
            else{
                JOptionPane.showMessageDialog(null, "Nenhum café no estoque");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    
    private void consultar_estoque_moido(){
        String sql = "select quantidade from tb_cafe_moido";
          
          try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            
            if(rs.next()){
                Qtd_estoque = rs.getFloat(1);
            }
            else{
                JOptionPane.showMessageDialog(null, "Nenhum café no estoque");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    
    private void atualizar_estoque_grao(float QtdTotal){
        consultar_estoque_grao();
        String sql = "update tb_cafe_grao set quantidade=? where id_cafe_grao=1";
        try {
                pst = conexao.prepareStatement(sql);
                
                Qtd_estoque_att = Qtd_estoque + QtdTotal;
                
                //System.out.println(Qtd_estoque_att);
                pst.setFloat(1, Qtd_estoque_att);
                pst.executeUpdate();
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
    }
    
    
    private void atualizar_estoque_moido(float QtdTotal){
        consultar_estoque_moido();
        String sql = "update tb_cafe_moido set quantidade=? where id_cafe_moido=1";
        
        try {
                pst = conexao.prepareStatement(sql);
                
                Qtd_estoque_att = Qtd_estoque + QtdTotal;
                
                //System.out.println(Qtd_estoque_att);
                pst.setFloat(1, Qtd_estoque_att);
                pst.executeUpdate();
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
    }
   
    
    private boolean checa_silos(float []qtd_silos){
        consultar_estoque_silos();
        int sem_estoque[] = new int[4];
        limpa_mensagens();
        for(int i = 0; i <= 3; i++){
            if(array_silos[i]<qtd_silos[i]){
                sem_estoque[i] = i+1;
                System.out.println("Silo "+ sem_estoque[i]+ " com estoque insuficiente");
                try {
                    caixa_mensagens.insertString(caixa_mensagens.getLength(), "\nSilo "+String.valueOf(sem_estoque[i])+" com estoque insuficiente", cor_mensagem_erro);
                } catch (BadLocationException ex) {
                    Logger.getLogger(TelaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        for(int i=0; i<=3; i++){
            if(sem_estoque[i]!=0){
                JOptionPane.showMessageDialog(null, "Quantidades de café no silo insifuciente!");
                return false;
            }
        }
        
        return true;
    }
    
    
    private void enviar_para_clp(){
        // Envia dados do blend para o PLC
        try {
            // 1 - Pega valores de quantidade de cada silo
            VALOR_SILO_1 = txtQtdSilo1.getText();
            VALOR_SILO_2 = txtQtdSilo2.getText();
            VALOR_SILO_3 = txtQtdSilo3.getText();
            VALOR_SILO_4 = txtQtdSilo4.getText();
            
            // 2 - Gera uma quantidade total de café
            QtdTotal = Float.parseFloat(VALOR_SILO_1) + Float.parseFloat(VALOR_SILO_2) + Float.parseFloat(VALOR_SILO_3) + Float.parseFloat(VALOR_SILO_4);
            // Gera um array com as quantidades individuais de cada silo
            float []qtd_silos = {Float.parseFloat(VALOR_SILO_1), Float.parseFloat(VALOR_SILO_2), Float.parseFloat(VALOR_SILO_3), Float.parseFloat(VALOR_SILO_4)};
            
            //3 - Checa estoque de silos e verifica se é suficiente
            
            if(checa_silos(qtd_silos) == false){
                return;
            }
            else if(checa_silos(qtd_silos) == true){
                // 4 - Retira quantidades do estoque
                atualizar_estoque_silos(qtd_silos);

                // 5 - Seta blend atual
                set_blend_atual();

                //6 - Salva dados do blend em registro
                salvar_registro_blend();

                //7 - Atualiza quantidade nos silos de cafe moido e inteiro de acordo com operação
                if(cbBlendOperacao.getSelectedIndex() == 1){
                    atualizar_estoque_moido(QtdTotal);
                }
                else if(cbBlendOperacao.getSelectedIndex() == 2){
                    atualizar_estoque_grao(QtdTotal);
                }

                //4 - Envia dados do blend para PLC
                m.writeSingleRegister(1, 1, Math.round(Float.parseFloat(VALOR_SILO_1)*10));
                m.writeSingleRegister(1, 2, Math.round(Float.parseFloat(VALOR_SILO_2)*10));
                m.writeSingleRegister(1, 3, Math.round(Float.parseFloat(VALOR_SILO_3)*10));
                m.writeSingleRegister(1, 4, Math.round(Float.parseFloat(VALOR_SILO_4)*10));
                
                
                
                caixa_mensagens.insertString(caixa_mensagens.getLength(), "\nDados enviados com sucesso!" , cor_novo_processo);
                JOptionPane.showMessageDialog(null, "Dados enviados com sucesso!");
            }
            
          
        } catch (ModbusProtocolException ex) {
            Logger.getLogger(TelaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ModbusNumberException ex) {
            Logger.getLogger(TelaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ModbusIOException ex) {
            Logger.getLogger(TelaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadLocationException ex) {
            Logger.getLogger(TelaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
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

        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        btnBlend = new javax.swing.JButton();
        btnSilos = new javax.swing.JButton();
        btnModBus = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jPanel40 = new javax.swing.JPanel();
        jPanel41 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        lblNomeBlend = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btnBlendSalvar = new javax.swing.JButton();
        btnBlendAdd = new javax.swing.JButton();
        btnBlendEditar = new javax.swing.JButton();
        btnBlendCancelar = new javax.swing.JButton();
        btnBlendDeletar = new javax.swing.JButton();
        btnBlendAdd1 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtStatus = new javax.swing.JTextPane();
        jPanel11 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        txtQtdSilo3 = new javax.swing.JTextField();
        jPanel14 = new javax.swing.JPanel();
        txtNomeBlend = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtIdBlend = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        txtBlendPesq = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbBlend = new javax.swing.JTable();
        jPanel16 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        txtQtdSilo1 = new javax.swing.JTextField();
        jPanel24 = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        jPanel26 = new javax.swing.JPanel();
        jPanel27 = new javax.swing.JPanel();
        jPanel28 = new javax.swing.JPanel();
        jPanel29 = new javax.swing.JPanel();
        jPanel30 = new javax.swing.JPanel();
        jPanel31 = new javax.swing.JPanel();
        txtQtdSilo4 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jPanel32 = new javax.swing.JPanel();
        jPanel33 = new javax.swing.JPanel();
        jPanel34 = new javax.swing.JPanel();
        jPanel35 = new javax.swing.JPanel();
        jPanel36 = new javax.swing.JPanel();
        jPanel37 = new javax.swing.JPanel();
        jPanel38 = new javax.swing.JPanel();
        jPanel39 = new javax.swing.JPanel();
        txtQtdSilo2 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        footerBlend = new javax.swing.JPanel();
        lblData = new javax.swing.JLabel();
        jPanel42 = new javax.swing.JPanel();
        cbBlendOperacao = new javax.swing.JComboBox<>();
        btnBlendEnviar = new javax.swing.JButton();
        btnBlendAtual = new javax.swing.JButton();

        jMenuItem1.setText("jMenuItem1");

        jMenu1.setText("jMenu1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Blend Jacutinga");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(25, 42, 86));
        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));

        btnBlend.setBackground(new java.awt.Color(25, 42, 86));
        btnBlend.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnBlend.setForeground(new java.awt.Color(255, 255, 255));
        btnBlend.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/blend/icones/coffee-bean (1).png"))); // NOI18N
        btnBlend.setBorderPainted(false);
        btnBlend.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBlend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBlendActionPerformed(evt);
            }
        });

        btnSilos.setBackground(new java.awt.Color(25, 42, 86));
        btnSilos.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnSilos.setForeground(new java.awt.Color(255, 255, 255));
        btnSilos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/blend/icones/silo (1).png"))); // NOI18N
        btnSilos.setBorderPainted(false);
        btnSilos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSilos.setFocusPainted(false);
        btnSilos.setFocusable(false);
        btnSilos.setRequestFocusEnabled(false);
        btnSilos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSilosActionPerformed(evt);
            }
        });

        btnModBus.setBackground(new java.awt.Color(25, 42, 86));
        btnModBus.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnModBus.setForeground(new java.awt.Color(255, 255, 255));
        btnModBus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/blend/icones/cpu (1).png"))); // NOI18N
        btnModBus.setBorderPainted(false);
        btnModBus.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnModBus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModBusActionPerformed(evt);
            }
        });

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));
        jPanel12.setPreferredSize(new java.awt.Dimension(2, 100));

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setPreferredSize(new java.awt.Dimension(2, 100));

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel40.setBackground(new java.awt.Color(255, 255, 255));
        jPanel40.setPreferredSize(new java.awt.Dimension(2, 100));

        jPanel41.setBackground(new java.awt.Color(255, 255, 255));
        jPanel41.setPreferredSize(new java.awt.Dimension(2, 100));

        javax.swing.GroupLayout jPanel41Layout = new javax.swing.GroupLayout(jPanel41);
        jPanel41.setLayout(jPanel41Layout);
        jPanel41Layout.setHorizontalGroup(
            jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );
        jPanel41Layout.setVerticalGroup(
            jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel40Layout = new javax.swing.GroupLayout(jPanel40);
        jPanel40.setLayout(jPanel40Layout);
        jPanel40Layout.setHorizontalGroup(
            jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel40Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel41, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel40Layout.setVerticalGroup(
            jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 74, Short.MAX_VALUE)
            .addGroup(jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel40Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel41, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel12Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel12Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel40, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel12Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
                    .addContainerGap()))
            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel12Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel40, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setPreferredSize(new java.awt.Dimension(2, 100));

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setPreferredSize(new java.awt.Dimension(2, 100));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setPreferredSize(new java.awt.Dimension(2, 100));

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setPreferredSize(new java.awt.Dimension(2, 100));

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel9Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 74, Short.MAX_VALUE)
            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel9Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
                    .addContainerGap()))
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnSilos, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addComponent(btnBlend)
                .addGap(18, 18, 18)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addComponent(btnModBus)
                .addContainerGap(1259, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnSilos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnBlend, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)
            .addComponent(btnModBus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1620, 80));

        lblNomeBlend.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        getContentPane().add(lblNomeBlend, new org.netbeans.lib.awtextra.AbsoluteConstraints(458, 119, -1, -1));

        jPanel2.setBackground(new java.awt.Color(25, 42, 86));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("BLEND SELECIONADO");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 10, -1, -1));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, 990, 50));

        jPanel3.setBackground(new java.awt.Color(25, 42, 86));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnBlendSalvar.setBackground(new java.awt.Color(255, 255, 255));
        btnBlendSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/blend/icones/save_48px.png"))); // NOI18N
        btnBlendSalvar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBlendSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBlendSalvarActionPerformed(evt);
            }
        });
        jPanel3.add(btnBlendSalvar, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, 80, 80));
        btnBlendSalvar.getAccessibleContext().setAccessibleName("Salvar");
        btnBlendSalvar.getAccessibleContext().setAccessibleDescription("Salvar");

        btnBlendAdd.setBackground(new java.awt.Color(255, 255, 255));
        btnBlendAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/blend/icones/add_48px.png"))); // NOI18N
        btnBlendAdd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBlendAdd.setPreferredSize(new java.awt.Dimension(62, 62));
        btnBlendAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBlendAddActionPerformed(evt);
            }
        });
        jPanel3.add(btnBlendAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 80, 80));
        btnBlendAdd.getAccessibleContext().setAccessibleName("Novo Blend");
        btnBlendAdd.getAccessibleContext().setAccessibleDescription("Novo Blend");

        btnBlendEditar.setBackground(new java.awt.Color(255, 255, 255));
        btnBlendEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/blend/icones/edit_48px.png"))); // NOI18N
        btnBlendEditar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBlendEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBlendEditarActionPerformed(evt);
            }
        });
        jPanel3.add(btnBlendEditar, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, 80, 80));
        btnBlendEditar.getAccessibleContext().setAccessibleName("Editar Blend");
        btnBlendEditar.getAccessibleContext().setAccessibleDescription("Editar Blend");

        btnBlendCancelar.setBackground(new java.awt.Color(255, 255, 255));
        btnBlendCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/blend/icones/cancel_48px.png"))); // NOI18N
        btnBlendCancelar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBlendCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBlendCancelarActionPerformed(evt);
            }
        });
        jPanel3.add(btnBlendCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 130, 80, 80));
        btnBlendCancelar.getAccessibleContext().setAccessibleName("Cancelar");
        btnBlendCancelar.getAccessibleContext().setAccessibleDescription("Cancelar");

        btnBlendDeletar.setBackground(new java.awt.Color(255, 255, 255));
        btnBlendDeletar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/blend/icones/delete_bin_48px.png"))); // NOI18N
        btnBlendDeletar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBlendDeletar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBlendDeletarActionPerformed(evt);
            }
        });
        jPanel3.add(btnBlendDeletar, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 240, 80, 80));
        btnBlendDeletar.getAccessibleContext().setAccessibleName("Deletar Blend");
        btnBlendDeletar.getAccessibleContext().setAccessibleDescription("Deletar Blend");

        btnBlendAdd1.setBackground(new java.awt.Color(255, 255, 255));
        btnBlendAdd1.setText("TESTE");
        btnBlendAdd1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBlendAdd1ActionPerformed(evt);
            }
        });
        jPanel3.add(btnBlendAdd1, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 240, 80, 80));

        jScrollPane3.setViewportView(txtStatus);

        jPanel3.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 390, 200, 120));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1060, 90, 240, 540));

        jPanel11.setBackground(new java.awt.Color(72, 126, 176));
        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2), "SILO 3", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 20), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel11.setPreferredSize(new java.awt.Dimension(210, 180));
        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel15.setBackground(new java.awt.Color(72, 126, 176));
        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 3), "SILO 2", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 20), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel15.setPreferredSize(new java.awt.Dimension(210, 180));
        jPanel15.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel11.add(jPanel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 210, 240, 180));

        jPanel18.setBackground(new java.awt.Color(72, 126, 176));
        jPanel18.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 3), "SILO 2", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 20), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel18.setPreferredSize(new java.awt.Dimension(210, 180));
        jPanel18.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel19.setBackground(new java.awt.Color(72, 126, 176));
        jPanel19.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 3), "SILO 2", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 20), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel19.setPreferredSize(new java.awt.Dimension(210, 180));
        jPanel19.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel18.add(jPanel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 210, 240, 180));

        jPanel11.add(jPanel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 210, 240, 180));

        jPanel20.setBackground(new java.awt.Color(72, 126, 176));
        jPanel20.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 3), "SILO 2", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 20), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel20.setPreferredSize(new java.awt.Dimension(210, 180));
        jPanel20.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel21.setBackground(new java.awt.Color(72, 126, 176));
        jPanel21.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 3), "SILO 2", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 20), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel21.setPreferredSize(new java.awt.Dimension(210, 180));
        jPanel21.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel20.add(jPanel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 210, 240, 180));

        jPanel22.setBackground(new java.awt.Color(72, 126, 176));
        jPanel22.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 3), "SILO 2", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 20), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel22.setPreferredSize(new java.awt.Dimension(210, 180));
        jPanel22.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel23.setBackground(new java.awt.Color(72, 126, 176));
        jPanel23.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 3), "SILO 2", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 20), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel23.setPreferredSize(new java.awt.Dimension(210, 180));
        jPanel23.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel22.add(jPanel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 210, 240, 180));

        jPanel20.add(jPanel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 210, 240, 180));

        jPanel11.add(jPanel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 210, 240, 180));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("QUANTIDADE (Kg)");
        jPanel11.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 80, -1, 20));

        txtQtdSilo3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jPanel11.add(txtQtdSilo3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 120, 150, 30));

        getContentPane().add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 210, 240, 180));

        jPanel14.setBackground(new java.awt.Color(72, 126, 176));
        jPanel14.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtNomeBlend.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jPanel14.add(txtNomeBlend, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 10, 256, -1));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("NOME DO BLEND");
        jPanel14.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        txtIdBlend.setEditable(false);
        txtIdBlend.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jPanel14.add(txtIdBlend, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 10, 40, -1));

        getContentPane().add(jPanel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 150, 990, 50));

        jPanel4.setBackground(new java.awt.Color(72, 126, 176));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtBlendPesq.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBlendPesqKeyReleased(evt);
            }
        });
        jPanel4.add(txtBlendPesq, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, 256, 32));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/blend/icones/proc.png"))); // NOI18N
        jLabel10.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel10MouseClicked(evt);
            }
        });
        jPanel4.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 30, 30));

        getContentPane().add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 400, 640, 50));

        jPanel5.setBackground(new java.awt.Color(72, 126, 176));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tbBlend.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tbBlend.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Nome", "Quantidade Silo 1", "Quantidade Silo 2", "Quantidade Silo 3", "Quantidade Silo 4"
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

        jPanel5.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 621, 110));

        getContentPane().add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 460, 640, 170));

        jPanel16.setBackground(new java.awt.Color(72, 126, 176));
        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2), "SILO 1", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 20), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel16.setPreferredSize(new java.awt.Dimension(210, 180));
        jPanel16.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel17.setBackground(new java.awt.Color(72, 126, 176));
        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 3), "SILO 2", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 20), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel17.setPreferredSize(new java.awt.Dimension(210, 180));
        jPanel17.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel16.add(jPanel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 210, 240, 180));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("QUANTIDADE (Kg)");
        jPanel16.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 80, -1, -1));

        txtQtdSilo1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jPanel16.add(txtQtdSilo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 120, 150, -1));

        getContentPane().add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 210, 240, 180));

        jPanel24.setBackground(new java.awt.Color(72, 126, 176));
        jPanel24.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2), "SILO 4", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 20), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel24.setPreferredSize(new java.awt.Dimension(210, 180));
        jPanel24.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel25.setBackground(new java.awt.Color(72, 126, 176));
        jPanel25.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 3), "SILO 2", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 20), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel25.setPreferredSize(new java.awt.Dimension(210, 180));
        jPanel25.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel24.add(jPanel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 210, 240, 180));

        jPanel26.setBackground(new java.awt.Color(72, 126, 176));
        jPanel26.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 3), "SILO 2", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 20), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel26.setPreferredSize(new java.awt.Dimension(210, 180));
        jPanel26.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel27.setBackground(new java.awt.Color(72, 126, 176));
        jPanel27.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 3), "SILO 2", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 20), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel27.setPreferredSize(new java.awt.Dimension(210, 180));
        jPanel27.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel26.add(jPanel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 210, 240, 180));

        jPanel24.add(jPanel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 210, 240, 180));

        jPanel28.setBackground(new java.awt.Color(72, 126, 176));
        jPanel28.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 3), "SILO 2", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 20), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel28.setPreferredSize(new java.awt.Dimension(210, 180));
        jPanel28.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel29.setBackground(new java.awt.Color(72, 126, 176));
        jPanel29.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 3), "SILO 2", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 20), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel29.setPreferredSize(new java.awt.Dimension(210, 180));
        jPanel29.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel28.add(jPanel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 210, 240, 180));

        jPanel30.setBackground(new java.awt.Color(72, 126, 176));
        jPanel30.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 3), "SILO 2", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 20), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel30.setPreferredSize(new java.awt.Dimension(210, 180));
        jPanel30.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel31.setBackground(new java.awt.Color(72, 126, 176));
        jPanel31.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 3), "SILO 2", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 20), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel31.setPreferredSize(new java.awt.Dimension(210, 180));
        jPanel31.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel30.add(jPanel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 210, 240, 180));

        jPanel28.add(jPanel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 210, 240, 180));

        jPanel24.add(jPanel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 210, 240, 180));

        txtQtdSilo4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jPanel24.add(txtQtdSilo4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 120, 150, -1));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("QUANTIDADE (Kg)");
        jPanel24.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 80, -1, -1));

        getContentPane().add(jPanel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 210, 240, 180));

        jPanel32.setBackground(new java.awt.Color(72, 126, 176));
        jPanel32.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2), "SILO 2", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 20), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel32.setPreferredSize(new java.awt.Dimension(210, 180));
        jPanel32.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel33.setBackground(new java.awt.Color(72, 126, 176));
        jPanel33.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 3), "SILO 2", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 20), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel33.setPreferredSize(new java.awt.Dimension(210, 180));
        jPanel33.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel32.add(jPanel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 210, 240, 180));

        jPanel34.setBackground(new java.awt.Color(72, 126, 176));
        jPanel34.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 3), "SILO 2", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 20), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel34.setPreferredSize(new java.awt.Dimension(210, 180));
        jPanel34.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel35.setBackground(new java.awt.Color(72, 126, 176));
        jPanel35.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 3), "SILO 2", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 20), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel35.setPreferredSize(new java.awt.Dimension(210, 180));
        jPanel35.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel34.add(jPanel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 210, 240, 180));

        jPanel32.add(jPanel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 210, 240, 180));

        jPanel36.setBackground(new java.awt.Color(72, 126, 176));
        jPanel36.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 3), "SILO 2", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 20), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel36.setPreferredSize(new java.awt.Dimension(210, 180));
        jPanel36.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel37.setBackground(new java.awt.Color(72, 126, 176));
        jPanel37.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 3), "SILO 2", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 20), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel37.setPreferredSize(new java.awt.Dimension(210, 180));
        jPanel37.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel36.add(jPanel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 210, 240, 180));

        jPanel38.setBackground(new java.awt.Color(72, 126, 176));
        jPanel38.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 3), "SILO 2", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 20), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel38.setPreferredSize(new java.awt.Dimension(210, 180));
        jPanel38.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel39.setBackground(new java.awt.Color(72, 126, 176));
        jPanel39.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 3), "SILO 2", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 20), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel39.setPreferredSize(new java.awt.Dimension(210, 180));
        jPanel39.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel38.add(jPanel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 210, 240, 180));

        jPanel36.add(jPanel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 210, 240, 180));

        jPanel32.add(jPanel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 210, 240, 180));

        txtQtdSilo2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jPanel32.add(txtQtdSilo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 120, 150, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("QUANTIDADE (Kg)");
        jPanel32.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 80, -1, -1));

        getContentPane().add(jPanel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 210, 240, 180));

        footerBlend.setBackground(new java.awt.Color(68, 141, 41));
        footerBlend.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblData.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblData.setForeground(new java.awt.Color(255, 255, 255));
        lblData.setText("Data - Observação");
        footerBlend.add(lblData, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 0, 920, 50));

        getContentPane().add(footerBlend, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 650, 1400, 50));

        jPanel42.setBackground(new java.awt.Color(25, 42, 86));
        jPanel42.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cbBlendOperacao.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbBlendOperacao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SELECIONE UMA OPERAÇÃO...", "MOER E EMPACOTAR", "EMPACOTAR INTEIRO" }));
        jPanel42.add(cbBlendOperacao, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 80, 230, 40));

        btnBlendEnviar.setBackground(new java.awt.Color(255, 51, 51));
        btnBlendEnviar.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnBlendEnviar.setForeground(new java.awt.Color(255, 255, 255));
        btnBlendEnviar.setText("Enviar para CLP");
        btnBlendEnviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBlendEnviarActionPerformed(evt);
            }
        });
        jPanel42.add(btnBlendEnviar, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 140, 230, 70));

        btnBlendAtual.setBackground(new java.awt.Color(255, 255, 255));
        btnBlendAtual.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnBlendAtual.setText("ULTIMO BLEND ENVIADO");
        btnBlendAtual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBlendAtualActionPerformed(evt);
            }
        });
        jPanel42.add(btnBlendAtual, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 20, 230, 40));

        getContentPane().add(jPanel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 400, 340, 230));

        setSize(new java.awt.Dimension(1410, 739));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents


    private void btnBlendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBlendActionPerformed
        //Chama tela de blend
        TelaBlend blend = new TelaBlend();
        blend.setVisible(true);
    }//GEN-LAST:event_btnBlendActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        //Bloqueia campos
        block_campos();
       
        // Consulta banco para pegar variaveis do ModBus
        buscar_modbus();
        
        //Data
        Date data = new Date();
        DateFormat formatador = DateFormat.getDateInstance(DateFormat.SHORT);
        lblData.setText(formatador.format(data)+" - Crie seu blend selecionando as quantidades de café vindas de cada silo");
    }//GEN-LAST:event_formWindowActivated

    private void btnSilosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSilosActionPerformed
        // Chama tela de Silos
        TelaSilos silos = new TelaSilos();
        silos.setVisible(true);
    }//GEN-LAST:event_btnSilosActionPerformed

    private void btnModBusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModBusActionPerformed
        // Chama tela do ModBus
        TelaModbus modbus = new TelaModbus();
        modbus.setVisible(true);
    }//GEN-LAST:event_btnModBusActionPerformed

    private void txtBlendPesqKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBlendPesqKeyReleased
        // Chama função para pesquisar blend criado
        pesquisar_blend();
    }//GEN-LAST:event_txtBlendPesqKeyReleased

    private void tbBlendMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbBlendMouseClicked
        // Chama função para preencher os campos de texto e muda o metodo
        Metodo = "pesquisar";
        set_campos_blend();
        btnBlendDeletar.setEnabled(true);
    }//GEN-LAST:event_tbBlendMouseClicked

    private void tbBlendKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbBlendKeyReleased
        
    }//GEN-LAST:event_tbBlendKeyReleased

    private void btnBlendAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBlendAddActionPerformed
        //Muda metodo para adicionar e desbloqueia campos
        Metodo="adicionar";
        
        btnBlendAdd.setBackground(new Color(198,198,198));
        btnBlendEditar.setEnabled(false);
        btnBlendDeletar.setEnabled(false);
        btnBlendAtual.setEnabled(false);
        cbBlendOperacao.setEnabled(false);
        btnBlendEnviar.setEnabled(false);
        limpa_campos();
        unlock_campos();
    }//GEN-LAST:event_btnBlendAddActionPerformed

    private void btnBlendEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBlendEditarActionPerformed
        //Muda metodo para editar e desbloqueia campos
        Metodo="editar";

        btnBlendEditar.setBackground(new Color(198,198,198));
        btnBlendAdd.setEnabled(false);
        btnBlendDeletar.setEnabled(false);
        btnBlendAtual.setEnabled(false);
        cbBlendOperacao.setEnabled(false);
        btnBlendEnviar.setEnabled(false);
        unlock_campos();
    }//GEN-LAST:event_btnBlendEditarActionPerformed

    private void btnBlendSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBlendSalvarActionPerformed
        // Checa o metodo e chama a função correspondente
        if(Metodo == "pesquisar"){
            if(Metodo == "pesquisar" && btnBlendAdd.isEnabled() == false){
                //permite que edite se usuario clicar primeiro no botao de editar e depois pesuisar
                editar_blend();
            }
            else{
                return;
            }
        }
        
        else{
            if(Metodo == "adicionar"){
                add_blend();
            }
            else if(Metodo == "editar"){
                if(txtIdBlend.getText()==null){
                    JOptionPane.showMessageDialog(null, "Selecione um blend para ser editado!");
                }
                else if(Metodo == "editar"){
                   editar_blend();
                }
            }
            else if(Metodo == null){
                return;
            }
            else{
                JOptionPane.showMessageDialog(null, "Erro inesperado!");
            }
        }
    }//GEN-LAST:event_btnBlendSalvarActionPerformed

    private void btnBlendDeletarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBlendDeletarActionPerformed
        // Chama função para deletar blend
        remover_blend();
    }//GEN-LAST:event_btnBlendDeletarActionPerformed

    private void btnBlendEnviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBlendEnviarActionPerformed
      //Checa se operação está selecionada e envia dados
        if(cbBlendOperacao.getSelectedIndex() == 0){
            JOptionPane.showMessageDialog(null, "Selecione uma operação!");
        }
        else {
            enviar_para_clp();
        }
    }//GEN-LAST:event_btnBlendEnviarActionPerformed

    private void btnBlendAdd1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBlendAdd1ActionPerformed
        // Teste de funções
        try {
            
        } catch (Exception e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_btnBlendAdd1ActionPerformed

    private void btnBlendCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBlendCancelarActionPerformed
        // Cancela ações do usuário
        if(Metodo != null){
            block_campos();
            buscar_blend_atual();
        }
        else{
            return;
        }
    }//GEN-LAST:event_btnBlendCancelarActionPerformed

    private void jLabel10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MouseClicked
        // Busca blends registrados
        buscar_blend();
    }//GEN-LAST:event_jLabel10MouseClicked

    private void btnBlendAtualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBlendAtualActionPerformed
        // Busca ultimo blend enviado ao CLP
        buscar_blend_atual();
    }//GEN-LAST:event_btnBlendAtualActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaPrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBlend;
    private javax.swing.JButton btnBlendAdd;
    private javax.swing.JButton btnBlendAdd1;
    private javax.swing.JButton btnBlendAtual;
    private javax.swing.JButton btnBlendCancelar;
    private javax.swing.JButton btnBlendDeletar;
    private javax.swing.JButton btnBlendEditar;
    private javax.swing.JButton btnBlendEnviar;
    private javax.swing.JButton btnBlendSalvar;
    private javax.swing.JButton btnModBus;
    private javax.swing.JButton btnSilos;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cbBlendOperacao;
    private javax.swing.JPanel footerBlend;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel41;
    private javax.swing.JPanel jPanel42;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblData;
    private javax.swing.JLabel lblNomeBlend;
    private javax.swing.JTable tbBlend;
    private javax.swing.JTextField txtBlendPesq;
    private javax.swing.JTextField txtIdBlend;
    private javax.swing.JTextField txtNomeBlend;
    private javax.swing.JTextField txtQtdSilo1;
    private javax.swing.JTextField txtQtdSilo2;
    private javax.swing.JTextField txtQtdSilo3;
    private javax.swing.JTextField txtQtdSilo4;
    private javax.swing.JTextPane txtStatus;
    // End of variables declaration//GEN-END:variables

}
