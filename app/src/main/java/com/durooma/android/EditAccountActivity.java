package com.durooma.android;

import com.durooma.android.api.Api;
import com.durooma.android.model.AccountBody;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.rengwuxian.materialedittext.MaterialEditText;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

@EActivity(R.layout.activity_edit_account)
public class EditAccountActivity extends EditActivity implements Observer<Void> {

    @ViewById
    @NotEmpty
    MaterialEditText name;

    @ViewById
    @NotEmpty
    MaterialEditText initialBalance;


    @Override
    public void onValidationSucceeded() {
        super.onValidationSucceeded();
        Api.get().addAccount(new AccountBody(
                name.getText().toString(),
                Double.parseDouble(initialBalance.getText().toString())
        ))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this);
    }

}
