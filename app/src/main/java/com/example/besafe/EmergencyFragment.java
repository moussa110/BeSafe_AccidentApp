package com.example.besafe;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

public class EmergencyFragment extends Fragment {
    boolean doubleBackToExitPressedOnce = false;
    View v;
    SwipeRefreshLayout swap;
    private static SharedPreferences sharedPreferences;
    Button btnadd;
    Dialog dialog;
    private RecyclerView recyclerView;
    List<Emergency> Data;
    GetEmergencyData getEmergencyData=new GetEmergencyData();
    public EmergencyFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v=inflater.inflate(R.layout.fragment_emergency, container, false);

        sharedPreferences=getActivity().getSharedPreferences("User", Context.MODE_PRIVATE);

        btnadd=(Button)v.findViewById(R.id.btnAddnCon);

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EmerAdapter emerAdapter=new EmerAdapter(getContext(),Data);
                if (emerAdapter.getItemCount()<3)
                {

                    dialog =new Dialog(getActivity());
                    dialog.setContentView(R.layout.dialog_emeradd);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    Button add=(Button)dialog.findViewById(R.id.btnAdd);
                    add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            EditText name=(EditText)dialog.findViewById(R.id.addName);
                            EditText phone=(EditText)dialog.findViewById(R.id.addPhone);
                            Database db = new Database();
                            Connection conn = db.ConnectDB();

                            if (conn == null) {
                                Toast.makeText(getContext(), "تحقق من الاتصال بالانترنت", Toast.LENGTH_SHORT).show();
                            } else {
                                String msg = db.RUNDML("insert into EmgContacts values(N'" + phone.getText().toString() + "',N'" + name.getText().toString() + "',N'"+sharedPreferences.getString("Phone","")+"')");
                                if (msg.equals("ok")) {
                                    Toast.makeText(getContext(), "اسحب لاسفل للتحديث ", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();

                                }
                            }

                            dialog.cancel();
                        }
                    });
                    dialog.show();

                }else {
                    Toast.makeText(getContext(), "لا يمكنك اضافة اكثر من ثلاث جهات اتصال  ", Toast.LENGTH_SHORT).show();
                }

            }
        });

        swap=(SwipeRefreshLayout)v.findViewById(R.id.swiper);
        swap.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                Data=new ArrayList<>(getEmergencyData.getdata(getContext()));
                recyclerView=(RecyclerView)v.findViewById(R.id.emerRecycle);
                EmerAdapter emerAdapter=new EmerAdapter(getContext(),Data);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(emerAdapter);
                swap.setRefreshing(false);

            }
        });

        Data=new ArrayList<>(getEmergencyData.getdata(getContext()));
        recyclerView=(RecyclerView)v.findViewById(R.id.emerRecycle);
        final EmerAdapter emerAdapter=new EmerAdapter(getContext(),Data);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(emerAdapter);


        return v;


    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getActivity(), "Permission denied to send sms", Toast.LENGTH_SHORT).show();
                }


                return;

                // other 'case' lines to check for other
                // permissions this app might request
            }
        }
    }
}
