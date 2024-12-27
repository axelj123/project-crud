package com.example.axelapp.database;

public interface ConstantesApp {

    String BDD="database.db";
    int VERSION=1;

    String TABLA_ESTADOS = "CREATE TABLE estado (\n" +
            "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "    nombre_estado VARCHAR(30) NOT NULL UNIQUE\n" +
            ");";

    String TABLA_PROYECTO = "CREATE TABLE proyecto (\n" +
            "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "    cod_proyecto VARCHAR(30) NOT NULL,\n" +
            "    cod_actividad VARCHAR(30) NOT NULL,\n" +
            "    estado_id INTEGER NOT NULL,\n" +
            "    detalles TEXT,\n" +
            "    fecha TEXT DEFAULT CURRENT_TIMESTAMP,\n" +
            "    FOREIGN KEY (estado_id) REFERENCES estados (id)\n" +
            ");";
}
