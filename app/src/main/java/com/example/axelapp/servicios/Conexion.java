package com.example.axelapp.servicios;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.axelapp.database.ConstantesApp;

public class Conexion extends SQLiteOpenHelper {

public Conexion(Context context){
    super(context, ConstantesApp.BDD, null, ConstantesApp.VERSION);

}
public Conexion(@Nullable Context context, @Nullable String name
        , @Nullable SQLiteDatabase.CursorFactory factory, int version){
    super(context, name, factory, version);

}

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
   sqLiteDatabase.execSQL(ConstantesApp.TABLA_ESTADOS);
   sqLiteDatabase.execSQL("INSERT INTO estado (nombre_estado) VALUES ('No iniciado')");
   sqLiteDatabase.execSQL("INSERT INTO estado (nombre_estado) VALUES ('Retrasado')");
   sqLiteDatabase.execSQL("INSERT INTO estado (nombre_estado) VALUES ('Ejecutando')");
   sqLiteDatabase.execSQL("INSERT INTO estado (nombre_estado) VALUES ('Terminado')");

   sqLiteDatabase.execSQL(ConstantesApp.TABLA_PROYECTO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS estado");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS proyecto");
        onCreate(sqLiteDatabase);
    }
}
