package br.unesp.rc.MobileDashboard.utils;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
    private Retrofit retrofit;

    public RetrofitService(String baseUrl){
        initializeRetrofit(baseUrl);
    }

    public void initializeRetrofit(String baseUrl){
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl) //
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public void setRetrofit(Retrofit retrofit) {
        this.retrofit = retrofit;
    }
}
