package com.example.csm.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.DialogInterface;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.csm.R;
import com.example.csm.model.Rooms;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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


public class EquipmentsDataActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ViewHolder viewHolder = new ViewHolder();
    private static final int PICK_REQUEST = 1;
    private StorageTask uploadTask;
    private Uri uri_image;
    Intent i;
    String photo = "";
    String id = "";
    DocumentReference docRef;

    // UPDATE SPINNER -- ROOM NAME //
    boolean isEdit = false;
    //////////////////////////////////////////////////// SPINNER UPDATE ////////////////////////////////////////////////
    String newSelectedCategory = "";

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipments_data);

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
        getSupportActionBar().setTitle("Detalhe do Equipamento");
        ///////////////////////////////////////////////////////////////////////////////

        viewHolder.iv_dataPhoto = findViewById(R.id.iv_dataPhoto);

        viewHolder.et_dataName = findViewById(R.id.et_dataName);
        viewHolder.et_dataBrand = findViewById(R.id.et_dataBrand);
        viewHolder.et_dataDescription = findViewById(R.id.et_dataDescription);
        viewHolder.et_dataAssign = findViewById(R.id.et_dataAssign);

        viewHolder.bt_dataEdit = findViewById(R.id.bt_dataEdit);
        viewHolder.bt_dataDelete = findViewById(R.id.bt_dataDelete);

        viewHolder.bt_dataGenQRCode = findViewById(R.id.bt_dataGenQRCode);
        viewHolder.bt_dataScanQRCode = findViewById(R.id.bt_dataScanQRCode);
        viewHolder.tv_dataDocId = findViewById(R.id.tv_dataDocId);

        viewHolder.spinner_equipment_category_edit = findViewById(R.id.spinner_equipment_category_edit);

        /// ROOMS NAME SPINNER UPDATE //
        viewHolder.spinner_roomsName_list = findViewById(R.id.spinner_roomsName_list);

        viewHolder.ll_dataAssign = findViewById(R.id.ll_dataAssign);

        i = getIntent();

        String path = i.getExtras().getString("path");
        id = path.substring(path.indexOf("/") + 1);

        docRef = db.collection("Equipments").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        //Toast.makeText(EquipmentsDataActivity.this, "Dados lidos com sucesso", Toast.LENGTH_SHORT).show();
                        viewHolder.et_dataName.setText(documentSnapshot.getData().get("name").toString());
                        viewHolder.et_dataBrand.setText(documentSnapshot.getData().get("brand").toString());
                        viewHolder.et_dataDescription.setText(documentSnapshot.getData().get("description").toString());
                        //viewHolder.et_dataRoomId.setText(documentSnapshot.getData().get("roomId").toString());
                        viewHolder.et_dataAssign.setText(documentSnapshot.getData().get("assign").toString());
                        viewHolder.tv_dataDocId.setText(documentSnapshot.getId());

                        if (viewHolder.et_dataAssign.length() > 0)
                            viewHolder.ll_dataAssign.setVisibility(View.VISIBLE);

                        //////////////////////////////// -----> //  SPINNER UPDATE // <------ // ////////////////////////////////////////

                        String getCategoryString = String.valueOf(documentSnapshot.getData().get("category"));
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

                        //ArrayAdapter aa = new ArrayAdapter(EquipmentsDataActivity.this, android.R.layout.simple_spinner_item, categoryList);
                        ArrayAdapter aa = new ArrayAdapter(EquipmentsDataActivity.this, R.layout.spinner_item, categoryList);
                        //aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        viewHolder.spinner_equipment_category_edit.setAdapter(aa);
                        viewHolder.spinner_equipment_category_edit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                                newSelectedCategory = categoryList.get(i).toString();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                                //Toast.makeText(MainActivity.this, "Nada selecionado", Toast.LENGTH_SHORT).show();
                            }
                        });
