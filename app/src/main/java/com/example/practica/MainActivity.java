package com.example.practica;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.practica.Configuracion.SQLiteConexion;
import com.example.practica.Configuracion.Transacciones;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    EditText edtNombre, edtTelefono, edtNota;
    Spinner pais;
    Button btnSalvar, btnContactos;

    ImageView Img;
    private static final int IMAGE_CAPTURE = 1;
    private byte[] fotoTomada;
    static final int Peticion_AccesoCamara = 101;
    static final int Peticion_TomarFoto = 102;

    Button btnFoto;
    String currentPhotoPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtNombre = (EditText) findViewById(R.id.edtNombre);
        edtTelefono = (EditText) findViewById(R.id.edtTelefono);
        edtNota = (EditText) findViewById(R.id.edtNota);
        btnSalvar = (Button) findViewById(R.id.btnGuardar);
        btnContactos = (Button) findViewById(R.id.btnContactos);
        pais = (Spinner) findViewById(R.id.spinner);
        Img = (ImageView) findViewById(R.id.Img);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.country_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pais.setAdapter(adapter);


        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = edtNombre.getText().toString();
                String telefono = edtTelefono.getText().toString();
                if (nombre.isEmpty()) {
                    edtNombre.setError("Este campo no puede estar vacío");
                }
                else if(telefono.isEmpty()) {
                    edtTelefono.setError("Este campo no puede estar vacío");
                }else{
                    AddContact();
                }


            }
        });

        btnContactos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActivityList.class);
                startActivity(intent);
            }
        });

        Img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Permisos();
            }
        });
    }


    private void Permisos() {
        if(ContextCompat.checkSelfPermission(getApplicationContext(),
               Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)

        {

            ActivityCompat.requestPermissions(this, new String[]{   Manifest.permission.CAMERA},
                    Peticion_AccesoCamara);

        }
        else
        {
            //Tomar foto
            dispatchTakePictureIntent();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == Peticion_AccesoCamara){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //Tomar foto
                dispatchTakePictureIntent();
            }else{
                Toast.makeText(getApplicationContext(), "Permiso denegado", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                //Proveedor de contenido- Provaider(Ofrece mecanismos para compartir archivos entre app
                //
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.practica.fileprovider", /*Se obtiene del build gradle Module(Ayuda a definir que pertenece a esta aplicación)*/
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, Peticion_TomarFoto);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        //Añade a formato JPEG
        String imageFileName = "JPEG_" + timeStamp + "_";
        //Accede al directorio de las imagenes
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        //Crea un formato jpg
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        //Capturar url de la img
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Peticion_TomarFoto && resultCode == RESULT_OK){
            /*
            *  Bundle extras = data.getExtras(); //Trae respuestas de la data
            Bitmap image = (Bitmap) extras.get("data");
            Img.setImageBitmap(image);
             */

            try {
                File foto = new File(currentPhotoPath);
                Img.setImageURI(Uri.fromFile(foto));

            }catch (Exception ex) {
                ex.toString();
            }

        }

    }


    private void AddContact () {
        try {
            if (currentPhotoPath!= null) {
                // Llamar la conexion
                SQLiteConexion Conexion = new SQLiteConexion(this, Transacciones.namedb, null, 1);
                // Escribir
                SQLiteDatabase db = Conexion.getWritableDatabase();
                // Contenedor de valores
                ContentValues Valores = new ContentValues();
                // Se llenan con los valores ingresados en los campos
                Valores.put(Transacciones.nombre, edtNombre.getText().toString());
                Valores.put(Transacciones.telefono, edtTelefono.getText().toString());
                Valores.put(Transacciones.nota, edtNota.getText().toString());
                Valores.put(Transacciones.pais, pais.getSelectedItem().toString());
                Valores.put(Transacciones.foto, currentPhotoPath);

                Long Result = db.insert(Transacciones.table, Transacciones.id, Valores);

                Toast.makeText(this, "Los datos se registraron correctamente", Toast.LENGTH_LONG).show();
                db.close();
            } else {
                Toast.makeText(this, "Debes tomar una foto antes de guardar el contacto.", Toast.LENGTH_LONG).show();
            }

        } catch (Exception exception) {
            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
        }
    }



}