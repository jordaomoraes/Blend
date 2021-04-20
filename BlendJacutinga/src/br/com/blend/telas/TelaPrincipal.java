package br.com.blend.telas;

import br.com.blend.dal.ModuloConexao;
import br.com.blend.dal.ModuloConexaoNuvem;
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
import java.awt.Toolkit;
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
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JTextArea;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.Border;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import net.proteanit.sql.DbUtils;


public class TelaPrincipal extends javax.swing.JFrame {
    
    //Variáveis de conexao para operações com banco
    Connection conexao = null;
    Connection nuvem = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    PreparedStatement pstNuvem = null;
    ResultSet rsNuvem = null;
    String Metodo;
    
    //Variáveis de CONTROLE GERAL
    int POWER = 0;
    boolean blendando = false, terminou_blendar = false, novo_blend;
    int blendador_ligado = 0;
    
    boolean inseriu_msg_blnd1 = false, inseriu_msg_blnd2 = true;
    boolean inseriu_blendando1 = false, inseriu_blendando2 = false;
    boolean inseriu_msg_msx1 = false, inseriu_msg_msx2 = true;
    boolean inseriu_msg_elev1 = false, inseriu_msg_elev2 = true;
    boolean inseriu_msg_modo1 = false, inseriu_msg_modo2 = true;
    
    boolean set_blendando = false;
    boolean [] array_coils;
    
    int manual = 0;
    boolean modo_manual = false;

    int mexedor = 0, elevador = 0;
   
    
    
    //Variáveis para tesar url
    boolean temos_internet = false, reconectado_internet = false;
    boolean operando_offline = false;
    static URL conexaoURL;
    static URLConnection conn;
    static InputStream fechar;
    
    int i = 0;
    
    //Variaveis para sincronizar
    String ultimo_id_nuvem, ultimo_id_registro_nuvem, ultimo_id_blend_local, ultimo_id_lote_nuvem;
    boolean precisa_sincrinizar = false, sincronizar_ao_ligar = false;
    int sincronizado = 0;
    String id_atual, nome_atual, qtds1_atual, qtds2_atual, qtds3_atual, qtds4_atual, operation;
    
    //sincrnonizar estoque silos (apagar, não utilizado)
    float []array_silos_sinc;
    
    
    //Variaveis Blend
    String IdBlend, NomeBlend, QtdSilo1, QtdSilo2, QtdSilo3, QtdSilo4;
    float QtdTotal = 0;
    float ValorCru, ValorTorrado, ValorTotal = 0;
    int lote = 0;
    boolean blend = false;
    int blend_disponivel = 0;
    
    //Variaveis Lote
    int lote_disponivel = 0;
    //Array list pois mais lotes serão criados, sem tamanho fixo
    ArrayList<Float> qtd_torrado = new ArrayList<Float>();
    
    //Variaveis Estoque | Silos | Processos manuais
    float Qtd_estoque_s1 = 0, Qtd_estoque_s2 = 0, Qtd_estoque_s3 = 0, Qtd_estoque_s4 = 0;
    float Qtd_estoque = 0, Qtd_estoque_moido = 0, Qtd_estoque_att = 0;
    float []array_silos = null, array_silos_nuvem = null;
    String cafe_silo1, cafe_silo2, cafe_silo3, cafe_silo4;
    boolean silo_1_abriu, silo_1_fechou, silo_2_abriu, silo_2_fechou, silo_3_abriu, silo_3_fechou, silo_4_abriu, silo_4_fechou;
    //Principal
    float []qtd_silos = null;
    boolean gerou_blend_manual = false;
    int silo1, silo2, silo3, silo4;
    float []preco_kg_cru, preco_kg_torrado;
    float preco_total_cru = 0, preco_total_torrado = 0;
    //Caso de erro ao atualizar estoque
    boolean falha_estq_silo = false;
    
    
    //Variáveis para balança
    int [] peso, pesof;
    int tarar = 0;
    float peso_inicial_1 = 0, peso_inicial_2 = 0, peso_inicial_3 = 0, peso_inicial_4 = 0;
    float peso_real = 0, peso_final = 0, variacao_peso = 0;
    boolean pegou_peso_inicia_l = false, pegou_peso_inicia_2 = false, pegou_peso_inicia_3 = false, pegou_peso_inicia_4 = false;
    
    
    //Variaveis para gerar registro manual
    String nome_blend, nome_cafe1, nome_cafe2, nome_cafe3, nome_cafe4, operacao;
    float qtd_cafe1 = 0, qtd_cafe2 = 0, qtd_cafe3 = 0, qtd_cafe4 = 0, qtd_total = 0, valor_cafe_cru = 0, valor_cafe_torrado = 0, valor_total = 0, lote_registro = 0;
    
    //Variaveis de status (Caixa mensagens)
    StyledDocument caixa_mensagens;
    SimpleAttributeSet cor_mensagem_erro = new SimpleAttributeSet();
    SimpleAttributeSet cor_tentando_conectar = new SimpleAttributeSet();
    SimpleAttributeSet cor_conectado = new SimpleAttributeSet();
    SimpleAttributeSet cor_conectado_internet = new SimpleAttributeSet();
    SimpleAttributeSet cor_novo_processo = new SimpleAttributeSet();
    SimpleAttributeSet cor_sincronizar = new SimpleAttributeSet();
    
    boolean inseriu_mensagem1 = false, inseriu_mensagem2 = false;
    
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
    
    //Variaveis de timer task
    final private Timer timer = new Timer();
    private TimerTask timer_clp, timer_internet, timer_blendador, timer_sincronizar, timer_blendando, timer_operacao, timer_lote, timer_mexedor, timer_elevador, timer_modo_blendador, timer_nuvem;
    int tempo_clp = (1000), tempo_internet = (1000), tempo_blendador = (1000), tempo_sincronizar = (1000), tempo_blendando = (1000), tempo_operacao = (500), tempo_lote = (2000), tempo_mexedor = (1000), tempo_elevador = (1000), tempo_modo_blendador=(1000), tempo_nuvem = (5000);
    int contador_tempo=0, contador_operacao_silos =0;

    public TelaPrincipal() {
        initComponents();
        conexao = ModuloConexao.conector();
        conecta_nuvem();
        
        //Caixa de saida
        caixa_mensagens = txtStatus.getStyledDocument();
        define_cores_mensagens();
        
        //Busca blend atual
        buscar_blend_atual();
        
        //Busca lotes cadastrados e os coloca na Combo box
        set_lotes();
        
        //Bloqueia Campos
        block_campos();

        // Consulta banco para pegar variaveis do ModBus
        buscar_modbus();
        
        //Data
        Date data = new Date();
        DateFormat formatador = DateFormat.getDateInstance(DateFormat.SHORT);
        lblData.setText(formatador.format(data)+" - Crie seu blend selecionando as quantidades de café vindas de cada silo");
        
        //Checa se dados estão sincronizados
        check_sincronizar();
        
        //Checa se existe algum blend (PARA CASO NAO TENHA BLEND CADASTRADO)
        check_blend();
        
        //Loops de checagem
        checa_conexao_clp();
        checa_conexao_internet();
        //checa_conexao_nuvem();
        checa_status_blendador();
        checa_modo_blendador();
        checa_status_mexedor();
        checa_status_elevador();
        checa_esta_blendando();
        checa_sincronizar();
        checa_balanca();
        checa_lotes();
    }
    
