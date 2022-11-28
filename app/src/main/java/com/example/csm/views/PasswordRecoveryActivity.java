package com.example.csm.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.csm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordRecoveryActivity extends AppCompatActivity {

    Button bt_password_recovery;
    EditText et_EmailRecovery;
    String email;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery);

        ////////////////////////////////// CHANGE COLOR ACTION BAR/////////////////////
        // Define ActionBar object
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        // Define ColorDrawable object and parse color
        // using parseColor method
        // with color hash code as its parameter
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#25183E"));
        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);

        ///////////////////////////////////////////////////////////////////////////////


        bt_password_recovery = findViewById(R.id.bt_password_recovery);
        et_EmailRecovery = findViewById(R.id.et_EmailRecovery);



        bt_password_recovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = et_EmailRecovery.getText().toString().trim();
                if (email.isEmpty()) {
                    Toast.makeText(PasswordRecoveryActivity.this, "Por favor, insira um email!", Toast.LENGTH_SHORT).show();
                }else {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(PasswordRecoveryActivity.this, "Foi enviado um email de recuperação!", Toast.LENGTH_SHORT).show();
                                finish();

                            } else {
                                Toast.makeText(PasswordRecoveryActivity.this, "Erro! Email inválido.", Toast.LENGTH_SHORT).show();


                            }
                        }
                    });
                }
            }
        });






    }
}