package com.example.axelapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.axelapp.modelo.Proyecto;
import com.example.axelapp.modeloDAO.EstadoDAO;
import com.example.axelapp.modeloDAO.ProyectoDAO;
import com.example.axelapp.servicios.DialogUtils;
import com.example.axelapp.servicios.ToastUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Busqueda extends AppCompatActivity {
    private RecyclerView rvProjects;
    private ProyectoDAO proyectoDAO;
    private List<Proyecto> listaProyectos;
    private ProyectoAdapter adapter;
    private AutoCompleteTextView etSearch, autoCompleteStatus;
    private MaterialButton btnSave,btnDelete;
    Proyecto proyecto=new Proyecto();

    private TextInputEditText etProjectID, etProjectCode, etActivityCode, etDate, etObservation;
    EstadoDAO estadoDAO=new EstadoDAO(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_busqueda);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rvProjects = findViewById(R.id.rvProjects);
        rvProjects.setLayoutManager(new LinearLayoutManager(this));

        etSearch = findViewById(R.id.etSearch);
        autoCompleteStatus = findViewById(R.id.autoCompleteStatus);

        etProjectCode = findViewById(R.id.etProjectCode);
        etProjectID=findViewById(R.id.etProjectID);
        etActivityCode = findViewById(R.id.etActivityCode);
        etDate = findViewById(R.id.etDate);
        etObservation = findViewById(R.id.etObservation);
        btnDelete=findViewById(R.id.btnDelete);
        btnSave=findViewById(R.id.btnSave);
        proyectoDAO = new ProyectoDAO(this);
        listaProyectos = proyectoDAO.listarProyectos();

        adapter = new ProyectoAdapter(listaProyectos, this::mostrarDetallesProyecto);
        rvProjects.setAdapter(adapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtrarProyectos(s.toString()); // Llama al método de filtrado
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        etDate.setOnClickListener(v -> {
            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Selecciona una fecha")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build();

            datePicker.show(getSupportFragmentManager(), "DATE_PICKER");

            datePicker.addOnPositiveButtonClickListener(selection -> {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String fechaSeleccionada = sdf.format(new Date(selection));

                etDate.setText(fechaSeleccionada);
            });
        });
        btnDelete.setOnClickListener(view -> {
            try {
                int idProyecto = Integer.parseInt(etProjectID.getText().toString());
                eliminarProyecto(idProyecto);
            } catch (NumberFormatException e) {
                ToastUtils.mostrarError(this, "Código de proyecto inválido. Asegúrate de que sea un número.");
            }
        });
        btnSave.setOnClickListener(view -> actualizarProyecto());

        List<String> estados = estadoDAO.obtenerNombresEstados();
        ArrayAdapter<String> estadosAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, estados);
        autoCompleteStatus.setAdapter(estadosAdapter);

        autoCompleteStatus.setOnClickListener(v -> autoCompleteStatus.showDropDown());

    }
    private void eliminarProyecto(int idProyecto) {
        DialogUtils.mostrarDialogoConfirmacion(this,
                "Confirmar Eliminación",
                "¿Está seguro de que desea eliminar este proyecto?",
                "Eliminar",
                "Cancelar",
                () -> {
                    // Llama al método para eliminar el proyecto
                    int resultado = proyectoDAO.eliminarProyecto(idProyecto);
                    if (resultado > 0) {
                        limpiarCampos();
                        listaProyectos = proyectoDAO.listarProyectos();
                        adapter.actualizarLista(listaProyectos);
                        ToastUtils.mostrarWarning(this, "Se ha eliminado correctamente");
                    } else {
                        ToastUtils.mostrarWarning(this, "Error al eliminar el proyecto. Verifica si el proyecto existe.");
                    }
                });
    }

    private void limpiarCampos() {
        etSearch.setText("");
        etProjectID.setText("");
        etProjectCode.setText("");
        etActivityCode.setText("");
        etObservation.setText("");
        etDate.setText("");
        autoCompleteStatus.setSelection(0);
    }


    private void filtrarProyectos(String texto) {
        List<Proyecto> proyectosFiltrados = new ArrayList<>();

        for (Proyecto proyecto : listaProyectos) {
            if (String.valueOf(proyecto.getId()).contains(texto) ||
                    proyecto.getCodigoProyecto().contains(texto)) {
                proyectosFiltrados.add(proyecto);
            }
        }


        adapter.actualizarLista(proyectosFiltrados);
    }

    private void mostrarDetallesProyecto(Proyecto proyecto) {
        etProjectID.setText(String.valueOf(proyecto.getId()));
        etProjectCode.setText(proyecto.getCodigoProyecto());
        etActivityCode.setText(proyecto.getCodigoActividad());
        etDate.setText(proyecto.getFecha());
        etObservation.setText(proyecto.getObservacion());


        if (autoCompleteStatus.getAdapter() != null) {
            String nombreEstado = proyectoDAO.obtenerNombreEstadoPorId(proyecto.getEstadoId());
            autoCompleteStatus.setText(nombreEstado, false);
        }
    }



    private class ProyectoAdapter extends RecyclerView.Adapter<ProyectoAdapter.ProyectoViewHolder> {
        private List<Proyecto> listaProyectos;
        private final OnProyectoClickListener listener;

        public ProyectoAdapter(List<Proyecto> listaProyectos, OnProyectoClickListener listener) {
            this.listaProyectos = listaProyectos;
            this.listener = listener;
        }

        @NonNull
        @Override
        public ProyectoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_proyecto, parent, false);
            return new ProyectoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ProyectoViewHolder holder, int position) {
            Proyecto proyecto = listaProyectos.get(position);

            holder.tvId.setText("ID: " + proyecto.getId());
            holder.tvProyectoId.setText("Código de proyecto: " + proyecto.getCodigoProyecto());
            holder.tvCodigoActividad.setText("Código de actividad: " + proyecto.getCodigoActividad());
            String nombreEstado = proyectoDAO.obtenerNombreEstadoPorId(proyecto.getEstadoId());

            holder.tvEstado.setText("Estado:" + nombreEstado);
            holder.tvFecha.setText("Fecha: " + proyecto.getFecha());
            holder.tvObservacion.setText("Observación: " + proyecto.getObservacion());

            holder.itemView.setOnClickListener(v -> listener.onProyectoClick(proyecto));
        }

        @Override
        public int getItemCount() {
            return listaProyectos.size();
        }

        public void actualizarLista(List<Proyecto> nuevaLista) {
            listaProyectos = nuevaLista;
            notifyDataSetChanged();
        }

        class ProyectoViewHolder extends RecyclerView.ViewHolder {
            TextView tvProyectoId, tvCodigoActividad,tvEstado, tvFecha, tvObservacion, tvId;

            public ProyectoViewHolder(@NonNull View itemView) {
                super(itemView);
                tvId = itemView.findViewById(R.id.tvID);
                tvProyectoId = itemView.findViewById(R.id.tvProyectoId);
                tvCodigoActividad = itemView.findViewById(R.id.tvCodigoActividad);
                tvEstado=itemView.findViewById(R.id.tvEstado);
                tvFecha = itemView.findViewById(R.id.tvFecha);
                tvObservacion = itemView.findViewById(R.id.tvObservacion);
            }
        }
    }
    private void actualizarProyecto() {
        try {
            String projectIDText = etProjectID.getText().toString().trim();
            if (projectIDText.isEmpty()) {
                ToastUtils.mostrarError(this, "Por favor, ingresa un ID de proyecto.");
                return;
            }


            int id = Integer.parseInt(projectIDText);

            String codigoProyecto = etProjectCode.getText().toString();
            String codigoActividad = etActivityCode.getText().toString();
            String nombreEstado = autoCompleteStatus.getText().toString();

            if (nombreEstado.isEmpty()) {
                ToastUtils.mostrarError(this, "Por favor, selecciona un estado.");
                return;
            }

            int estadoId = obtenerEstadoIdPorNombre(nombreEstado);

            if (estadoId == -1) {
                ToastUtils.mostrarError(this, "El estado seleccionado no es válido.");
                return;
            }

            String observacion = etObservation.getText().toString();
            String fecha = etDate.getText().toString();

            proyecto.setId(id);
            proyecto.setCodigoProyecto(codigoProyecto);
            proyecto.setCodigoActividad(codigoActividad);
            proyecto.setEstadoId(estadoId);
            proyecto.setObservacion(observacion);
            proyecto.setFecha(fecha);

            boolean actualizado = proyectoDAO.actualizarProyecto(proyecto);

            if (actualizado) {
                ToastUtils.mostrarSuccess(this, "Se ha actualizado correctamente");
            limpiarCampos();
                listaProyectos = proyectoDAO.listarProyectos();
                adapter.actualizarLista(listaProyectos);
            } else {
                ToastUtils.mostrarError(this, "Error al actualizar el proyecto");
            }
        } catch (NumberFormatException e) {
            ToastUtils.mostrarError(this, "ID de proyecto inválido. Asegúrate de que sea un número.");
        } catch (Exception e) {
            ToastUtils.mostrarError(this, "Ocurrió un error inesperado: " + e.getMessage());
        }
    }  private int obtenerEstadoIdPorNombre(String nombreEstado) {

        return estadoDAO.obtenerEstadoId(nombreEstado);
    }


    public interface OnProyectoClickListener {
        void onProyectoClick(Proyecto proyecto);
    }
}
