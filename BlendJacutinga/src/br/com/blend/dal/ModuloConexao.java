package br.com.blend.dal;

import java.sql.*;

public class ModuloConexao {
    public static Connection conector(){
        java.sql.Connection conexao = null;
        
        /*String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://192.169.80.2:3306/actualmo_nuvem?autoReconnect=true&useSSL=false";
        String user = "actualmo_nuvem";
        String password = "solucoes4941";*/
        
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3434/db_blendjacutinga";
        String user = "root";
        String password = "";
        
        
        
        try{
            Class.forName(driver);
            conexao = DriverManager.getConnection(url,user, password);
            return conexao;
        }
        catch (Exception e){
            System.out.println(e);
            return null;
        }
    }
}
