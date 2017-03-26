package com.durooma.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import com.durooma.android.api.Api;
import com.durooma.android.model.Account;
import com.durooma.android.util.DialogUtil;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

@EActivity(R.layout.activity_transaction)
public class TransactionActivity extends AppCompatActivity {

    @ViewById
    ProgressBar progress;

    @AfterViews
    void init() {
        progress.setVisibility(View.VISIBLE);
        Api.get().getAccounts().enqueue(new Callback<List<Account>>() {
            @Override
            public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                if (response.isSuccessful()) {

                } else {
                    DialogUtil.showError(TransactionActivity.this, response);
                }
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<Account>> call, Throwable t) {
                t.printStackTrace();
                DialogUtil.showError(TransactionActivity.this, t.getLocalizedMessage());
                progress.setVisibility(View.GONE);
            }
        });
    }

}
