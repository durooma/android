package com.durooma.android;

import android.accounts.*;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import com.andretietz.retroauth.AuthAccountManager;
import com.durooma.android.controller.UserAccountAdapter;
import com.durooma.android.model.User;
import com.durooma.android.util.MaterialSpinner;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener {

    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    private int selectedNavigationItem;

    private Spinner userSpinner;
    private UserAccountAdapter userAdapter;
    private AuthAccountManager authAccountManager;

    private Fragment dashboard;
    private Fragment accounts;
    private Fragment transactions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer);
        drawerToggle = new ActionBarDrawerToggle(this, drawer, R.string.drawer_open, R.string.drawer_close);
        drawer.addDrawerListener(drawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        navigationView = (NavigationView) findViewById(R.id.navigation);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }
        selectedNavigationItem = navigationView.getMenu().getItem(0).getItemId();

        AccountManager accountManager = AccountManager.get(this);
        Account[] accounts = accountManager.getAccountsByType(getString(R.string.authentication_account));
        authAccountManager = new AuthAccountManager();

        if (accounts.length == 0) {
            accountManager.addAccount(getString(R.string.authentication_account), null, null, null, this, new AccountManagerCallback<Bundle>() {
                @Override
                public void run(AccountManagerFuture<Bundle> future) {
                    setupUsers();
                }
            }, null);
        } else {
            setupUsers();
        }
    }

    private void setupUsers() {
        AccountManager accountManager = AccountManager.get(this);
        Account[] accounts = accountManager.getAccountsByType(getString(R.string.authentication_account));
        if (accounts.length > 0) {
            Account active = authAccountManager.getActiveAccount(getString(R.string.authentication_account));
            if (active == null) {
                active = accounts[0];
                authAccountManager.setActiveAccount(getString(R.string.authentication_account), active.name);
            }

            int index = -1;
            for (int i = 0; i < accounts.length; ++i) {
                if (accounts[i] == active) {
                    index = i;
                    break;
                }
            }

            userAdapter = new UserAccountAdapter(this);
            userAdapter.addAll(accounts);
            userSpinner = (Spinner) navigationView.getHeaderView(0).findViewById(R.id.nav_user);
            userSpinner.setAdapter(userAdapter);
            userSpinner.setSelection(index);
            userSpinner.setOnItemSelectedListener(this);
        } else {
            // login was aborted:
            finish();
        }
    }

    private void createFragments() {
        dashboard = new DashboardFragment_();
        accounts = new AccountsFragment_();
        transactions = new TransactionsFragment_();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    private void showFragment(Fragment f) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, f)
                .commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        selectedNavigationItem = item.getItemId();
        switch (item.getItemId()) {
            case R.id.nav_item_dashboard:
                showFragment(dashboard);
                break;
            case R.id.nav_item_accounts:
                showFragment(accounts);
                break;
            case R.id.nav_item_transactions:
                showFragment(transactions);
                break;
        }
        drawer.closeDrawers();
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        authAccountManager.setActiveAccount(getString(R.string.authentication_account), userAdapter.getItem(position).name);
        com.durooma.android.model.Account.clearCache();
        createFragments();
        navigationView.getMenu().performIdentifierAction(selectedNavigationItem, 0);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //    @ViewById
//    ProgressBar progress;
//
//    @AfterViews
//    void init() {
//        progress.setVisibility(View.VISIBLE);
//        Api.get().getAccounts().enqueue(new Callback<List<Account>>() {
//            @Override
//            public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
//                if (response.isSuccessful()) {
//
//                } else {
//                    DialogUtil.showError(MainActivity.this, response);
//                }
//                progress.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onFailure(Call<List<Account>> call, Throwable t) {
//                t.printStackTrace();
//                DialogUtil.showError(MainActivity.this, t.getLocalizedMessage());
//                progress.setVisibility(View.GONE);
//            }
//        });
//    }

}
