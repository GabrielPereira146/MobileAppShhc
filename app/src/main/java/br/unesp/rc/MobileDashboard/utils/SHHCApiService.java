package br.unesp.rc.MobileDashboard.utils;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface SHHCApiService {
    @GET("/temperature")
    Call<ResponseBody> getTemperature();

    @GET("/shhc/")
    Call<ResponseBody> getNumberAPI();


}
