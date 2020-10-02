package com.example.requisieshttp.api;

import com.example.requisieshttp.CEP;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CEPinteface {
    @GET("json")
    Call<CEP> getCEP();
}
