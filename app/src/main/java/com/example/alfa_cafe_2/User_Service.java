package com.example.alfa_cafe_2;

import com.example.alfa_cafe_2.ui.Request.StringRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface User_Service {
    @POST("api")
    Call<StringRequest> request(@Body String string);
}
