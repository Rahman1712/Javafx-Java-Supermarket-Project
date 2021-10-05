
package com.ar.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;


public final class BillerDatabaseSection {
    
    public  static BillerDatabaseSection derbyhandler;

    private static final String DB_URL = "jdbc:derby:database;create=true";
    private static  Connection conn = null;
    private static  Statement stmt = null;

    public static BillerDatabaseSection getInstance() throws ClassNotFoundException, SQLException {
        if (derbyhandler == null) {
            derbyhandler = new BillerDatabaseSection();
        }
        return derbyhandler;
    }
    private BillerDatabaseSection() throws ClassNotFoundException, SQLException{
        createConnection();
        setupBiller();
    }
    
     void createConnection() throws ClassNotFoundException,SQLException {
      try{
          Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
          conn = DriverManager.getConnection(DB_URL);
      } catch (ClassNotFoundException ex) {
          System.out.println("no driver");
          Logger.getLogger(BillerDatabaseSection.class.getName()).log(Level.SEVERE, null, ex);
           throw ex;
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(BillerDatabaseSection.class.getName()).log(Level.SEVERE, null, ex);
        }
         System.out.println("jdbc  derby driver registered");
         
    }
       public ResultSet resultexecQuery(String q){
         ResultSet r;
        try {
            stmt=conn.createStatement();
            r=stmt.executeQuery(q);
        } catch (SQLException ex) {
            Logger.getLogger(BillerDatabaseSection.class.getName()).log(Level.SEVERE, null, ex);
       
            System.out.println("exeption at resultset query");
            return null;
}
finally{
    
}
        return r;
     }
public boolean executeQuery(String query){
        try {
            stmt=conn.createStatement();
            stmt.execute(query);
            return true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error occuring in insertion Check it \n "
                    + "* may be empty column \n "
                    + "* may be use already existing ProductName \n"
                    + "   (primarykey Violation)", "ERROR OCCURED !"
                            + "message\n "+ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            System.out.println("Error in insertion");
            Logger.getLogger(BillerDatabaseSection.class.getName()).log(Level.SEVERE, null, ex);
             return false;
        }
         finally{ 
        }
       }

 void setupBiller(){
           String TABLE_NAME="BILLERNAME";
           String TABLE_SCHEMA_NAME="SUPERMARKET";
        try {
            stmt=conn.createStatement();
            DatabaseMetaData dbm=conn.getMetaData();
            ResultSet tables=dbm.getTables(null,null, TABLE_NAME.toUpperCase(), null);
            if(tables.next()){
                System.out.println("Table "+TABLE_NAME+" already exists Ready lets go");
            }else{
                stmt.execute("CREATE TABLE "+TABLE_NAME+"("
                        + "bno int primary key not null,\n"
                        + "billerusername varchar(200)  not null)");
                        
                
                System.out.println("Successfully created "+TABLE_NAME);
            }
            
        } catch (SQLException ex) {
            System.out.println("Setup DataBase-----!");
            Logger.getLogger(BillerDatabaseSection.class.getName()).log(Level.SEVERE, null, ex);
        }
           finally{
        }
         String checkItem = "SELECT COUNT(*) FROM BILLERNAME WHERE BNO=1";
         Boolean result = isBillerusernameAlreadyExist(checkItem);
         if(result){System.out.println("bno=1 alredy exists dont create new one !");}
         else{
         String queryFirstInsertion ="INSERT INTO BILLERNAME(bno,billerusername) VALUES(1,'billuser')";
         if(executeQuery(queryFirstInsertion)){
             System.out.println("create brno=1 successfully");
         }
         else{
             System.out.println("failed brno=1 creation");
         }
        }
       }
 private boolean isBillerusernameAlreadyExist(String q){
        try {
            stmt=conn.createStatement();
            ResultSet rs = stmt.executeQuery(q);
            if(rs.next()){
                int count = rs.getInt(1);
                System.out.println("Count   :"+count);
                return (count>0);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BillerDatabaseSection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
     }
}
