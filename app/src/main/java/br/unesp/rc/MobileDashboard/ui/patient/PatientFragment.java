package br.unesp.rc.MobileDashboard.ui.patient;


import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.io.IOException;

import br.unesp.rc.MobileDashboard.R;
import br.unesp.rc.MobileDashboard.databinding.FragmentPatientsBinding;
import br.unesp.rc.MobileDashboard.utils.RetrofitService;
import br.unesp.rc.MobileDashboard.utils.SHHCApiService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatientFragment extends Fragment {

    private FragmentPatientsBinding binding;
    String[] labels = {"Temperature", "Blood Pressure", "Glucose", "Heart Rate", "Air Flow", "Oxygen"};
    LinearLayout selectedLayout = null; // Variável para rastrear o LinearLayout selecionado atualmente
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PatientViewModel patientViewModel =
                new ViewModelProvider(this).get(PatientViewModel.class);

        binding = FragmentPatientsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        //While(Consumir da API)
        for (int i= 0; i<6; i++){
            createDynamicLayout(root, patientViewModel);
        }

        return root;
    }

    private void createDynamicLayout(View root,  PatientViewModel patientViewModel) {
        LinearLayout containerLayout = root.findViewById(R.id.container);
        LinearLayout dynamicLayout = patientViewModel.createDynamicLayout(requireContext());

        // Função que deixa o layout clicavel
        dynamicLayout.setOnClickListener(v -> {
            // ADD your action here
            toggleLayoutSelection(dynamicLayout);

        });

        containerLayout.addView(dynamicLayout);
    }

    private void toggleLayoutSelection(LinearLayout containerLayout) {
        if (containerLayout == selectedLayout) {
            // Se o layout clicado já estiver selecionado, redefina para o estado normal
            resetLayoutState(containerLayout);
            selectedLayout = null; // Nenhum layout está selecionado
        } else {
            // Se outro layout já estiver selecionado, redefina-o para o estado normal
            if (selectedLayout != null) {
                resetLayoutState(selectedLayout);
            }
            // Defina o layout clicado como selecionado
            setLayoutSelected(containerLayout);
            selectedLayout = containerLayout;
        }
    }

    private void setLayoutSelected(LinearLayout containerLayout) {


        // For que divide os panes na posição certa
        for (int i = 0; i < labels.length/2; i++) {
            LinearLayout paneSensors = new LinearLayout(containerLayout.getContext());
            LinearLayout.LayoutParams layoutParamsSensors = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, // largura
                    LinearLayout.LayoutParams.WRAP_CONTENT // altura
            );
            paneSensors.setOrientation(LinearLayout.HORIZONTAL);
            paneSensors.setLayoutParams(layoutParamsSensors);
            containerLayout.addView(paneSensors);


            // For que cria os Panes
            for (int j = i; j < Math.min(i + 2, labels.length); j++) {

                // Cria um novo LinearLayout (pane)
                LinearLayout pane = new LinearLayout(paneSensors.getContext());

                // Cria parâmetros de layout com margens
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        100 * (int) containerLayout.getContext().getResources().getDisplayMetrics().density,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1 // Peso
                );

                // Define as margens desejadas (esquerda, superior, direita, inferior)
                layoutParams.setMargins(20 * (int) containerLayout.getContext().getResources().getDisplayMetrics().density,
                        10 * (int) containerLayout.getContext().getResources().getDisplayMetrics().density,
                        20 * (int) containerLayout.getContext().getResources().getDisplayMetrics().density,
                        10 * (int) containerLayout.getContext().getResources().getDisplayMetrics().density);

                // Define os parâmetros de layout no LinearLayout
                pane.setLayoutParams(layoutParams);

                pane.setBackgroundColor(ContextCompat.getColor(containerLayout.getContext(), R.color.SensorP_Normal));
                pane.setOrientation(LinearLayout.VERTICAL);

                // Outros componentes ao "pane",
                TextView title = new TextView(pane.getContext());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

                params.setMargins(0, 10 * (int) pane.getContext().getResources().getDisplayMetrics().density, 0, 0);
                title.setLayoutParams(params);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(labels[j+i]);
                pane.addView(title);

                TextView stats = new TextView(pane.getContext());
                LinearLayout.LayoutParams statsParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                statsParams.setMargins(0, 20 * (int) pane.getContext().getResources().getDisplayMetrics().density, 0, 20 * (int) pane.getContext().getResources().getDisplayMetrics().density);
                stats.setLayoutParams(statsParams);
                stats.setGravity(Gravity.CENTER);
                String text  = "Normal";
                stats.setText(text);
                stats.setTextColor(ContextCompat.getColor(containerLayout.getContext(), R.color.SensorText_Normal));
                pane.addView(stats);

                // Adiciona o "pane" ao LinearLayout principal
                paneSensors.addView(pane);


            }
        }

    }

    private void resetLayoutState(LinearLayout containerLayout) {
        for (int i = 0; i < containerLayout.getChildCount(); i++) {
          if(i>3)
              containerLayout.removeViewAt(i);
            //containerLayout.getChildAt(i).setBackgroundColor(containerLayout.getContext().getColor(android.R.color.black));
        }
        containerLayout.removeViewAt(4);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}