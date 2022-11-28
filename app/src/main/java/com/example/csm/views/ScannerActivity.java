package com.example.csm.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.csm.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class ScannerActivity extends AppCompatActivity {

    public static final int CAMERA_PERMISSION_CODE = 100;

    private SurfaceView surfaceView;
    private CameraSource cameraSource;
    private TextView scannedText, tv_scanned_info;
    private BarcodeDetector barcodeDetector;
    private Button bt_open_scan;
    private ImageView gif_scanner_check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

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
        getSupportActionBar().setTitle("QR Code");
        ///////////////////////////////////////////////////////////////////////////////

        surfaceView = findViewById(R.id.sv_camera);
        scannedText = findViewById(R.id.tv_text_scanned);
        bt_open_scan = findViewById(R.id.bt_open_scan);
        tv_scanned_info = findViewById(R.id.tv_scanned_info);
        gif_scanner_check = findViewById(R.id.gif_scanner_check);


        ////////////////////////////ABRE O EQUIPAMENTO DO ID QUE FEZ SCAN///////////////////////
        bt_open_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String path = scannedText.getText().toString();

                Intent i = new Intent(ScannerActivity.this, EquipmentsDataActivity.class);
                i.putExtra("path", path);
                startActivity(i);
            }
        });
        ////////////////////////END ABRE O EQUIPAMENTO DO ID QUE FEZ SCAN///////////////////////

        //INICIA O DETECTOR DE QR E A CAMARA
        barcodeDetector = new BarcodeDetector.Builder(getApplicationContext()).setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSource = new CameraSource.Builder(getApplicationContext(), barcodeDetector).setRequestedPreviewSize(1280, 960).build();

        ////////////////////////////ASSOCIA A CAMARA AO SURFACE VIEW, CASO TENHA AUTORIZAÇÃO/////////////////////
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                try {
                    cameraSource.start(surfaceHolder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

            }
        });
        ///////////////////////   END   ////////////////////////////////////

        ////////////////// DETETOR DO QR ////////////////////////////
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(@NonNull Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrcode = detections.getDetectedItems();
                if (qrcode.size() != 0) {
                    scannedText.post(new Runnable() {
                        @Override
                        public void run() {
                            scannedText.setText(qrcode.valueAt(0).displayValue);
                            tv_scanned_info.setText("QR Code lido com sucesso.");
                            bt_open_scan.setVisibility(View.VISIBLE);
                            gif_scanner_check.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });
        ////////////////// END DETETOR DO QR ////////////////////////////

    }

    ///////////////////////////////// VERIFICA PERMISSÃO DA APP À CAMARA ///////////////////////////
    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(ScannerActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(ScannerActivity.this, new String[]{permission}, requestCode);
        } else {
            Toast.makeText(this, "Já efetuada a permissão.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permissão efetuada.", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent());
            } else {
                Toast.makeText(this, "Permissão negada.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    ///////////////////////////////// END VERIFICA PERMISSÃO DA APP À CAMARA ///////////////////////////

    @Override
    protected void onStart() {
        super.onStart();

        checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
    }
}