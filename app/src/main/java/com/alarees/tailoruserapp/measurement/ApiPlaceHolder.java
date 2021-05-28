package com.alarees.tailoruserapp.measurement;

import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

interface ApiPlaceHolder {
    @Headers({"Content-Type: application/json",
            "Authorization: APIKey a81c478a60cff15d5b60b32d03158965f4dd9b0b"})
    @POST("persons/?measurements_type=all")
    Call<JSONObject> CreateUser(@Body JSONObject jsonObject);
}
