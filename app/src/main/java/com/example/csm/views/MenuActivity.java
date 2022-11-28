package com.example.csm.views;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.csm.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MenuActivity extends AppCompatActivity {
    private ViewHolder mviewHolder = new ViewHolder();


    // UPDATE LOGIN  -- version 1.0 //
    private static final String TAG = "MenuActivity";
    Button btnLogOut, bt_administration, bt_user_account;
    FirebaseAuth mAuth;
    //

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

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

        mviewHolder.bt_menuRooms = findViewById(R.id.bt_menuRooms);
        mviewHolder.bt_menuEquipments = findViewById(R.id.bt_menuEquipments);
        mviewHolder.bt_menuQRcode = findViewById(R.id.bt_menuQRcode);


        // UPDATE LOGIN  -- version 1.0 //

        btnLogOut = findViewById(R.id.btnLogout);
        bt_user_account = findViewById(R.id.bt_user_account);

        mAuth = FirebaseAuth.getInstance();




        // BUTTON FOR LOGOUT
        btnLogOut.setOnClickListener(view ->{
            mAuth.signOut();
            startActivity(new Intent(MenuActivity.this, LoginActivity.class));
        });



        // BUTTON --> USER'S INFO/MENU
        bt_user_account.setOnClickListener(view ->{
            Intent i = new Intent(MenuActivity.this, UserAccountActivity.class);
            startActivity(i);

        });


        ////////////////////////////////////////// GETTING USER'S SESSION  ////////////////////////////////////
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String email = user.getEmail();
            //Toast.makeText(this, String.valueOf(email), Toast.LENGTH_SHORT).show();
        }





        //////////////////
        mviewHolder.bt_menuRooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuActivity.this, ListRoomsActivity.class);
                startActivity(i);
            }
        });

        mviewHolder.bt_menuEquipments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuActivity.this, ListEquipmentsActivity.class);
                startActivity(i);
            }
        });


        mviewHolder.bt_menuQRcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, ScannerActivity.class));
            }
        });

////END
    }

    private class ViewHolder {
        Button bt_menuRooms, bt_menuEquipments, bt_menuQRcode;

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(MenuActivity.this, LoginActivity.class));
        }
    }

    ////END
}