    //Checa se tem net e conecta com banco na nuvem
    private void conecta_nuvem(){
        if(testa_url("http://192.169.80.2")){
            try {
                nuvem = ModuloConexaoNuvem.conector();
                temos_internet = true;
            } catch (Exception e) {
                System.out.println("Falha ao conectar com banco online, sistema no OFFLINE");
            }
            System.out.println("Operando NORMALMENTE");
        }
        else{
            System.out.println("Operando OFFLINE");
            temos_internet = false;
        }
    }
    
    
    //Para testar conexao com banco na nuvem (OTIMIZAÇÃO)
    private boolean consulta_nuvem(){
        String sql = "select id_blend_atual from tb_blend_atual";
        try {
            pstNuvem = nuvem.prepareStatement(sql);
            rsNuvem = pstNuvem.executeQuery();
            if(rsNuvem.next()){
                //System.out.println("TUDO OK");
                return true;
            }
            else{
                return false;
            }
        } catch (Exception e) {
            //System.out.println("CAIU NO CATCH");
            return false;
        }
    }
    
    
    //Para testar se tem net e determinar para qual banco vai
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
    
    
    //Metodos para checar conexoes
    public void checa_conexao_clp() {
        if (timer_clp != null) {
            return;
        }
        timer_clp = new TimerTask() {
            @Override
            public void run() {
                //Checa conexao com CLP (tenta ler valor)
                //System.out.println("vou checar connection");
                try {
                    m.readCoils(escravo, 3, 1);
                } catch (Exception e) {
                    clp_conectado = false;
                }
                if(clp_conectado == false){
                    System.out.println("Tentando se conectar com CLP...");
                    
                    //Tenta reconectar
                    try {
                        limpa_mensagens();
                        caixa_mensagens.insertString(caixa_mensagens.getLength(), "Tentando se reconectar com CLP..." , cor_tentando_conectar);
                        //Conecta com clp
                        conecta_com_clp();
                    } catch (ModbusProtocolException | ModbusNumberException ex) {
                        clp_conectado = false;
                        Logger.getLogger(TelaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (BadLocationException ex) {
                        Logger.getLogger(TelaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                    } catch(Exception e){
                        System.out.println(e);
                    }
                }
            }
        };
        timer.scheduleAtFixedRate(timer_clp, 1, tempo_clp);
    }
    
    
    //Metodo principal de checagem de internets
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
                    reconectado_internet = true;
                    lblBlendWifi.setVisible(false);
                    lblWifiDesc.setVisible(false);
                    //checa_conexao_nuvem();
                }
                
                else if(!testa_url("http://192.169.80.2")){
                    temos_internet = false;
                    operando_offline = true;
                    lblBlendWifi.setVisible(true);
                    lblWifiDesc.setVisible(true);
                }
                
                //Cancela as operações de editar e deletar caso internet caia no meio do processo (deprecated)
                if(temos_internet == false && Metodo == "editar"){
                    Metodo = null;
                    block_campos();
                    buscar_blend_atual();
                }
            }};
        timer.scheduleAtFixedRate(timer_internet, 1, tempo_internet);
    }
    
    //Checa se conexao com banco ainda está aberta (em progresso)
    private void checa_conexao_nuvem(){
        try {
            if(consulta_nuvem()){
                return;
            }
            else if(!consulta_nuvem()){
                System.out.println("Conexao com nuvem perdida, reconectando...");
                nuvem = ModuloConexaoNuvem.conector(); 
            }
            else{
                System.out.println("Falha catastrófica");
            }
        } catch (Exception e) {
            System.out.println("Falha catastrófica (loop) ao checar conexao com banco (nuvem)!");
            System.out.println(e);
        }
    }
    
    
    //Checa se ciclo está iniciado ou não
    private void checa_status_blendador(){
         if (timer_blendador != null) {
            return;
        }
        timer_blendador = new TimerTask() {
            @Override
            public void run() {
            //Checa se BLENDADOR está ON ou OFF
            if(check_blendador() == true){
                blendador_ligado = 1;
                btnBlendPower.setText("PARAR");
                btnBlendPower.setBackground(new Color(255,51,51));
                inseriu_msg_blnd2 = false;
                if(inseriu_msg_blnd1 == false && clp_conectado == true){
                try {
                    caixa_mensagens.insertString(caixa_mensagens.getLength(), "\nCiclo iniciado! " , cor_conectado);
                    inseriu_msg_blnd1 = true;
                    //Insere no banco falando que está desligado
                    if(temos_internet == true){
                        set_blendador_nuvem_1();
                    }
                    else{
                        precisa_sincrinizar = true;
                    }
                } catch (Exception e) {
                    System.out.println("Falhao ao setar sincronizar para 1");
                    System.out.println(e);
                }}
            }
            else if(check_blendador() == false){
                blendador_ligado = 0;
                btnBlendPower.setText("INICIAR");
                btnBlendPower.setBackground(new Color(68,141,41));
                inseriu_msg_blnd1 = false;
                if(inseriu_msg_blnd2 == false && clp_conectado == true){
                    try {
                        caixa_mensagens.insertString(caixa_mensagens.getLength(), "\nCiclo terminado! " , cor_mensagem_erro);
                        inseriu_msg_blnd2 = true;
                        //Insere no banco falando que está desligado
                        if(temos_internet == true){
                            set_blendador_nuvem_0();
                        }
                        else{
                            precisa_sincrinizar = true;
                        }
                    } catch (Exception e) {
                        System.out.println("Falha ao setar sincronizar para 0");
                        System.out.println(e);
                    }  
                }
            }
        }};timer.scheduleAtFixedRate(timer_blendador, 1, tempo_blendador);
    }
    
    
    //Checa se está blendando
    private void checa_esta_blendando(){
          if (timer_blendando != null) {
            return;
        }
        timer_blendando = new TimerTask() {
            @Override
            public void run() {
                //Verifica se está blendando
                esta_blendando();
                if(set_blendando == true && clp_conectado == true && blendador_ligado == 1){
                    timer_blendando();
                    inseriu_blendando2 = false;
                    if(inseriu_blendando1 == false){
                       try {
                            caixa_mensagens.insertString(caixa_mensagens.getLength(), "\nBlendando... " , cor_novo_processo);
                            inseriu_blendando1 = true;
                            terminou_blendar = true;
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                }
                else if(set_blendando == false && clp_conectado == true && blendador_ligado == 1 && inseriu_blendando2 == false && terminou_blendar == true){
                    inseriu_blendando1 = false;
                    if(inseriu_blendando2 == false){
                       try {
                            caixa_mensagens.insertString(caixa_mensagens.getLength(), "\nBlend Pronto! " , cor_conectado_internet);
                            
                            if(modo_manual == false){
                                btnBlendEnviar.setEnabled(true);
                            }
                            terminou_blendar = false;
                            inseriu_blendando2 = true;
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                }
            }
        };timer.scheduleAtFixedRate(timer_blendando, 1, tempo_blendando);
    }
    
    
    //Checa modo blendador (MANUAL | AUTOMATICO)
    private void checa_modo_blendador(){
        if (timer_modo_blendador != null) {
            return;
        }
        timer_modo_blendador = new TimerTask() {
            @Override
            public void run() {
                //Checa se mexedor está ligado ou desligado
                try {
                    if(check_modo_blendador()){
                        btnBlendManual.setText("MODO AUTOMATICO");
                        //System.out.println("MANUAL LIGADO");
                        manual = 1;
                        modo_manual = true;
                        unlock_manual();
                        inseriu_msg_modo2 = false;
                        if(inseriu_msg_modo1 == false){
                            try {
                                caixa_mensagens.insertString(caixa_mensagens.getLength(), "\nModo manual ativado! " , cor_mensagem_erro);
                                inseriu_msg_modo1 = true;
                            } catch (Exception e) {
                                System.out.println(e);
                            }
                        }
                        
                    }
                    else if(!check_modo_blendador()){
                        btnBlendManual.setText("MODO MANUAL");
                        //System.out.println("MANUAL DESLIGADO");
                        modo_manual = false;
                        manual = 0;
                        unlock_manual();
                        inseriu_msg_modo1 = false;
                        if(inseriu_msg_modo2 == false){
                            try {
                                caixa_mensagens.insertString(caixa_mensagens.getLength(), "\nModo automático ativado!" , cor_conectado);
                                inseriu_msg_modo2 = true;
                            } catch (Exception e) {
                                System.out.println(e);
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Falha ao checar modo do blendador");
                    System.out.println(e);
                }
            }
        };timer.scheduleAtFixedRate(timer_modo_blendador, 1, tempo_modo_blendador);
    }
    
    //Checa status mexedor (ON | OFF)
    private void checa_status_mexedor(){
        if (timer_mexedor != null) {
            return;
        }
        timer_mexedor = new TimerTask() {
            @Override
            public void run() {
                //Checa se mexedor está ligado ou desligado
                try {
                    if(check_mexedor()){
                        mexedor = 1;
                        btnBlendMexedor.setText("DESLIGAR");
                        btnBlendMexedor.setBackground(new Color(255,51,51));
                        //System.out.println("Mexedor está ligado");
                        inseriu_msg_msx2 = false;
                        if(inseriu_msg_msx1 == false){
                            try {
                                caixa_mensagens.insertString(caixa_mensagens.getLength(), "\nMexedor ligado! " , cor_novo_processo);
                                inseriu_msg_msx1 = true;
                            } catch (Exception e) {
                                System.out.println(e);
                            }
                        }
                    }
                    else if(!check_mexedor()){
                        mexedor = 0;
                        btnBlendMexedor.setText("LIGAR");
                        btnBlendMexedor.setBackground(new Color(68,141,41));
                        //System.out.println("Mexedor está desligado");
                        inseriu_msg_msx1 = false;
                        if(inseriu_msg_msx2 == false){
                            try {
                                caixa_mensagens.insertString(caixa_mensagens.getLength(), "\nMexedor desligado! " , cor_tentando_conectar);
                                inseriu_msg_msx2 = true;
                            } catch (Exception e) {
                                System.out.println(e);
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Falha ao checar status mexedor");
                    System.out.println(e);
                }
            }
        };timer.scheduleAtFixedRate(timer_mexedor, 1, tempo_mexedor);
    }
    
    
    //Checa status elevador (ON | OFF)
    private void checa_status_elevador(){
        if (timer_elevador != null) {
            return;
        }
        timer_elevador = new TimerTask() {
            @Override
            public void run() {
                //Checa se mexedor está ligado ou desligado
                try {
                    if(check_elevador()){
                        elevador = 1;
                        btnBlendElevador.setText("DESLIGAR");
                        btnBlendElevador.setBackground(new Color(255,51,51));
                        //System.out.println("Mexedor está ligado");
                        inseriu_msg_elev2 = false;
                        if(inseriu_msg_elev1 == false){
                            try {
                                caixa_mensagens.insertString(caixa_mensagens.getLength(), "\nElevador ligado! " , cor_novo_processo);
                                inseriu_msg_elev1 = true;
                            } catch (Exception e) {
                                System.out.println(e);
                            }
                        }
                    }
                    else if(!check_elevador()){
                        elevador = 0;
                        btnBlendElevador.setText("LIGAR");
                        btnBlendElevador.setBackground(new Color(68,141,41));
                        //System.out.println("Mexedor está desligado");
                        inseriu_msg_elev1 = false;
                        if(inseriu_msg_elev2 == false){
                            try {
                                caixa_mensagens.insertString(caixa_mensagens.getLength(), "\nElevador Desligado! " , cor_tentando_conectar);
                                inseriu_msg_elev2 = true;
                            } catch (Exception e) {
                                System.out.println(e);
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Falha ao checar status elevador");
                    System.out.println(e);
                }
            }
        };timer.scheduleAtFixedRate(timer_elevador, 1, tempo_elevador);
    }
    
    
    //Checa se precisa sincronizar o sistema
    private void checa_sincronizar(){
        if (timer_sincronizar != null) {
            return;
        }
        timer_sincronizar = new TimerTask() {
            @Override
            public void run() {
                //Sincroniza tudo
                if(precisa_sincrinizar == true && reconectado_internet == true && temos_internet == true || sincronizado == 1){
                    precisa_sincrinizar = false;
                    reconectado_internet = false;
                    //sincronizar_ao_ligar = false;
                    try {
                        check_sincronizar();
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Falha ao sincronizar dados!");
                        System.out.println(e);
                    }
                }
            }
        };timer.scheduleAtFixedRate(timer_sincronizar, 1, tempo_sincronizar);
    }
    
    
    //Checa peso da balanca (DANGER ZONE)
    private void checa_balanca(){
        if (timer_operacao != null) {
            return;
        }
        timer_operacao = new TimerTask() {
            @Override
            public void run() {
                if(clp_conectado == true){
                    try {
                        
                        //Atualiza balanca em tempo real
                        peso = m.readHoldingRegisters(escravo, Integer.parseInt(PESO), 1);
                        peso_real = peso[0];
                        String peso_string = String.format("%.2f", peso_real/10);
                        lblBlendPeso.setText(peso_string + "Kg");
                        
                        //Obtém peso inical uma vez que algum silo for aberto
                        if(silo_1_abriu == true && pegou_peso_inicia_l == false){
                            peso_inicial_1 = peso[0];
                            pegou_peso_inicia_l = true;
                        }
                        else if(silo_2_abriu == true && pegou_peso_inicia_2 == false){
                            peso_inicial_2 = peso[0];
                            //System.out.println("Peso inicial do silo 2 é "+peso_inicial_2/10);
                            pegou_peso_inicia_2 = true;
                        }
                        else if(silo_3_abriu == true && pegou_peso_inicia_3 == false){
                            peso_inicial_3 = peso[0];
                            pegou_peso_inicia_3 = true;
                        }
                        else if(silo_4_abriu == true && pegou_peso_inicia_4 == false){
                            peso_inicial_4 = peso[0];
                            pegou_peso_inicia_4 = true;
                        }
                        
                        if(silo_1_abriu == true && silo_1_fechou == true && pegou_peso_inicia_l == true){
                            silo_1_fechou = false;
                            silo_1_abriu = false;
                            pegou_peso_inicia_l = false;
                            
                            if(silo1 == 0){
                                pesof = m.readHoldingRegisters(escravo, Integer.parseInt(PESO), 1);
                                peso_final = pesof[0];
                                variacao_peso = (peso_final - peso_inicial_1)/10;
                                //Parte relatorio manual:
                                qtd_cafe1 = 0;
                                qtd_cafe1 = variacao_peso;
                                System.out.println("Qtd de cafe silo 1 "+qtd_cafe1);
                                
                                if(variacao_peso == 0){
                                    JOptionPane.showMessageDialog(null, "Acabou o café do silo 1");
                                }
                                //Atualiza estoque de silo correspondente
                                if(temos_internet == true){
                                    atualiza_silo_manual(variacao_peso, 0);
                                    atualiza_silo_manual_nuvem(variacao_peso, 0);
                                    gerou_blend_manual = true;
                                }
                                else{
                                    atualiza_silo_manual(variacao_peso, 0);
                                    gerou_blend_manual = true;
                                    precisa_sincrinizar = true;
                                    set_sincronizar_1();
                                }
                                
                            }
                        }
                        
                        else if(silo_2_abriu == true && silo_2_fechou == true && pegou_peso_inicia_2 == true){
                            silo_2_fechou = false;
                            silo_2_abriu = false;
                            pegou_peso_inicia_2 = false;
                            
                            if(silo2 == 0){
                                pesof = m.readHoldingRegisters(escravo, Integer.parseInt(PESO), 1);
                                peso_final = pesof[0];
                                variacao_peso = (peso_final - peso_inicial_2)/10;
                                //Parte relatorio manual:
                                qtd_cafe2 = 0;
                                qtd_cafe2 = variacao_peso;
                                System.out.println("Qtd de cafe silo 2 "+qtd_cafe2);
                                
                                if(variacao_peso == 0){
                                    JOptionPane.showMessageDialog(null, "Acabou o café do silo 2");
                                }
                                //Atualiza estoque de silo correspondente
                                if(temos_internet == true){
                                    atualiza_silo_manual(variacao_peso, 1);
                                    atualiza_silo_manual_nuvem(variacao_peso, 1);
                                    gerou_blend_manual = true;
                                }
                                else{
                                    atualiza_silo_manual(variacao_peso, 1);
                                    gerou_blend_manual = true;
                                    precisa_sincrinizar = true;
                                    set_sincronizar_1();
                                }
                                
                            }
                        }
                        else if(silo_3_abriu == true && silo_3_fechou == true && pegou_peso_inicia_3 == true){
                            silo_3_fechou = false;
                            silo_3_abriu = false;
                            pegou_peso_inicia_3 = false;
                            
                            if(silo3 == 0){
                                pesof = m.readHoldingRegisters(escravo, Integer.parseInt(PESO), 1);
                                peso_final = pesof[0];
                                variacao_peso = (peso_final - peso_inicial_3)/10;
                                //Parte relatorio manual:
                                qtd_cafe3 = 0;
                                qtd_cafe3 = variacao_peso;
                                System.out.println("Qtd de cafe silo 3 "+qtd_cafe3);
                                
                                if(variacao_peso == 0){
                                    JOptionPane.showMessageDialog(null, "Acabou o café do silo 3");
                                }
                                //Atualiza estoque de silo correspondente
                                if(temos_internet == true){
                                    atualiza_silo_manual(variacao_peso, 2);
                                    atualiza_silo_manual_nuvem(variacao_peso, 2);
                                    gerou_blend_manual = true;
                                }
                                else{
                                    atualiza_silo_manual(variacao_peso, 2);
                                    gerou_blend_manual = true;
                                    precisa_sincrinizar = true;
                                    set_sincronizar_1();
                                }
                            }
                        }
                        else if(silo_4_abriu == true && silo_4_fechou == true && pegou_peso_inicia_4 == true){
                            silo_4_fechou = false;
                            silo_4_abriu = false;
                            pegou_peso_inicia_4 = false;
                            
                            if(silo4 == 0){
                                pesof = m.readHoldingRegisters(escravo, Integer.parseInt(PESO), 1);
                                peso_final = pesof[0];
                                variacao_peso = (peso_final - peso_inicial_4)/10;
                                //Parte relatorio manual:
                                qtd_cafe4 = 0;
                                qtd_cafe4 = variacao_peso;
                                System.out.println("Qtd de cafe silo 4 "+qtd_cafe4);
                                
                                if(variacao_peso == 0){
                                    JOptionPane.showMessageDialog(null, "Acabou o café do silo 4");
                                }
                                //Atualiza estoque de silo correspondente
                                
                                if(temos_internet == true){
                                    atualiza_silo_manual(variacao_peso, 3);
                                    atualiza_silo_manual_nuvem(variacao_peso, 3);
                                    gerou_blend_manual = true;
                                }
                                else{
                                    atualiza_silo_manual(variacao_peso, 3);
                                    gerou_blend_manual = true;
                                    precisa_sincrinizar = true;
                                    set_sincronizar_1();
                                }
                                
                            }
                        }
                    } catch (ModbusProtocolException | ModbusNumberException | ModbusIOException ex) {
                        System.out.println(ex);
                    }
                }
            }
        };timer.scheduleAtFixedRate(timer_operacao, 1, tempo_operacao);
    }
    
    //MUITO IMPORTANTE, recebe dados para gerar relatório após blend manual
    private void gerar_registro_manual(float qtd_cafe1, float qtd_cafe2, float qtd_cafe3, float qtd_cafe4, float qtd_cafe_total){
        String sql = "insert into tb_blend_registros (nome_blend, nome_cafe1, qtd_cafe1, nome_cafe2, qtd_cafe2, nome_cafe3, qtd_cafe3, nome_cafe4, qtd_cafe4, operacao, qtd_total, valor_cafe_cru, valor_cafe_torrado, valor_total, lote) values (?, (select cafe_atual from tb_silos where id_silo = 1), ?, (select cafe_atual from tb_silos where id_silo = 2), ?, (select cafe_atual from tb_silos where id_silo = 3), ?, (select cafe_atual from tb_silos where id_silo = 4), ?, ?, ?, ?, ?, ?, ?)";
        
        float []array_qtd_cafe = {qtd_cafe1, qtd_cafe2, qtd_cafe3, qtd_cafe4};
        float valor_total = 0;
        
        try {
            consultar_lote_atual();
            
            qtd_cafe_total = 0;
            qtd_cafe_total = qtd_cafe1 + qtd_cafe2 + qtd_cafe3 + qtd_cafe4;
            
            
            //Atualizar qtd_torrado do lote utilizado
            atualizar_qtd_torrado_lote(qtd_cafe_total);
            
            //Chamando função que gera preço do cru, passando parametros diferentes
            gerar_preco_cru(array_qtd_cafe);
            gerar_preco_torrado(array_qtd_cafe);
            
            valor_total = ValorCru + ValorTorrado;
            
            pst = conexao.prepareStatement(sql);
            
            pst.setString(1, "Blend Manual");
            pst.setFloat(2, qtd_cafe1);
            pst.setFloat(3, qtd_cafe2);
            pst.setFloat(4, qtd_cafe3);
            pst.setFloat(5, qtd_cafe4);
            pst.setString(6, "Manual");
            pst.setFloat(7, qtd_cafe_total);
            pst.setFloat(8, ValorCru);
            pst.setFloat(9, ValorTorrado);
            pst.setFloat(10, valor_total);
            pst.setFloat(11, lote);
            
            pst.executeUpdate();
            
            
            //System.out.println("RELATORIO MANUAL GERADO COM SUCESSO!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Falha ao gerar relatório após blend manual");
            System.out.println(e);
        }
        
    }
    
    private void gerar_registro_manual_nuvem(float qtd_cafe1, float qtd_cafe2, float qtd_cafe3, float qtd_cafe4, float qtd_cafe_total){
        String sql = "insert into tb_blend_registros (nome_blend, nome_cafe1, qtd_cafe1, nome_cafe2, qtd_cafe2, nome_cafe3, qtd_cafe3, nome_cafe4, qtd_cafe4, operacao, qtd_total, valor_cafe_cru, valor_cafe_torrado, valor_total, lote) values (?, (select cafe_atual from tb_silos where id_silo = 1), ?, (select cafe_atual from tb_silos where id_silo = 2), ?, (select cafe_atual from tb_silos where id_silo = 3), ?, (select cafe_atual from tb_silos where id_silo = 4), ?, ?, ?, ?, ?, ?, ?)";
        
        float []array_qtd_cafe = {qtd_cafe1, qtd_cafe2, qtd_cafe3, qtd_cafe4};
        float valor_total = 0;
        
        try {
            nuvem = ModuloConexaoNuvem.conector();
            
            consultar_lote_atual();
            
            qtd_cafe_total = 0;
            qtd_cafe_total = qtd_cafe1 + qtd_cafe2 + qtd_cafe3 + qtd_cafe4;
            
            //Atualizando qtd_torrado de lotes(nuvem)
            atualizar_qtd_torrado_lote_nuvem(qtd_cafe_total);
            
            
            //Chamando função que gera preço do cru, passando parametros diferentes
            gerar_preco_cru(array_qtd_cafe);
            gerar_preco_torrado(array_qtd_cafe);
            
            valor_total = ValorCru + ValorTorrado;
            
            pstNuvem = nuvem.prepareStatement(sql);
            
            pstNuvem.setString(1, "Blend Manual");
            pstNuvem.setFloat(2, qtd_cafe1);
            pstNuvem.setFloat(3, qtd_cafe2);
            pstNuvem.setFloat(4, qtd_cafe3);
            pstNuvem.setFloat(5, qtd_cafe4);
            pstNuvem.setString(6, "Manual");
            pstNuvem.setFloat(7, qtd_cafe_total);
            pstNuvem.setFloat(8, ValorCru);
            pstNuvem.setFloat(9, ValorTorrado);
            pstNuvem.setFloat(10, valor_total);
            pstNuvem.setFloat(11, lote);
            
            pstNuvem.executeUpdate();
            
            //resetando variveis para gerar novo relatório posteriormente
            reset_variaveis_registro();
            //System.out.println("RELATORIO MANUAL GERADO COM SUCESSO (NUVEM)!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Falha ao gerar relatório após blend manual (nuvem)!");
            System.out.println(e);
        }
        
    }
    
    private void reset_variaveis_registro(){
        //Quantidades
        qtd_cafe1 = 0; qtd_cafe2 = 0; qtd_cafe3 = 0; qtd_cafe4 = 0;
        //Variaveis de controle
        gerou_blend_manual = false;
    }
    
    
    //Checa se novo lote foi criado
    private boolean lote_disponivel(){
        String sql = "select novo_lote from tb_lote_atual";
        
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            
            if(rs.next()){
                lote_disponivel = rs.getInt(1);
                if(lote_disponivel == 1){
                    return true;
                }
                else if(lote_disponivel == 0){
                    return false;
                }
            }
        } catch (Exception e) {
            System.out.println("Falha ao consultar se há lote disponível");
            System.out.println(e);
        }
        return false;
    }
    
    //Checa se há novo lote disponível
    private void checa_lotes(){
        if (timer_lote != null) {
            return;
        }
        timer_lote = new TimerTask() {
            @Override
            public void run() {
               if(lote_disponivel()){
                   set_lotes();
                   set_novo_lote_0();
               }
               else{
                   return;
               }
            }
        };timer.scheduleAtFixedRate(timer_lote, 1, tempo_lote);
    }
    
    
    //Executado após o cadastro/sincronização, para notificar que nao há novos lotes
    private void set_novo_lote_0(){
        String sql = "update tb_lote_atual set novo_lote = 0";
        
        try {
            pst = conexao.prepareStatement(sql);
            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println("Falha ao setar novo lote para 0");
            System.out.println(e);
        }
    }
    
    
    //Metodos referentes a criação de blends manuais
    private void atualiza_silo_manual(float peso, int id_silo){
        //Atualiza estoque de silo após criar blend manual
        consultar_estoque_silos();
        String sql = "update tb_silos set qtd_atual=? where id_silo=?";
        try {
           float peso_update = array_silos[id_silo] - peso;
            try {
                pst = conexao.prepareStatement(sql);
                pst.setFloat(1, peso_update);
                pst.setInt(2, id_silo+1);
                pst.executeUpdate();
            } catch (Exception e) {
                System.out.println("Falha ao atualizar silo apos blend manual");
                System.out.println(e);
            }
                
           //System.out.println("Estoque atualizado após criação de blend manual\nVariação de: "+peso+"Kg");
        } catch (Exception e) {
            System.out.println("Falha ao atualizar silo apos blend manual");
            System.out.println(e);
        }
    }
    
    
    private void atualiza_silo_manual_nuvem(float peso, int id_silo){
        //Atualiza estoque de silo após criar blend manual (nuvem)
        consultar_estoque_silos();
        String sql_nuvem = "update tb_silos set qtd_atual=? where id_silo=?";
        try {
            nuvem = ModuloConexaoNuvem.conector();
            
           float peso_update = array_silos[id_silo] - peso;
            try {
                pstNuvem = nuvem.prepareStatement(sql_nuvem);
                pstNuvem.setFloat(1, peso_update);
                pstNuvem.setInt(2, id_silo+1);
                pstNuvem.executeUpdate();
            } catch (Exception e) {
                System.out.println("Falha ao atualizar silo na nuvem apos blend manual");
                System.out.println(e);
            }
                
           //System.out.println("Estoque atualizado após criação de blend manual\nVariação de: "+peso+"Kg");
        } catch (Exception e) {
            System.out.println("Falha ao atualizar silo na nuvem apos blend manual");
            System.out.println(e);
        }
    }
    
    
    //Metodos referentes ao blendador
    private void esta_blendando(){
        boolean [] array_coils;
        try {
            array_coils = m.readCoils(escravo, 1, 1);
            //System.out.println(array_coils[0]);
            if(array_coils[0] == true){
                set_blendando = true;
                btnBlendEnviar.setEnabled(false);
            }
            else{
                lblBlendHeader.setText("BLEND SELECIONADO");
                lblBlendHeader.setForeground(new Color(255,255,255));
                set_blendando = false;
                contador_tempo = 0;
            }
        } catch (Exception e) {
            clp_conectado = false;
            falha_conexao();
            System.out.println("Falha ao checar se está blendando");
            System.out.println(e);
        }
    }
    
    
    private void timer_blendando(){
        contador_tempo++;
        lblBlendHeader.setText("BLENDANDO!");
        lblBlendHeader.setForeground(new Color(255,51,51));
        //System.out.println(contador_tempo);
        if(set_blendando == false){
            set_blendando = false;
        }
    }
    
    
    //Metodos referentes a PROCESSOS GERAIS
    private boolean check_blendador(){
        try {
            boolean [] array_coils;
            array_coils = m.readCoils(escravo, 0, 1);
            
            if(array_coils[0] == false){
                return false;
            }
            else{
                return true;
            }
                                   
        } catch (ModbusIOException | ModbusNumberException | ModbusProtocolException ex) {
            System.out.println(ex);
        }
        return false;
    }
    
    
    private void set_blendador_nuvem_0(){
        nuvem = ModuloConexaoNuvem.conector();
        String sql = "update tb_modbus set STATUS_BLENDADOR = 0 where id_modbus";
        try {
            pstNuvem = nuvem.prepareStatement(sql);
            pstNuvem.executeUpdate();
        } catch (Exception e) {
            System.out.println("Falha ao setar status blendador para 0 (nuvem)");
            System.out.println(e);
        }
    }
    
    
    private void set_blendador_nuvem_1(){
        nuvem = ModuloConexaoNuvem.conector();
        String sql = "update tb_modbus set STATUS_BLENDADOR = 1 where id_modbus";
        try {
            pstNuvem = nuvem.prepareStatement(sql);
            pstNuvem.executeUpdate();
        } catch (Exception e) {
            System.out.println("Falha ao setar status de blendador para 1 (Nuvem)");
            System.out.println(e);
        }
    }
    
    
    private boolean check_modo_blendador(){
        try {
            boolean [] array_coils;
            array_coils = m.readCoils(escravo, 42, 1);
            
            if(array_coils[0] == false){
                return false;
            }
            else{
                return true;
            }
                                   
        } catch (ModbusIOException | ModbusNumberException | ModbusProtocolException ex) {
            System.out.println(ex);
        }
        return false;
    }
    
    
    private boolean check_mexedor(){
        try {
            boolean [] array_coils;
            array_coils = m.readCoils(escravo, Integer.parseInt(MEXEDOR), 1);
            
            if(array_coils[0] == false){
                return false;
            }
            else{
                return true;
            }
                                   
        } catch (ModbusIOException | ModbusNumberException | ModbusProtocolException ex) {
            System.out.println(ex);
        }
        return false;
    }
    
    
    private boolean check_elevador(){
        try {
            boolean [] array_coils;
            array_coils = m.readCoils(escravo, Integer.parseInt(ESTEIRA), 1);
            
            if(array_coils[0] == false){
                return false;
            }
            else{
                return true;
            }
                                   
        } catch (ModbusIOException | ModbusNumberException | ModbusProtocolException ex) {
            System.out.println(ex);
        }
        return false;
    }
    
    
    //Metodos referentes ao CLP
      
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
                    clp_conectado = true;
                    recupera_conexao();
                    limpa_mensagens();
                    caixa_mensagens.insertString(caixa_mensagens.getLength(), "Conexão com CLP iniciada! " , cor_conectado);
                    System.out.println("Conectado ao CLP com sucesso");
                } catch (Exception e) {
                    falha_conexao();
                    System.out.println(e +" Erro na linha 731");
                }
            } catch (ModbusIOException e) {
                falha_conexao();
                //JOptionPane.showMessageDialog(null, "Erro ao se conectar com CLP");
                System.out.println(e);
            } finally {
                try {
                    m.disconnect();
                } catch (ModbusIOException e) {
                   falha_conexao();
                  //JOptionPane.showMessageDialog(null, "Erro ao se conectar com CLP");
                  System.out.println(e);
                }
            }
        } catch (RuntimeException e) {
            falha_conexao();
            throw (e);
        } catch (UnknownHostException e) {
            falha_conexao();
            System.out.println("Erro "+e);
        }
    }
    
    
    private void falha_conexao(){
        footerBlend.setBackground(new Color(255,51,51));
        
        btnBlendPower.setEnabled(false);
        btnBlendMexedor.setEnabled(false);
        btnBlendElevador.setEnabled(false);
        btnBlendManual.setEnabled(false);
        
        btnSilos.setEnabled(false);
        btnModBus.setEnabled(false);
        
        lblBlendHeader.setText("BLEND SELECIONADO");
        lblBlendHeader.setForeground(new Color(255,255,255));
        cbBlendLote.setEnabled(false);
        
        btnBlendSalvar.setEnabled(false);
        btnBlendCancelar.setEnabled(false);
        
        cbBlendOperacao.setEnabled(false);
        btnBlendEnviar.setEnabled(false);
        btnBlendAtual.setEnabled(false);
        tbBlend.setEnabled(false);
        txtBlendPesq.setEnabled(false);        
        lblBlendPesq.setEnabled(false);
        
        btnBlendTarar.setEnabled(false);
        btnBlendNewMeta.setEnabled(false);
        
        
        btnSilo1Abrir.setEnabled(false);
        btnSilo2Abrir.setEnabled(false);
        btnSilo3Abrir.setEnabled(false);
        btnSilo4Abrir.setEnabled(false);
        
        lblSilosOpen1.setVisible(false);
        lblSilosOpen2.setVisible(false);
        lblSilosOpen3.setVisible(false);
        lblSilosOpen4.setVisible(false);
    }
    
    
    private void recupera_conexao(){
        //Volta estilo ao padrão
        footerBlend.setBackground(new Color(68,141,41));
        
        block_campos();
        btnBlendSalvar.setEnabled(true);
        btnBlendCancelar.setEnabled(true);
        cbBlendOperacao.setEnabled(true);
        cbBlendLote.setEnabled(true);
        if(modo_manual == false){
            btnBlendEnviar.setEnabled(true);
            btnBlendMexedor.setEnabled(true);
            btnBlendElevador.setEnabled(true);
        }
        else{
            btnBlendEnviar.setEnabled(false);
            btnBlendMexedor.setEnabled(false);
            btnBlendElevador.setEnabled(false);
        }
        btnBlendManual.setEnabled(true);
        btnBlendAtual.setEnabled(true);
        tbBlend.setEnabled(true);
        txtBlendPesq.setEnabled(true);        
        lblBlendPesq.setEnabled(true);
        
        btnBlendPower.setEnabled(true);
        
        
        btnSilos.setEnabled(true);
        btnModBus.setEnabled(true); 
        lblSilosOpen1.setVisible(false);
        lblSilosOpen2.setVisible(false);
        lblSilosOpen3.setVisible(false);
        lblSilosOpen4.setVisible(false);
        
        if(modo_manual == true){
           btnSilo1Abrir.setEnabled(true);
           btnSilo2Abrir.setEnabled(true);
           btnSilo3Abrir.setEnabled(true);
           btnSilo4Abrir.setEnabled(true); 
        }
        else{
           btnSilo1Abrir.setEnabled(false);
           btnSilo2Abrir.setEnabled(false);
           btnSilo3Abrir.setEnabled(false);
           btnSilo4Abrir.setEnabled(false);  
        }
        
        btnBlendTarar.setEnabled(true);
        btnBlendNewMeta.setEnabled(true);
    }
    
    
    private void buscar_modbus() {
        String sql = "select * from tb_modbus";

        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();

            if (rs.next()) {
                //Valores silos serão os endereços
                VALOR_SILO_1 = rs.getString(2);
                VALOR_SILO_2 = rs.getString(3);
                VALOR_SILO_3 = rs.getString(4);
                VALOR_SILO_4 = rs.getString(5);
                ACC_MEXEDOR = rs.getString(6);
                FATOR = rs.getString(7);
                LASTRO_SECUR = rs.getString(8);
                PESO = rs.getString(9);
                LIGA = rs.getString(10);
                //Silo_ mostrarão se está desligado ou ligado
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
            JOptionPane.showMessageDialog(null, "Falha ao buscar endereçamento de PLC");
            System.out.println(e);
        }
    }
    
    
    public void define_cores_mensagens() {
        StyleConstants.setForeground(cor_mensagem_erro, new Color(205, 0, 0));

        StyleConstants.setForeground(cor_tentando_conectar, new Color(255, 140, 0));

        StyleConstants.setForeground(cor_conectado, new Color(34, 139, 34));
        
        StyleConstants.setForeground(cor_conectado_internet, new Color(10, 10, 10));

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
            System.out.println("Falha ao limpar console!");
        }
    }
    
    //Metodos para sincronizar cadastros e estoque
    
    private void sincronizar(){
        try {
            set_sincronizar_0();
            nuvem = ModuloConexaoNuvem.conector();
            caixa_mensagens.insertString(caixa_mensagens.getLength(), "\nSincronizando dados..." , cor_tentando_conectar);
            ultimo_blend_local();
            ultimo_lote_nuvem();
            consultar_estoque_silos();
            consultar_estoque_grao();
            consultar_estoque_moido();
            consultar_qtd_torrado_lotes();
            ultimo_registro_nuvem();
            sincronizar_blend_local();
            sincronizar_estoque_silos();
            sincronizar_estoque_grao();
            sincronizar_estoque_moido();
            sincronizar_blend_atual();
            sincronizar_registros();
            sincronizar_lotes();
            sincronizar_qtd_torrado_lotes();
            sincronizar_status_blendador();
            caixa_mensagens.insertString(caixa_mensagens.getLength(), "\nDados sincronizados! " , cor_sincronizar);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Falha ao sincronizar dados!");
            System.out.println(e);
        }
    }
    
    
    private void check_sincronizar(){
        String sql = "select SINCRONIZADO from tb_modbus";
        
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            
            if(rs.next()){
                sincronizado = (rs.getInt(1));
                if(sincronizado == 1 && temos_internet == true){
                    sincronizar();
                }
                else if(sincronizado == 0){
                    System.out.println("Sistema sincronizado!");
                }
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Falha ao sincronizar dados ao iniciar");
            System.out.println(e);
        }
    }
      
    
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
    
    private void set_sincronizar_0(){
        String sql = "update tb_modbus set SINCRONIZADO = 0 where id_modbus";
        
        try {
            pst = conexao.prepareStatement(sql);
            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println("Falha ao alterar variavel sincronizado para 0");
            System.out.println(e);
        }
    }
    
    
    private void ultimo_blend_local(){
        String sql = "select max(id_blend) as id from tb_blend";
        
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            
            if(rs.next()){
                ultimo_id_blend_local = rs.getString(1);
            }
        } catch (Exception e) {
            System.out.println("Falha ao obter ultimo id de blend local");
            System.out.println(e);
        }
    }
    
    
    private void sincronizar_blend_local(){
        nuvem = ModuloConexaoNuvem.conector();
        
        String sqlNuvem = "select * from tb_blend where id_blend > ?";
        String sql = "insert into tb_blend (nome, fk_silo1, qtd_silo1, fk_silo2, qtd_silo2, fk_silo3, qtd_silo3, fk_silo4, qtd_silo4) values (?, 1, ?, 2, ?, 3, ?, 4, ?)";
        
        try {
            //Pega dados salvos apenas na nuvem
            pstNuvem = nuvem.prepareStatement(sqlNuvem);
            pstNuvem.setString(1, ultimo_id_blend_local);
            rsNuvem = pstNuvem.executeQuery();
            
            //Insere dados obtidos na nuvem e os insere no local
            pst = conexao.prepareStatement(sql);
            
            while(rsNuvem.next()){
                try {
                    pst.setString(1, rsNuvem.getString(2));
                    pst.setString(2, rsNuvem.getString(4));
                    pst.setString(3, rsNuvem.getString(6));
                    pst.setString(4, rsNuvem.getString(8));
                    pst.setString(5, rsNuvem.getString(10));

                    pst.executeUpdate();
                } catch (Exception e) {
                    System.out.println("Falha ao sincronizar blends (local)!");
                    System.out.println(e);
                }
            }
        } catch (Exception e) {
            System.out.println("Falha ao sincronizar dados (blends) da nuvem para o local");
            System.out.println(e);
        }
    }
    
    
    private void ultimo_lote_nuvem(){
        nuvem = ModuloConexaoNuvem.conector();
        String sql = "select max(id_lote) as id from tb_lotes";
        try {
            pstNuvem = nuvem.prepareStatement(sql);
            rsNuvem = pstNuvem.executeQuery();

            if(rsNuvem.next()){
                ultimo_id_lote_nuvem = rsNuvem.getString(1);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Falha ao obter ultimo registro (nuvem)!");
            System.out.println(e);
        }
    }
    
    
    private void sincronizar_lotes(){
         nuvem = ModuloConexaoNuvem.conector();
        
        String sql = "select * from tb_lotes where id_lote > ?";
        String sql_insert_nuvem = "insert into tb_lotes(nome_lote, num_lote, data_lote, tipo_cafe, qtd_torrado, obs) values (?, ?, ?, ?, ?, ?)";
        
        try {
            //Pega dados salvos apenas localmente
            pst = conexao.prepareStatement(sql);
            pst.setString(1, ultimo_id_lote_nuvem);
            rs = pst.executeQuery();
            
            //Insere dados obtidos na local e os insere na nuvem
            pstNuvem = nuvem.prepareStatement(sql_insert_nuvem);
            
            while(rs.next()){
                try {
                    pstNuvem.setString(1, rs.getString(2));
                    pstNuvem.setString(2, rs.getString(3));
                    pstNuvem.setString(3, rs.getString(4));
                    pstNuvem.setString(4, rs.getString(5));
                    pstNuvem.setString(5, rs.getString(6));
                    pstNuvem.setString(6, rs.getString(7));
                    pstNuvem.executeUpdate();
                    
                } catch (Exception e) {
                    System.out.println("Falha ao sincronizar Lotes (nuvem)!");
                    System.out.println(e);
                }
            }
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao sincronizar lotes (nuvem)!");
            System.out.println(ex);
        }
    }
    
    
    private void consultar_qtd_torrado_lotes(){
        String sql = "select qtd_torrado from tb_lotes";
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            
            qtd_torrado.clear();
            while(rs.next()){
                qtd_torrado.add(rs.getFloat(1));
            }
            
            //System.out.println(qtd_torrado);
        } catch (Exception e) {
            System.out.println("Falha ao consultar qtd_torrado em lotes");
            System.out.println(e);
        }
    }
    
    
    private void sincronizar_qtd_torrado_lotes(){
        String sql = "update tb_lotes set qtd_torrado = ? where id_lote=?";
        //Criando array baseado em tamanho da lista (convertendo lista para array)
        Float []array_qtd_torrado = qtd_torrado.toArray(new Float[0]);
        int tamanho = array_qtd_torrado.length;
        
        //Tamanho -1 pois após conversão, a lista gera 1 indice a mais
        for(int i=0; i<= (tamanho -1); i++){
            try {
                nuvem = ModuloConexaoNuvem.conector();
                pstNuvem = nuvem.prepareStatement(sql);
                
                pstNuvem.setFloat(1, array_qtd_torrado[i]);
                pstNuvem.setInt(2, i+1);

                pstNuvem.executeUpdate();
            } catch (Exception e) {
                System.out.println("Falha ao sincronizar qtd_torrado em lotes (nuvem)"+e);
            }
        }
    }
    
    
    private void sincronizar_status_blendador(){
        if(temos_internet == true){
            nuvem = ModuloConexaoNuvem.conector();
            try {
            boolean []array_coils_nuvem;
            array_coils_nuvem = m.readCoils(escravo, 0, 1);
            
            if(array_coils_nuvem[0] == true && temos_internet == true){
                //System.out.println(array_coils_nuvem[0]);
                set_blendador_nuvem_1();
            }
            else if(temos_internet == true){
                //System.out.println(array_coils_nuvem[0]);
                set_blendador_nuvem_0();
            }
            }catch (ModbusIOException | ModbusNumberException | ModbusProtocolException e) {
                System.out.println("Falha ao sincronizar status do blendador (nuvem)");
                System.out.println(e);
            }
        }
        else{
            precisa_sincrinizar = true;
        }
    }
    
    private void sincronizar_estoque_silos(){
        nuvem = ModuloConexaoNuvem.conector();
        String sql = "update tb_silos set qtd_atual=? where id_silo=?";
        float value = 0;
        
        for(int i =0; i<=3; i++ ){
            try {
                value = array_silos[i];
                pstNuvem = nuvem.prepareStatement(sql);
                
                pstNuvem.setFloat(1, array_silos[i]);
                pstNuvem.setInt(2, i + 1);
                
                //System.out.println(value);
                pstNuvem.executeUpdate();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Falha ao sincronizar dados locais com estoque dos silos (nuvem)");
                System.out.println(e);         
            }
        }
    }
   
    
    private void sincronizar_estoque_grao(){
        nuvem = ModuloConexaoNuvem.conector();
        String sql = "update tb_cafe_grao set quantidade=? where id_cafe_grao";
        
        try {
                pstNuvem = nuvem.prepareStatement(sql);

                //System.out.println(Qtd_estoque_att);
                pstNuvem.setFloat(1, Qtd_estoque);
                pstNuvem.executeUpdate();
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Falha ao atualizar estoque de grãos (nuvem)");
                System.out.println(e); 
            }
    }
    
    
    private void sincronizar_estoque_moido(){
        nuvem = ModuloConexaoNuvem.conector();
        String sql = "update tb_cafe_moido set quantidade=? where id_cafe_moido";
        
        try {
                pstNuvem = nuvem.prepareStatement(sql);
                
                //System.out.println(Qtd_estoque_att);
                pstNuvem.setFloat(1, Qtd_estoque_moido);
                pstNuvem.executeUpdate();
                
            } catch (Exception e) {
               JOptionPane.showMessageDialog(null, "Falha ao atualizar estoque de café moído (nuvem)");
                System.out.println(e); 
        }
    }
    
    
    private void sincronizar_blend_atual(){
       nuvem = ModuloConexaoNuvem.conector();
       consultar_blend_atual();
       String sql = "update tb_blend_atual set id_blend_atual=?, nome=?, qtd_silo1=?, qtd_silo2=?, qtd_silo3=?, qtd_silo4=?, operacao=? where id_blend_atual";
        
        try {
            pstNuvem = nuvem.prepareStatement(sql);
            
            pstNuvem.setString(1,id_atual);
            pstNuvem.setString(2,nome_atual);
            pstNuvem.setFloat(3,Float.parseFloat(qtds1_atual));
            pstNuvem.setFloat(4,Float.parseFloat(qtds2_atual));
            pstNuvem.setFloat(5,Float.parseFloat(qtds3_atual));
            pstNuvem.setFloat(6,Float.parseFloat(qtds4_atual));
            pstNuvem.setString(7,operation);
            
            pstNuvem.executeUpdate();
            
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Falha ao setar blend atual na nuvem");
            System.out.println(e);
        }
    }
    
    
    private void sincronizar_registros(){
        nuvem = ModuloConexaoNuvem.conector();
        
        String sql = "select * from tb_blend_registros where id_registro > ?";
        String sql_insert_nuvem = "insert into tb_blend_registros(nome_blend, nome_cafe1, qtd_cafe1, nome_cafe2, qtd_cafe2, nome_cafe3, qtd_cafe3, nome_cafe4, qtd_cafe4, operacao, data, qtd_total, valor_cafe_cru, valor_cafe_torrado, valor_total, lote) values (?, (select cafe_atual from tb_silos where id_silo = 1), ?, (select cafe_atual from tb_silos where id_silo = 2), ?, (select cafe_atual from tb_silos where id_silo = 3), ?, (select cafe_atual from tb_silos where id_silo = 4), ?, ?, ?, ?, ?, ?, ?,? )";
        try {
            //Pega dados salvos apenas localmente
            pst = conexao.prepareStatement(sql);
            pst.setString(1, ultimo_id_registro_nuvem);
            rs = pst.executeQuery();
            
            //Insere dados obtidos na local e os insere na nuvem
            pstNuvem = nuvem.prepareStatement(sql_insert_nuvem);
            
            while(rs.next()){
                try {
                    pstNuvem.setString(1, rs.getString(2));
                    pstNuvem.setString(2, rs.getString(4));
                    pstNuvem.setString(3, rs.getString(6));
                    pstNuvem.setString(4, rs.getString(8));
                    pstNuvem.setString(5, rs.getString(10));
                    pstNuvem.setString(6, rs.getString(11));
                    pstNuvem.setString(7, rs.getString(12));
                    pstNuvem.setString(8, rs.getString(13));
                    pstNuvem.setString(9, rs.getString(14));
                    pstNuvem.setString(10, rs.getString(15));
                    pstNuvem.setString(11, rs.getString(16));
                    pstNuvem.setString(12, rs.getString(17));

                    pstNuvem.executeUpdate();
                    
                } catch (Exception e) {
                    System.out.println("Falha ao sincronizar Registros (nuvem)!");
                    System.out.println(e);
                }
            }
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Falha ao sincronizar Registros (nuvem)!");
            System.out.println(ex);
        }
    }
    
    private void ultimo_registro_nuvem(){
        nuvem = ModuloConexaoNuvem.conector();
         //1 - Obter ultimo registro da tabela offline
        String sql = "select max(id_registro) as id from tb_blend_registros";
        try {
            pstNuvem = nuvem.prepareStatement(sql);
            rsNuvem = pstNuvem.executeQuery();

            if(rsNuvem.next()){
                ultimo_id_registro_nuvem = rsNuvem.getString(1);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Falha ao obter ultimo registro (nuvem)!");
            System.out.println(e);
        }
    }
    
           
    //Metodos referentes ao Blend
    private void check_blend(){
        String sql = "select * from tb_blend";
        
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            
            if(rs.next()){
                blend = true;
            }
            else{
                blend = false;
            }
        } catch (Exception e) {
            System.out.println("Falha ao checar se há blends cadastrados");
            System.out.println(e);
        }
    }
    
    
    private void consultar_blend_atual(){
        String sql = "select id_blend_atual, nome, qtd_silo1, qtd_silo2, qtd_silo3, qtd_silo4, operacao from tb_blend_atual";
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            
            if(rs.next()){
                id_atual = (rs.getString(1));
                nome_atual = (rs.getString(2));
                qtds1_atual = (rs.getString(3));
                qtds2_atual =(rs.getString(4));
                qtds3_atual =(rs.getString(5));
                qtds4_atual =(rs.getString(6));
                operation = rs.getString(7);
            }
            else{
                caixa_mensagens.insertString(caixa_mensagens.getLength(), "Blend Atual ainda não cadastrado!" , cor_mensagem_erro);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Falha ao buscar blend atual para sincronizar");
            System.out.println(e);
        }
    }
    
    
    private void buscar_blend_atual(){
        //Busca blend atual, ultimo enviado ao plc
        String sql = "select id_blend_atual, nome, qtd_silo1, qtd_silo2, qtd_silo3, qtd_silo4, operacao from tb_blend_atual";
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
                String operacao = rs.getString(7);
                if(operacao.equals("moer")){
                    cbBlendOperacao.setSelectedIndex(1);
                }
                else if (operacao.equals("grao")){
                    cbBlendOperacao.setSelectedIndex(2);
                }
                else{
                    cbBlendOperacao.setSelectedIndex(0);
                }
            }
            else{
                caixa_mensagens.insertString(caixa_mensagens.getLength(), "Blend Atual ainda não cadastrado!" , cor_mensagem_erro);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Falha ao buscar blend atual");
            System.out.println(e);
        }
    }
    
    
    private void buscar_blend(){
        String sql = "select * from tb_blend";
        
        try {
            if(temos_internet == true){
                tbBlendTitulo.setText("RECEITAS CADASTRADAS");
                nuvem = ModuloConexaoNuvem.conector();
                pstNuvem = nuvem.prepareStatement(sql);
                rsNuvem = pstNuvem.executeQuery();
                
                tbBlend.setModel(DbUtils.resultSetToTableModel(rsNuvem));
            }
            else{
                tbBlendTitulo.setText("RECEITAS CADASTRADAS (LOCALMENTE)");
                pst = conexao.prepareStatement(sql);
                rs = pst.executeQuery();
                tbBlend.setModel(DbUtils.resultSetToTableModel(rs));
            }
            
            //Remove campos desnecessários da table
            remove_colunas();
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Falha ao buscar blend");
        }
    }
    
    
    private void pesquisar_blend(){
        String sql = "select * from tb_blend where nome like ?";
        
        try {
            if(temos_internet == true){
                nuvem = ModuloConexaoNuvem.conector();
                pstNuvem = nuvem.prepareStatement(sql);
                
                pstNuvem.setString(1, txtBlendPesq.getText()+'%');
                rsNuvem = pstNuvem.executeQuery();
                
                tbBlend.setModel(DbUtils.resultSetToTableModel(rsNuvem));
            }
            else{
                pst = conexao.prepareStatement(sql);
            
                pst.setString(1,txtBlendPesq.getText()+'%');
                rs = pst.executeQuery();

                tbBlend.setModel(DbUtils.resultSetToTableModel(rs));
            }
            
           
            remove_colunas();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Falha ao pesquisar blend");
        }
        
    }

    
    private void set_campos_blend(){
        try {
            int setar = tbBlend.getSelectedRow();
            
            //lblNomeBlend.setText(tbBlend.getModel().getValueAt(setar,1).toString());
            txtIdBlend.setText(tbBlend.getModel().getValueAt(setar,0).toString());
            txtNomeBlend.setText(tbBlend.getModel().getValueAt(setar,1).toString());
            txtQtdSilo1.setText(tbBlend.getModel().getValueAt(setar,3).toString());
            txtQtdSilo2.setText(tbBlend.getModel().getValueAt(setar,5).toString());
            txtQtdSilo3.setText(tbBlend.getModel().getValueAt(setar,7).toString());
            txtQtdSilo4.setText(tbBlend.getModel().getValueAt(setar,9).toString()); 
            
        } catch (Exception e) {
            System.out.println("Falha ao setar campos da tabela" + e);
        }
        
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
        btnBlendAtual.setEnabled(true);
        cbBlendOperacao.setEnabled(true);
        if(modo_manual == false){
            btnBlendEnviar.setEnabled(true);
        }
        else{
            btnBlendEnviar.setEnabled(false);
        }
        txtStatus.setEditable(false);
        txtIdBlend.setVisible(true);
        txtNomeBlend.setEditable(false);
        txtQtdSilo1.setEditable(false);
        txtQtdSilo2.setEditable(false);
        txtQtdSilo3.setEditable(false);
        txtQtdSilo4.setEditable(false);
        btnBlendNewMeta.setBackground(new Color(255,255,255));
        btnBlendNewMeta.setEnabled(true);
        txtBlendMetaMoido.setEditable(false);
        txtBlendMetaGrao.setEditable(false);
    }
    
    
    private void unlock_manual(){
        if(modo_manual == true){
            btnSilo1Abrir.setEnabled(true);
            btnSilo2Abrir.setEnabled(true);
            btnSilo3Abrir.setEnabled(true);
            btnSilo4Abrir.setEnabled(true); 
            
            btnBlendPower.setEnabled(false);
            btnBlendMexedor.setEnabled(true);
            btnBlendElevador.setEnabled(true);
            
            btnBlendEnviar.setEnabled(false);
            cbBlendOperacao.setEnabled(false);
            btnBlendAtual.setEnabled(false);
        }
        else{
            btnSilo1Abrir.setEnabled(false);
            btnSilo2Abrir.setEnabled(false);
            btnSilo3Abrir.setEnabled(false);
            btnSilo4Abrir.setEnabled(false); 
            
            btnBlendPower.setEnabled(true);
            btnBlendMexedor.setEnabled(false);
            btnBlendElevador.setEnabled(false);
            
            if(blendando == false){
                btnBlendEnviar.setEnabled(true);
            }
            else{
                btnBlendEnviar.setEnabled(true);
            }
            cbBlendOperacao.setEnabled(true);
            btnBlendAtual.setEnabled(true);
        }
    }
    
      
    private void set_blend_atual(){
        //Atualiza/sobrescreve dados da tabela blend atual, para manter o sistema sincronizado com os dados enviados ao PLC
        
        String sql = "update tb_blend_atual set id_blend_atual=?, nome=?, qtd_silo1=?, qtd_silo2=?, qtd_silo3=?, qtd_silo4=?, operacao=? where id_blend_atual";
        
        try {
            pst = conexao.prepareStatement(sql);
            
            IdBlend = txtIdBlend.getText();
            NomeBlend = txtNomeBlend.getText();
            QtdSilo1 = txtQtdSilo1.getText();
            QtdSilo2 = txtQtdSilo2.getText();
            QtdSilo3 = txtQtdSilo3.getText();
            QtdSilo4 = txtQtdSilo4.getText();
            int operacao = cbBlendOperacao.getSelectedIndex();
            
            pst.setString(1,IdBlend);
            pst.setString(2,NomeBlend);
            pst.setFloat(3,Float.parseFloat(QtdSilo1));
            pst.setFloat(4,Float.parseFloat(QtdSilo2));
            pst.setFloat(5,Float.parseFloat(QtdSilo3));
            pst.setFloat(6,Float.parseFloat(QtdSilo4));
            if(operacao == 1){
                pst.setString(7, "moer");
            }
            else if(operacao == 2){
               pst.setString(7, "grao"); 
            }
            
            pst.executeUpdate();
            
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Falha ao setar blend atual");
            System.out.println(e);
        }
    }
    
    
    private void set_blend_atual_nuvem(){
        String sql = "update tb_blend_atual set id_blend_atual=?, nome=?, qtd_silo1=?, qtd_silo2=?, qtd_silo3=?, qtd_silo4=? where id_blend_atual";
        
        try {
            pstNuvem = nuvem.prepareStatement(sql);
            
            IdBlend = txtIdBlend.getText();
            NomeBlend = txtNomeBlend.getText();
            QtdSilo1 = txtQtdSilo1.getText();
            QtdSilo2 = txtQtdSilo2.getText();
            QtdSilo3 = txtQtdSilo3.getText();
            QtdSilo4 = txtQtdSilo4.getText();
            
            pstNuvem.setString(1,IdBlend);
            pstNuvem.setString(2,NomeBlend);
            pstNuvem.setFloat(3,Float.parseFloat(QtdSilo1));
            pstNuvem.setFloat(4,Float.parseFloat(QtdSilo2));
            pstNuvem.setFloat(5,Float.parseFloat(QtdSilo3));
            pstNuvem.setFloat(6,Float.parseFloat(QtdSilo4));
            
            pstNuvem.executeUpdate();
            
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Falha ao setar blend atual na nuvem");
            System.out.println(e);
        }
    }
    
    
    //Metodos relacionados aos lotes
    private void ultimo_lote_usado(){
        //Inicia combobox com ultimo lote utilizado
        String sql = "select id_lote from tb_lotes where num_lote = (select num_lote_atual from tb_lote_atual)";
        
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            
            if(rs.next()){
               cbBlendLote.setSelectedIndex(rs.getInt(1));
               
               String selected = cbBlendLote.getSelectedItem().toString();
               //System.out.println(selected);
            }
            else{
                JOptionPane.showMessageDialog(null, "Nenhum lote disponível!");
            }
        } catch (Exception e) {
            System.out.println("Falha ao setar lote utilizado em cbBlendLotes");
            System.out.println(e);
        }
        
    }
    
    private void set_lotes(){
        //Popula combobox com lotes criados
        String sql = "select nome_lote from tb_lotes";
        
        try {
            //Reseta combobox antes de preenche-la
            cbBlendLote.removeAllItems();
            cbBlendLote.addItem("SELECIONE UM LOTE...");
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            
            while(rs.next()){
                String nome_lote = rs.getString(1);
                cbBlendLote.addItem(nome_lote);
            }
            ultimo_lote_usado();
            
        } catch (Exception e) {
            System.out.println("Falha ao popular combobox com lotes salvos");
            System.out.println(e);
        }
    }
    
    private void atualizar_qtd_torrado_lote(float QtdTotal){
        //Atualiza td_torrado em lote selecionado pelo usuário(após enviar blend apenas)
        //Permitindo que usuário reutilize lotes
        String sql = "update tb_lotes set qtd_torrado = (select qtd_torrado + ?) where nome_lote = ?";
        
        try {
            String selected = cbBlendLote.getSelectedItem().toString();
            
            pst = conexao.prepareStatement(sql);
            pst.setFloat(1, QtdTotal);
            pst.setString(2, selected);
            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println("Falha ao atualizar qtd_torrado em lotes");
            System.out.println(e);
        }
    }
    
    
    //Faz a mesma coisa que o método de cima, porém para a núvem
    private void atualizar_qtd_torrado_lote_nuvem(float QtdTotal){
        String sql = "update tb_lotes set qtd_torrado = (select qtd_torrado + ?) where nome_lote = ?";
        
        try {
            String selected = cbBlendLote.getSelectedItem().toString();
            
            pstNuvem = nuvem.prepareStatement(sql);
            pstNuvem.setFloat(1, QtdTotal);
            pstNuvem.setString(2, selected);
            pstNuvem.executeUpdate();
            System.out.println("ATUALIZADO TORRADO NUVEM");
        } catch (Exception e) {
            System.out.println("Falha ao atualizar qtd_torrado em lotes (nuvem)");
            System.out.println(e);
        }
    }
    
    
    private void set_lote_atual_nuvem(){
        String selected = cbBlendLote.getSelectedItem().toString();
        
        //Atualiza lote baseando-se no nome do lote selecionado na combobox
        String sql = "update tb_lote_atual set num_lote_atual = (select num_lote from tb_lotes where nome_lote = ?), nome_lote_atual = ? where id_lote_atual";
        
        try {
            pstNuvem = nuvem.prepareStatement(sql);
            pstNuvem.setString(1, selected);
            pstNuvem.setString(2, selected);
            pstNuvem.executeUpdate();
            
        } catch (Exception e) {
            System.out.println("Falha ao atualizar Lote Atual");
            System.out.println(e);
        }
    }
    
    
    private void set_lote_atual(){
        String selected = cbBlendLote.getSelectedItem().toString();
        
        //Atualiza lote baseando-se no nome do lote selecionado na combobox
        String sql = "update tb_lote_atual set num_lote_atual = (select num_lote from tb_lotes where nome_lote = ?), nome_lote_atual = ? where id_lote_atual";
        
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, selected);
            pst.setString(2, selected);
            pst.executeUpdate();
            
        } catch (Exception e) {
            System.out.println("Falha ao atualizar Lote Atual");
            System.out.println(e);
        }
    }
    
    
    //Metodos relacionados aos registros
    private void consultar_lote_atual(){
        String sql = "select num_lote_atual from tb_lote_atual";
        
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            if(rs.next()){
                lote = rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("Falha ao obter id de lote atual");
        }
    }
    
    
    
    private void salvar_registro_blend(float QtdTotal, float ValorTotalCru, float ValorTotalTorrado){
        consultar_lote_atual();
        
        String sql="insert into tb_blend_registros( nome_blend, nome_Cafe1, qtd_cafe1, nome_cafe2, qtd_cafe2, nome_cafe3, qtd_cafe3, nome_cafe4, qtd_cafe4, operacao, qtd_total, valor_cafe_cru, valor_cafe_torrado, valor_total, lote) values (?, (select cafe_atual from tb_silos where id_silo = 1) , ?, (select cafe_atual from tb_silos where id_silo = 2), ?, (select cafe_atual from tb_silos where id_silo = 3), ?, (select cafe_atual from tb_silos where id_silo = 4), ?, ?, "+QtdTotal+", "+ValorTotalCru+", "+ValorTotalTorrado+", ?, ?)";
        
        ValorTotal = 0;
        
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
                
                ValorTotal = ValorTotalCru + ValorTotalTorrado;
                
                pst.setFloat(7, ValorTotal);
                pst.setInt(8, lote);
                
                pst.executeUpdate();
                
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Falha ao salvar registro de Blend!");
            System.out.println(e);
        }
    }
    
    
    private void salvar_registro_bend_nuvem(float QtdTotal, float ValorTotalCru, float ValorTotalTorrado){
        consultar_lote_atual();
        
        String sql="insert into tb_blend_registros( nome_blend, nome_Cafe1, qtd_cafe1, nome_cafe2, qtd_cafe2, nome_cafe3, qtd_cafe3, nome_cafe4, qtd_cafe4, operacao, qtd_total, valor_cafe_cru, valor_cafe_torrado, valor_total, lote) values (?, (select cafe_atual from tb_silos where id_silo = 1) , ?, (select cafe_atual from tb_silos where id_silo = 2), ?, (select cafe_atual from tb_silos where id_silo = 3), ?, (select cafe_atual from tb_silos where id_silo = 4), ?, ?, "+QtdTotal+", "+ValorTotalCru+", "+ValorTotalTorrado+", ?, ?)";
        
        ValorTotal = 0;
        
        try {
                nuvem = ModuloConexaoNuvem.conector();
                pstNuvem = nuvem.prepareStatement(sql);
            
                NomeBlend = txtNomeBlend.getText();
                QtdSilo1 = txtQtdSilo1.getText();
                QtdSilo2 = txtQtdSilo2.getText();
                QtdSilo3 = txtQtdSilo3.getText();
                QtdSilo4 = txtQtdSilo4.getText();
                String Moer = "moer e empacotar";
                String Grao = "empacotar inteiro";
                    
                pstNuvem.setString(1,NomeBlend);
                pstNuvem.setFloat(2,Float.parseFloat(QtdSilo1));
                pstNuvem.setFloat(3,Float.parseFloat(QtdSilo2));
                pstNuvem.setFloat(4,Float.parseFloat(QtdSilo3));
                pstNuvem.setFloat(5,Float.parseFloat(QtdSilo4));
            
                if(cbBlendOperacao.getSelectedIndex() == 1){
                    pstNuvem.setString(6,Moer);
                }
                else if(cbBlendOperacao.getSelectedIndex() == 2){
                    pstNuvem.setString(6,Grao);
                }
                
                ValorTotal = ValorTotalCru + ValorTotalTorrado;
                
                pstNuvem.setFloat(7, ValorTotal);
                pstNuvem.setInt(8, lote);
                
                pstNuvem.executeUpdate();
                
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Falha ao salvar registro de Blend na Nuvem!");
            System.out.println(e);
        }
    }
       
    
    
    private void consultar_estoque_silos(){
        String sql = "select qtd_atual from tb_silos";
        array_silos = new float[4];       
        int i = 0;
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            
            while (rs.next()) {                
                array_silos[i] = (rs.getFloat(1));
                //System.out.println(array_silos[i]);
                i++;
            }
            System.out.println("Estoque dos silos consultado!");
            
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Falha ao consultar estoque dos silos");
            System.out.println(e);
        }
    }
    
    
    private void consultar_estoque_silos_nuvem() {
        //Fix
        nuvem = ModuloConexaoNuvem.conector();
        
        String sql = "select qtd_atual from tb_silos";
        //5 pois tem 1 cadastro a mais sem relação com o blend
        array_silos_nuvem = new float[5];       
        int i = 0;
        try {
            pstNuvem = nuvem.prepareStatement(sql);
            rsNuvem = pstNuvem.executeQuery();
            
            while (rsNuvem.next()) {                
                array_silos_nuvem[i] = (rsNuvem.getFloat(1));
                //System.out.println(array_silos_nuvem[i]);
                i++;
            }
            
            System.out.println("Estoque dos silos consultado!(nuvem)");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Falha ao consultar estoque dos silos da nuvem");
            System.out.println(e);
        }
    }
    
    
    private void atualizar_estoque_silos(float[] qtd_silos){
        //consultar_estoque_silos();
        String sql = "update tb_silos set qtd_atual=? where id_silo=?";
        float value = 0;
        for (int i = 0; i <= 3; i++) {
            try {
                //value = array_silos[i];
                pst = conexao.prepareStatement(sql);
                
                try {
                    pst.setFloat(1, array_silos[i] - qtd_silos[i]);
                    System.out.println("Primeiro parametro: ");
                    System.out.println(array_silos[i] - qtd_silos[i]);
                    pst.setInt(2, i + 1);
                    System.out.println("Segundo parametro:");
                    System.out.println(i+1); 
                    pst.executeUpdate();
                } catch (Exception e) {
                    falha_estq_silo = true;
                    System.out.println("ERRO RUIM"+e);
                }
                
                
                System.out.println("Estoque dos silos atualizado");
                //java.sql.SQLException: Parameter index out of range (2 > number of parameters, which is 0).
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Falha ao atualizar estoque dos silos");
                falha_estq_silo = true;
                System.out.println(e);         
            }
        }
    }
    
    
    private void atualizar_estoque_silos_nuvem(float[] qtd_silos){
        
        consultar_estoque_silos_nuvem();
        String sql = "update tb_silos set qtd_atual=? where id_silo=?";
        float value = 0;
        for (int i = 0; i <= 3; i++) {
            try {
                value = array_silos_nuvem[i];
                pstNuvem = nuvem.prepareStatement(sql);
                
                pstNuvem.setFloat(1, array_silos_nuvem[i] - qtd_silos[i]);
                System.out.println("Primeiro parametro (nuvem): ");
                System.out.println(array_silos_nuvem[i] - qtd_silos[i]);
                pstNuvem.setInt(2, i + 1);
                System.out.println("Segundo parametro (nuvem): ");
                System.out.println(i+1);
                
                System.out.println("Estoque dos silos atualizado (nuvem)");
                pstNuvem.executeUpdate();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Falha ao atualizar estoque dos silos (nuvem)");
                falha_estq_silo = true;
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
                JOptionPane.showMessageDialog(null, "Nenhum grao cadastrado!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Falha ao consultar estoque de grãos");
            System.out.println(e);  
        }
    }
    
    
    private void consultar_estoque_grao_nuvem(){
          String sql = "select quantidade from tb_cafe_grao";
          
          try {
            pstNuvem = nuvem.prepareStatement(sql);
            rsNuvem = pstNuvem.executeQuery();
            
            if(rsNuvem.next()){
                Qtd_estoque = rsNuvem.getFloat(1);
            }
            else{
                return;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Falha ao consultar estoque de grãos (nuvem)");
            System.out.println(e);  
        }
    }
    
    
    private void consultar_estoque_moido(){
        String sql = "select quantidade from tb_cafe_moido";
          
          try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            
            if(rs.next()){
                Qtd_estoque_moido = rs.getFloat(1);
            }
            else{
                JOptionPane.showMessageDialog(null, "Nenhum café cadastrado no estoque!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Falha ao consultar estoque de café moído");
            System.out.println(e); 
        }
    }
    
    
    private void consultar_estoque_moido_nuvem(){
        String sql = "select quantidade from tb_cafe_moido";
          
          try {
            pstNuvem = nuvem.prepareStatement(sql);
            rsNuvem = pstNuvem.executeQuery();
            
            if(rsNuvem.next()){
                Qtd_estoque_moido = rsNuvem.getFloat(1);
            }
            else{
                return;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Falha ao consultar estoque de café moído (nuvem)");
            System.out.println(e); 
        }
    }
    

    private void atualizar_estoque_grao(float QtdTotal){
        consultar_estoque_grao();
        String sql = "update tb_cafe_grao set quantidade=? where id_cafe_grao";
        try {
                pst = conexao.prepareStatement(sql);
                
                Qtd_estoque_att = Qtd_estoque + QtdTotal;
                
                //System.out.println(Qtd_estoque_att);
                pst.setFloat(1, Qtd_estoque_att);
                pst.executeUpdate();
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Falha ao atualizar estoque de grãos");
                System.out.println(e); 
            }
    }
    
    
    private void atualizar_estoque_grao_nuvem(float QtdTotal){
        consultar_estoque_grao_nuvem();
        String sql = "update tb_cafe_grao set quantidade=? where id_cafe_grao";
        try {
                pstNuvem = nuvem.prepareStatement(sql);
                
                Qtd_estoque_att = Qtd_estoque + QtdTotal;
                
                //System.out.println(Qtd_estoque_att);
                pstNuvem.setFloat(1, Qtd_estoque_att);
                pstNuvem.executeUpdate();
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Falha ao atualizar estoque de grãos");
                System.out.println(e); 
            }
    }
    
    
    private void atualizar_estoque_moido(float QtdTotal){
        consultar_estoque_moido();
        String sql = "update tb_cafe_moido set quantidade=? where id_cafe_moido";
        
        try {
                pst = conexao.prepareStatement(sql);
                
                Qtd_estoque_att = Qtd_estoque_moido + QtdTotal;
                
                //System.out.println(Qtd_estoque_att);
                pst.setFloat(1, Qtd_estoque_att);
                pst.executeUpdate();
                
            } catch (Exception e) {
               JOptionPane.showMessageDialog(null, "Falha ao atualizar estoque de café moído");
                System.out.println(e); 
            }
    }
    
    
    private void atualizar_estoque_moido_nuvem(float QtdTotal){
        consultar_estoque_moido_nuvem();
        String sql = "update tb_cafe_moido set quantidade=? where id_cafe_moido";
        
        try {
                pstNuvem = nuvem.prepareStatement(sql);
                
                Qtd_estoque_att = Qtd_estoque_moido + QtdTotal;
                
                //System.out.println(Qtd_estoque_att);
                pstNuvem.setFloat(1, Qtd_estoque_att);
                pstNuvem.executeUpdate();
                
            } catch (Exception e) {
               JOptionPane.showMessageDialog(null, "Falha ao atualizar estoque de café moído (nuvem)");
                System.out.println(e); 
            }
    }
   
    
    private boolean checa_silos(float []qtd_silos){
        try {
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
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Falha ao checar capacidade dos silos!");
                System.out.println(e);
            }
        
        return false;
    }
    
    
    private boolean checa_silos_nuvem(float []qtd_silos){
        try {
            consultar_estoque_silos_nuvem();
            int sem_estoque[] = new int[4];
            for(int i = 0; i <= 3; i++){
                if(array_silos_nuvem[i]<qtd_silos[i]){
                    sem_estoque[i] = i+1;
                }
            }

            for(int i=0; i<=3; i++){
                if(sem_estoque[i]!=0){
                    return false;
                }
            }

            return true;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Falha ao checar capacidade dos silos (nuvem)!");
                System.out.println(e);
            }
        
        return false;
    }
    
    
    //Metodos relacionados ao preco do blend
    private void consultar_preco_kg_cru(){
        String sql = "select preco_kg_cru from tb_silos";
        
        preco_kg_cru = new float[5];       
        int i = 0;
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
        
        while (rs.next()) {                
            preco_kg_cru[i] = (rs.getFloat(1));
            //System.out.println("preco por quilo do cafe cru"+ preco_kg_cru[i]);
            i++;
        }
        } catch (Exception e) {
            System.out.println("falha ao consultar preço por kilo do café cru");
        }
    }
    
    private void consultar_preco_kg_torrado(){
        String sql = "select preco_kg_torrado from tb_silos";
        
        preco_kg_torrado = new float[5];       
        int i = 0;
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
        
        while (rs.next()) {                
            preco_kg_torrado[i] = (rs.getFloat(1));
            i++;
        }
        } catch (Exception e) {
            System.out.println("falha ao consultar preço por kilo do café torrado");
        }
    }
    
    
    private void gerar_preco_cru(float []qtd_silos){
        consultar_preco_kg_cru();
        ValorCru = 0;
        float []preco_total_cru = new float[4];
        float soma_preco_cru = 0;
        
        try {
            for(int i=0; i<=3; i++){
                preco_total_cru[i] = preco_kg_cru[i] * qtd_silos[i];
                //System.out.println(preco_total_cru[i]);
                
                soma_preco_cru += preco_total_cru[i];
            }
            ValorCru = soma_preco_cru;
            System.out.println("2 - Preço total do café crú usado no blend: "+soma_preco_cru);
            
        } catch (Exception e) {
            System.out.println("Erro ao gerar preco total do cru no blend "+ e);
        }
    }
    
    
    private void gerar_preco_torrado(float []qtd_silos){
        consultar_preco_kg_torrado();
        ValorTorrado = 0;
        float []preco_total_torrado = new float[4];
        float soma_preco_torrado = 0;
        
        try {
            for(int i=0; i<=3; i++){
                preco_total_torrado[i] = preco_kg_torrado[i] * qtd_silos[i];
                //System.out.println(preco_total_cru[i]);
                
                soma_preco_torrado += preco_total_torrado[i];
            }
            ValorTorrado = soma_preco_torrado;
            System.out.println("1 - Preço total do café torrado usado no blend: "+soma_preco_torrado);
            
        } catch (Exception e) {
            System.out.println("Erro ao gerar preco total do torrado no blend "+ e);
        }
    }
    
    
    private void enviar_para_clp(){
        // Envia dados do blend para o PLC
        if(clp_conectado == false){
            block_campos();
            falha_conexao();
            try {
                caixa_mensagens.insertString(caixa_mensagens.getLength(), "Tentando se reconectar com clp" , cor_tentando_conectar);
                JOptionPane.showMessageDialog(null, "Conexao com CLP perdia, reconectando...");
                conecta_com_clp();
            } catch (Exception e) {
                System.out.println("Não foi possível se conectar com CLP!");
                JOptionPane.showMessageDialog(null, "Não foi possível se conectar com CLP!");
            }
        }
        else if(blendador_ligado == 0){
            JOptionPane.showMessageDialog(null, "Inicie um ciclo para enviar dados!");
        }
        else if(cbBlendLote.getSelectedIndex() == 0){
            JOptionPane.showMessageDialog(null, "Selecione um lote!");
        }
        else{
            try {
            // 1 - Pega valores de quantidade de cada silo
            cafe_silo1 = txtQtdSilo1.getText();
            cafe_silo2 = txtQtdSilo2.getText();
            cafe_silo3 = txtQtdSilo3.getText();
            cafe_silo4= txtQtdSilo4.getText();
            
            // 2.1 - Gera uma quantidade total de café
            QtdTotal = Float.parseFloat(cafe_silo1) + Float.parseFloat(cafe_silo2) + Float.parseFloat(cafe_silo3) + Float.parseFloat(cafe_silo4);
            // Gera um array com as quantidades individuais de cada silo
            qtd_silos = null;
            qtd_silos = new float[]{Float.parseFloat(cafe_silo1), Float.parseFloat(cafe_silo2), Float.parseFloat(cafe_silo3), Float.parseFloat(cafe_silo4)};           
            
            //2.2 - Gera preços totais do cafe usado no blend
            //passa array de quantidades para metodo de gerar preço
            gerar_preco_torrado(qtd_silos);
            gerar_preco_cru(qtd_silos);
            
            if(temos_internet == true){
                //3 - Checa estoque de silos e verifica se é suficiente
                if(checa_silos(qtd_silos) == false && checa_silos_nuvem(qtd_silos) == false){
                    return;
                }
                else if(checa_silos(qtd_silos) == true && checa_silos_nuvem(qtd_silos) == true){
                    
                    // 4 - Retira quantidades do estoque
                    atualizar_estoque_silos(qtd_silos);
                    atualizar_estoque_silos_nuvem(qtd_silos);
                    
                    
                    //Fix temporário para erro desconhecido (falha att estq silos)
                    if(falha_estq_silo == false){
                        // 5.1 - Seta blend atual (local e/ou nuvem)
                        set_blend_atual();
                        set_blend_atual_nuvem();

                        // 5.2 - Atualiza qtd_torrado em lotes (soma quantidades)
                        atualizar_qtd_torrado_lote(QtdTotal);
                        atualizar_qtd_torrado_lote_nuvem(QtdTotal);

                        // 5.3 - Atualiza lote atual por aquele selecionado pelo usuario
                        set_lote_atual();
                        set_lote_atual_nuvem();

                        //6 - Salva dados do blend em registro
                        salvar_registro_blend(QtdTotal, ValorCru, ValorTorrado);
                        salvar_registro_bend_nuvem(QtdTotal, ValorCru, ValorTorrado);

                        //7 - Atualiza quantidade nos silos de cafe moido e inteiro de acordo com operação
                        if(cbBlendOperacao.getSelectedIndex() == 1){
                            atualizar_estoque_moido(QtdTotal);
                            atualizar_estoque_moido_nuvem(QtdTotal);
                            m.writeSingleRegister(escravo, 47, 0);
                        }
                        else if(cbBlendOperacao.getSelectedIndex() == 2){
                            atualizar_estoque_grao(QtdTotal);
                            atualizar_estoque_grao_nuvem(QtdTotal);
                            m.writeSingleRegister(escravo, 47, 1);
                        }


                        //4 - Envia dados do blend para PLC
                        m.writeSingleRegister(escravo, Integer.parseInt(VALOR_SILO_1), Math.round(Float.parseFloat(cafe_silo1)*10));
                        m.writeSingleRegister(escravo, Integer.parseInt(VALOR_SILO_2), Math.round(Float.parseFloat(cafe_silo2)*10));
                        m.writeSingleRegister(escravo, Integer.parseInt(VALOR_SILO_3), Math.round(Float.parseFloat(cafe_silo3)*10));
                        m.writeSingleRegister(escravo, Integer.parseInt(VALOR_SILO_4), Math.round(Float.parseFloat(cafe_silo4)*10));

                        caixa_mensagens.insertString(caixa_mensagens.getLength(), "Dados enviados com sucesso!" , cor_novo_processo);
                        //System.out.println(ValorCru+"e"+ValorTorrado);
                        JOptionPane.showMessageDialog(null, "Dados enviados com sucesso!");
                        System.out.println("Dados enviados com sucesso!");
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "Falha ao enviar dados! Tente novamente!");
                        falha_estq_silo = false;
                    }
                    
                }
            }
            

            //Operação no modo OFFLINE
            
            else if(temos_internet == false){
                set_sincronizar_1();
                precisa_sincrinizar = true;
                
                //3 - Checa estoque de silos e verifica se é suficiente
                if(checa_silos(qtd_silos) == false){
                    return;
                }
                else if(checa_silos(qtd_silos) == true){
                    // 4 - Retira quantidades do estoque
                    atualizar_estoque_silos(qtd_silos);
                    
                    if(falha_estq_silo == false){
                        // 5.1 - Seta blend atual (local e/ou nuvem)
                        set_blend_atual();

                        //5.2 - Atualiza qtd de torrado em lote
                        atualizar_qtd_torrado_lote(QtdTotal);

                        //5.3 - Atualiza lote atual por aquele selecionado pelo usuario
                        set_lote_atual();


                        //6 - Salva dados do blend em registro
                        salvar_registro_blend(QtdTotal ,ValorCru, ValorTorrado);

                        //7 - Atualiza quantidade nos silos de cafe moido e inteiro de acordo com operação
                        if(cbBlendOperacao.getSelectedIndex() == 1){
                            atualizar_estoque_moido(QtdTotal);
                            m.writeSingleRegister(escravo, 47, 0);
                        }
                        else if(cbBlendOperacao.getSelectedIndex() == 2){
                            atualizar_estoque_grao(QtdTotal);
                            m.writeSingleRegister(escravo, 47, 1);
                        }

                        //4 - Envia dados do blend para PLC
                        m.writeSingleRegister(escravo, Integer.parseInt(VALOR_SILO_1), Math.round(Float.parseFloat(cafe_silo1)*10));
                        m.writeSingleRegister(escravo, Integer.parseInt(VALOR_SILO_2), Math.round(Float.parseFloat(cafe_silo2)*10));
                        m.writeSingleRegister(escravo, Integer.parseInt(VALOR_SILO_3), Math.round(Float.parseFloat(cafe_silo3)*10));
                        m.writeSingleRegister(escravo, Integer.parseInt(VALOR_SILO_4), Math.round(Float.parseFloat(cafe_silo4)*10));

                        caixa_mensagens.insertString(caixa_mensagens.getLength(), "Dados enviados com sucesso!" , cor_novo_processo);
                        JOptionPane.showMessageDialog(null, "Dados enviados com sucesso!");
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "Falha ao enviar dados! Tente novamente");
                    }
                }
                    
            }
            
          
            } catch (ModbusProtocolException | ModbusNumberException | ModbusIOException | BadLocationException ex) {
                JOptionPane.showMessageDialog(null, "Falha ao enviar dados para CLP!");
                System.out.println(ex);
            } catch(Exception e){
                JOptionPane.showMessageDialog(null, "Não foi possivel enviar os dados para o PLC!");
                System.out.println(e);
            }
        }
    }
    
    
    private void set_meta(){
        String MetaMoido = txtBlendMetaMoido.getText();
        String MetaGrao = txtBlendMetaMoido.getText();
        
        if(MetaMoido.isEmpty() || MetaGrao.isEmpty()){
            JOptionPane.showMessageDialog(null, "Preencha os valores das metas!");
            block_campos();
        }
        else if(Integer.parseInt(MetaMoido) < 1 || Integer.parseInt(MetaGrao) < 1){
            JOptionPane.showMessageDialog(null, "Insira um valor válido para as metas!");
            block_campos();
        }
        else{
            try {
                m.writeSingleRegister(escravo, 40, Integer.parseInt(MetaMoido));
                m.writeSingleRegister(escravo, 41, Integer.parseInt(MetaGrao));
                JOptionPane.showMessageDialog(null, "Nova meta definida com sucesso!");
                
                btnBlendNewMeta.setBackground(new Color(255, 255, 255));
                
                txtBlendMetaMoido.setEditable(false);
                txtBlendMetaGrao.setEditable(false);
            } catch (Exception e) {
                falha_conexao();
                JOptionPane.showMessageDialog(null, "Falha ao definir nova meta!");
                System.out.println(e);
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

        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        btnSilos = new javax.swing.JButton();
        btnModBus = new javax.swing.JButton();
        lblBlendWifi = new javax.swing.JLabel();
        lblWifiDesc = new javax.swing.JLabel();
        jPanel41 = new javax.swing.JPanel();
        jPanel43 = new javax.swing.JPanel();
        jPanel44 = new javax.swing.JPanel();
        jPanel45 = new javax.swing.JPanel();
        jPanel46 = new javax.swing.JPanel();
        jPanel47 = new javax.swing.JPanel();
        jPanel48 = new javax.swing.JPanel();
        jPanel49 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        btnLotes = new javax.swing.JButton();
        lblNomeBlend = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        lblBlendHeader = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btnBlendSalvar = new javax.swing.JButton();
        btnBlendCancelar = new javax.swing.JButton();
        btnBlendAdd1 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtStatus = new javax.swing.JTextPane();
        btnBlendTarar = new javax.swing.JButton();
        btnBlendNewMeta = new javax.swing.JButton();
        btnBlendManual = new javax.swing.JButton();
        btnBlendLimpar = new javax.swing.JButton();
        btnEmergencia = new javax.swing.JButton();
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
        lblSilosOpen3 = new javax.swing.JLabel();
        btnSilo3Abrir = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        txtNomeBlend = new javax.swing.JTextField();
        txtIdBlend = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        cbBlendLote = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        txtBlendPesq = new javax.swing.JTextField();
        lblBlendPesq = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbBlend = new javax.swing.JTable();
        tbBlendTitulo = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        txtQtdSilo1 = new javax.swing.JTextField();
        lblSilosOpen1 = new javax.swing.JLabel();
        btnSilo1Abrir = new javax.swing.JButton();
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
        lblSilosOpen4 = new javax.swing.JLabel();
        btnSilo4Abrir = new javax.swing.JButton();
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
        lblSilosOpen2 = new javax.swing.JLabel();
        btnSilo2Abrir = new javax.swing.JButton();
        footerBlend = new javax.swing.JPanel();
        lblData = new javax.swing.JLabel();
        jPanel42 = new javax.swing.JPanel();
        btnBlendEnviar = new javax.swing.JButton();
        cbBlendOperacao = new javax.swing.JComboBox<>();
        btnBlendAtual = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        txtBlendMetaGrao = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtBlendMetaMoido = new javax.swing.JTextField();
        jPanel40 = new javax.swing.JPanel();
        lblBlendPeso = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel50 = new javax.swing.JPanel();
        jPanel51 = new javax.swing.JPanel();
        btnBlendMexedor = new javax.swing.JButton();
        jPanel52 = new javax.swing.JPanel();
        jPanel53 = new javax.swing.JPanel();
        btnBlendElevador = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        btnBlendPower = new javax.swing.JButton();

        jMenuItem1.setText("jMenuItem1");

        jMenu1.setText("jMenu1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Actual Soluções - Produção de Blend");
        setIconImage(Toolkit.getDefaultToolkit().getImage(TelaPrincipal.class.getResource("/br/com/blend/icones/coffee-bean (1).png"))
        );
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(25, 42, 86));
        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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
        jPanel1.add(btnSilos, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 3, 67, 74));

        btnModBus.setBackground(new java.awt.Color(25, 42, 86));
        btnModBus.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnModBus.setForeground(new java.awt.Color(255, 255, 255));
        btnModBus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/blend/icones/cpu (1).png"))); // NOI18N
        btnModBus.setBorderPainted(false);
        btnModBus.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnModBus.setFocusPainted(false);
        btnModBus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModBusActionPerformed(evt);
            }
        });
        jPanel1.add(btnModBus, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 3, -1, 74));

        lblBlendWifi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/blend/icones/no-wifi.png"))); // NOI18N
        jPanel1.add(lblBlendWifi, new org.netbeans.lib.awtextra.AbsoluteConstraints(1084, 0, 80, 80));

        lblWifiDesc.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblWifiDesc.setForeground(new java.awt.Color(255, 51, 51));
        lblWifiDesc.setText("* Operando OFFLINE");
        jPanel1.add(lblWifiDesc, new org.netbeans.lib.awtextra.AbsoluteConstraints(1190, 30, 180, -1));

        jPanel41.setBackground(new java.awt.Color(255, 255, 255));
        jPanel41.setPreferredSize(new java.awt.Dimension(2, 100));

        jPanel43.setBackground(new java.awt.Color(255, 255, 255));
        jPanel43.setPreferredSize(new java.awt.Dimension(2, 100));

        javax.swing.GroupLayout jPanel43Layout = new javax.swing.GroupLayout(jPanel43);
        jPanel43.setLayout(jPanel43Layout);
        jPanel43Layout.setHorizontalGroup(
            jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );
        jPanel43Layout.setVerticalGroup(
            jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 52, Short.MAX_VALUE)
        );

        jPanel44.setBackground(new java.awt.Color(255, 255, 255));
        jPanel44.setPreferredSize(new java.awt.Dimension(2, 100));

        jPanel45.setBackground(new java.awt.Color(255, 255, 255));
        jPanel45.setPreferredSize(new java.awt.Dimension(2, 100));

        javax.swing.GroupLayout jPanel45Layout = new javax.swing.GroupLayout(jPanel45);
        jPanel45.setLayout(jPanel45Layout);
        jPanel45Layout.setHorizontalGroup(
            jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );
        jPanel45Layout.setVerticalGroup(
            jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel44Layout = new javax.swing.GroupLayout(jPanel44);
        jPanel44.setLayout(jPanel44Layout);
        jPanel44Layout.setHorizontalGroup(
            jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel44Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel45, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel44Layout.setVerticalGroup(
            jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 74, Short.MAX_VALUE)
            .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel44Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel45, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        javax.swing.GroupLayout jPanel41Layout = new javax.swing.GroupLayout(jPanel41);
        jPanel41.setLayout(jPanel41Layout);
        jPanel41Layout.setHorizontalGroup(
            jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel41Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel43, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel41Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel44, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel41Layout.setVerticalGroup(
            jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 74, Short.MAX_VALUE)
            .addGroup(jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel41Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel43, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
                    .addContainerGap()))
            .addGroup(jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel41Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel44, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jPanel1.add(jPanel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 3, -1, 74));

        jPanel46.setBackground(new java.awt.Color(255, 255, 255));
        jPanel46.setPreferredSize(new java.awt.Dimension(2, 100));

        jPanel47.setBackground(new java.awt.Color(255, 255, 255));
        jPanel47.setPreferredSize(new java.awt.Dimension(2, 100));

        javax.swing.GroupLayout jPanel47Layout = new javax.swing.GroupLayout(jPanel47);
        jPanel47.setLayout(jPanel47Layout);
        jPanel47Layout.setHorizontalGroup(
            jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );
        jPanel47Layout.setVerticalGroup(
            jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 52, Short.MAX_VALUE)
        );

        jPanel48.setBackground(new java.awt.Color(255, 255, 255));
        jPanel48.setPreferredSize(new java.awt.Dimension(2, 100));

        jPanel49.setBackground(new java.awt.Color(255, 255, 255));
        jPanel49.setPreferredSize(new java.awt.Dimension(2, 100));

        javax.swing.GroupLayout jPanel49Layout = new javax.swing.GroupLayout(jPanel49);
        jPanel49.setLayout(jPanel49Layout);
        jPanel49Layout.setHorizontalGroup(
            jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );
        jPanel49Layout.setVerticalGroup(
            jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel48Layout = new javax.swing.GroupLayout(jPanel48);
        jPanel48.setLayout(jPanel48Layout);
        jPanel48Layout.setHorizontalGroup(
            jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel48Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel49, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel48Layout.setVerticalGroup(
            jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 74, Short.MAX_VALUE)
            .addGroup(jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel48Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel49, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        javax.swing.GroupLayout jPanel46Layout = new javax.swing.GroupLayout(jPanel46);
        jPanel46.setLayout(jPanel46Layout);
        jPanel46Layout.setHorizontalGroup(
            jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel46Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel47, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel46Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel48, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel46Layout.setVerticalGroup(
            jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 74, Short.MAX_VALUE)
            .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel46Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel47, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
                    .addContainerGap()))
            .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel46Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel48, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jPanel1.add(jPanel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 3, -1, 74));

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
            .addGap(0, 52, Short.MAX_VALUE)
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
            .addGap(0, 74, Short.MAX_VALUE)
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

        jPanel1.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 3, -1, 74));

        btnLotes.setBackground(new java.awt.Color(25, 42, 86));
        btnLotes.setForeground(new java.awt.Color(255, 255, 255));
        btnLotes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/blend/icones/coffee-bean (1).png"))); // NOI18N
        btnLotes.setBorderPainted(false);
        btnLotes.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLotes.setFocusPainted(false);
        btnLotes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLotesActionPerformed(evt);
            }
        });
        jPanel1.add(btnLotes, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 0, 80, 80));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1620, 80));

        lblNomeBlend.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        getContentPane().add(lblNomeBlend, new org.netbeans.lib.awtextra.AbsoluteConstraints(458, 119, -1, -1));

        jPanel2.setBackground(new java.awt.Color(25, 42, 86));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblBlendHeader.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        lblBlendHeader.setForeground(new java.awt.Color(255, 255, 255));
        lblBlendHeader.setText("BLEND SELECIONADO");
        jPanel2.add(lblBlendHeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 10, -1, -1));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 150, 990, 50));

        jPanel3.setBackground(new java.awt.Color(25, 42, 86));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnBlendSalvar.setBackground(new java.awt.Color(255, 255, 255));
        btnBlendSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/blend/icones/save_48px.png"))); // NOI18N
        btnBlendSalvar.setToolTipText("Salvar alterações");
        btnBlendSalvar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBlendSalvar.setFocusPainted(false);
        btnBlendSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBlendSalvarActionPerformed(evt);
            }
        });
        jPanel3.add(btnBlendSalvar, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 90, 90));
        btnBlendSalvar.getAccessibleContext().setAccessibleName("Salvar");
        btnBlendSalvar.getAccessibleContext().setAccessibleDescription("Salvar");

        btnBlendCancelar.setBackground(new java.awt.Color(255, 255, 255));
        btnBlendCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/blend/icones/cancel_48px.png"))); // NOI18N
        btnBlendCancelar.setToolTipText("Cancelar");
        btnBlendCancelar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBlendCancelar.setFocusPainted(false);
        btnBlendCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBlendCancelarActionPerformed(evt);
            }
        });
        jPanel3.add(btnBlendCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 10, 90, 90));
        btnBlendCancelar.getAccessibleContext().setAccessibleName("Cancelar");

        btnBlendAdd1.setBackground(new java.awt.Color(255, 255, 255));
        btnBlendAdd1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/blend/icones/info.png"))); // NOI18N
        btnBlendAdd1.setToolTipText("Informações");
        btnBlendAdd1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBlendAdd1.setFocusPainted(false);
        btnBlendAdd1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBlendAdd1ActionPerformed(evt);
            }
        });
        jPanel3.add(btnBlendAdd1, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 120, 90, 90));

        jScrollPane3.setViewportView(txtStatus);

        jPanel3.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 510, 200, 150));

        btnBlendTarar.setBackground(new java.awt.Color(255, 255, 255));
        btnBlendTarar.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnBlendTarar.setText("TARAR BALANÇA");
        btnBlendTarar.setBorderPainted(false);
        btnBlendTarar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBlendTarar.setFocusPainted(false);
        btnBlendTarar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBlendTararActionPerformed(evt);
            }
        });
        jPanel3.add(btnBlendTarar, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 430, 200, 50));

        btnBlendNewMeta.setBackground(new java.awt.Color(255, 255, 255));
        btnBlendNewMeta.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnBlendNewMeta.setText("NOVA META");
        btnBlendNewMeta.setBorderPainted(false);
        btnBlendNewMeta.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBlendNewMeta.setFocusPainted(false);
        btnBlendNewMeta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBlendNewMetaActionPerformed(evt);
            }
        });
        jPanel3.add(btnBlendNewMeta, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 370, 200, 50));

        btnBlendManual.setBackground(new java.awt.Color(255, 255, 255));
        btnBlendManual.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnBlendManual.setText("MODO MANUAL");
        btnBlendManual.setBorderPainted(false);
        btnBlendManual.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBlendManual.setFocusPainted(false);
        btnBlendManual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBlendManualActionPerformed(evt);
            }
        });
        jPanel3.add(btnBlendManual, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 310, 200, 50));

        btnBlendLimpar.setBackground(new java.awt.Color(255, 255, 255));
        btnBlendLimpar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/blend/icones/erase_48px.png"))); // NOI18N
        btnBlendLimpar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBlendLimpar.setFocusPainted(false);
        btnBlendLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBlendLimparActionPerformed(evt);
            }
        });
        jPanel3.add(btnBlendLimpar, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 90, 90));

        btnEmergencia.setBackground(new java.awt.Color(255, 51, 51));
        btnEmergencia.setFont(new java.awt.Font("Segoe UI", 1, 19)); // NOI18N
        btnEmergencia.setForeground(new java.awt.Color(250, 250, 250));
        btnEmergencia.setText("EMERGENCIA");
        btnEmergencia.setBorderPainted(false);
        btnEmergencia.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEmergencia.setFocusPainted(false);
        btnEmergencia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmergenciaActionPerformed(evt);
            }
        });
        jPanel3.add(btnEmergencia, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, 200, 60));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1060, 90, 240, 680));

        jPanel11.setBackground(new java.awt.Color(72, 126, 176));
        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2), "SILO 3", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 20), new java.awt.Color(255, 255, 255))); // NOI18N
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
        jPanel11.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 130, -1, 20));

        txtQtdSilo3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtQtdSilo3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtQtdSilo3.setToolTipText("");
        jPanel11.add(txtQtdSilo3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 160, 150, 30));

        lblSilosOpen3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/blend/icones/bol1.gif"))); // NOI18N
        jPanel11.add(lblSilosOpen3, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 20, -1, -1));

        btnSilo3Abrir.setBackground(new java.awt.Color(68, 141, 41));
        btnSilo3Abrir.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnSilo3Abrir.setForeground(new java.awt.Color(250, 250, 250));
        btnSilo3Abrir.setText("ABRIR");
        btnSilo3Abrir.setBorderPainted(false);
        btnSilo3Abrir.setFocusPainted(false);
        btnSilo3Abrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSilo3AbrirActionPerformed(evt);
            }
        });
        jPanel11.add(btnSilo3Abrir, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 60, 130, 45));

        getContentPane().add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 270, 240, 210));

        jPanel14.setBackground(new java.awt.Color(72, 126, 176));
        jPanel14.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtNomeBlend.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jPanel14.add(txtNomeBlend, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 10, 256, -1));

        txtIdBlend.setEditable(false);
        txtIdBlend.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jPanel14.add(txtIdBlend, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 10, 40, -1));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("LOTE");
        jPanel14.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 10, -1, 30));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("NOME DO BLEND");
        jPanel14.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, 30));

        cbBlendLote.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbBlendLote.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SELECIONE UM LOTE..." }));
        cbBlendLote.setToolTipText("Selecionar Lote");
        jPanel14.add(cbBlendLote, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 10, 210, 30));

        getContentPane().add(jPanel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 210, 990, 50));

        jPanel4.setBackground(new java.awt.Color(72, 126, 176));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtBlendPesq.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBlendPesqKeyReleased(evt);
            }
        });
        jPanel4.add(txtBlendPesq, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, 256, 32));

        lblBlendPesq.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblBlendPesq.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/blend/icones/proc.png"))); // NOI18N
        lblBlendPesq.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblBlendPesq.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblBlendPesqMouseClicked(evt);
            }
        });
        jPanel4.add(lblBlendPesq, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 30, 30));

        jButton1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton1.setText("BUSCAR");
        jButton1.setPreferredSize(new java.awt.Dimension(87, 31));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 10, 100, -1));

        getContentPane().add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 610, 640, 50));

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
        tbBlend.setToolTipText("Receitas cadastradas");
        tbBlend.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
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

        jPanel5.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 621, 110));

        tbBlendTitulo.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        tbBlendTitulo.setForeground(new java.awt.Color(255, 255, 255));
        tbBlendTitulo.setText("RECEITAS CADASTRADAS");
        jPanel5.add(tbBlendTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        getContentPane().add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 670, 640, 180));

        jPanel16.setBackground(new java.awt.Color(72, 126, 176));
        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2), "SILO 1", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 20), new java.awt.Color(255, 255, 255))); // NOI18N
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
        jPanel16.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 130, -1, -1));

        txtQtdSilo1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtQtdSilo1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel16.add(txtQtdSilo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 160, 150, -1));

        lblSilosOpen1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/blend/icones/bol1.gif"))); // NOI18N
        jPanel16.add(lblSilosOpen1, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 20, -1, -1));

        btnSilo1Abrir.setBackground(new java.awt.Color(68, 141, 41));
        btnSilo1Abrir.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnSilo1Abrir.setForeground(new java.awt.Color(250, 250, 250));
        btnSilo1Abrir.setText("ABRIR");
        btnSilo1Abrir.setBorderPainted(false);
        btnSilo1Abrir.setFocusPainted(false);
        btnSilo1Abrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSilo1AbrirActionPerformed(evt);
            }
        });
        jPanel16.add(btnSilo1Abrir, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 60, 130, 45));

        getContentPane().add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 270, 240, 210));

        jPanel24.setBackground(new java.awt.Color(72, 126, 176));
        jPanel24.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2), "SILO 4", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 20), new java.awt.Color(255, 255, 255))); // NOI18N
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
        txtQtdSilo4.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel24.add(txtQtdSilo4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 160, 150, -1));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("QUANTIDADE (Kg)");
        jPanel24.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 130, -1, -1));

        lblSilosOpen4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/blend/icones/bol1.gif"))); // NOI18N
        jPanel24.add(lblSilosOpen4, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 20, -1, -1));

        btnSilo4Abrir.setBackground(new java.awt.Color(68, 141, 41));
        btnSilo4Abrir.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnSilo4Abrir.setForeground(new java.awt.Color(250, 250, 250));
        btnSilo4Abrir.setText("ABRIR");
        btnSilo4Abrir.setBorderPainted(false);
        btnSilo4Abrir.setFocusPainted(false);
        btnSilo4Abrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSilo4AbrirActionPerformed(evt);
            }
        });
        jPanel24.add(btnSilo4Abrir, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 60, 130, 45));

        getContentPane().add(jPanel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 270, 240, 210));

        jPanel32.setBackground(new java.awt.Color(72, 126, 176));
        jPanel32.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2), "SILO 2", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 20), new java.awt.Color(255, 255, 255))); // NOI18N
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
        txtQtdSilo2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel32.add(txtQtdSilo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 160, 150, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("QUANTIDADE (Kg)");
        jPanel32.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 130, -1, -1));

        lblSilosOpen2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/blend/icones/bol1.gif"))); // NOI18N
        jPanel32.add(lblSilosOpen2, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 20, -1, -1));

        btnSilo2Abrir.setBackground(new java.awt.Color(68, 141, 41));
        btnSilo2Abrir.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnSilo2Abrir.setForeground(new java.awt.Color(250, 250, 250));
        btnSilo2Abrir.setText("ABRIR");
        btnSilo2Abrir.setBorderPainted(false);
        btnSilo2Abrir.setFocusPainted(false);
        btnSilo2Abrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSilo2AbrirActionPerformed(evt);
            }
        });
        jPanel32.add(btnSilo2Abrir, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 60, 130, 45));

        getContentPane().add(jPanel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 270, 240, 210));

        footerBlend.setBackground(new java.awt.Color(68, 141, 41));
        footerBlend.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblData.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblData.setForeground(new java.awt.Color(255, 255, 255));
        lblData.setText("Data - Observação");
        footerBlend.add(lblData, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 0, 920, 50));

        getContentPane().add(footerBlend, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 870, 1400, 60));

        jPanel42.setBackground(new java.awt.Color(25, 42, 86));
        jPanel42.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnBlendEnviar.setBackground(new java.awt.Color(255, 51, 51));
        btnBlendEnviar.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnBlendEnviar.setForeground(new java.awt.Color(255, 255, 255));
        btnBlendEnviar.setText("Enviar para CLP");
        btnBlendEnviar.setToolTipText("Enviar dados ao CLP");
        btnBlendEnviar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBlendEnviar.setFocusPainted(false);
        btnBlendEnviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBlendEnviarActionPerformed(evt);
            }
        });
        jPanel42.add(btnBlendEnviar, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 20, 230, 70));

        cbBlendOperacao.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cbBlendOperacao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SELECIONE UMA OPERAÇÃO...", "TRADICIONAL MOÍDO", "GRÃO" }));
        cbBlendOperacao.setToolTipText("Selecionar operação");
        jPanel42.add(cbBlendOperacao, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 100, 230, 40));

        btnBlendAtual.setBackground(new java.awt.Color(255, 255, 255));
        btnBlendAtual.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnBlendAtual.setText("ULTIMO BLEND ENVIADO");
        btnBlendAtual.setToolTipText("Ultimo blend enviado");
        btnBlendAtual.setFocusPainted(false);
        btnBlendAtual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBlendAtualActionPerformed(evt);
            }
        });
        jPanel42.add(btnBlendAtual, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 150, 230, 40));

        getContentPane().add(jPanel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 610, 340, 240));

        jPanel7.setBackground(new java.awt.Color(25, 42, 86));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel7.add(txtBlendMetaGrao, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 10, 100, 31));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(240, 240, 240));
        jLabel7.setText("META GRÃO:");
        jPanel7.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 0, -1, 50));

        getContentPane().add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 90, 340, 50));

        jPanel13.setBackground(new java.awt.Color(25, 42, 86));
        jPanel13.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(240, 240, 240));
        jLabel3.setText("META MOÍDO:");
        jPanel13.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 0, -1, 50));
        jPanel13.add(txtBlendMetaMoido, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 10, 100, 31));

        getContentPane().add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 90, 360, 50));

        jPanel40.setBackground(new java.awt.Color(25, 42, 86));
        jPanel40.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblBlendPeso.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        lblBlendPeso.setForeground(new java.awt.Color(240, 240, 240));
        lblBlendPeso.setText("00,0");
        jPanel40.add(lblBlendPeso, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 0, -1, 50));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(240, 240, 240));
        jLabel2.setText("PESO:");
        jPanel40.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, -1, 50));

        getContentPane().add(jPanel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, 270, 50));

        jPanel50.setBackground(new java.awt.Color(72, 126, 176));
        jPanel50.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2), "MEXEDOR", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 18), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel50.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel51.setBackground(new java.awt.Color(72, 126, 176));
        jPanel51.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel50.add(jPanel51, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 490, 240, 120));

        btnBlendMexedor.setBackground(new java.awt.Color(68, 141, 41));
        btnBlendMexedor.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnBlendMexedor.setForeground(new java.awt.Color(255, 255, 255));
        btnBlendMexedor.setText("MEXEDOR");
        btnBlendMexedor.setBorderPainted(false);
        btnBlendMexedor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBlendMexedor.setFocusPainted(false);
        btnBlendMexedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBlendMexedorActionPerformed(evt);
            }
        });
        jPanel50.add(btnBlendMexedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 40, 140, 42));

        getContentPane().add(jPanel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 490, 330, 110));

        jPanel52.setBackground(new java.awt.Color(72, 126, 176));
        jPanel52.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2), "ELEVADOR", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 18), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel52.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel53.setBackground(new java.awt.Color(72, 126, 176));
        jPanel53.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel52.add(jPanel53, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 490, 240, 120));

        btnBlendElevador.setBackground(new java.awt.Color(68, 141, 41));
        btnBlendElevador.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnBlendElevador.setForeground(new java.awt.Color(255, 255, 255));
        btnBlendElevador.setText("ELEVADOR");
        btnBlendElevador.setBorderPainted(false);
        btnBlendElevador.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBlendElevador.setFocusPainted(false);
        btnBlendElevador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBlendElevadorActionPerformed(evt);
            }
        });
        jPanel52.add(btnBlendElevador, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 40, 140, 40));

        getContentPane().add(jPanel52, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 490, 320, 110));

        jPanel12.setBackground(new java.awt.Color(72, 126, 176));
        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2), "INICIAR CICLO", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 18), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnBlendPower.setBackground(new java.awt.Color(68, 141, 41));
        btnBlendPower.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        btnBlendPower.setForeground(new java.awt.Color(255, 255, 255));
        btnBlendPower.setText("INICIAR");
        btnBlendPower.setBorderPainted(false);
        btnBlendPower.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBlendPower.setFocusPainted(false);
        btnBlendPower.setPreferredSize(new java.awt.Dimension(97, 73));
        btnBlendPower.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBlendPowerActionPerformed(evt);
            }
        });
        jPanel12.add(btnBlendPower, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 40, 140, 42));

        getContentPane().add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 490, 320, 110));

        setSize(new java.awt.Dimension(1410, 967));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents


    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        //Bloqueia campos
        //block_campos();
    }//GEN-LAST:event_formWindowActivated

    private void btnSilosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSilosActionPerformed
        // Chama tela senha Silos
        TelaSenhaSilos senhaSilos = new TelaSenhaSilos();
        senhaSilos.setVisible(true);
    }//GEN-LAST:event_btnSilosActionPerformed

    private void btnModBusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModBusActionPerformed
        // Chama tela do ModBus
        TelaSenhaModbus senhaModbus = new TelaSenhaModbus();
        senhaModbus.setVisible(true);
    }//GEN-LAST:event_btnModBusActionPerformed

    private void txtBlendPesqKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBlendPesqKeyReleased
        // Chama função para pesquisar blend criado
        pesquisar_blend();
    }//GEN-LAST:event_txtBlendPesqKeyReleased

    private void tbBlendMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbBlendMouseClicked
        // Chama função para preencher os campos de texto e muda o metodo
        Metodo = "pesquisar";
        if(modo_manual == true){
            return;
        }
        else{
            set_campos_blend();
        }
    }//GEN-LAST:event_tbBlendMouseClicked

    private void tbBlendKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbBlendKeyReleased
        
    }//GEN-LAST:event_tbBlendKeyReleased

    private void btnBlendSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBlendSalvarActionPerformed
        // Checa o metodo e chama a função correspondente
        if(Metodo == "pesquisar"){
            
        }
        else{
            if(Metodo == "new_meta"){
                set_meta();
            }
            else if(Metodo == null){
                return;
            }
            else{
                JOptionPane.showMessageDialog(null, "Erro inesperado ao salvar!");
            }
        }
    }//GEN-LAST:event_btnBlendSalvarActionPerformed

    private void btnBlendEnviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBlendEnviarActionPerformed
      //Checa se operação está selecionada e envia dados
        if(cbBlendOperacao.getSelectedIndex() == 0){
            JOptionPane.showMessageDialog(null, "Selecione uma operação!");
        }
        else if(blend == false){
            JOptionPane.showMessageDialog(null, "Selecione uma receita cadastrada!");
        }
        else {
            //exibe_msg_enviando();
            enviar_para_clp();
        }
    }//GEN-LAST:event_btnBlendEnviarActionPerformed

    private void btnBlendAdd1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBlendAdd1ActionPerformed
        TelaAjuda ajuda = new TelaAjuda();
        ajuda.setVisible(true);
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

    private void lblBlendPesqMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblBlendPesqMouseClicked
        // Busca blends registrados
        buscar_blend();
    }//GEN-LAST:event_lblBlendPesqMouseClicked

    private void btnBlendAtualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBlendAtualActionPerformed
        // Busca ultimo blend e operação enviado ao CLP
        buscar_blend_atual();
    }//GEN-LAST:event_btnBlendAtualActionPerformed

    private void btnBlendPowerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBlendPowerActionPerformed
        // Desliga/Liga Blendador
            switch (blendador_ligado) {
            case 0:
                blendador_ligado = 1;
            {
                try {
                    m.writeSingleCoil(escravo, 0, true);
                    btnBlendPower.setText("PARAR");
                    btnBlendPower.setBackground(new Color(255,51,51));
                } catch (ModbusProtocolException | ModbusNumberException | ModbusIOException ex) {
                    falha_conexao();
                    clp_conectado = false;
                    Logger.getLogger(TelaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
                break;
            case 1:
                int confirma = JOptionPane.showConfirmDialog(null,"Tem certeza de que quer parar o ciclo?","Atenção",JOptionPane.YES_NO_OPTION);
                if(confirma == JOptionPane.YES_OPTION){
                    blendador_ligado = 0;
                    try {
                        m.writeSingleCoil(escravo, 0, false);
                        btnBlendPower.setText("INICIAR");
                        btnBlendPower.setBackground(new Color(68,141,41));
                    } catch (ModbusProtocolException | ModbusNumberException | ModbusIOException ex) {
                        falha_conexao();
                        clp_conectado = false;
                        Logger.getLogger(TelaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                }
            }
    }//GEN-LAST:event_btnBlendPowerActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        //Função para buscar blends
        buscar_blend();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnBlendTararActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBlendTararActionPerformed
        // Tara o peso da balança
        try {
            m.writeSingleRegister(escravo, Integer.parseInt(PESO), 0);
        } catch (Exception e) {
            falha_conexao();
            clp_conectado = false;
            System.out.println("Falha ao tarar balança");
            System.out.println(e);
        }
    }//GEN-LAST:event_btnBlendTararActionPerformed

    private void btnEmergenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmergenciaActionPerformed
        // Causa PARADA DE EMERGENCIA
        int confirma = JOptionPane.showConfirmDialog(null,"Tem certeza de que quer parar todos os processos?","Atenção",JOptionPane.YES_NO_OPTION);
        if(confirma == JOptionPane.YES_OPTION){
            try {
                m.writeSingleCoil(escravo, Integer.parseInt(EMERGENCIA), true);
            } catch (Exception e) {
                falha_conexao();
                System.out.println("Falha ao parar processos, EMERGENCIA");
                System.out.println(e);
            }
        }
    }//GEN-LAST:event_btnEmergenciaActionPerformed

    private void btnSilo1AbrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSilo1AbrirActionPerformed
        // Abre ou fecha silo 1
        switch (silo1) {
            case 0:
            {
                silo1 = 1;
                silo_1_abriu = true;
                try {
                    m.writeSingleCoil(escravo, Integer.parseInt(SILO_1), true);
                    btnSilo1Abrir.setText("FECHAR");
                    btnSilo1Abrir.setBackground(new Color(255,51,51));
                    lblSilosOpen1.setVisible(true);
                } catch (ModbusProtocolException | ModbusNumberException | ModbusIOException ex) {
                    System.out.println(ex);
                }
            }
                break;
            case 1:
                silo1 = 0;
                silo_1_fechou = true;
                try {
                    m.writeSingleCoil(escravo, Integer.parseInt(SILO_1), false);
                    btnSilo1Abrir.setText("ABRIR");
                    btnSilo1Abrir.setBackground(new Color(68,141,41));
                    lblSilosOpen1.setVisible(false);
                } catch (ModbusProtocolException | ModbusNumberException | ModbusIOException ex) {
                    System.out.println(ex);
                }
                break;
            }
    }//GEN-LAST:event_btnSilo1AbrirActionPerformed

    private void btnSilo2AbrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSilo2AbrirActionPerformed
        // Abre ou fecha silo 2
        switch (silo2) {
            case 0:
            {
                silo2 = 1;
                silo_2_abriu = true;
                try {
                    m.writeSingleCoil(escravo, Integer.parseInt(SILO_2), true);
                    btnSilo2Abrir.setText("FECHAR");
                    btnSilo2Abrir.setBackground(new Color(255,51,51));
                    lblSilosOpen2.setVisible(true);
                } catch (ModbusProtocolException | ModbusNumberException | ModbusIOException ex) {
                    System.out.println(ex);
                }
            }
                break;
            case 1:
                silo2 = 0;
                silo_2_fechou = true;
                try {
                    m.writeSingleCoil(escravo, Integer.parseInt(SILO_2), false);
                    btnSilo2Abrir.setText("ABRIR");
                    btnSilo2Abrir.setBackground(new Color(68,141,41));
                    lblSilosOpen2.setVisible(false);
                } catch (ModbusProtocolException | ModbusNumberException | ModbusIOException ex) {
                    System.out.println(ex);
                }
                break;
            }
    }//GEN-LAST:event_btnSilo2AbrirActionPerformed

    private void btnSilo3AbrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSilo3AbrirActionPerformed
        // Abre ou fecha silo 3
        switch (silo3) {
            case 0:
            {
                silo3 = 1;
                silo_3_abriu = true;
                try {
                    m.writeSingleCoil(escravo, Integer.parseInt(SILO_3), true);
                    btnSilo3Abrir.setText("FECHAR");
                    btnSilo3Abrir.setBackground(new Color(255,51,51));
                    lblSilosOpen3.setVisible(true);
                } catch (ModbusProtocolException | ModbusNumberException | ModbusIOException ex) {
                    System.out.println(ex);
                }
            }
                break;
            case 1:
                silo3 = 0;
                silo_3_fechou = true;
                try {
                     m.writeSingleCoil(escravo, Integer.parseInt(SILO_3), false);
                    btnSilo3Abrir.setText("ABRIR");
                    btnSilo3Abrir.setBackground(new Color(68,141,41));
                    lblSilosOpen3.setVisible(false);
                } catch (ModbusProtocolException | ModbusNumberException | ModbusIOException ex) {
                    Logger.getLogger(TelaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
    }//GEN-LAST:event_btnSilo3AbrirActionPerformed

    private void btnSilo4AbrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSilo4AbrirActionPerformed
        // Abrir ou fechar silo 4
        switch (silo4) {
            case 0:
            {
                silo4 = 1;
                silo_4_abriu = true;
                try {
                    m.writeSingleCoil(escravo, Integer.parseInt(SILO_4), true);
                    btnSilo4Abrir.setText("FECHAR");
                    btnSilo4Abrir.setBackground(new Color(255,51,51));
                    lblSilosOpen4.setVisible(true);
                } catch (ModbusProtocolException | ModbusNumberException | ModbusIOException ex) {
                    System.out.println(ex);
                }
            }
                break;
            case 1:
                silo4 = 0;
                silo_4_fechou = true;
                try {
                     m.writeSingleCoil(escravo, Integer.parseInt(SILO_4), false);
                    btnSilo4Abrir.setText("ABRIR");
                    btnSilo4Abrir.setBackground(new Color(68,141,41));
                    lblSilosOpen4.setVisible(false);
                } catch (ModbusProtocolException | ModbusNumberException | ModbusIOException ex) {
                    System.out.println(ex);
                }
                break;
            }
    }//GEN-LAST:event_btnSilo4AbrirActionPerformed

    private void btnBlendNewMetaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBlendNewMetaActionPerformed
        // Edita metas
        Metodo = "new_meta";
        btnBlendNewMeta.setBackground(new Color(198,198,198));
        btnBlendEnviar.setEnabled(false);
        txtBlendMetaMoido.setEditable(true);
        txtBlendMetaGrao.setEditable(true);
    }//GEN-LAST:event_btnBlendNewMetaActionPerformed

    private void btnLotesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLotesActionPerformed
        // Direciona para tela de lotes
        TelaLotes lotes = new TelaLotes();
        lotes.setVisible(true);
    }//GEN-LAST:event_btnLotesActionPerformed

    private void btnBlendManualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBlendManualActionPerformed
        // ATIVA/DESATIVA MODO MANUAL
        switch (manual) {
            case 0:
            {
               if(blendador_ligado == 1){
                   JOptionPane.showMessageDialog(null, "Pare o ciclo para ativar modo manual");
               }
               else{
                   try {
                       m.writeSingleCoil(escravo, 42, true);
                       manual = 1;
                       modo_manual = true;
                       btnBlendManual.setText("MODO AUTOMÁTICO");
                       unlock_manual();
                       /*txtQtdSilo1.setText(null);
                       txtQtdSilo2.setText(null);
                       txtQtdSilo3.setText(null);
                       txtQtdSilo4.setText(null);*/
                   } catch (Exception e) {
                       falha_conexao();
                       JOptionPane.showMessageDialog(null, "Falha ao ativar modo manual");
                       System.out.println(e);
                   }
                    
               }
               //System.out.println("MODO MANUAL ATIVADO");
            }
                break;
            case 1:
                if(mexedor == 1){
                    JOptionPane.showMessageDialog(null, "Desligue o mexedor para ativar modo automático");
                }
                else if(elevador == 1){
                    JOptionPane.showMessageDialog(null, "Desligue o elevador para ativar modo automático");
                }
                else{
                    try {
                        m.writeSingleCoil(escravo, 42, false);
                        manual = 0;
                        modo_manual = false;
                        btnBlendManual.setText("MODO MANUAL");
                        unlock_manual();
                        //System.out.println("MODO MANUAL DESATIVADO");
                    } catch (Exception e) {
                       falha_conexao();
                       JOptionPane.showMessageDialog(null, "Falha ao ativar modo manual");
                       System.out.println(e);
                    }
                }
                break;
            }
    }//GEN-LAST:event_btnBlendManualActionPerformed

    private void btnBlendMexedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBlendMexedorActionPerformed
        // Liga/Desliga mexedor
        switch (mexedor) {
            case 0:
            {   
                mexedor = 1;
                try {
                    m.writeSingleCoil(escravo, Integer.parseInt(MEXEDOR), true);
                    btnBlendMexedor.setText("DESLIGAR");
                    btnBlendMexedor.setBackground(new Color(255,51,51));
                } catch (ModbusProtocolException | ModbusNumberException | ModbusIOException ex) {
                    falha_conexao();
                    System.out.println(ex);
                }
                
                //System.out.println("Mexedor ligado");
            }
                break;
            case 1:
                mexedor = 0;
                try {
                    m.writeSingleCoil(escravo, Integer.parseInt(MEXEDOR), false);
                    btnBlendMexedor.setText("LIGAR");
                    btnBlendMexedor.setBackground(new Color(68,141,41));
                } catch (ModbusProtocolException | ModbusNumberException | ModbusIOException ex) {
                    falha_conexao();
                    System.out.println(ex);
                }
                //System.out.println("Mexedor desligado");
                break;
        }
    }//GEN-LAST:event_btnBlendMexedorActionPerformed

    private void btnBlendElevadorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBlendElevadorActionPerformed
        // Ativa/Desativa elevador
        switch (elevador) {
            case 0:
            {   
                elevador = 1;
                try {
                    m.writeSingleCoil(escravo, Integer.parseInt(ESTEIRA), true);
                    btnBlendElevador.setText("DESLIGAR");
                    btnBlendElevador.setBackground(new Color(255,51,51));
                } catch (ModbusProtocolException | ModbusNumberException | ModbusIOException ex) {
                    falha_conexao();
                    System.out.println(ex);
                }
                
                //System.out.println("Elevador ligado");
            }
                break;
            case 1:
                elevador = 0;
                try {
                    m.writeSingleCoil(escravo, Integer.parseInt(ESTEIRA), false);
                    btnBlendElevador.setText("LIGAR");
                    btnBlendElevador.setBackground(new Color(68,141,41));
                    
                    if(gerou_blend_manual == true){
                        if(temos_internet == true){
                            gerar_registro_manual(qtd_cafe1, qtd_cafe2, qtd_cafe3, qtd_cafe4, qtd_total);
                            gerar_registro_manual_nuvem(qtd_cafe1, qtd_cafe2, qtd_cafe3, qtd_cafe4, qtd_total);
                            //resetando variveis para gerar novo relatório posteriormente
                            reset_variaveis_registro();
                        }
                        else{
                            gerar_registro_manual(qtd_cafe1, qtd_cafe2, qtd_cafe3, qtd_cafe4, qtd_total);
                            reset_variaveis_registro();
                            set_sincronizar_1();
                            precisa_sincrinizar = true;
                        }
                        
                        try {
                            caixa_mensagens.insertString(caixa_mensagens.getLength(), "\nBlend manual concuído! " , cor_conectado_internet);
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                } catch (ModbusProtocolException | ModbusNumberException | ModbusIOException ex) {
                    falha_conexao();
                    System.out.println(ex);
                }
                //System.out.println("Elevador desligado");
                break;
        }
    }//GEN-LAST:event_btnBlendElevadorActionPerformed

    private void btnBlendLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBlendLimparActionPerformed
        // Limpa caixa de mensagens
        limpa_mensagens();
        if(clp_conectado == true){
            try {
                caixa_mensagens.insertString(caixa_mensagens.getLength(), "Conexão com CLP iniciada!" , cor_conectado);
            } catch (Exception e) {
                System.out.println("Falha ao insirir msg 4253");
            }
        }
        consultar_qtd_torrado_lotes();
    }//GEN-LAST:event_btnBlendLimparActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaPrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBlendAdd1;
    private javax.swing.JButton btnBlendAtual;
    private javax.swing.JButton btnBlendCancelar;
    private javax.swing.JButton btnBlendElevador;
    private javax.swing.JButton btnBlendEnviar;
    private javax.swing.JButton btnBlendLimpar;
    private javax.swing.JButton btnBlendManual;
    private javax.swing.JButton btnBlendMexedor;
    private javax.swing.JButton btnBlendNewMeta;
    private javax.swing.JButton btnBlendPower;
    private javax.swing.JButton btnBlendSalvar;
    private javax.swing.JButton btnBlendTarar;
    private javax.swing.JButton btnEmergencia;
    private javax.swing.JButton btnLotes;
    private javax.swing.JButton btnModBus;
    private javax.swing.JButton btnSilo1Abrir;
    private javax.swing.JButton btnSilo2Abrir;
    private javax.swing.JButton btnSilo3Abrir;
    private javax.swing.JButton btnSilo4Abrir;
    private javax.swing.JButton btnSilos;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cbBlendLote;
    private javax.swing.JComboBox<String> cbBlendOperacao;
    private javax.swing.JPanel footerBlend;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
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
    private javax.swing.JPanel jPanel43;
    private javax.swing.JPanel jPanel44;
    private javax.swing.JPanel jPanel45;
    private javax.swing.JPanel jPanel46;
    private javax.swing.JPanel jPanel47;
    private javax.swing.JPanel jPanel48;
    private javax.swing.JPanel jPanel49;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel50;
    private javax.swing.JPanel jPanel51;
    private javax.swing.JPanel jPanel52;
    private javax.swing.JPanel jPanel53;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblBlendHeader;
    private javax.swing.JLabel lblBlendPeso;
    private javax.swing.JLabel lblBlendPesq;
    private javax.swing.JLabel lblBlendWifi;
    private javax.swing.JLabel lblData;
    private javax.swing.JLabel lblNomeBlend;
    private javax.swing.JLabel lblSilosOpen1;
    private javax.swing.JLabel lblSilosOpen2;
    private javax.swing.JLabel lblSilosOpen3;
    private javax.swing.JLabel lblSilosOpen4;
    private javax.swing.JLabel lblWifiDesc;
    private javax.swing.JTable tbBlend;
    private javax.swing.JLabel tbBlendTitulo;
    private javax.swing.JTextField txtBlendMetaGrao;
    private javax.swing.JTextField txtBlendMetaMoido;
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