// SPINNER SET ON POSITION FROM DATABASE
                        int index = 0;
                        for (int i = 0; i < categoryList.size(); i++) {
                            if (getCategoryString.contains((CharSequence) categoryList.get(i))) {
                                index = i;
                                break;
                            } else {
                                index = 0;
                            }
                        }
                        //Toast.makeText(EquipmentsDataActivity.this, String.valueOf(index), Toast.LENGTH_SHORT).show();
                        // FINDING THE POSITION OF CATEGORY INSIDE SPINNER
                        viewHolder.spinner_equipment_category_edit.setSelection(index);
                        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                        try {
                            Picasso.get().load(documentSnapshot.getData().get("photo").toString()).into(viewHolder.iv_dataPhoto);
                        } catch (Exception e) {

                        }
                    } else {
                        Toast.makeText(EquipmentsDataActivity.this, "Erro ao apresentar dados.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Toast.makeText(EquipmentsDataActivity.this, "Erro ao apresentar dados.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        //////// --------------->> TESTE SPINNER SALAS <<---------------- /////////


        //FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference notebookRef = db.collection("Rooms");

        notebookRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List roomsNameList = new ArrayList();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Rooms rooms = documentSnapshot.toObject(Rooms.class);
                    String RoomsName = rooms.getRoomName();
                    roomsNameList.add(RoomsName);
                }


                //////////////////////////// populating spinner ///////////////////////////

                ArrayAdapter aa = new ArrayAdapter(EquipmentsDataActivity.this, R.layout.spinner_item, roomsNameList);

                //aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                viewHolder.spinner_roomsName_list.setAdapter(aa);
                viewHolder.spinner_roomsName_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        String posName = roomsNameList.get(i).toString();
                        //Toast.makeText(EquipmentsDataActivity.this, posName, Toast.LENGTH_SHORT).show();
                        if (isEdit)
                            // updating inside FIRESTORE
                            docRef.update("roomId", posName);


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        //Toast.makeText(MainActivity.this, "Nada selecionado", Toast.LENGTH_SHORT).show();
                    }
                });
