package br.unesp.rc.MobileDashboard.ui.patient;



import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.lifecycle.ViewModel;


public class PatientViewModel extends ViewModel {

    public LinearLayout createDynamicLayout(Context context) {
        //Criando LinearLayout
        LinearLayout containerLayout = new LinearLayout(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, // largura
                LinearLayout.LayoutParams.WRAP_CONTENT // altura
        );

        // Adicionas margens ao layout
        layoutParams.setMargins(
                20 * (int) context.getResources().getDisplayMetrics().density, // margem esquerda
                10 * (int) context.getResources().getDisplayMetrics().density, // margem superior
                20 * (int) context.getResources().getDisplayMetrics().density, // margem direita
                10 * (int) context.getResources().getDisplayMetrics().density // margem inferior
        );
        containerLayout.setLayoutParams(layoutParams);
        containerLayout.setOrientation(LinearLayout.VERTICAL);

        // Adiciona borda arredondada ao layout
        containerLayout.setBackground(createRoundRectDrawable(context));

        //Criando os TextFields
        String[] labels = {"Name:", "Age:", "Height:", "Weight:"};
        for (int i = 0; i < labels.length; i++) {
            TextView textView = new TextView(context);
            textView.setLayoutParams(new ViewGroup.LayoutParams(
                    70 * (int) context.getResources().getDisplayMetrics().density,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            textView.setGravity(Gravity.END);
            textView.setTypeface(null, Typeface.BOLD);
            textView.setText(labels[i]);


           // Define as medidas do texto
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    70 * (int) context.getResources().getDisplayMetrics().density,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            // Defina margem superior maior para o primeiro texto e inferior maior para o último texto
            if (i == 0) {
                params.setMargins(15 * (int) context.getResources().getDisplayMetrics().density, 20 * (int) context.getResources().getDisplayMetrics().density, 5 * (int) context.getResources().getDisplayMetrics().density, 5 * (int) context.getResources().getDisplayMetrics().density);
            } else if (i == labels.length - 1) {
                params.setMargins(15 * (int) context.getResources().getDisplayMetrics().density, 5 * (int) context.getResources().getDisplayMetrics().density, 5 * (int) context.getResources().getDisplayMetrics().density, 20 * (int) context.getResources().getDisplayMetrics().density);
            } else {
                params.setMargins(15 * (int) context.getResources().getDisplayMetrics().density, 5 * (int) context.getResources().getDisplayMetrics().density, 5 * (int) context.getResources().getDisplayMetrics().density, 5 * (int) context.getResources().getDisplayMetrics().density);
            }

            textView.setLayoutParams(params);

            containerLayout.addView(textView);
        }
        return containerLayout;
    }

    private static GradientDrawable createRoundRectDrawable(Context context) {

        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(dpToPx(context, 10)); // Define o raio do canto
        drawable.setColor(context.getColor(android.R.color.system_background_light)); // Cor do fundo
        drawable.setStroke(dpToPx(context, 1), 0x80FFFFFF); // Cor da borda e largura da borda
        return drawable;
    }

    private static int dpToPx(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

}