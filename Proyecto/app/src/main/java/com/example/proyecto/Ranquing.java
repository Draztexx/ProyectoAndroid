package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Ranquing extends AppCompatActivity {

    List<ListElement> elements;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranquing);

        init();


    }

    public void init(){
        elements=new ArrayList<>();
        ConexionSqliteHelper con= new ConexionSqliteHelper(this,"misUsuarios",null,1);

        SQLiteDatabase db=con.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT nombre,puntos FROM usuarios ORDER BY puntos DESC;",null);
        if (cursor.moveToFirst()){

            do {
                @SuppressLint("Range") String nombre = cursor.getString(cursor.getColumnIndex("nombre"));
                @SuppressLint("Range") int puntos = cursor.getInt(cursor.getColumnIndex("puntos"));
                elements.add(new ListElement(nombre,puntos));
            } while (cursor.moveToNext());

        }

        ListAdapter listAdapter=new ListAdapter(elements,this);
        RecyclerView recyclerView=findViewById(R.id.listRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);

    }

}