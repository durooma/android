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
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

@EActivity(R.layout.activity_edit_account)
@OptionsMenu(R.menu.activity_edit)
public class EditAccountActivity extends AppCompatActivity implements Observer<Void> {

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
            ))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this);
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
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        setLoading(false);
        DialogUtil.showError(this, e);
    }

    @Override
    public void onNext(Void aVoid) {
        setLoading(false);
        setResult(RESULT_SUCCESS);
        finish();
    }
}
