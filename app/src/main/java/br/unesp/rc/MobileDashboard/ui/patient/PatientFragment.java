package br.unesp.rc.MobileDashboard.ui.patient;


import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import br.unesp.rc.MobileDashboard.R;
import br.unesp.rc.MobileDashboard.databinding.FragmentPatientsBinding;
import br.unesp.rc.MobileDashboard.model.AirFlow;
import br.unesp.rc.MobileDashboard.model.BloodPressure;
import br.unesp.rc.MobileDashboard.model.Glucose;
import br.unesp.rc.MobileDashboard.model.HeartRate;
import br.unesp.rc.MobileDashboard.model.PatientDTO;
import br.unesp.rc.MobileDashboard.model.PulseOxygen;
import br.unesp.rc.MobileDashboard.model.Temperature;
import br.unesp.rc.MobileDashboard.utils.RetrofitService;
import br.unesp.rc.MobileDashboard.utils.SHHCApiService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatientFragment extends Fragment {

    private static final long POLLING_INTERVAL_MS = 20000;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable pollingRunnable;
    String baseUrl = "http://10.0.2.2:";
    CountDownLatch latch;

    private FragmentPatientsBinding binding;
    String[] labels = {"Temperature", "AirFlow", "Pulse Oxygen", "Heart Rate", "Glucose", "Blood Pressure"};
    LinearLayout selectedLayout = null; // Variável para rastrear o LinearLayout selecionado atualmente
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PatientViewModel patientViewModel =
                new ViewModelProvider(this).get(PatientViewModel.class);



        binding = FragmentPatientsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Inicia o loop de consumo da API
        startPolling(root, patientViewModel);

        return root;
    }

    /**
     * Inicia o polling que atualiza a aba de visualização dos pacientes
     * @param root
     * @param patientViewModel
     */
    private void startPolling(View root, PatientViewModel patientViewModel){
        pollingRunnable = new Runnable() {
            @Override
            public void run() {

                // Atualiza os dados
                fetchData(root, patientViewModel);

                // Intervalo de 20 segundos
                handler.postDelayed(this, POLLING_INTERVAL_MS);
            }
        };
        handler.post(pollingRunnable);
    }

    /**
     * Realiza as requisições HTTP para obter os pacientes
     * @param root
     * @param patientViewModel
     */
    private void fetchData(View root, PatientViewModel patientViewModel) {

        // Inicializando variáveis para consumo
        RetrofitService retrofitService = new RetrofitService(baseUrl + "8080");
        SHHCApiService apiService = retrofitService.getRetrofit().create(SHHCApiService.class);

        apiService.getNumberAPI().enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                System.out.println("Request URL: " + call.request().url());

                // Verifica se os dados foram requisitados com sucesso
                if(response.isSuccessful() && response.body() != null){
                    try {

                        /**
                         * Variável que armazena quantos pacientes na API tem
                         */
                        int numberPatients = Integer.parseInt(response.body().string());

                        // Atualizando o layout com base nos pacientes existentes
                        updateLayout(numberPatients, root, patientViewModel);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    System.err.println("Request failed with code: " + response.code());
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    private void updateLayout(int numberPatients, View root, PatientViewModel patientViewModel) {
        // Encontrar o contêiner onde as views dinâmicas são adicionadas
        LinearLayout container = root.findViewById(R.id.container);


        int viewCount = container.getChildCount();

        // Lista para armazenar pacientes
        List<PatientDTO> patients = new ArrayList<>();

        // Adicionar novas views
        for (int i = 0; i < numberPatients; i++) {
            int port = 8081 + i;

            // Criar uma thread para obter os dados do paciente
            new Thread(() -> {
                try {
                    PatientDTO patient = getPatientData(String.valueOf(port));
                    if (patient != null) {
                        synchronized (patients) {
                            patients.add(patient);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        // Aguarda até que todas as threads tenham terminado
        // Note que não usamos latch.await() aqui pois estamos gerenciando dentro de getPatientData()



        // Atualiza o layout com base nos pacientes existentes
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            for (PatientDTO patient : patients) {
                // Atualiza o layout com base nos pacientes existentes
                if (patients.indexOf(patient) >= viewCount) {
                    createDynamicLayout(root, patientViewModel, patient.getPort());
                }
                View view = container.getChildAt(patients.indexOf(patient));
                updateDinamicLayout(view, patient);
                System.out.println("---------------- PACIENTE -------------- ");
                System.out.println(patient.toString());
            }
        }, 5000); // Aguarda 5 segundos antes de atualizar o layout
    }

    private void updateDinamicLayout(View view, PatientDTO patient) {
        LinearLayout containerLayout = (LinearLayout)view;
        TextView nameLabel = (TextView)containerLayout.getChildAt(0);
        nameLabel.setText("Name: " + patient.getFirstName() + " " + patient.getLastName());

        TextView ageLabel = (TextView)containerLayout.getChildAt(1);
        ageLabel.setText("Age: " + patient.getAge());

        TextView heightLabel = (TextView)containerLayout.getChildAt(2);
        heightLabel.setText("Height: " + patient.getHeight());

        TextView weightLabel = (TextView)containerLayout.getChildAt(3);
        weightLabel.setText("Weight: " + patient.getWeight());

    }

    private PatientDTO getPatientData(String port) throws InterruptedException {
        // Inicializando variáveis para consumo
        RetrofitService retrofitService = new RetrofitService(baseUrl + port);
        SHHCApiService apiService = retrofitService.getRetrofit().create(SHHCApiService.class);

        final PatientDTOContainer patientContainer = new PatientDTOContainer();
        final CountDownLatch responseLatch = new CountDownLatch(1); // Latch para a chamada específica

        System.out.println("BATATA");
        // Pegando os dados do paciente
        apiService.getPatient().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                System.out.println("Request URL PATIENT: " + call.request().url());
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        patientContainer.patient = new Gson().fromJson(response.body().string(), PatientDTO.class);
                        System.out.println("DENTRO DO GET PATIENT " + patientContainer.patient.toString());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    System.err.println("Request failed with code: " + response.code());
                }
                responseLatch.countDown(); // Decrementa o contador do latch
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("FALHEI MISERAVELMENTE");
                t.printStackTrace();
                responseLatch.countDown(); // Decrementa o contador do latch mesmo em caso de falha
            }
        });

        responseLatch.await(5, TimeUnit.SECONDS); // Espera até 5 segundos pela resposta

        if (patientContainer.patient == null) {
            System.out.println("Patient data is null after waiting for the response");
        } else {
            System.out.println("Received patient data: " + patientContainer.patient);
        }
        return patientContainer.patient;
    }

    private static class PatientDTOContainer {
        private PatientDTO patient = new PatientDTO();
    }

    private void createDynamicLayout(View root,  PatientViewModel patientViewModel, String port) {
        LinearLayout containerLayout = root.findViewById(R.id.container);
        LinearLayout dynamicLayout = patientViewModel.createDynamicLayout(requireContext());

        // Função que deixa o layout clicavel
        dynamicLayout.setOnClickListener(v -> {
            // ADD your action here
            toggleLayoutSelection(dynamicLayout, port);

        });

        containerLayout.addView(dynamicLayout);
    }

    private void toggleLayoutSelection(LinearLayout containerLayout, String port) {
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
            setLayoutSelected(containerLayout, port);
            selectedLayout = containerLayout;
        }
    }

    private List<Integer> getSensorsData(String port) {
        // Inicializando variáveis para consumo
        RetrofitService retrofitService = new RetrofitService(baseUrl + port);
        SHHCApiService apiService = retrofitService.getRetrofit().create(SHHCApiService.class);

        final SensorContainer sensorContainer = new SensorContainer();
        final CountDownLatch responseLatch = new CountDownLatch(6); // Latch para 6 chamadas assíncronas

        // Obtendo Temperature
        apiService.getTemperature().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        Temperature temperature = new Gson().fromJson(response.body().string(), Temperature.class);
                        sensorContainer.sensorsValues.add(temperature.getValue());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    System.err.println("Request failed with code: " + response.code());
                }
                responseLatch.countDown(); // Decrementa o contador do latch
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("FALHEI MISERAVELMENTE");
                t.printStackTrace();
                responseLatch.countDown(); // Decrementa o contador do latch mesmo em caso de falha
            }
        });

        // Obtendo AirFlow
        apiService.getAirFlow().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        AirFlow airFlow = new Gson().fromJson(response.body().string(), AirFlow.class);
                        sensorContainer.sensorsValues.add(airFlow.getValue());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    System.err.println("Request failed with code: " + response.code());
                }
                responseLatch.countDown(); // Decrementa o contador do latch
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("FALHEI MISERAVELMENTE");
                t.printStackTrace();
                responseLatch.countDown(); // Decrementa o contador do latch mesmo em caso de falha
            }
        });

        // Obtendo PulseOxygen
        apiService.getPulseOxygen().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        PulseOxygen pulseOxygen = new Gson().fromJson(response.body().string(), PulseOxygen.class);
                        sensorContainer.sensorsValues.add(pulseOxygen.getValue());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    System.err.println("Request failed with code: " + response.code());
                }
                responseLatch.countDown(); // Decrementa o contador do latch
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("FALHEI MISERAVELMENTE");
                t.printStackTrace();
                responseLatch.countDown(); // Decrementa o contador do latch mesmo em caso de falha
            }
        });

        // Obtendo HeartRate
        apiService.getHeartRate().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        HeartRate heartRate = new Gson().fromJson(response.body().string(), HeartRate.class);
                        sensorContainer.sensorsValues.add(heartRate.getValue());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    System.err.println("Request failed with code: " + response.code());
                }
                responseLatch.countDown(); // Decrementa o contador do latch
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("FALHEI MISERAVELMENTE");
                t.printStackTrace();
                responseLatch.countDown(); // Decrementa o contador do latch mesmo em caso de falha
            }
        });

        // Obtendo Glucose
        apiService.getGlucose().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        Glucose glucose = new Gson().fromJson(response.body().string(), Glucose.class);
                        sensorContainer.sensorsValues.add(glucose.getValue());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    System.err.println("Request failed with code: " + response.code());
                }
                responseLatch.countDown(); // Decrementa o contador do latch
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("FALHEI MISERAVELMENTE");
                t.printStackTrace();
                responseLatch.countDown(); // Decrementa o contador do latch mesmo em caso de falha
            }
        });

        // Obtendo BloodPressure
        apiService.getBloodPressure().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        BloodPressure bloodPressure = new Gson().fromJson(response.body().string(), BloodPressure.class);
                        sensorContainer.sensorsValues.add(bloodPressure.getDiastolicValue());
                        sensorContainer.sensorsValues.add(bloodPressure.getSystolicValue());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    System.err.println("Request failed with code: " + response.code());
                }
                responseLatch.countDown(); // Decrementa o contador do latch
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("FALHEI MISERAVELMENTE");
                t.printStackTrace();
                responseLatch.countDown(); // Decrementa o contador do latch mesmo em caso de falha
            }
        });

        try {
            responseLatch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return sensorContainer.sensorsValues;
    }

    private static class SensorContainer{
        private List<Integer> sensorsValues = new ArrayList<>();
    }

    private void setLayoutSelected(LinearLayout containerLayout, String port) {


//        List<Integer> sensorsData = getSensorsData(port);
//        System.out.println("----------------- SENSORES ------------------");
//        System.out.println(sensorsData);

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

//                switch (labels[j]) {
//                    case "Temperature":
//                        title.setText("Temperature: " + sensorsData.get(0));
//                        break;
//
//                    case "AirFlow":
//                        title.setText("AirFlow: " + sensorsData.get(1));
//                        break;
//
//                    case "Pulse Oxygen":
//                        title.setText("Pulse Oxygen: " + sensorsData.get(2));
//                        break;
//
//                    case "Heart Rate":
//                        title.setText("Heart Rate: " + sensorsData.get(3));
//                        break;
//
//                    case "Glucose":
//                        title.setText("Glucose: " + sensorsData.get(4));
//                        break;
//
//                    case "Blood Pressure":
//                        title.setText("Blood Pressure: " + sensorsData.get(5) + "/" + sensorsData.get(6));
//                        break;
//
//                }

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
        handler.removeCallbacks(pollingRunnable);
    }
}