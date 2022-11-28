package com.example.csm.views;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.csm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class UserAccountActivity extends AppCompatActivity {


    EditText et_edit_user_email, et_confirm_editEmail_password;
    Button bt_edit_user_resetPassword, bt_edit_user_deleteUser, bt_edit_user_data, bt_edit_user_confirm_passoword, bt_delete_user_confirm_password;
    View layout_email_edit_user, layout_confirm_password_user;
    TextView tv_user_info_pageTitle;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

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

        et_edit_user_email = findViewById(R.id.et_edit_user_email);
        bt_edit_user_resetPassword = findViewById(R.id.bt_edit_user_resetPassword);
        bt_edit_user_deleteUser = findViewById(R.id.bt_edit_user_deleteUser);
        bt_edit_user_data = findViewById(R.id.bt_edit_user_data);
        et_confirm_editEmail_password = findViewById(R.id.et_confirm_editEmail_password);
        bt_edit_user_confirm_passoword = findViewById(R.id.bt_edit_user_confirm_passoword);
        layout_confirm_password_user = findViewById(R.id.layout_confirm_password_user);
        bt_delete_user_confirm_password = findViewById(R.id.bt_delete_user_confirm_password);
        layout_email_edit_user = findViewById(R.id.layout_email_edit_user);
        tv_user_info_pageTitle = findViewById(R.id.tv_user_info_pageTitle);

        et_confirm_editEmail_password.setVisibility(View.GONE);
        bt_edit_user_confirm_passoword.setVisibility(View.GONE);
        layout_confirm_password_user.setVisibility(View.GONE);
        bt_delete_user_confirm_password.setVisibility(View.GONE);

        // GETTING CURRENT USER'S EMAIL
        et_edit_user_email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());


        /////////////////////////////// UPDATING EMAIL AND ASK FOR PASSWORD CONFIRMATION /////////////////////////////////////
        bt_edit_user_data.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                et_edit_user_email.setVisibility(View.GONE);
                layout_email_edit_user.setVisibility(View.GONE);
                bt_edit_user_data.setVisibility(View.GONE);
                bt_edit_user_deleteUser.setVisibility(View.INVISIBLE);
                bt_edit_user_resetPassword.setVisibility(View.INVISIBLE);

                layout_confirm_password_user.setVisibility(View.VISIBLE);
                bt_edit_user_confirm_passoword.setVisibility(View.VISIBLE);
                et_confirm_editEmail_password.setVisibility(View.VISIBLE);
                et_confirm_editEmail_password.requestFocus();


                tv_user_info_pageTitle.setText("Insira a sua password");

                // SUBMIT EDITS BUTTON
                bt_edit_user_confirm_passoword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!et_confirm_editEmail_password.getText().toString().isEmpty()) {

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            // Get auth credentials from the user for re-authentication
                            AuthCredential credential = EmailAuthProvider
                                    .getCredential(String.valueOf(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail()), et_confirm_editEmail_password.getText().toString()); // Current Login Credentials \\
                            // Prompt the user to re-provide their sign-in credentials
                            user.reauthenticate(credential)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            //Toast.makeText(UserAccountActivity.this, "User re-authenticated.", Toast.LENGTH_SHORT).show();
                                            //Log.d(TAG, "User re-authenticated.");
                                            //Now change your email address \\
                                            //----------------Code for Changing Email Address----------\\

                                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                            user.updateEmail(et_edit_user_email.getText().toString())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                //Log.d(TAG, "User email address updated.");
                                                                Toast.makeText(UserAccountActivity.this, "Email atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });


                                            //----------------------------------------------------------\\

                                        }
                                    });

                            finish();
                            Intent i = new Intent(UserAccountActivity.this, MenuActivity.class);
                            startActivity(i);
                        }else {
                            Toast.makeText(UserAccountActivity.this, "Por favor, confirme a password antes de avançar!", Toast.LENGTH_SHORT).show();
                        }



                    }
                });

            }
        });

        //////////////////////////////////////////// END UPDATING EMAIL //////////////////////////////////////////////


        //_________________//
        ////////////////////////////////////////////// CHANGING PASSWORD ///////////////////////////////////////////////
        //_________________//

        bt_edit_user_resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth.getInstance().sendPasswordResetEmail(et_edit_user_email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(UserAccountActivity.this, "Foi enviado um email de recuperação!", Toast.LENGTH_SHORT).show();
                            // Reaniciar a Activity
                            //finish();
                            //startActivity(getIntent());

                        } else {
                            Toast.makeText(UserAccountActivity.this, "Erro! Email inválido.", Toast.LENGTH_SHORT).show();

                        }
                    }
                });


            }
        });

        ////////////////////////////////////////////// END CHANGING PASSWORD ///////////////////////////////////////////////



        //////////////////////////////////////////////  DELETE ACCOUNT  ///////////////////////////////////////////////


        bt_edit_user_deleteUser.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {

                et_edit_user_email.setVisibility(View.GONE);
                layout_email_edit_user.setVisibility(View.GONE);
                bt_edit_user_data.setVisibility(View.GONE);
                bt_edit_user_deleteUser.setVisibility(View.INVISIBLE);
                bt_edit_user_resetPassword.setVisibility(View.INVISIBLE);

                layout_confirm_password_user.setVisibility(View.VISIBLE);
                bt_delete_user_confirm_password.setVisibility(View.VISIBLE);
                et_confirm_editEmail_password.setVisibility(View.VISIBLE);
                et_confirm_editEmail_password.requestFocus();

                tv_user_info_pageTitle.setText("Insira a sua password");



                // CONFIRM DELETE USER BUTTON
                bt_delete_user_confirm_password.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (!et_confirm_editEmail_password.getText().toString().isEmpty()) {

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            // Get auth credentials from the user for re-authentication
                            AuthCredential credential = EmailAuthProvider
                                    .getCredential(String.valueOf(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail()), et_confirm_editEmail_password.getText().toString()); // Current Login Credentials \\
                            // Prompt the user to re-provide their sign-in credentials
                            user.reauthenticate(credential)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            //Toast.makeText(UserAccountActivity.this, "User re-authenticated.", Toast.LENGTH_SHORT).show();
                                            //Log.d(TAG, "User re-authenticated.");
                                            //Now removing user \\
                                            //----------------Code for REMOVING USER's ACCOUNT ----------\\

                                            // DELETING USER
                                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                            user.delete()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                //Log.d(TAG, "User account deleted.");
                                                                Toast.makeText(UserAccountActivity.this, "Conta removida com sucesso!", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });

                                        }
                                    });
                            //////

                            // GOING TO A LOGIN ACTIVITY
                            finish();
                            Intent i = new Intent(UserAccountActivity.this, LoginActivity.class);
                            startActivity(i);
                        }else {
                            Toast.makeText(UserAccountActivity.this, "Por favor, confirme a password antes de avançar!", Toast.LENGTH_SHORT).show();
                        }

                    }


                });

                //////
            }
        });
        ////////////////////////////////////////////// END DELETE ACCOUNT ///////////////////////////////////////////////

    }
}