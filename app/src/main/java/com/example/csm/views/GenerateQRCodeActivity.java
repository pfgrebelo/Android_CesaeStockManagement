package com.example.csm.views;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.csm.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class GenerateQRCodeActivity extends AppCompatActivity {

    public final static int QRCodeWidth = 500;
    Bitmap bitmap;


    private Button bt_generate, bt_download, bt_email;
    private ImageView iv_output;
    private String email = "cesaestockmanagement@gmail.com";
    private TextView tv_doc_id, tv_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qrcode);

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
        getSupportActionBar().setTitle("Gerar QR Code");
        ///////////////////////////////////////////////////////////////////////////////

        bt_generate = findViewById(R.id.bt_generate);
        bt_download = findViewById(R.id.bt_download);
        bt_download.setVisibility(View.INVISIBLE);
        iv_output = findViewById(R.id.iv_output);
        bt_email = findViewById(R.id.bt_email);
        bt_email.setVisibility(View.INVISIBLE);
        tv_doc_id = findViewById(R.id.tv_doc_id);
        tv_name = findViewById(R.id.tv_name);

        Intent i = getIntent();
        Bundle extras = i.getExtras();
        String docId = extras.getString("docId");
        String name = extras.getString("name");

        tv_doc_id.setText(docId);
        tv_name.setText(name);

        String textName = tv_name.getText().toString();

        ///////////////////// BOTAO PARA CRIAR O QR CODE E MOSTRA BOTAO DOWNLOAD E EMAIL /////////////
        bt_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = tv_doc_id.getText().toString().trim();
                if (text.isEmpty()) {
                    Toast.makeText(GenerateQRCodeActivity.this, "Sem dados para gerar o QR Code", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        bitmap = textToImageEncode(text);
                        iv_output.setImageBitmap(bitmap);
                        bt_download.setVisibility(View.VISIBLE);
                        bt_download.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "QR-" + textName + "-" + text, null);
                                Toast.makeText(GenerateQRCodeActivity.this, "Gravado na Galeria", Toast.LENGTH_SHORT).show();
                            }
                        });
                        bt_email.setVisibility(View.VISIBLE);
                        bt_email.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(Intent.ACTION_SENDTO);
                                intent.setData(Uri.parse("mailto:"));
                                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                                intent.putExtra(Intent.EXTRA_SUBJECT, "Imprimir QR-CODE - " + textName + " - " + text);
                                intent.putExtra(Intent.EXTRA_TEXT, "Esta mensagem foi enviada através da app CSM.");

                                //intent.putExtra(Intent.EXTRA_STREAM, bitmap);
                                /*if (intent.resolveActivity(getPackageManager()) != null) {
                                    startActivity(intent);
                                }*/
                                startActivity(intent);
                            }
                        });
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        /////////////////////END BOTAO PARA CRIAR O QR CODE E MOSTRA BOTAO DOWNLOAD E EMAIL /////////////
        bt_generate.performClick();     //AUTOCLICK NO BOTAO GERAR QR
    }

    ////////////////////// CRIA A IMAGEM A PARTIR DO TEXTO ////////////////////////
    private Bitmap textToImageEncode(String value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(value, BarcodeFormat.DATA_MATRIX.QR_CODE, QRCodeWidth, QRCodeWidth, null);
        } catch (IllegalArgumentException e) {
            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();
        int bitMatrixHeight = bitMatrix.getHeight();
        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offSet = y * bitMatrixWidth;
            for (int x = 0; x < bitMatrixWidth; x++) {
                pixels[offSet + x] = bitMatrix.get(x, y) ? getResources().getColor(R.color.black) : getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);
        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }
    //////////////////////END  CRIA A IMAGEM A PARTIR DO TEXTO ////////////////////////
}