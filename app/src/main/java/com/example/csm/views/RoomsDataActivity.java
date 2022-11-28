package com.example.csm.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.csm.R;
import com.example.csm.adapter.EquipmentsAdapter;
import com.example.csm.model.Equipments;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RoomsDataActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ViewHolder viewHolder = new ViewHolder();
    Intent i;
    String id = "";
    DocumentReference docRef;


    // LIST OF EQUIPMENTS UPDATE // <<--------------------------------------------------------

    private CollectionReference equipRef = db.collection("Equipments");
    private EquipmentsAdapter adapter;
    private FloatingActionButton bt_InRoom_equipmentAdd;
    Query query;
    //Spinner spinnerInRoom_category_search;

    String roomName;
    //////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms_data);

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
        getSupportActionBar().setTitle("Pesquisar equipamento");
        ///////////////////////////////////////////////////////////////////////////////

        viewHolder.et_dataRoomsName = findViewById(R.id.et_dataRoomsName);
        viewHolder.et_dataDesignation = findViewById(R.id.et_dataDesignation);

        viewHolder.bt_dataRoomEdit = findViewById(R.id.bt_dataRoomEdit);
        viewHolder.bt_dataRoomDelete = findViewById(R.id.bt_dataRoomDelete);
        viewHolder.bt_dataRoomBack = findViewById(R.id.bt_dataRoomBack);


        viewHolder.spinnerInRoom_category_search = findViewById(R.id.spinnerInRoom_category_search);


        //UPDATE LIST OF EQUIPEMENT <<<<---------------------------------------------------------------------
        bt_InRoom_equipmentAdd = findViewById(R.id.bt_InRoom_equipmentAdd);

        //spinnerInRoom_category_search = findViewById(R.id.spinnerInRoom_category_search);

        ///////////////////////////////


        i = getIntent();
        String path = i.getExtras().getString("path");
        id = path.substring(path.indexOf("/") + 1);

        docRef = db.collection("Rooms").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        //Toast.makeText(EquipmentsDataActivity.this, "Dados lidos com sucesso", Toast.LENGTH_SHORT).show();
                        viewHolder.et_dataRoomsName.setText(documentSnapshot.getData().get("roomName").toString());
                        roomName = Objects.requireNonNull(documentSnapshot.getData().get("roomName")).toString().trim();
                        viewHolder.et_dataDesignation.setText(documentSnapshot.getData().get("designation").toString());


                        ///////////////////////////////////////// LIST OF EQUIPMENTS UPDATE // <<--------------------------------------------------------


                        /////////////////////////////////////////////// SPINNER UPDATE ////////////////////////////////////////


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

                        //ArrayAdapter aa = new ArrayAdapter(RoomsDataActivity.this, android.R.layout.simple_spinner_item, categoryList);
                        //aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        ArrayAdapter aa = new ArrayAdapter(RoomsDataActivity.this,R.layout.spinner_item, categoryList);
                        viewHolder.spinnerInRoom_category_search.setAdapter(aa);


                        /////////// SPINNER FILTER OF DISPLAY OF EQUIPMENTS /////////////

                        viewHolder.spinnerInRoom_category_search.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                                if (i == 1) {
                                    SearchForCategory("Computadores");
                                    //Toast.makeText(ListEquipmentsActivity.this, String.valueOf(i), Toast.LENGTH_SHORT).show();
                                } else if (i == 2) {
                                    SearchForCategory("Monitores");
                                } else if (i == 3) {
                                    SearchForCategory("Periféricos");
                                } else if (i == 4) {
                                    SearchForCategory("Mobiliário");
                                } else if (i == 5) {
                                    SearchForCategory("Outros");
                                } else if (i == 0) {
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

                                Intent i = new Intent(RoomsDataActivity.this, EquipmentsDataActivity.class);
                                i.putExtra("path", path);
                                startActivity(i);
                            }
                        });

                        bt_InRoom_equipmentAdd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(RoomsDataActivity.this, newEquipmentActivity.class);
                                startActivity(i);
                            }
                        });


                        ////////////////////////////////////////////////////////// END OF LIST OF EQUIPMENTS UPDATE ///////////////////////////////////////////////////////////////////////////////////////////


                    } else {
                        Toast.makeText(RoomsDataActivity.this, "Erro ao apresentar dados", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Toast.makeText(RoomsDataActivity.this, "Erro ao apresentar dados", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        viewHolder.bt_dataRoomBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        viewHolder.bt_dataRoomEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                docRef.update("roomName", viewHolder.et_dataRoomsName.getText().toString());
                docRef.update("designation", viewHolder.et_dataDesignation.getText().toString());
                Toast.makeText(RoomsDataActivity.this, "Sala editada com sucesso.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        viewHolder.bt_dataRoomDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(viewHolder.et_dataRoomsName.getContext());
                builder.setTitle("Tem certeza?");

                builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        docRef.delete();
                        Toast.makeText(RoomsDataActivity.this, "Eliminado com sucesso.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });


    }

    private class ViewHolder {
        EditText et_dataRoomsName, et_dataDesignation;
        Button bt_dataRoomEdit, bt_dataRoomDelete, bt_dataRoomBack;
        Spinner spinnerInRoom_category_search;


    }


    ///////////////////////////////////////// LIST OF EQUIPMENTS UPDATE // <<--------------------------------------------------------


    private void setUpRecyclerView() {

        //Query query = equipRef.orderBy("name", Query.Direction.ASCENDING);
        Query query = equipRef.whereEqualTo("roomId", roomName);

        FirestoreRecyclerOptions<Equipments> options = new FirestoreRecyclerOptions.Builder<Equipments>()
                .setQuery(query, Equipments.class).build();

        adapter = new EquipmentsAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.rv_listEquipments);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

   /* @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }*/

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

                Intent i = new Intent(RoomsDataActivity.this, EquipmentsDataActivity.class);
                i.putExtra("path", path);
                startActivity(i);
            }
        });


//  --------->>>>             //////////////////////////////////// END ///////////////////////////////////
    }


    /////////////////////////////////// SEARCHING FOR CATEGORIES ///////////////////////////////////////

    private void SearchForCategory(String category) {
        query = equipRef.whereEqualTo("roomId", roomName).whereEqualTo("category", category).orderBy("name", Query.Direction.ASCENDING);
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

                Intent i = new Intent(RoomsDataActivity.this, EquipmentsDataActivity.class);
                i.putExtra("path", path);
                startActivity(i);
            }
        });
    }
