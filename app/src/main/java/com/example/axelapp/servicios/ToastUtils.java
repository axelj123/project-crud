package com.example.axelapp.servicios;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ToastUtils {
    private static final String SUCCESS_COLOR = "#4CAF50";
    private static final String ERROR_COLOR = "#F44336";
    private static final String WARNING_COLOR = "#FF9800";
    private static final String INFO_COLOR = "#2196F3";
    private static final String NORMAL_COLOR = "#757575";

    private static Toast crearToastBase(Context context, String mensaje, String backgroundColor, int iconRes) {
        LinearLayout toastLayout = new LinearLayout(context);
        toastLayout.setOrientation(LinearLayout.HORIZONTAL);
        toastLayout.setGravity(Gravity.CENTER_VERTICAL);

        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(40f);
        shape.setColor(Color.parseColor(backgroundColor));
        toastLayout.setBackground(shape);

        toastLayout.setPadding(40, 15, 40, 15);

        if (iconRes != 0) {
            ImageView icon = new ImageView(context);
            icon.setImageResource(iconRes);
            icon.setColorFilter(Color.WHITE);
            LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            iconParams.setMarginEnd(15);
            icon.setLayoutParams(iconParams);
            toastLayout.addView(icon);
        }

        TextView text = new TextView(context);
        text.setText(mensaje);
        text.setTextColor(Color.WHITE);
        text.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
        text.setTextSize(14);
        toastLayout.addView(text);

        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(toastLayout);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 100);

        return toast;
    }

    public static void mostrarSuccess(Context context, String mensaje) {
        crearToastBase(context, mensaje, SUCCESS_COLOR, android.R.drawable.ic_menu_info_details).show();
    }

    public static void mostrarError(Context context, String mensaje) {
        crearToastBase(context, mensaje, ERROR_COLOR, android.R.drawable.ic_delete).show();
    }

    public static void mostrarWarning(Context context, String mensaje) {
        crearToastBase(context, mensaje, WARNING_COLOR, android.R.drawable.ic_dialog_alert).show();
    }

    public static void mostrarInfo(Context context, String mensaje) {
        crearToastBase(context, mensaje, INFO_COLOR, android.R.drawable.ic_dialog_info).show();
    }

    public static void mostrarNormal(Context context, String mensaje) {
        crearToastBase(context, mensaje, NORMAL_COLOR, 0).show();
    }

    public static void mostrarCustom(Context context, String mensaje, String color, int iconRes) {
        crearToastBase(context, mensaje, color, iconRes).show();
    }
}