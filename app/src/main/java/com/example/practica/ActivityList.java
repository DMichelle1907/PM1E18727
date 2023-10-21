package com.example.practica;

import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;

import com.example.practica.Configuracion.SQLiteConexion;
import com.example.practica.Configuracion.Transacciones;
import com.example.practica.Models.Contactos;

import java.util.ArrayList;
import java.util.Arrays;

public class ActivityList extends AppCompatActivity {
    SQLiteConexion Conexion;
    ListView ListView;
    ArrayList<Contactos> ListCountry;
    ArrayList<String> ArregloContactos;
    private int idContact;
    Button btnCompartir, btnActualizar, btnEliminar, btnVer, btnBuscar;

    private final Intent pasarDatosAct = new Intent(getApplicationContext(), ActivityUpdate.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);


        btnActualizar = (Button) findViewById(R.id.btnActualizar);
        btnEliminar = (Button) findViewById(R.id.btnEliminar);
        btnCompartir = (Button) findViewById(R.id.btnCompartir);
        btnVer = (Button) findViewById(R.id.btnVer);
        btnActualizar = (Button) findViewById(R.id.btnActualizar);
        btnBuscar = (Button) findViewById(R.id.btnBuscar);

        try {
            Conexion = new SQLiteConexion(this, Transacciones.namedb, null, 1);
            ListView = (ListView) findViewById(R.id.ListView);
            GetCountry();

            ArrayAdapter adp = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ArregloContactos);
            ListView.setAdapter(adp);
            ListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    final String ItemName = ListCountry.get(position).getNombre();
                    final int telefono = ListCountry.get(position).getTelefono();
                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityList.this);
                    builder.setMessage("¿Llamar a " + ItemName + "?")
                            .setPositiveButton("Llamar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + telefono));
                                    startActivity(intent);

                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                    return true;
                }
            });
            ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    idContact = ListCountry.get(i).getId();
                    if(ListCountry.get(i).getId() != null && ListCountry.get(i).getId() != 0) {
                        pasarDatosAct.putExtra("id", ListCountry.get(i).getId());
                    }if(ListCountry.get(i).getNombre() != null && !ListCountry.get(i).getNombre().isEmpty()){
                        pasarDatosAct.putExtra("nombre", ListCountry.get(i).getNombre());
                    }if(ListCountry.get(i).getTelefono() != null && ListCountry.get(i).getTelefono() != 0){
                        pasarDatosAct.putExtra("telefono", ListCountry.get(i).getTelefono());
                    }if(ListCountry.get(i).getPais() != null && !ListCountry.get(i).getPais().isEmpty()){
                        pasarDatosAct.putExtra("pais", ListCountry.get(i).getPais());
                    }if(ListCountry.get(i).getNota() != null && !ListCountry.get(i).getNota().isEmpty()){
                        pasarDatosAct.putExtra("nota", ListCountry.get(i).getNota());
                    }if(ListCountry.get(i).getImg() != null && ListCountry.get(i).getImg().length != 0){
                        pasarDatosAct.putExtra("imagen", ListCountry.get(i).getImg());
                    }
                }
            });




        } catch (Exception ex) {
            ex.toString();
        }

     //Opciones
        View.OnClickListener butonclik = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Class<?> actividad = null;
                if (v.getId() == R.id.btnActualizar) {
                    startActivity(pasarDatosAct);
                    actividad = ActivityUpdate.class;
                }  else if (v.getId() == R.id.btnCompartir) {
                   // actividad = ActivityCom.class;
                } else if (v.getId() == R.id.btnVer) {
                    actividad = ActivityVFoto.class;
                }
                else if(v.getId() == R.id.btnEliminar){
                    if (idContact != 0) {
                        deletContact(idContact);
                    } else {
                        Toast.makeText(getApplicationContext(), "No hay datos para borrar", Toast.LENGTH_LONG).show();
                    }
                }
                if (actividad != null) {
                    NoveActivity(actividad);
                }

            }
        };
        btnActualizar.setOnClickListener(butonclik);
         btnEliminar.setOnClickListener(butonclik);
         btnVer.setOnClickListener(butonclik);
        btnCompartir.setOnClickListener(butonclik);

    }
    //Metodo para eliminar registro
    private void deletContact(int id) {

        SQLiteDatabase db = Conexion.getWritableDatabase();
        try {
            String selection = Transacciones.id + " = ?";
            String[] selectionArgs = {String.valueOf(id)};
            int deletedRows = db.delete(Transacciones.table, selection, selectionArgs);

            if(deletedRows > 0){
                Toast.makeText(this, "Contacto eliminado", Toast.LENGTH_LONG).show();
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private void NoveActivity(Class<?> actividad) {
        Intent intent = new Intent(getApplicationContext(), actividad);
        startActivity(intent);

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
            byte[] imageBytes = Cursor.getBlob(5);
            Country.setImg(imageBytes);
            ListCountry.add(Country);
        }
        Cursor.close();
        FillList();
    }

    private void FillList() {
        ArregloContactos = new ArrayList<String>();
        for (int i = 0; i < ListCountry.size(); i++) {
            ArregloContactos.add(ListCountry.get(i).getId() + " - " +
                    ListCountry.get(i).getNombre() + " - " +
                    ListCountry.get(i).getTelefono());
        }
    }


}
