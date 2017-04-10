package com.durooma.android;

import android.content.Context;
import android.content.Intent;
import android.widget.ArrayAdapter;
import com.durooma.android.api.Api;
import com.durooma.android.controller.TransactionAdapter;
import com.durooma.android.model.Account;
import com.durooma.android.model.Transaction;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import rx.Observable;
import rx.functions.Func1;

import java.util.List;

import static com.durooma.android.EditTransactionActivity.EXTRA_TRANSACTION_TYPE;

@EFragment
public class TransactionListFragment extends ListFragment<Transaction> {

    @FragmentArg
    String type;

    @Override
    protected ArrayAdapter<Transaction> onCreateAdapter(Context context) {
        return new TransactionAdapter(getContext());
    }

    @Override
    protected Observable<List<Transaction>> getItems() {
        if (Account.hasCache()) {
            return Api.get().getTransactions(type);
        } else {
            return Api.get().getAccounts().flatMap(
                    new Func1<List<Account>, Observable<List<Transaction>>>() {
                        @Override
                        public Observable<List<Transaction>> call(List<Account> accounts) {
                            Account.updateCache(accounts);
                            return Api.get().getTransactions(type);
                        }
                    }
            );
        }
    }

    @Override
    protected Observable<Void> removeItem(long id) {
        return Api.get().removeTransaction(id);
    }

    @Click(R.id.fab)
    void newItem() {
        Intent i = new Intent(getContext(), EditTransactionActivity_.class);
        i.putExtra(EXTRA_TRANSACTION_TYPE, type);
        startActivity(i);
    }

    @Override
    protected String getEditModeTitle() {
        return getString(R.string.edit_transactions);
    }
}
