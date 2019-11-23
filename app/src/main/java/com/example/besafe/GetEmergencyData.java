package com.example.besafe;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetEmergencyData {
    private static SharedPreferences prefs;

    public List<Emergency> getdata(Context cn){
        List<Emergency> data=new ArrayList<>();

        prefs = cn.getSharedPreferences("User", Context.MODE_PRIVATE);
        Database db=new Database();
        Connection conn=db.ConnectDB();
        if (conn==null) {
            Toast.makeText(cn, "Check Your Internet Connectoin ", Toast.LENGTH_SHORT).show();
        }else
        {
            ResultSet rs = db.RunSearch("select * from EmgContacts where userPhone='"+prefs.getString("Phone","")+"'");
            try {
                while (rs.next())
                {

                    Emergency emergency = new Emergency();
                    emergency.setId(rs.getInt(4));
                    emergency.setPhone(rs.getString(1));
                    emergency.setName(rs.getString(2));
                    emergency.setUserPhone(rs.getString(3));
                    data.add(emergency);

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return data;
    }
}
