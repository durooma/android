package com.durooma.android.api;

import com.andretietz.retroauth.AndroidAuthenticationHandler;
import com.andretietz.retroauth.Retroauth;
import com.durooma.android.TokenProvider;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LocalEnvironment implements Environment<DuroomaApi> {

    private Retrofit retrofit;

    LocalEnvironment() {
        retrofit = new Retroauth.Builder<>(AndroidAuthenticationHandler.create(new TokenProvider()))
                .baseUrl("http://10.0.2.2:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Override
    public DuroomaApi createService() {
        return retrofit.create(DuroomaApi.class);
    }

}
