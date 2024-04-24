package br.unesp.rc.MobileDashboard.ui.ambulance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.io.IOException;

import br.unesp.rc.MobileDashboard.R;
import br.unesp.rc.MobileDashboard.databinding.FragmentAmbulanceBinding;
import br.unesp.rc.MobileDashboard.utils.RetrofitService;
import br.unesp.rc.MobileDashboard.utils.SHHCApiService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AmbulanceFragment extends Fragment {

    private FragmentAmbulanceBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AmbulanceViewModel ambulanceViewModel =
                new ViewModelProvider(this).get(AmbulanceViewModel.class);

        binding = FragmentAmbulanceBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initializeComponents(root, ambulanceViewModel);
        return root;
    }

    private void initializeComponents(View root, AmbulanceViewModel ambulanceViewModel){
        LinearLayout containerLayout = root.findViewById(R.id.container);
//        LinearLayout dynamicLayout = ambulanceViewModel; //Arrumar


        RetrofitService retrofitService = new RetrofitService();
        SHHCApiService apiService = retrofitService.getRetrofit().create(SHHCApiService.class);

        apiService.getTemperature().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String responseData = response.body().string();

                    getActivity().runOnUiThread(()-> {
                        TextView textView = new TextView(root.findViewById());
                        textView.setText(responseData);
                    });
                } catch (IOException e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                /**
                 * Manipular erro do servidor aqui
                 */
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}