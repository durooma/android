package com.durooma.android;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;
import com.durooma.android.api.Api;
import com.durooma.android.model.Account;
import com.durooma.android.model.TransactionBody;
import com.durooma.android.util.DialogUtil;
import com.durooma.android.util.MaterialSpinner;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.rengwuxian.materialedittext.MaterialEditText;
import org.androidannotations.annotations.*;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

import java.math.BigDecimal;
import java.util.List;

@EActivity(R.layout.activity_edit_transaction)
@OptionsMenu(R.menu.activity_edit)
public class EditTransactionActivity extends AppCompatActivity implements Observer<Void>, Validator.ValidationListener {

    public static final int RESULT_SUCCESS = 0;
    public static final int RESULT_CANCELED = 1;

    public static final String EXTRA_TRANSACTION_TYPE = "com.durooma.android.EditTransactionActivity.EXTRA_TRANSACTION_TYPE";

    private ArrayAdapter<Account> adapter;
    private Validator validator;

    @ViewById
    @NotEmpty
    MaterialEditText amount;

    @ViewById
    @NotEmpty
    MaterialSpinner source;

    @ViewById
    @NotEmpty
    MaterialSpinner target;

    @ViewById
    CheckBox isExempt;

    @ViewById
    MaterialEditText exemptAmount;

    @ViewById
    View progress;

    @Extra("com.durooma.android.EditTransactionActivity.EXTRA_TRANSACTION_TYPE")
    String mode;

    @AfterViews
    void init() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setLoading(false);

        adapter = new ArrayAdapter<>(this, R.layout.item_account_dropdown);
        adapter.addAll(Account.all());
        source.setAdapter(adapter);
        target.setAdapter(adapter);
        exemptAmount.setEnabled(isExempt.isChecked());

        validator = new Validator(this);
        validator.setValidationListener(this);

        switch (mode) {
            case "expense":
                target.setVisibility(View.GONE);
                break;
            case "income":
                source.setVisibility(View.GONE);
                // deliberately not breaking to hide exempt for income
            case "transfer":
                isExempt.setVisibility(View.GONE);
                exemptAmount.setVisibility(View.GONE);
                break;
        }
    }

    void setLoading(boolean loading) {
        if (loading) {
            progress.setVisibility(View.VISIBLE);
            amount.setEnabled(false);
            source.setEnabled(false);
        } else {
            progress.setVisibility(View.GONE);
            amount.setEnabled(true);
            source.setEnabled(true);
        }
    }

    @OptionsItem
    void accept() {
        validator.validate();
    }

    @OptionsItem(android.R.id.home)
    void back() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Click(R.id.is_exempt)
    void onIsExemptClick() {
        if (isExempt.isChecked() && exemptAmount.getText().length() == 0) {
            exemptAmount.setText(amount.getText());
        }
        exemptAmount.setEnabled(isExempt.isChecked());
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

    @Override
    public void onValidationSucceeded() {
        Api.get().addTransaction(new TransactionBody(
                source.getSelectedPosition() >= 0 ? adapter.getItem(source.getSelectedPosition()).getId() : null,
                target.getSelectedPosition() >= 0 ? adapter.getItem(target.getSelectedPosition()).getId() : null,
                new BigDecimal(amount.getText().toString()),
                exemptAmount.getText().length() > 0 ? new BigDecimal(exemptAmount.getText().toString()) : new BigDecimal("0.00")
        ))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this);
        setLoading(true);
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            ((EditText) error.getView()).setError(error.getCollatedErrorMessage(this));
        }
    }

}
