package com.durooma.android.api;

import com.andretietz.retroauth.Authenticated;
import com.durooma.android.R;
import com.durooma.android.model.*;
import retrofit2.http.*;
import rx.Observable;

import java.util.List;

public interface DuroomaApi {

    @POST("/user")
    Observable<Void> signup(@Body UserRegistration userRegistration);

    @POST("/session")
    Observable<Session> login(@Body UserCredentials userCredentials);

    @Authenticated({R.string.authentication_account, R.string.authentication_token})
    @GET("/session")
    Observable<Session> getSession();

    @Authenticated({R.string.authentication_account, R.string.authentication_token})
    @GET("/account")
    Observable<List<Account>> getAccounts();

    @Authenticated({R.string.authentication_account, R.string.authentication_token})
    @POST("/account")
    Observable<Void> addAccount(@Body AccountBody account);

    @Authenticated({R.string.authentication_account, R.string.authentication_token})
    @DELETE("/account/{id}")
    Observable<Void> removeAccount(@Path("id") long id);

}
