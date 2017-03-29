package com.durooma.android;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import com.durooma.android.api.Api;
import com.durooma.android.model.UserRegistration;
import com.durooma.android.util.DialogUtil;
import com.durooma.android.util.TextInputLayoutAdapter;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.exception.ConversionException;
import org.androidannotations.annotations.*;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

import java.util.List;

@EActivity(R.layout.activity_signup)
@OptionsMenu(R.menu.activity_signup)
public class SignupActivity extends AppCompatActivity implements Observer<Void>, Validator.ValidationListener {

    @ViewById
    @NotEmpty
    @Email
    TextInputLayout email;

    @ViewById
    @Password(scheme = Password.Scheme.ALPHA_NUMERIC_MIXED_CASE)
    TextInputLayout password;

    @ViewById
    TextInputLayout firstName;

    @ViewById
    TextInputLayout lastName;

    @ViewById
    ProgressBar progress;

    private Validator validator;
    private TextInputLayoutAdapter adapter = new TextInputLayoutAdapter();

    @AfterViews
    void init() {
        validator = new Validator(this);
        validator.setValidationListener(this);
        validator.registerAdapter(TextInputLayout.class, adapter);
    }

    @OptionsItem
    void signup() {
        validator.validate();
    }

    @Override
    public void onValidationSucceeded() {
        try {
            Api.get().signup(new UserRegistration(
                    adapter.getData(email),
                    adapter.getData(password),
                    adapter.getData(firstName),
                    adapter.getData(lastName)
            ))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this);
            progress.setVisibility(View.VISIBLE);
            email.setError(null);
            password.setError(null);
            firstName.setError(null);
            lastName.setError(null);
        } catch (ConversionException ignored) {}
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            ((TextInputLayout) error.getView()).setError(error.getCollatedErrorMessage(this));
        }
    }

    @Override
    public void onCompleted() {
        progress.setVisibility(View.INVISIBLE);
        startActivity(new Intent(this, TransactionsFragment.class));
    }

    @Override
    public void onError(Throwable e) {
        DialogUtil.showError(this, e);
    }

    @Override
    public void onNext(Void aVoid) {
    }
}
