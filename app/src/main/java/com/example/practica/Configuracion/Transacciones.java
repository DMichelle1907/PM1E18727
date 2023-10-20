package com.example.practica.Configuracion;

public class Transacciones {

        //Nombre de la bd
        public static final String namedb = "Agenda";

        //Nombre de la tabla de la bd
        public static final String table = "contactos";

        //Nombre de los campos de la bd
        public static final String id = "id";
        public static final String nombre ="nombre";
        public static final String telefono = "telefono";
        public static final String nota = "nota";
        public static final String pais = "pais";

        public static final String foto = "foto";
        //Consultas BD
        public static final String CreateTableContactos = "CREATE TABLE contactos "
                + "(id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, telefono INTEGER, nota TEXT, pais TEXT, foto BLOB)";

        //DML
        public static final String DropTableContactos = "DROP TABLE IF EXISTS contactos";

        //Seleccionar
        public static final String SelectTableContactos = "SELECT * FROM " + Transacciones.table;
        public static final String UpdateContacto = "UPDATE " + Transacciones.table + " SET " +
                Transacciones.nombre + " = ?, " +
                Transacciones.telefono + " = ?, " +
                Transacciones.nota + " = ?, " +
                Transacciones.pais + " = ?, " +
                Transacciones.foto + " = ? " +
                "WHERE " + Transacciones.id + " = ?";




}
