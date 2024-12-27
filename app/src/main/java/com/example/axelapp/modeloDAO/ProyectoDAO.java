package com.example.axelapp.modeloDAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.example.axelapp.modelo.Proyecto;
import com.example.axelapp.servicios.Conexion;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProyectoDAO {

    private Conexion conexion;
SQLiteDatabase db=null;
    Cursor cursor = null;

    public ProyectoDAO(Context context){
        conexion=new Conexion(context);


        }
    private ContentValues prepararContentValues(Proyecto proyecto) {
        ContentValues values = new ContentValues();
        values.put("cod_proyecto", proyecto.getCodigoProyecto());
        values.put("cod_actividad", proyecto.getCodigoActividad());
        values.put("estado_id", proyecto.getEstadoId());
        values.put("detalles", proyecto.getObservacion());
        values.put("fecha", obtenerFecha(proyecto.getFecha()));
        return values;
    }

    private String obtenerFecha(String fecha) {
        if (fecha != null && !fecha.isEmpty()) {
            return fecha;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(new Date());
    }
    public List<Proyecto> listarProyectos() {
        List<Proyecto> listaProyectos = new ArrayList<>();
        try {
            db = conexion.getReadableDatabase();
            String query = "SELECT id, cod_proyecto, cod_actividad, estado_id, detalles, fecha FROM proyecto";
            cursor = db.rawQuery(query, null);

            while (cursor.moveToNext()) {
                // Crear un nuevo objeto Proyecto y asignarle valores
                Proyecto proyecto = new Proyecto();
                proyecto.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                proyecto.setCodigoProyecto(cursor.getString(cursor.getColumnIndexOrThrow("cod_proyecto")));
                proyecto.setCodigoActividad(cursor.getString(cursor.getColumnIndexOrThrow("cod_actividad")));
                proyecto.setEstadoId(cursor.getInt(cursor.getColumnIndexOrThrow("estado_id")));
                proyecto.setObservacion(cursor.getString(cursor.getColumnIndexOrThrow("detalles")));
                proyecto.setFecha(cursor.getString(cursor.getColumnIndexOrThrow("fecha")));

                // Agregar el proyecto a la lista
                listaProyectos.add(proyecto);
            }
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
        return listaProyectos;
    }
    public String obtenerNombreEstadoPorId(int estadoId) {
        String nombreEstado = "";
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = conexion.getReadableDatabase();
            String query = "SELECT nombre_estado FROM estado WHERE id = ?";
            cursor = db.rawQuery(query, new String[]{String.valueOf(estadoId)});

            if (cursor.moveToFirst()) {
                nombreEstado = cursor.getString(cursor.getColumnIndexOrThrow("nombre_estado"));
                Log.d("ProyectoDAO", "Buscando nombre del estado para ID: " + estadoId);

            }
        } catch (Exception e) {
            Log.e("ProyectoDAO", "Error al obtener nombre del estado: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return nombreEstado;
    }




    public boolean insertarProyecto(Proyecto proyecto) {
        SQLiteDatabase db = null;
        try {
            db = conexion.getWritableDatabase();
            ContentValues values = prepararContentValues(proyecto);

            long result = db.insert("proyecto", null, values);

            if (result == -1) {
                Log.e("ProyectoDAO", "Error al insertar proyecto: inserción fallida");
                return false;
            } else {
                Log.e("ProyectoDAO","Se ha insertado correctamente en la Base de datos");

                return true; // Inserción exitosa
            }

        } catch (SQLException ex) {
            Log.e("ProyectoDAO", "Error de base de datos: " + ex.getMessage());
            return false;
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }
    public int eliminarProyecto(int id) {
         db = conexion.getWritableDatabase();
        int rowsAffected = db.delete("proyecto", "id = ?", new String[]{String.valueOf(id)}); // Realiza la eliminación
        db.close();
        return rowsAffected;
    }


    public List<String> obtenerCodigosProyectos() {
        List<String> codigos = new ArrayList<>();

        try (SQLiteDatabase db = conexion.getReadableDatabase();
             Cursor cursor = db.rawQuery("SELECT id FROM proyecto", null)) {

            while (cursor.moveToNext()) {
                codigos.add(cursor.getString(0));
            }
        } catch (Exception e) {
            Log.e("ProyectoDAO", "Error al obtener códigos de proyectos: " + e.getMessage());
        }

        return codigos;
    }

    public Proyecto obtenerProyectoPorCodigo(String codigoProyecto) {
        String query = "SELECT * FROM proyecto WHERE codigo_proyecto = ?";
        try (SQLiteDatabase db = conexion.getReadableDatabase();
             Cursor cursor = db.rawQuery(query, new String[]{codigoProyecto})) {

            if (cursor.moveToFirst()) {
                return crearProyectoDesdeCursor(cursor);
            }
        } catch (Exception e) {
            Log.e("ProyectoDAO", "Error al obtener proyecto por código: " + e.getMessage());
        }

        return null;
    }


    private Proyecto crearProyectoDesdeCursor(Cursor cursor) {
        Proyecto proyecto = new Proyecto();

        proyecto.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
        proyecto.setCodigoProyecto(cursor.getString(cursor.getColumnIndexOrThrow("cod_proyecto")));
        proyecto.setCodigoActividad(cursor.getString(cursor.getColumnIndexOrThrow("cod_actividad")));
        proyecto.setEstadoId(cursor.getInt(cursor.getColumnIndexOrThrow("estado_id")));
        proyecto.setObservacion(cursor.getString(cursor.getColumnIndexOrThrow("detalles")));
        proyecto.setFecha(cursor.getString(cursor.getColumnIndexOrThrow("fecha")));

        return proyecto;
    }

    public boolean actualizarProyecto(Proyecto proyecto) {
        try (SQLiteDatabase db = conexion.getWritableDatabase()) {
            ContentValues values = prepararContentValues(proyecto);

            int rowsAffected = db.update("proyecto", values, "id = ?", new String[]{String.valueOf(proyecto.getId())});

            if (rowsAffected > 0) {
                return true;
            } else {
                Log.e("ProyectoDAO", "Error al actualizar proyecto: no se encontró el proyecto con ID " + proyecto.getId());
                return false;
            }
        } catch (SQLiteException e) {
            Log.e("ProyectoDAO", "Error de base de datos: " + e.getMessage());
            return false;
        }
    }
}
