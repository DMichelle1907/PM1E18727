package com.example.practica;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.practica.Configuracion.SQLiteConexion;
import com.example.practica.Configuracion.Transacciones;
import com.example.practica.Models.Contactos;

import java.util.ArrayList;

public class ActivityUpdate extends AppCompatActivity {
    EditText edtNombre, edtTelefono, edtNota;
    SQLiteConexion Conexion;
    Button btnActualizar;
    ArrayList<Contactos> ListCountry;
    ArrayList<String> ArregloContactos;
    Spinner spinner;
    private int idContact;
    private String nombre;
    private String telefono;
    private String nota;
    private String pais;
    private byte[] image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        edtNombre = (EditText) findViewById(R.id.edtNombre);
        edtTelefono = (EditText) findViewById(R.id.edtTelefono);
        edtNota = (EditText) findViewById(R.id.edtNota);
        btnActualizar = (Button)findViewById(R.id.btnUpdate);
        spinner = (Spinner)findViewById(R.id.SpinnerAct);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.country_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        try {
            Conexion = new SQLiteConexion(this, Transacciones.namedb, null, 1);
            GetCountry();

            btnActualizar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    nombre = String.valueOf(edtNombre);
                    telefono = String.valueOf(edtTelefono);
                    nota = String.valueOf(edtNota);

                    if(image.length != 0 && !nombre.trim().isEmpty() &&
                            !telefono.trim().isEmpty() && !nota.trim().isEmpty()){

                        updateContact(idContact);
                    }
                }
            });

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    image = ListCountry.get(i).getImg();
                    pais = ListCountry.get(i).getPais();
                    nombre = ListCountry.get(i).getNombre();
                    telefono = String.valueOf(ListCountry.get(i).getTelefono());
                    nota = ListCountry.get(i).getNota();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void updateContact(int id){
        SQLiteDatabase db = Conexion.getWritableDatabase();

        // Nuevos valores para las columnas
        String Nombre = String.valueOf(edtNombre);
        String Telefono = String.valueOf(edtTelefono);
        String Nota = String.valueOf(edtNota);

        ContentValues values = new ContentValues();
        values.put(Transacciones.foto, image);
        values.put(Transacciones.nombre, pais);
        values.put(Transacciones.nombre, Nombre);
        values.put(Transacciones.telefono, Telefono);
        values.put(Transacciones.nota, Nota);

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
            Country.setId(Cursor.getInt(0));
            Country.setNombre(Cursor.getString(1));
            Country.setTelefono(Cursor.getInt(2));
            Country.setNota(Cursor.getString(3));
            Country.setPais(Cursor.getString(4));
            image = Cursor.getBlob(5);
            Country.setImg(image);
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
}
