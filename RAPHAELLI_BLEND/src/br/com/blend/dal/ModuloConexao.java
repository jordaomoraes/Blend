package br.com.blend.dal;


import java.sql.*;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JOptionPane;

public class ModuloConexao {
    //Variaveis timer
    final private Timer timer = new Timer();
    private TimerTask timer_cronometro;
    int tempo_cronometro = (1000);
    int contador_tempo=0;
    
    public static Connection conector(){
        java.sql.Connection conexao = null;
        
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3434/db_blendjacutinga";
        String user = "root";
        String password = "";
        
        //Conecta no offline
        try{
            Class.forName(driver);
            conexao = DriverManager.getConnection(url, user, password);
            System.out.println("Conexao com banco LOCAL estabelecida com sucesso!");
            return conexao;
        }
        catch (Exception e){
            JOptionPane.showMessageDialog(null, "Falha ao se conectar com banco LOCAL");
            return null;
        }
    }
}
