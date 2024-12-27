package com.example.axelapp.servicios;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class DialogUtils {

    public static void mostrarDialogoConfirmacion(Context context, String titulo, String mensaje,
                                                  String textoPositivo, String textoNegativo,
                                                  Runnable accionPositiva) {
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(context);

        GradientDrawable background = new GradientDrawable();
        background.setColor(Color.WHITE);
        background.setCornerRadius(16f);
        dialogBuilder.setBackground(background);

        dialogBuilder.setTitle(titulo)
                .setMessage(mensaje)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(textoPositivo, (dialog, which) -> {
                    if (accionPositiva != null) {
                        accionPositiva.run();
                    }
                })
                .setNegativeButton(textoNegativo, (dialog, which) -> {

                    dialog.dismiss();
                })
                .setCancelable(false);

        AlertDialog dialog = dialogBuilder.create();

        dialog.setOnShowListener(dialogInterface -> {
            Button btnPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            Button btnNegative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

            btnPositive.setBackgroundColor(Color.parseColor("#F44336"));
            btnPositive.setTextColor(Color.WHITE);
            btnNegative.setTextColor(Color.BLACK);

            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) btnPositive.getLayoutParams();
            layoutParams.setMargins(16, 0, 16, 0);
            btnPositive.setLayoutParams(layoutParams);
            btnNegative.setLayoutParams(layoutParams);
        });

        dialog.show();
    }
}
