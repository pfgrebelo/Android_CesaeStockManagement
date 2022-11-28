package com.example.csm.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.csm.R;
import com.example.csm.model.Equipments;
import com.example.csm.model.Rooms;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class newEquipmentActivity extends AppCompatActivity {

    private ViewHolder viewHolder = new ViewHolder();
    private static final int PICK_REQUEST = 1;
    private StorageTask uploadTask;
    private Uri uri_image;
    private String name = "", brand = "", description = "", photo = "", roomId = "", assign = "";

    //////////////////////////////////////////////////// SPINNER UPDATE ////////////////////////////////////////////////
    String newSelectedCategory = "";
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_equipment);

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
        getSupportActionBar().setTitle("Novo Equipamento");
        ///////////////////////////////////////////////////////////////////////////////

        viewHolder.iv_newPhoto = findViewById(R.id.iv_newPhoto);
        viewHolder.et_newName = findViewById(R.id.et_newName);
        viewHolder.et_newBrand = findViewById(R.id.et_newBrand);
        viewHolder.et_newDescription = findViewById(R.id.et_newDescription);

        viewHolder.et_newAssign = findViewById(R.id.et_newAssign);
        viewHolder.bt_newAdd = findViewById(R.id.bt_newAdd);
        viewHolder.bt_newBack = findViewById(R.id.bt_newBack);
        viewHolder.spinner_addnew_equip_roomsName_list = findViewById(R.id.spinner_addnew_equip_roomsName_list);

        //////////////////////////////// -----> //  SPINNER UPDATE // <------ // ////////////////////////////////////////

        viewHolder.spinner_newEquipment_category = findViewById(R.id.spinner_newEquipment_category);

        ///////////////////////// ~~~~~~~~~~~ ROOM LIST SPINNER ~~~~~~~~~~~ //////////////////////////////////


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference finalRoomRef = db.collection("Rooms");


        finalRoomRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List roomsNameList = new ArrayList();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Rooms rooms = documentSnapshot.toObject(Rooms.class);
                    String RoomsName = rooms.getRoomName();
                    roomsNameList.add(RoomsName);
                }


                //////////////////////////// populating spinner ///////////////////////////

                ArrayAdapter aa = new ArrayAdapter(newEquipmentActivity.this, R.layout.spinner_item, roomsNameList);
                //aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                viewHolder.spinner_addnew_equip_roomsName_list.setAdapter(aa);
                viewHolder.spinner_addnew_equip_roomsName_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (i > 0) {
                            String posName = roomsNameList.get(i).toString();
                            //Toast.makeText(EquipmentsDataActivity.this, posName, Toast.LENGTH_SHORT).show();

                            // updating inside FIRESTORE
                            roomId = posName;

                            //docRef.update("roomId", posName);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        //Toast.makeText(MainActivity.this, "Nada selecionado", Toast.LENGTH_SHORT).show();
                    }
                });
                ///////////////////////////////////////////////////////

                // Toast.makeText(newEquipmentActivity.this, roomId, Toast.LENGTH_SHORT).show();
                // ORDERING THE LIST
                Collections.sort(roomsNameList);
                roomsNameList.add(0, "Selecione uma opção");

            }


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Log.d(TAG,e.toString());
            }
        });


        ////////////////////////////////////////////////////////////////////////////////////////////////////////


        //Toast.makeText(EquipmentsDataActivity.this, getCategoryString, Toast.LENGTH_SHORT).show();

        //Creating list for categories
        List categoryList = new ArrayList();

        categoryList.add("Selecione uma opção");
        categoryList.add("Computadores");
        categoryList.add("Monitores");
        categoryList.add("Periféricos");
        categoryList.add("Mobiliário");
        categoryList.add("Outros");


        // - POPULATING SPINNER - //

        //ArrayAdapter aa = new ArrayAdapter(newEquipmentActivity.this, android.R.layout.simple_spinner_item, categoryList);
        //aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter aa = new ArrayAdapter(newEquipmentActivity.this, R.layout.spinner_item, categoryList);
        viewHolder.spinner_newEquipment_category.setAdapter(aa);

        viewHolder.spinner_newEquipment_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                newSelectedCategory = categoryList.get(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Toast.makeText(MainActivity.this, "Nada selecionado", Toast.LENGTH_SHORT).show();
            }
        });


        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        viewHolder.bt_newAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = viewHolder.et_newName.getText().toString();
                brand = viewHolder.et_newBrand.getText().toString();
                description = viewHolder.et_newDescription.getText().toString();
                //roomId = viewHolder.et_newRoomId.getText().toString();
                assign = viewHolder.et_newAssign.getText().toString();
                //category = viewHolder.et_newCategory.getText().toString();
                photo = "";


