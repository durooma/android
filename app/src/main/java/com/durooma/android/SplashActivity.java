package com.durooma.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.durooma.android.api.Api;
import com.durooma.android.model.Session;
import com.durooma.android.util.DialogUtil;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@EActivity(R.layout.activity_splash)
public class SplashActivity extends AppCompatActivity implements Callback<Session> {

    @AfterViews
    public void init() {
        Api.get().getSession().enqueue(this);
    }

    @Override
    public void onResponse(Call<Session> call, Response<Session> response) {
        if (response.isSuccessful()) {

        } else {
            DialogUtil.showError(this, response);
        }
    }

    @Override
    public void onFailure(Call<Session> call, Throwable t) {
        t.printStackTrace();
        DialogUtil.showError(this, t.getLocalizedMessage());
    }
}
