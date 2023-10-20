package com.example.practica.Configuracion;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLiteConexion extends SQLiteOpenHelper {
    //Gestionar la creacion de bd y actualizacion
    public SQLiteConexion(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    //Metodo cuando la bd se crea por primera vez, se llaman secuencias SQL
    @Override
    public void onCreate(SQLiteDatabase db) {
        //crear objetos de bd
        db.execSQL(Transacciones.CreateTableContactos);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        //borrar datos de la bd
        db.execSQL(Transacciones.DropTableContactos);
        onCreate(db); //volviendo a crear la db
    }

    public void onUpdate(SQLiteDatabase db, int i, int i1) {
        //Actualizar bd
        db.execSQL(Transacciones.UpdateContacto);
        onCreate(db); //volviendo a crear la db
    }



}
