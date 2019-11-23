package com.example.besafe;



import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfileFragment extends Fragment {

    SharedPreferences prefs;
    View v;
    public  TextView name, address, phone,email;
    public  String n,p;

    public ProfileFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_profile, container, false);
        prefs = getContext().getSharedPreferences("User", Context.MODE_PRIVATE);
        name = (TextView) v.findViewById(R.id.txtPName);
        address = (TextView) v.findViewById(R.id.txtPAddress);
        phone = (TextView) v.findViewById(R.id.txtPPhone);
        email=(TextView)v.findViewById(R.id.txtPemail);

        Database db=new Database();
        Connection conn=db.ConnectDB();
        if (conn==null) {
            Toast.makeText(getContext(), "Check Your Internet Connectoin ", Toast.LENGTH_SHORT).show();
        }else
        {
            ResultSet rs = db.RunSearch("select * from users where userPhone='"+prefs.getString("Phone","")+"'");
            try {
                while (rs.next())
                {
                    p=rs.getString(1);
                    n=rs.getString(2);
                    SharedPreferences myPrefs = getContext().getSharedPreferences("name", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = myPrefs.edit();
                    editor.putString("name", n);
                    editor.apply();
                    address.setText(rs.getString(3));
                    email.setText(rs.getString(4));
                    phone.setText(p);
                    name.setText(n);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return v;

    }
}
