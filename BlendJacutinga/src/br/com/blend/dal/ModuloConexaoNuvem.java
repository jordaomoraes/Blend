package br.com.blend.dal;

import java.sql.*;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JOptionPane;

public class ModuloConexaoNuvem {
    public static Connection conector(){
        java.sql.Connection conexao = null;
        
        
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://192.169.80.2:3306/actualmo_nuvem?autoReconnect=true&useSSL=false";
        String user = "actualmo_nuvem";
        String password = "solucoes4941";
        
        //Conecta no online
        try{
            Class.forName(driver);
            conexao = DriverManager.getConnection(url, user, password);
            System.out.println("Conexao com banco ONLINE estabelecida com sucesso!");
            return conexao;
        }
        catch (Exception e){
           // JOptionPane.showMessageDialog(null, "Falha ao se conectar com banco ONLINE");
            return null;
        }
    }
   
}
