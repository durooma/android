package com.durooma.android.api;

import com.durooma.android.model.UserRegistration;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface DuroomaApi {

    @POST("/user")
    Call<Void> signup(@Body UserRegistration userRegistration);

}
