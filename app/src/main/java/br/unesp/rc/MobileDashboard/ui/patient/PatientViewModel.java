package br.unesp.rc.MobileDashboard.ui.patient;


import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import br.unesp.rc.MobileDashboard.R;

public class PatientViewModel extends ViewModel {

    public LinearLayout createDynamicLayout(Context context) {

        LinearLayout containerLayout = new LinearLayout(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT , // largura
                LinearLayout.LayoutParams.WRAP_CONTENT // altura
        );

        layoutParams.setMargins(
                20 * (int) context.getResources().getDisplayMetrics().density, // margem esquerda
                10 * (int) context.getResources().getDisplayMetrics().density, // margem superior
                20 * (int) context.getResources().getDisplayMetrics().density, // margem direita
                10 * (int) context.getResources().getDisplayMetrics().density // margem inferior
        );
        containerLayout.setLayoutParams(layoutParams);
        containerLayout.setOrientation(LinearLayout.VERTICAL);


        String[] labels = {"Name:", "Age:", "Height:", "Weight:"};
        for (String label : labels) {
            TextView textView = new TextView(context);

            textView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setGravity(Gravity.END);
            textView.setTypeface(null, Typeface.BOLD);
            textView.setText(label);
            containerLayout.addView(textView);
        }
        return containerLayout;
    }

}