//  --------->>>>             //////////////////////////////////// END ///////////////////////////////////


/////////////////////////////////// SEARCHING FOR ALL ///////////////////////////////////////

    private void SearchForAll() {
        query = equipRef.whereEqualTo("roomId", roomName).orderBy("name", Query.Direction.ASCENDING);
        //query = equipRef.whereEqualTo("category", category);
        //Query query = equipRef.orderBy("name", Query.Direction.ASCENDING).startAt(str).endAt(str+"~");
        FirestoreRecyclerOptions<Equipments> options = new FirestoreRecyclerOptions.Builder<Equipments>()
                .setQuery(query, Equipments.class).build();

        adapter = new EquipmentsAdapter(options);
        adapter.startListening();

        RecyclerView recyclerView = findViewById(R.id.rv_listEquipments);
        recyclerView.setAdapter(adapter);

//  --------->>>>  //////////////////////////////// ADDING ADAPTER ///////////////////////////////////////

        adapter.setOnItemClickListener(new EquipmentsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int pos) {
                Equipments equipments = documentSnapshot.toObject(Equipments.class);
                String path = documentSnapshot.getReference().getPath();

                Intent i = new Intent(RoomsDataActivity.this, EquipmentsDataActivity.class);
                i.putExtra("path", path);
                startActivity(i);
            }
        });

    }
//  --------->>>>             //////////////////////////////////// END ///////////////////////////////////


}