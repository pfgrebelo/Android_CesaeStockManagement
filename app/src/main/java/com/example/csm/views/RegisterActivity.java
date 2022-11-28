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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.csm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    Button bt_Register;
    EditText et_RegEmail, et_RegPass, et_confirm_RegPass;
    TextView tv_LoginHere;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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

        bt_Register = findViewById(R.id.bt_Register);
        et_RegEmail = findViewById(R.id.et_RegEmail);
        et_RegPass = findViewById(R.id.et_RegPass);
        et_confirm_RegPass = findViewById(R.id.et_confirm_RegPass);
        tv_LoginHere = findViewById(R.id.tv_LoginHere);

        mAuth = FirebaseAuth.getInstance();

        bt_Register.setOnClickListener(view ->{


            createUser();
        });

        tv_LoginHere.setOnClickListener(view ->{
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });
    }



    private void createUser() {
        String email = et_RegEmail.getText().toString();
        String password = et_RegPass.getText().toString();
        String confirm_password = et_confirm_RegPass.getText().toString();

        if (password.equals(confirm_password)) {

            if (TextUtils.isEmpty(email)) {
                et_RegEmail.setError("O Email tem de ser preenchido!");
                et_RegEmail.requestFocus();
            } else if (TextUtils.isEmpty(password)) {
                et_RegPass.setError("A password tem de ser preenchido!");
                et_RegPass.requestFocus();
            } else {
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Novo utilizador registado com sucesso!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        } else {
                            Toast.makeText(RegisterActivity.this, "Erro! Dados inválidos ou a conta já existe na base de dados." + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            //end if
        }else {
            Toast.makeText(this, "As passwords tem de ser iguais, por favor tente novamente!", Toast.LENGTH_SHORT).show();


        }

    }

}