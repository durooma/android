package com.durooma.android;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.durooma.android.api.Api;
import com.durooma.android.model.AccountBody;
import com.durooma.android.util.DialogUtil;
import com.durooma.android.util.TextInputLayoutAdapter;
import com.mobsandgeeks.saripaar.exception.ConversionException;
import org.androidannotations.annotations.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@EActivity(R.layout.activity_edit_account)
@OptionsMenu(R.menu.activity_edit)
public class EditAccountActivity extends AppCompatActivity implements Callback<Void> {

    public static final int RESULT_SUCCESS = 0;
    public static final int RESULT_CANCELED = 1;

    @ViewById
    TextInputLayout name;

    @ViewById
    TextInputLayout initialBalance;

    @ViewById
    View progress;

    private TextInputLayoutAdapter adapter = new TextInputLayoutAdapter();

    @AfterViews
    void init() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setLoading(false);
    }

    void setLoading(boolean loading) {
        if (loading) {
            progress.setVisibility(View.VISIBLE);
            name.setEnabled(false);
            initialBalance.setEnabled(false);
        } else {
            progress.setVisibility(View.GONE);
            name.setEnabled(true);
            initialBalance.setEnabled(true);
        }
    }

    @OptionsItem
    void accept() {
        try {
            Api.get().addAccount(new AccountBody(
                    adapter.getData(name),
                    Double.parseDouble(adapter.getData(initialBalance))
            )).enqueue(this);
            setLoading(true);
        } catch (ConversionException e) {
            e.printStackTrace();
        }
    }

    @OptionsItem(android.R.id.home)
    void back() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void onResponse(Call<Void> call, Response<Void> response) {
        setLoading(false);
        if (response.isSuccessful()) {
            setResult(RESULT_SUCCESS);
            finish();
        } else {
            DialogUtil.showError(this, response);
        }
    }

    @Override
    public void onFailure(Call<Void> call, Throwable t) {
        setLoading(false);
        DialogUtil.showError(this, t);
    }
}
