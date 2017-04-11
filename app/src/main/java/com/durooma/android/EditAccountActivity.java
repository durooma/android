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
public class EditAccountActivity extends EditActivity implements Observer<Void> {

    @ViewById
    TextInputLayout name;

    @ViewById
    TextInputLayout initialBalance;

    private TextInputLayoutAdapter adapter = new TextInputLayoutAdapter();

    @Override
    public void onValidationSucceeded() {
        super.onValidationSucceeded();
        try {
            Api.get().addAccount(new AccountBody(
                    adapter.getData(name),
                    Double.parseDouble(adapter.getData(initialBalance))
            ))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this);
        } catch (ConversionException e) {
            e.printStackTrace();
        }
    }

}
