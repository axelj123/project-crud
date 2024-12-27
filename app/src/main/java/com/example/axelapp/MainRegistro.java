package com.example.axelapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.axelapp.modelo.Proyecto;
import com.example.axelapp.modeloDAO.EstadoDAO;
import com.example.axelapp.modeloDAO.ProyectoDAO;
import com.example.axelapp.servicios.ToastUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class MainRegistro extends AppCompatActivity {
    private Spinner spinner;
    private TextInputEditText codProyecto,codActividad,
        etObservacion;
    private MaterialButton btnSave,btnHistorial;
    EstadoDAO estadoDAO=new EstadoDAO(this);
    ProyectoDAO proyectoDAO =new ProyectoDAO(this);
    Proyecto pr=new Proyecto();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        spinner = findViewById(R.id.tilState);
        codProyecto = findViewById(R.id.etProjectCode);
        codActividad = findViewById(R.id.etActivityCode);
        etObservacion = findViewById(R.id.etObservation);
        btnSave=findViewById(R.id.btnSave);
        btnHistorial=findViewById(R.id.btnHistorial);

        List<String> nombresEstados = estadoDAO.obtenerNombresEstados();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nombresEstados);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        btnSave.setOnClickListener(view -> guardarProyecto());
btnHistorial.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent=new Intent(MainRegistro.this,Busqueda.class);
        startActivity(intent);
    }
});
    }
    public void guardarProyecto() {

        try {
            String estadoSeleccionado = spinner.getSelectedItem().toString();
            pr.setEstadoId(estadoDAO.obtenerEstadoId(estadoSeleccionado));

            pr.setCodigoProyecto(codProyecto.getText().toString());
            pr.setCodigoActividad(codActividad.getText().toString());
            pr.setObservacion(etObservacion.getText().toString());


            if (pr.getCodigoProyecto().isEmpty() || pr.getCodigoActividad().isEmpty() || pr.getObservacion().isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            proyectoDAO.insertarProyecto(pr);

            ToastUtils.mostrarSuccess(this, "Proyecto guardado correctamente");

            codProyecto.setText("");
            codActividad.setText("");
            etObservacion.setText("");
            spinner.setSelection(0);

        } catch (Exception e) {
            Toast.makeText(this, "Error al guardar el proyecto", Toast.LENGTH_SHORT).show();
            Log.e("GuardarProyecto", "Error: " + e.getMessage());
        }
    }


}