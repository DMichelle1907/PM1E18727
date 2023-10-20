package com.example.practica;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.practica.Configuracion.SQLiteConexion;
import com.example.practica.Configuracion.Transacciones;
import com.example.practica.Models.Contactos;

import java.util.ArrayList;

public class ActivityVFoto extends AppCompatActivity {
    ImageView imgGuardada;
    SQLiteConexion Conexion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vfoto);
        imgGuardada = (ImageView) findViewById(R.id.ImgGuardada);
    }


}