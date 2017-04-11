package com.durooma.android;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
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

public class EditActivity extends AppCompatActivity implements Observer<Void>, Validator.ValidationListener {

    private static final int RESULT_SUCCESS = 0;
    private static final int RESULT_CANCELED = 1;

    private Validator validator;
    private View progress;
    private ViewGroup content;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_edit);

        progress = findViewById(R.id.progress);
        content = (ViewGroup)findViewById(R.id.content);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setHomeButtonEnabled(true);
        }
        setLoading(false);

        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        getLayoutInflater().inflate(layoutResID, content, true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.accept:
                validator.validate();
                return true;
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setLoading(boolean loading) {
        if (loading) {
            progress.setVisibility(View.VISIBLE);
        } else {
            progress.setVisibility(View.GONE);
        }
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
        setLoading(true);
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            ((EditText) error.getView()).setError(error.getCollatedErrorMessage(this));
        }
    }

}
