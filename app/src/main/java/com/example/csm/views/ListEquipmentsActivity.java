package com.example.csm.views;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;

import com.example.csm.R;
import com.example.csm.adapter.EquipmentsAdapter;
import com.example.csm.model.Equipments;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class ListEquipmentsActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference equipRef = db.collection("Equipments");
    private EquipmentsAdapter adapter;
    private FloatingActionButton bt_equipmentAdd;
    Query query;
    Spinner spinner_category_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_equipments);
        bt_equipmentAdd = findViewById(R.id.bt_equipmentAdd);

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
        getSupportActionBar().setTitle("Equipamentos");
        ///////////////////////////////////////////////////////////////////////////////

        /////////////////////////////////////////////// SPINNER UPDATE ////////////////////////////////////////

        spinner_category_search = findViewById(R.id.spinner_category_search);

        //query = equipRef.orderBy("name", Query.Direction.ASCENDING);

        //Creating list for categories
        List categoryList = new ArrayList();

        categoryList.add("Todos");
        categoryList.add("Computadores");
        categoryList.add("Monitores");
        categoryList.add("Periféricos");
        categoryList.add("Mobiliário");
        categoryList.add("Outros");


        // - POPULATING SPINNER - //

        //ArrayAdapter aa = new ArrayAdapter(ListEquipmentsActivity.this, android.R.layout.simple_spinner_item, categoryList);
        //aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter aa = new ArrayAdapter(ListEquipmentsActivity.this, R.layout.spinner_item, categoryList);
        spinner_category_search.setAdapter(aa);


        /////////// SPINNER FILTER OF DISPLAY OF EQUIPMENTS /////////////

        spinner_category_search.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 1) {
                    SearchForCategory("Computadores");
                } else if (i == 2) {
                    SearchForCategory("Monitores");
                } else if (i == 3) {
                    SearchForCategory("Periféricos");
                } else if (i == 4) {
                    SearchForCategory("Mobiliário");
                } else if (i == 5) {
                    SearchForCategory("Outros");
                } else {
                    SearchForAll();


                }

            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        setUpRecyclerView();


        adapter.setOnItemClickListener(new EquipmentsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int pos) {
                Equipments equipments = documentSnapshot.toObject(Equipments.class);
                String path = documentSnapshot.getReference().getPath();

                Intent i = new Intent(ListEquipmentsActivity.this, EquipmentsDataActivity.class);
                i.putExtra("path", path);
                startActivity(i);
            }
        });

        bt_equipmentAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListEquipmentsActivity.this, newEquipmentActivity.class);
                startActivity(i);
            }
        });

    }

    private void setUpRecyclerView() {

        Query query = equipRef.orderBy("name", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Equipments> options = new FirestoreRecyclerOptions.Builder<Equipments>()
                .setQuery(query, Equipments.class).build();

        adapter = new EquipmentsAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.rv_listEquipments);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    ////// Search button/////

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                txtSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                txtSearch(query);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void txtSearch(String str) {
        Query query = equipRef.orderBy("name", Query.Direction.ASCENDING).startAt(str).endAt(str + "~");
        FirestoreRecyclerOptions<Equipments> options = new FirestoreRecyclerOptions.Builder<Equipments>()
                .setQuery(query, Equipments.class).build();

        adapter = new EquipmentsAdapter(options);
        adapter.startListening();

        RecyclerView recyclerView = findViewById(R.id.rv_listEquipments);
        recyclerView.setAdapter(adapter);

//  --------->>>>             //////////////////////////////// ADDING ADAPTER ///////////////////////////////////////
        adapter.setOnItemClickListener(new EquipmentsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int pos) {
                Equipments equipments = documentSnapshot.toObject(Equipments.class);
                String path = documentSnapshot.getReference().getPath();

                Intent i = new Intent(ListEquipmentsActivity.this, EquipmentsDataActivity.class);
                i.putExtra("path", path);
                startActivity(i);
            }
        });


//  --------->>>>             //////////////////////////////////// END ///////////////////////////////////
    }


    /////////////////////////////////// SEARCHING FOR CATEGORIES ///////////////////////////////////////

    private void SearchForCategory(String category) {
        query = equipRef.whereEqualTo("category", category);
        //Query query = equipRef.orderBy("name", Query.Direction.ASCENDING).startAt(str).endAt(str+"~");
        FirestoreRecyclerOptions<Equipments> options = new FirestoreRecyclerOptions.Builder<Equipments>()
                .setQuery(query, Equipments.class).build();

        adapter = new EquipmentsAdapter(options);
        adapter.startListening();

        RecyclerView recyclerView = findViewById(R.id.rv_listEquipments);
        recyclerView.setAdapter(adapter);

//  --------->>>>             //////////////////////////////// ADDING ADAPTER ///////////////////////////////////////
        adapter.setOnItemClickListener(new EquipmentsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int pos) {
                Equipments equipments = documentSnapshot.toObject(Equipments.class);
                String path = documentSnapshot.getReference().getPath();

                Intent i = new Intent(ListEquipmentsActivity.this, EquipmentsDataActivity.class);
                i.putExtra("path", path);
                startActivity(i);
            }
        });
    }
//  --------->>>>             //////////////////////////////////// END ///////////////////////////////////


/////////////////////////////////// SEARCHING FOR ALL ///////////////////////////////////////

    private void SearchForAll() {
        query = equipRef.orderBy("name", Query.Direction.ASCENDING);
        //Query query = equipRef.orderBy("name", Query.Direction.ASCENDING).startAt(str).endAt(str+"~");
        FirestoreRecyclerOptions<Equipments> options = new FirestoreRecyclerOptions.Builder<Equipments>()
                .setQuery(query, Equipments.class).build();

        adapter = new EquipmentsAdapter(options);
        adapter.startListening();

        RecyclerView recyclerView = findViewById(R.id.rv_listEquipments);
        recyclerView.setAdapter(adapter);

//  --------->>>>             //////////////////////////////// ADDING ADAPTER ///////////////////////////////////////
        adapter.setOnItemClickListener(new EquipmentsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int pos) {
                Equipments equipments = documentSnapshot.toObject(Equipments.class);
                String path = documentSnapshot.getReference().getPath();

                Intent i = new Intent(ListEquipmentsActivity.this, EquipmentsDataActivity.class);
                i.putExtra("path", path);
                startActivity(i);
            }
        });


    }
//  --------->>>>             //////////////////////////////////// END ///////////////////////////////////


    /////
}