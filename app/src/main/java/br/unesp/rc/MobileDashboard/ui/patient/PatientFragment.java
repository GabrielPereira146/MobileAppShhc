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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import br.unesp.rc.MobileDashboard.R;
import br.unesp.rc.MobileDashboard.databinding.FragmentPatientsBinding;

public class PatientFragment extends Fragment {

    private FragmentPatientsBinding binding;
    LinearLayout selectedLayout = null; // Variável para rastrear o LinearLayout selecionado atualmente
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PatientViewModel patientViewModel =
                new ViewModelProvider(this).get(PatientViewModel.class);

        binding = FragmentPatientsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        //While(Consumir da API)
        for (int i= 0; i<10; i++){
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
        String[] labels = {"Temperature", "Blood Pressure", "Glucose", "Heart Rate", "Air Flow", "Oxygen"};
        for (String label : labels) {

            // Cria um novo LinearLayout (pane)
            LinearLayout pane = new LinearLayout(containerLayout.getContext());

            // Cria parâmetros de layout com margens
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    150 * (int) containerLayout.getContext().getResources().getDisplayMetrics().density,
                    150 * (int) containerLayout.getContext().getResources().getDisplayMetrics().density
            );

            // Define as margens desejadas (esquerda, superior, direita, inferior)
            layoutParams.setMargins(20 * (int) containerLayout.getContext().getResources().getDisplayMetrics().density,
                    10 * (int) containerLayout.getContext().getResources().getDisplayMetrics().density,
                    20 * (int) containerLayout.getContext().getResources().getDisplayMetrics().density,
                    10 * (int) containerLayout.getContext().getResources().getDisplayMetrics().density);

            // Define os parâmetros de layout no LinearLayout
            pane.setLayoutParams(layoutParams);

            pane.setBackgroundColor( containerLayout.getContext().getColor(android.R.color.darker_gray));
            pane.setOrientation(LinearLayout.HORIZONTAL);

            // Outros componentes ao "pane",
            TextView textView = new TextView(containerLayout.getContext());
            textView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            textView.setGravity(Gravity.CENTER);
            textView.setTypeface(null, Typeface.BOLD);
            textView.setText(label);
            pane.addView(textView);

            // Adiciona o "pane" ao LinearLayout principal
            containerLayout.addView(pane);
        }
    }

    private void resetLayoutState(LinearLayout containerLayout) {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}