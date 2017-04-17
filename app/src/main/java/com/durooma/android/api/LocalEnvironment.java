package com.durooma.android.api;

import com.andretietz.retroauth.AndroidAuthenticationHandler;
import com.andretietz.retroauth.Retroauth;
import com.durooma.android.TokenProvider;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

public class LocalEnvironment implements Environment<DuroomaApi> {

    private Retrofit retrofit;

    LocalEnvironment() {

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        retrofit = new Retroauth.Builder<>(AndroidAuthenticationHandler.create(new TokenProvider()))
                .baseUrl("http://10.0.2.2:8080")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();
    }

    @Override
    public DuroomaApi createService() {
        return retrofit.create(DuroomaApi.class);
    }

}
