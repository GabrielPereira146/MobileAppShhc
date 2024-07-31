package br.unesp.rc.MobileDashboard.utils;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface SHHCApiService {

    @GET("/shhc/")
    Call<ResponseBody> getNumberAPI();

    @GET("/shhc/Patient/")
    Call<ResponseBody> getPatient();

    @GET("/shhc/PulseOxygen/")
    Call<ResponseBody> getPulseOxygen();

    @GET("/shhc/HeartRate/")
    Call<ResponseBody> getHeartRate();

    @GET("/shhc/Glucose/")
    Call<ResponseBody> getGlucose();

    @GET("/shhc/BloodPressure/")
    Call<ResponseBody> getBloodPressure();

    @GET("/shhc/AirFlow/")
    Call<ResponseBody> getAirFlow();

    @GET("/shhc/Temperature/")
    Call<ResponseBody> getTemperature();

}
