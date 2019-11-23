package com.example.besafe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText editphonenum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editphonenum = findViewById(R.id.etextlogphone);

        findViewById(R.id.btnlogphone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = "20";
                String number = editphonenum.getText().toString().trim();

                if (number.isEmpty() || number.length() < 11 ||number.length() > 11) {
                    editphonenum.setError("رجاء ادخال رقم صحيح");
                    editphonenum.requestFocus();
                    return;
                }
                String phoneNumber = "+" + code + number;

                Intent intent = new Intent(LoginActivity.this, VerifyPhoneActivity.class);
                intent.putExtra("phonenumber", phoneNumber);
                startActivity(intent);

                //set up SharedPreferences
                SharedPreferences myPrefs = getSharedPreferences("User", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = myPrefs.edit();
                editor.putString("Phone", number);
                editor.apply();




            }
        });

    }





    //when login dosen't open login activity
    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
        }
    }


}
