package br.unesp.rc.MobileDashboard.ui.patient;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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
        for (String label : labels){
            LinearLayout containerSensors = new LinearLayout(containerLayout.getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    100 * (int) requireContext().getResources().getDisplayMetrics().density, // largura
                    50 * (int) requireContext().getResources().getDisplayMetrics().density // altura
            );

            // Adicionas margens ao layout
            layoutParams.setMargins(
                    5 * (int) containerLayout.getContext().getResources().getDisplayMetrics().density, // margem esquerda
                    10 * (int) containerLayout.getContext().getResources().getDisplayMetrics().density, // margem superior
                    5 * (int) containerLayout.getContext().getResources().getDisplayMetrics().density, // margem direita
                    10 * (int) containerLayout.getContext().getResources().getDisplayMetrics().density // margem inferior
            );
            containerLayout.setLayoutParams(layoutParams);
            containerLayout.setOrientation(LinearLayout.VERTICAL);
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