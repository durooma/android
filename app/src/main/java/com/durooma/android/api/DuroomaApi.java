package com.durooma.android.api;

import com.andretietz.retroauth.Authenticated;
import com.durooma.android.R;
import com.durooma.android.model.Account;
import com.durooma.android.model.Session;
import com.durooma.android.model.UserCredentials;
import com.durooma.android.model.UserRegistration;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

import java.util.List;

public interface DuroomaApi {

    @POST("/user")
    Call<Void> signup(@Body UserRegistration userRegistration);

    @POST("/session")
    Call<Session> login(@Body UserCredentials userCredentials);

    @Authenticated({R.string.authentication_account, R.string.authentication_token})
    @GET("/session")
    Call<Session> getSession();

    @Authenticated({R.string.authentication_account, R.string.authentication_token})
    @GET("/account")
    Call<List<Account>> getAccounts();

}
