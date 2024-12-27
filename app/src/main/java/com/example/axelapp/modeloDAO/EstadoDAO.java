package com.example.axelapp.modeloDAO;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.axelapp.servicios.Conexion;

import java.util.ArrayList;
import java.util.List;

public class EstadoDAO {

    private final Conexion conexion;
    SQLiteDatabase db = null;
    Cursor cursor = null;

    public EstadoDAO(Context context) {
        this.conexion = new Conexion(context);
    }

    public List<String> obtenerNombresEstados() {
        List<String> nombresEstados = new ArrayList<>();

        try {
            db = conexion.getReadableDatabase();
            String query = "SELECT nombre_estado FROM estado";
            cursor = db.rawQuery(query, null);

            while (cursor.moveToNext()) {
                nombresEstados.add(cursor.getString(0));
            }
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
        return nombresEstados;
    }

    public int obtenerEstadoId(String nombreEstado) {

        Integer estadoId = null;

        try {
            db = conexion.getReadableDatabase();
            String query = "SELECT id FROM estado WHERE nombre_estado = ?";
            cursor = db.rawQuery(query, new String[]{nombreEstado});

            if (cursor.moveToFirst()) {
                estadoId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            }
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }

        return estadoId;
    }


}
