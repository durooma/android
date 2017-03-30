package com.durooma.android;

import android.content.Context;
import android.content.Intent;
import android.widget.ArrayAdapter;
import com.durooma.android.api.Api;
import com.durooma.android.controller.AccountAdapter;
import com.durooma.android.model.Account;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import rx.Observable;

import java.util.List;

@EFragment
public class AccountsFragment extends ListFragment<Account> {

    @Override
    protected ArrayAdapter<Account> onCreateAdapter(Context context) {
        return new AccountAdapter(context);
    }

    @Override
    protected Observable<List<Account>> getItems() {
        return Api.get().getAccounts();
    }

    @Override
    protected Observable<Void> removeItem(long id) {
        return Api.get().removeAccount(id);
    }

    @Click(R.id.fab)
    public void newAccount() {
        startActivity(new Intent(getContext(), EditAccountActivity_.class));
    }
}
