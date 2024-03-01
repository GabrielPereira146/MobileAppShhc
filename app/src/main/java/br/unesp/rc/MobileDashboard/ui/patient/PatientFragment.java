package br.unesp.rc.MobileDashboard.ui.patient;

import android.os.Bundle;
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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PatientViewModel patientViewModel =
                new ViewModelProvider(this).get(PatientViewModel.class);

        binding = FragmentPatientsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        //While(Consumir da API)
        createDynamicLayout(root, patientViewModel);
        return root;
    }

    private void createDynamicLayout(View root,  PatientViewModel patientViewModel) {
        LinearLayout containerLayout = root.findViewById(R.id.container);
        //containerLayout.removeAllViews(); // Remove qualquer conteúdo anterior
        LinearLayout dynamicLayout = patientViewModel.createDynamicLayout(requireContext());
        containerLayout.addView(dynamicLayout);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}