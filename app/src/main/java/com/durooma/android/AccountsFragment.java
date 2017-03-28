package com.durooma.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.durooma.android.api.Api;
import com.durooma.android.api.ApiLoader;
import com.durooma.android.controller.AccountAdapter;
import com.durooma.android.model.Account;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EFragment(R.layout.fragment_accounts)
public class AccountsFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Account>>, AbsListView.MultiChoiceModeListener {

    protected static final int STATUS_LOADING = 0;
    protected static final int STATUS_EMPTY = 1;
    protected static final int STATUS_LIST = 2;
    protected static final int STATUS_ERROR = 3;

    protected static final int LOAD_ACCOUNTS = 0;

    @ViewById
    ListView list;

    @ViewById
    View progress;

    @ViewById
    View empty;

    @ViewById
    View error;

    @ViewById
    FloatingActionButton fab;

    private ArrayAdapter<Account> listAdapter;

    private View[] statusViews;

    @AfterViews
    public void init() {
        statusViews = new View[] { progress, empty, list, error };
        getLoaderManager().initLoader(LOAD_ACCOUNTS, null, this);
        listAdapter = new AccountAdapter(getContext());
        list.setAdapter(listAdapter);
        list.setMultiChoiceModeListener(this);
        setStatus(STATUS_LOADING);
    }

    public void setStatus(int status) {
        progress.setVisibility(View.GONE);
        list.setVisibility(View.GONE);
        empty.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        statusViews[status].setVisibility(View.VISIBLE);
    }

    @Override
    public Loader<List<Account>> onCreateLoader(int id, Bundle args) {
        return new ApiLoader<>(getContext(), Api.get().getAccounts());
    }

    @Override
    public void onLoadFinished(Loader<List<Account>> loader, List<Account> data) {
        listAdapter.clear();
        if (data != null) {
            listAdapter.addAll(data);
            setStatus(data.isEmpty() ? STATUS_EMPTY : STATUS_LIST);
        } else {
            setStatus(STATUS_ERROR);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Account>> loader) {
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

    }

    @Click(R.id.fab)
    public void newAccount() {
        startActivity(new Intent(getContext(), EditAccountActivity_.class));
    }
}
