package com.example.csm.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.csm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {


    TextInputEditText et_LoginEmail;
    TextInputEditText et_LoginPassword;
    Button bt_Login;
    TextView tv_password_recovery, tv_RegisterHere;
    FirebaseAuth mAuth;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_LoginEmail = findViewById(R.id.et_LoginEmail);
        et_LoginPassword = findViewById(R.id.et_LoginPassword);
        bt_Login = findViewById(R.id.bt_Login);
        tv_password_recovery = findViewById(R.id.tv_password_recovery);
        tv_RegisterHere = findViewById(R.id.tv_RegisterHere);

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
        //getSupportActionBar().setTitle("");
        ///////////////////////////////////////////////////////////////////////////////

        mAuth = FirebaseAuth.getInstance();

        // LOGIN BUTTON
        bt_Login.setOnClickListener(view -> {
            loginUser();
        });


        // PASSWORD RECOVERY TEXTVIEW ON CLICK EVENT
        tv_password_recovery.setOnClickListener(view ->{
            Intent i = new Intent(LoginActivity.this, PasswordRecoveryActivity.class);
            startActivity(i);

        });

        // REGISTERING NEW USER
        tv_RegisterHere.setOnClickListener(view ->{
            Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(i);

        });



    }

    private void loginUser(){
        String email = et_LoginEmail.getText().toString();
        String password = et_LoginPassword.getText().toString();

        if (TextUtils.isEmpty(email)){
            et_LoginEmail.setError("Por favor, preencha o email!");
            et_LoginEmail.requestFocus();
        }else if (TextUtils.isEmpty(password)){
            et_LoginPassword.setError("A campo da palavra-passe não pode estar vazio.");
            et_LoginPassword.requestFocus();
        }else{
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(LoginActivity.this, "Início de sessão bem-sucedido!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MenuActivity.class));
                    }else{
                        Toast.makeText(LoginActivity.this, "Erro no Login.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}