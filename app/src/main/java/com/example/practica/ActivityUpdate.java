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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.practica.Configuracion.SQLiteConexion;
import com.example.practica.Configuracion.Transacciones;
import com.example.practica.Models.Contactos;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class ActivityUpdate extends AppCompatActivity {
    EditText edtNombre, edtTelefono, edtNota, edtPais;
    SQLiteConexion Conexion;
    Button btnActualizar;
    ArrayList<Contactos> ListCountry;
    ArrayList<String> ArregloContactos;
    ImageView Img;
    Spinner spinner;
    private int idContact;

    static final int Peticion_AccesoCamara = 101;
    static final int Peticion_TomarFoto = 102;
    String currentPhotoPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        edtNombre = (EditText) findViewById(R.id.edtNombre);
        edtTelefono = (EditText) findViewById(R.id.edtTelefono);
        edtNota = (EditText) findViewById(R.id.edtnota);
        edtPais = (EditText) findViewById(R.id.edtPais);
        btnActualizar = (Button)findViewById(R.id.btnUpdate);
        spinner = (Spinner)findViewById(R.id.SpinnerAct);
        Img = (ImageView)findViewById(R.id.imgActualizar);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.country_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        try {
            Conexion = new SQLiteConexion(this, Transacciones.namedb, null, 1);
            GetCountry();

            Intent obtenerDatos = getIntent();

            idContact = obtenerDatos.getIntExtra("id", 0);

            Bitmap viewImage = BitmapFactory.decodeByteArray(obtenerDatos.getByteArrayExtra("imagen"),0,obtenerDatos.getByteArrayExtra("imagen").length);

            edtNombre.setText(obtenerDatos.getStringExtra("nombre"));
            edtTelefono.setText(obtenerDatos.getIntExtra("telefono", 0));
            edtPais.setText(obtenerDatos.getCharSequenceExtra("pais"));
            edtNota.setText(obtenerDatos.getCharSequenceExtra("nota"));
            Img.setImageBitmap(viewImage);

            btnActualizar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(idContact != 0){
                        updateContact(idContact,currentPhotoPath, String.valueOf(edtPais), String.valueOf(edtNombre),edtTelefono, String.valueOf(edtNota));
                    }else{
                        Toast.makeText(getApplicationContext(), "no hay datos para borrar", Toast.LENGTH_LONG).show();
                    }
                }
            });

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                    idContact = ListCountry.get(i).getId();
//                    image = ListCountry.get(i).getImg();
//                    pais = ListCountry.get(i).getPais();
//                    nombre = ListCountry.get(i).getNombre();
//                    telefono = String.valueOf(ListCountry.get(i).getTelefono());
//                    nota = ListCountry.get(i).getNota();

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        }catch (Exception ex){
            ex.printStackTrace();
        }


        Img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Permisos();
            }
        });
    }

    private void updateContact(int id, String image, String pais, String nombre, EditText telefono, String nota){
        SQLiteDatabase db = Conexion.getWritableDatabase();

        // Nuevos valores para las columnas
        ContentValues values = new ContentValues();
        //Validar si se ingreso un dato de lo contrario es porque no se actualizara
        if (nombre != null && !nombre.isEmpty()) {
            values.put(Transacciones.nombre, nombre);
        }if (telefono != null) {
            values.put(Transacciones.telefono, String.valueOf(telefono));
        }if (nota != null && !nota.isEmpty()) {
            values.put(Transacciones.nota, nota);
        }if (pais != null && !pais.isEmpty()) {
            values.put(Transacciones.pais, pais);
        }if (image != null && !image.isEmpty()) {
            values.put(Transacciones.foto, image);
        }

        String selection = Transacciones.id+ " = ?";
        String[] selectionArgs = { String.valueOf(id) };

        int count = db.update(
                Transacciones.table,
                values,
                selection,
                selectionArgs);


    }

    private void GetCountry() {
        SQLiteDatabase db = Conexion.getReadableDatabase();
        Contactos Country = null;
        ListCountry = new ArrayList<Contactos>();
        Cursor Cursor = db.rawQuery(Transacciones.SelectTableContactos, null);
        while (Cursor.moveToNext()) {
            Country = new Contactos();
            Country.setPais(Cursor.getString(4));
            ListCountry.add(Country);
        }
//        while (Cursor.moveToNext()) {
//            String nombre = Cursor.getString(Cursor.getColumnIndexOrThrow(Transacciones.nombre));
//            int telefono = Cursor.getInt(Cursor.getColumnIndexOrThrow(Transacciones.telefono));
//            String nota = Cursor.getString(Cursor.getColumnIndexOrThrow(Transacciones.nota));
//            String pais = Cursor.getString(Cursor.getColumnIndexOrThrow(Transacciones.pais));
//            // (Asumiendo que tienes EditText con IDs: editTextNombre, editTextTelefono, editTextNota, editTextPais)
//            edtNombre.setText(nombre);
//            edtTelefono.setText(String.valueOf(telefono));
//            edtNota.setText(nota);
//        }
        Cursor.close();
    }


    //Imagen


    private void Permisos() {
        if(ContextCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)

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
}
