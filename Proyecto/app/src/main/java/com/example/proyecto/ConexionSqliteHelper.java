package com.example.proyecto;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ConexionSqliteHelper extends SQLiteOpenHelper {

    final String CREAR_TABLA_USUARIO="CREATE TABLE usuarios(idu INTEGER PRIMARY KEY AUTOINCREMENT ,nombre TEXT,punto INTEGER)";
    final String CREAR_TABLA_IMAGENES="CREATE TABLE imagenes(idi INTEGER PRIMARY KEY AUTOINCREMENT,localizacion TEXT)";


    public ConexionSqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREAR_TABLA_USUARIO);
        db.execSQL(CREAR_TABLA_IMAGENES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        db.execSQL("DROP TABLE IF EXISTS imagenes");
        onCreate(db);
    }
}
