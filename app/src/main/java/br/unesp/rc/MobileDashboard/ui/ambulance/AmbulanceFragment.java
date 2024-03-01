package br.unesp.rc.MobileDashboard.ui.ambulance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import br.unesp.rc.MobileDashboard.databinding.FragmentAmbulanceBinding;

public class AmbulanceFragment extends Fragment {

    private FragmentAmbulanceBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AmbulanceViewModel ambulanceViewModel =
                new ViewModelProvider(this).get(AmbulanceViewModel.class);

        binding = FragmentAmbulanceBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textGallery;
        ambulanceViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}