//  --------->>>>           // VERIFY IF THE INPUTS ARE EMPTY      ----->>>>>>   //////////////// SPINNER UPDATE VALIDATION //////////////////
                if (name.trim().isEmpty() || brand.trim().isEmpty() || newSelectedCategory.equals(categoryList.get(0))) {
                    Toast.makeText(newEquipmentActivity.this, "Preencher todos os campos", Toast.LENGTH_SHORT).show();

                } else {
                    //////// END
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference("Equipments");
                    if (uri_image != null) {
                        StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uri_image));
                        fileRef.putFile(uri_image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(newEquipmentActivity.this, "Upload da imagem com sucesso", Toast.LENGTH_SHORT).show();
                                Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                                while (!uri.isComplete()) ;
                                photo = uri.getResult().toString();
                                CollectionReference reference = FirebaseFirestore.getInstance().collection("Equipments");
                                reference.add(new Equipments(name, brand, description, photo, roomId, assign, newSelectedCategory)); /// spinner update


                                clearAll();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(newEquipmentActivity.this, "Erro no upload da imagem", Toast.LENGTH_SHORT).show();

                            }
                        });
                    } else {
//  --------->>>>            // IF NO IMG/PHOTO IS SELECTED/UPLOADED BY USER
                        photo = "";
                        CollectionReference reference = FirebaseFirestore.getInstance().collection("Equipments");
                        reference.add(new Equipments(name, brand, description, photo, roomId, assign, newSelectedCategory));  /// spinner update
                        Toast.makeText(newEquipmentActivity.this, "Novo equipamento adicionado", Toast.LENGTH_SHORT).show();
                        clearAll();
                        finish();
                        /// END
                    }

                    /*else{
                        Toast.makeText(newEquipmentActivity.this, "Imagem não selecionada", Toast.LENGTH_SHORT).show();

                    }*/

                    //
                    Toast.makeText(newEquipmentActivity.this, "Novo equipamento adicionado", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });


        viewHolder.iv_newPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(i, PICK_REQUEST);
            }
        });


        viewHolder.bt_newBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri_image = data.getData();
            Picasso.get().load(uri_image).into(viewHolder.iv_newPhoto);
        }
    }

    private String getFileExtension(Uri uri_image) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri_image));
    }


    private class ViewHolder {
        ImageView iv_newPhoto;
        EditText et_newName, et_newBrand, et_newDescription, et_newRoomId, et_newAssign;
        Button bt_newBack, bt_newAdd;

        // SPINNER UPDATE /// <<<------------------------------------------------------------------------
        Spinner spinner_newEquipment_category, spinner_addnew_equip_roomsName_list;
        //////////////////////////////////////////////////////////////////////////////////////////////////
    }

    private void clearAll() {
        viewHolder.et_newName.setText("");
        viewHolder.et_newBrand.setText("");
        viewHolder.et_newDescription.setText("");
        //viewHolder.et_newColor.setText("");
        //viewHolder.et_newRoomId.setText("");
        viewHolder.et_newAssign.setText("");
        //viewHolder.et_newCategory.setText("");
    }

}