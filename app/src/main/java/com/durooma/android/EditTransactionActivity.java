package com.durooma.android;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import com.durooma.android.api.Api;
import com.durooma.android.model.Account;
import com.durooma.android.model.TransactionBody;
import com.durooma.android.util.MaterialSpinner;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import org.androidannotations.annotations.*;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@EActivity(R.layout.activity_edit_transaction)
public class EditTransactionActivity extends EditActivity implements Observer<Void>, Validator.ValidationListener, DatePickerDialog.OnDateSetListener {

    static final String EXTRA_TRANSACTION_TYPE = "com.durooma.android.EditTransactionActivity.EXTRA_TRANSACTION_TYPE";

    private ArrayAdapter<Account> adapter;

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

    @ViewById(R.id.date)
    @NotEmpty
    MaterialEditText dateText;
    Date date;
    DateFormat dateFormat = SimpleDateFormat.getDateInstance();

    @ViewById
    MaterialEditText description;

    @Extra("com.durooma.android.EditTransactionActivity.EXTRA_TRANSACTION_TYPE")
    String mode;

    @AfterViews
    void init() {

        adapter = new ArrayAdapter<>(this, R.layout.item_account_dropdown);
        adapter.addAll(Account.all());
        source.setAdapter(adapter);
        target.setAdapter(adapter);
        exemptAmount.setEnabled(isExempt.isChecked());

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

    @Click(R.id.date)
    void onDateClick() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dialog = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH));
        dialog.show(getFragmentManager(), "DatePickerDialog");
    }

    @Click(R.id.is_exempt)
    void onIsExemptClick() {
        if (isExempt.isChecked() && exemptAmount.getText().length() == 0) {
            exemptAmount.setText(amount.getText());
        }
        exemptAmount.setEnabled(isExempt.isChecked());
    }

    @Override
    public void onValidationSucceeded() {
        super.onValidationSucceeded();
        Api.get().addTransaction(new TransactionBody(
                date,
                source.getSelectedPosition() >= 0 ? adapter.getItem(source.getSelectedPosition()).getId() : null,
                target.getSelectedPosition() >= 0 ? adapter.getItem(target.getSelectedPosition()).getId() : null,
                new BigDecimal(amount.getText().toString()),
                exemptAmount.getText().length() > 0 ? new BigDecimal(exemptAmount.getText().toString()) : new BigDecimal("0.00"),
                description.getText().toString()
        ))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(year, monthOfYear, dayOfMonth);
        date = c.getTime();
        dateText.setText(dateFormat.format(date));
    }
}
