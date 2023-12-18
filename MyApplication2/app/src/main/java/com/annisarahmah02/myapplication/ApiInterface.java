package com.annisarahmah02.myapplication;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET("http://192.168.100.251:80/tampil_data.php/") // Ganti dengan URL API Anda
    Call<List<HealthData>> getHealthData();
}
