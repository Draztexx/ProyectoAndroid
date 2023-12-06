package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Guardar extends AppCompatActivity implements View.OnClickListener {

    TextView Nombre,Puntos;
    Button VolverJugar,Ranquing;

    int puntos;



    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardar);

        Nombre=findViewById(R.id.TVNombre);
        Puntos=findViewById(R.id.TVPuntos);
        VolverJugar=findViewById(R.id.BTVolveraJugar);
        Ranquing=findViewById(R.id.BTRanquing);

        VolverJugar.setOnClickListener(this);
        Ranquing.setOnClickListener(this);




        SharedPreferences preferences= getSharedPreferences("User", MODE_PRIVATE);

       Nombre.setText(preferences.getString("Nombre","error nombre").toString());
       Puntos.setText(preferences.getInt("Puntos",0)+"");

        ConexionSqliteHelper con= new ConexionSqliteHelper(this,"misUsuarios",null,1);

        SQLiteDatabase db=con.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT nombre,puntos FROM usuarios WHERE nombre LIKE '"+Nombre.getText().toString()+"';",null);

        if (cursor.moveToFirst()){

            puntos=cursor.getInt(cursor.getColumnIndex("puntos"));
            db.close();
            puntos+=preferences.getInt("Puntos",0);
            Toast.makeText(getApplicationContext(), "Puntos: "+puntos, Toast.LENGTH_SHORT).show();
            SQLiteDatabase db2=con.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("puntos", puntos);
            db2.update("usuarios", values, "nombre=?", new String[]{Nombre.getText().toString()});
            db2.close();

        }else{
            SQLiteDatabase db1=con.getWritableDatabase();

            ContentValues contenedor= new ContentValues();

            contenedor.put("nombre",Nombre.getText().toString());
            contenedor.put("puntos",Integer.parseInt(Puntos.getText().toString()));

            long result=db1.insert("usuarios","id",contenedor);

            Toast.makeText(this, "Se ha insertado "+result+"", Toast.LENGTH_SHORT).show();

            db1.close();
        }








    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.BTVolveraJugar:
                Toast.makeText(getApplicationContext(), "Puntos: "+puntos, Toast.LENGTH_SHORT).show();
                break;

            case R.id.BTRanquing:
                break;

        }
    }
}