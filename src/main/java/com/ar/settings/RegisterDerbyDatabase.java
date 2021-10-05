
package com.ar.settings;


import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;


public final class RegisterDerbyDatabase {
    
    public static RegisterDerbyDatabase derbydbHandler;
    
    private static final String DB_URL = "jdbc:derby:regsetup;create=true";
    private static  Connection conn = null;
    private static  Statement stmt = null;
    
     public static RegisterDerbyDatabase getInstance() throws ClassNotFoundException, SQLException {
        if (derbydbHandler == null) {
            derbydbHandler = new RegisterDerbyDatabase();
        }
        return derbydbHandler;
    }
     
      private RegisterDerbyDatabase() throws ClassNotFoundException, SQLException{
        createConnection();
        setupRegisterId();
     }
    
     void createConnection() throws ClassNotFoundException,SQLException {
      try{
          Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
          conn = DriverManager.getConnection(DB_URL);
      } catch (ClassNotFoundException ex) {
          System.out.println("no driver");
          Logger.getLogger(RegisterDerbyDatabase.class.getName()).log(Level.SEVERE, null, ex);
           throw ex;
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(RegisterDerbyDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
         System.out.println("jdbc  derby driver registered");
         
    }
     public ResultSet resultexecQuery(String q){
         ResultSet r;
        try {
            stmt=conn.createStatement();
            r=stmt.executeQuery(q);
        } catch (SQLException ex) {
            Logger.getLogger(RegisterDerbyDatabase.class.getName()).log(Level.SEVERE, null, ex);
       
            System.out.println("exeption at resultset query");
            return null;
}
finally{
    
}
        return r;
     }
     
     void setupRegisterId(){
           String TABLE_NAME="REGISTERTABLE";
//           String TABLE_SCHEMA_NAME="SUPERMARKET";
        try {
            stmt=conn.createStatement();
            DatabaseMetaData dbm=conn.getMetaData();
            ResultSet tables=dbm.getTables(null,null, TABLE_NAME.toUpperCase(), null);
            if(tables.next()){
                System.out.println("Table "+TABLE_NAME+" already exists Ready lets go");
            }else{
                stmt.execute("CREATE TABLE "+TABLE_NAME+"("
                        + "registerid varchar(20) primary key not null,\n"
                        + "registerstatus boolean default false,\n"
                        + "registertime timestamp default CURRENT_TIMESTAMP ,\n"
                        + "isfirsttime boolean default true ,\n"
                        + "isthirtydaysmode boolean default false,\n"
                        + "isthirtydaysmodefirsttime boolean default true,\n"
                        + "isfreemode boolean default false,\n"
                        + "registersettime timestamp "
                        + ")");
                        
                
                System.out.println("Successfully created "+TABLE_NAME);
            }
            
        } catch (SQLException ex) {
            System.out.println("Setup DataBase-----!");
            Logger.getLogger(RegisterDerbyDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
           finally{
        }
         String checkItem = "SELECT COUNT(*) FROM REGISTERTABLE WHERE REGISTERID='SUPERMARKETID'";
         Boolean result = isRegisterIdAlreadyExist(checkItem);
         if(result){System.out.println("SUPERMARKETID already exists dont create new one !");}
         else{
         String queryFirstInsertion ="INSERT INTO REGISTERTABLE(registerid) VALUES('SUPERMARKETID')";
         if(executeQuery(queryFirstInsertion)){
             System.out.println("create registerid = SUPERMARKETID successfully");
         }
         else{
             System.out.println("failed registerid creation");
         }
        }
       }
     
     public boolean executeQuery(String query){
        try {
            stmt=conn.createStatement();
            stmt.execute(query);
            return true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error occuring in RegisterKey  Fetching \n "
                    + "* may be file Missing \n "
                    + "* may be expire existing RegisterKey \n"
                    , "ERROR OCCURED !"
                            + "message\n "+ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            System.out.println("Error in register key");
            Logger.getLogger(RegisterDerbyDatabase.class.getName()).log(Level.SEVERE, null, ex);
             return false;
        }
         finally{ 
        }
       }
     
     public boolean executeUpdateQuery(String query){
        try {
            stmt=conn.createStatement();
            stmt.executeUpdate(query);
            return true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error occuring in RegisterKey  Fetching \n "
                    + "* may be file Missing \n "
                    + "* may be expire existing RegisterKey \n"
                    , "ERROR OCCURED !"
                            + "message\n "+ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            System.out.println("Error in register key");
            Logger.getLogger(RegisterDerbyDatabase.class.getName()).log(Level.SEVERE, null, ex);
             return false;
        }
         finally{ 
        }
       }
     
     private boolean isRegisterIdAlreadyExist(String q){
        try {
            stmt=conn.createStatement();
            ResultSet rs = stmt.executeQuery(q);
            if(rs.next()){
                int count = rs.getInt(1);
                System.out.println("Count   :"+count);
                return (count>0);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RegisterDerbyDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
     }
}
