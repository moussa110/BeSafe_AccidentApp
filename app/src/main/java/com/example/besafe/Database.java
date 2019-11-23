package com.example.besafe;

import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    Connection conn=null;
    public Connection ConnectDB()
    {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            conn= DriverManager.getConnection("jdbc:jtds:sqlserver://sql5041.site4now.net/DB_A4C448_gdgbesafe","DB_A4C448_gdgbesafe_admin","mosa103105");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
    //يستخدم لللاضافة و التعديل و الحذف
    public String RUNDML(String st)
    {
        try {
            Statement DML=conn.createStatement();
            DML.executeUpdate(st);
            return "ok";
        } catch (SQLException ex) {
            return ex.getMessage();
        }

    }
    //to search
    public ResultSet RunSearch(String st)
    {
        ResultSet Rsearch;
        try {
            Statement stm=conn.createStatement();
            Rsearch= stm.executeQuery(st);
            return  Rsearch;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

}
