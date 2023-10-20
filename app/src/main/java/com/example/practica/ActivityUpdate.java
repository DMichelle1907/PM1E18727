package com.example.practica;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
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
        spinner.setAdapter(adapter);

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetCountry();
            }
        });
    }

    private void GetCountry() {
        SQLiteDatabase db = Conexion.getReadableDatabase();
        Cursor Cursor = db.rawQuery(Transacciones.SelectTableContactos, null);
        while (Cursor.moveToNext()) {
            String nombre = Cursor.getString(Cursor.getColumnIndexOrThrow(Transacciones.nombre));
            int telefono = Cursor.getInt(Cursor.getColumnIndexOrThrow(Transacciones.telefono));
            String nota = Cursor.getString(Cursor.getColumnIndexOrThrow(Transacciones.nota));
            String pais = Cursor.getString(Cursor.getColumnIndexOrThrow(Transacciones.pais));
            // (Asumiendo que tienes EditText con IDs: editTextNombre, editTextTelefono, editTextNota, editTextPais)
            edtNombre.setText(nombre);
            edtTelefono.setText(String.valueOf(telefono));
            edtNota.setText(nota);
        }
        Cursor.close();
    }
}
