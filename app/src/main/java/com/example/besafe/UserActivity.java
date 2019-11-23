package com.example.besafe;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.besafe.slideview.MainActivity;

import java.sql.Connection;

public class UserActivity extends AppCompatActivity {

    EditText editname,editaddress,editemai;
    ImageView btnUreg;
    SharedPreferences myPrefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_user);
        editname=(EditText)findViewById (R.id.txtUname);
        editaddress=(EditText)findViewById (R.id.txtUaddress);
        editemai=(EditText)findViewById(R.id.txtUemail);
        btnUreg=(ImageView) findViewById (R.id.btnUreg);


        //Intent intent = new Intent(UserActivity.this, GetEmergencyData.class);
        //intent.putExtra("phone", getPhone());


        btnUreg.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {

                if (editname.getText().toString().isEmpty()){
                    editname.setError("قم بادخال الاسم");
                    editname.requestFocus();
                }else if (editaddress.getText().toString().isEmpty()){
                    editaddress.setError("قم بادخال العنوان");
                    editaddress.requestFocus();
                }else if (editemai.getText().toString().isEmpty()){
                    editemai.setError("قم بادخال البريد الالكتروني");
                    editemai.requestFocus();
                }else {
                    Database db=new Database ();
                    Connection conn=db.ConnectDB ();

                    if (conn==null){
                        Toast.makeText (UserActivity.this,"تحقق من الاتصال بالانترنت ..", Toast.LENGTH_SHORT).show ();
                    }else {
                        String msg=db.RUNDML ("insert into users values(N'"+getPhone()+"',N'"+editname.getText ().toString ()+"',N'"+editaddress.getText ().toString () +"','"+editemai.getText().toString()+"')");
                        if (msg.equals ("ok")){
                            AlertDialog.Builder builder=new AlertDialog.Builder (UserActivity.this)
                                    .setTitle ("Be Safe")
                                    .setMessage ("تمت الاضافة بنجاح ☺")
                                    .setIcon (R.drawable.logonew)
                                    .setPositiveButton ("مرحبا..", new DialogInterface.OnClickListener () {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            startActivity (new Intent (UserActivity.this, MainActivity.class));
                                        }
                                    });
                            builder.create ();
                            builder.show ();
                        }else if(msg.toString().contains("PRIMARY KEY")){
                            AlertDialog.Builder mBuilder=new AlertDialog.Builder(UserActivity.this)
                                    .setTitle("Be Safe")
                                    .setMessage("هذا الحساب موجود بالفعل ")
                                    .setIcon(R.drawable.man)
                                    .setNegativeButton("دخول", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            startActivity(new Intent(UserActivity.this,MainActivity.class));
                                        }
                                    }).setPositiveButton("رجوع", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            startActivity(new Intent(UserActivity.this,LoginActivity.class));
                                        }
                                    });
                            mBuilder.create();
                            mBuilder.show();

                        }else {
                            Toast.makeText (UserActivity.this, msg, Toast.LENGTH_SHORT).show ();
                        }
                    }
                }
            }
        });
        editemai.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String emilvalidation="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if (editemai.getText ().toString ().matches (emilvalidation))
                {

                }else {
                    editemai.setError ("Invalid email address (user@domain)");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    public String getPhone(){
        myPrefs=getSharedPreferences("User",MODE_PRIVATE);
        String ph= myPrefs.getString("Phone", "");
        return ph;
    }
}
