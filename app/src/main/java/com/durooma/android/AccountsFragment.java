package com.durooma.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.*;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.durooma.android.api.Api;
import com.durooma.android.api.ApiLoader;
import com.durooma.android.controller.AccountAdapter;
import com.durooma.android.model.Account;
import org.androidannotations.annotations.*;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_accounts)
@OptionsMenu(R.menu.fragment_list)
public class AccountsFragment
        extends Fragment
        implements LoaderManager.LoaderCallbacks<List<Account>>,
        AbsListView.MultiChoiceModeListener,
        SwipeRefreshLayout.OnRefreshListener {

    protected static final int STATUS_EMPTY = 0;
    protected static final int STATUS_LIST = 1;
    protected static final int STATUS_ERROR = 2;

    protected static final int LOAD_ACCOUNTS = 0;

    @ViewById
    ListView list;

    @ViewById
    View empty;

    @ViewById
    View error;

    @ViewById
    FloatingActionButton fab;

    @ViewById
    SwipeRefreshLayout swipeRefresh;

    private ArrayAdapter<Account> listAdapter;

    private View[] statusViews;

    @AfterViews
    public void init() {
        statusViews = new View[]{empty, list, error};
        getLoaderManager().initLoader(LOAD_ACCOUNTS, null, this);
        listAdapter = new AccountAdapter(getContext());
        list.setAdapter(listAdapter);
        list.setMultiChoiceModeListener(this);
        swipeRefresh.setOnRefreshListener(this);
        swipeRefresh.setRefreshing(true);
    }

    public void setStatus(int status) {
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
        swipeRefresh.setRefreshing(false);
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
        MenuInflater menuInflater = mode.getMenuInflater();
        menuInflater.inflate(R.menu.edit_items, menu);
        mode.setTitle(R.string.edit_accounts);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return true;
    }

    @Override
    public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
        List<Observable<Void>> deleteOperations = new ArrayList<>();
        for (Long id : list.getCheckedItemIds()) {
            deleteOperations.add(Api.get().removeAccount(id));
        }
        Observable.merge(deleteOperations)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        getLoaderManager().restartLoader(LOAD_ACCOUNTS, null, AccountsFragment.this);
                        mode.finish();
                    }
                });
        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
    }

    @Click(R.id.fab)
    public void newAccount() {
        startActivity(new Intent(getContext(), EditAccountActivity_.class));
    }

    void refresh() {
        getLoaderManager().restartLoader(LOAD_ACCOUNTS, null, this);
    }

    @OptionsItem(R.id.menu_refresh)
    void menuRefresh() {
        swipeRefresh.setRefreshing(true);
        refresh();
    }


    @Override
    public void onRefresh() {
        refresh();
    }
}