///////////////////////////////////////////////////////

                // ORDERING THE LIST
                Collections.sort(roomsNameList);

                ///////////////////////////////////
                i = getIntent();

                String path = i.getExtras().getString("path");
                id = path.substring(path.indexOf("/") + 1);

                docRef = db.collection("Equipments").document(id);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()) {
                                //Toast.makeText(EquipmentsDataActivity.this, "Dados lidos com sucesso", Toast.LENGTH_SHORT).show();

                                //viewHolder.et_dataRoomId.setText(documentSnapshot.getData().get("roomId").toString());
                                String getRoomName = String.valueOf(documentSnapshot.getData().get("roomId"));
                                //Toast.makeText(EquipmentsDataActivity.this, getRoomName, Toast.LENGTH_SHORT).show();


                                // SPINNER SET ON POSITION FROM DATABASE

                                int index = 0;
                                for (int i = 0; i < roomsNameList.size(); i++) {
                                    if (getRoomName.contains((CharSequence) roomsNameList.get(i))) {
                                        index = i;
                                        break;
                                    } else {
                                        index = 0;
                                    }
                                }
                                //Toast.makeText(EquipmentsDataActivity.this, String.valueOf(index), Toast.LENGTH_SHORT).show();
                                // FINDING THE POSITION OF CATEGORY INSIDE SPINNER

                                viewHolder.spinner_roomsName_list.setSelection(index);
                                ///////////////////////////////////////////////////////////
                                isEdit = true;

                            }
                        }
                    }
                });

            }


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Log.d(TAG,e.toString());
            }
        });


        ///////////////// --------------------------------------- ///////////////////

        viewHolder.iv_dataPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(i, PICK_REQUEST);
            }
        });

        viewHolder.bt_dataEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (viewHolder.et_dataName.getText().toString().isEmpty() || viewHolder.et_dataBrand.getText().toString().isEmpty() || newSelectedCategory.equals("Selecione uma opção")) {
                    Toast.makeText(EquipmentsDataActivity.this, "Por favor, preencha os dados antes de avançar!", Toast.LENGTH_SHORT).show();

                } else {
                    updateEquip();
                    uploadPhoto();
                    Toast.makeText(EquipmentsDataActivity.this, "Equipamento editado com sucesso.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

        });

        viewHolder.bt_dataDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(viewHolder.et_dataName.getContext());
                builder.setTitle("Tem certeza?");

                builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        docRef.delete();
                        Toast.makeText(EquipmentsDataActivity.this, "Eliminado com sucesso.", Toast.LENGTH_SHORT).show();
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

        ///////////////////////////////////BOTAO GERAR QR CODE////////////////////////////////////

        viewHolder.bt_dataGenQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EquipmentsDataActivity.this, GenerateQRCodeActivity.class);
                i.putExtra("docId", viewHolder.tv_dataDocId.getText().toString());
                i.putExtra("name", viewHolder.et_dataName.getText().toString());
                startActivity(i);
            }
        });

        ///////////////////////////////END BOTAO GERAR QR CODE////////////////////////////////////

        /////////////////////////////////// BOTAO SCAN QR CODE PARA ASSIGNAR //////////////////////
        viewHolder.bt_dataScanQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EquipmentsDataActivity.this, ScannerAssignActivity.class);
                i.putExtra("docId", viewHolder.tv_dataDocId.getText().toString());
                startActivity(i);
            }
        });
        //////////////////////////// END   BOTAO SCAN QR CODE PARA ASSIGNAR //////////////////////

    }

    private void uploadPhoto() {

        if (uploadTask != null && uploadTask.isInProgress()) {
            Toast.makeText(EquipmentsDataActivity.this, "Imagem em upload, aguarde", Toast.LENGTH_SHORT).show();
        } else {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference("Equipments");
            if (uri_image != null) {
                StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uri_image));
                fileRef.putFile(uri_image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(EquipmentsDataActivity.this, "Upload da imagem com sucesso.", Toast.LENGTH_SHORT).show();
                        Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uri.isComplete()) ;
                        photo = uri.getResult().toString();
                        docRef.update("photo", photo);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EquipmentsDataActivity.this, "Erro no upload da imagem.", Toast.LENGTH_SHORT).show();

                    }
                });
            }
//  --------->>>>
            /*else {
                StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uri_image));
                fileRef.putFile(uri_image);

                updateEquip();

                Toast.makeText(EquipmentsDataActivity.this, "Imagem não selecionada", Toast.LENGTH_SHORT).show();
            }*/
//  --------->>>>
        }


    }


    private void updateEquip() {
        docRef.update("name", viewHolder.et_dataName.getText().toString());
        docRef.update("brand", viewHolder.et_dataBrand.getText().toString());
        docRef.update("description", viewHolder.et_dataDescription.getText().toString());
        //docRef.update("roomId", viewHolder.et_dataRoomId.getText().toString());
        docRef.update("assign", viewHolder.et_dataAssign.getText().toString());
        docRef.update("category", newSelectedCategory);

    }


    private String getFileExtension(Uri uri_image) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri_image));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri_image = data.getData();
            Picasso.get().load(uri_image).into(viewHolder.iv_dataPhoto);
        }
    }

    private class ViewHolder {
        ImageView iv_dataPhoto;
        EditText et_dataName, et_dataBrand, et_dataDescription, et_dataRoomId, et_dataAssign, et_dataCategory;
        Button bt_dataEdit, bt_dataDelete, bt_dataCancel, bt_dataOk, bt_dataGenQRCode, bt_dataScanQRCode;
        TextView tv_dataDocId;
        Spinner spinner_equipment_category_edit, spinner_roomsName_list;
        LinearLayout ll_dataAssign;
    }


}