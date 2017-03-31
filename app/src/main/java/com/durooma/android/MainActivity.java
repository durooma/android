package com.durooma.android;

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
import android.widget.TextView;
import com.durooma.android.model.User;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawer;

    private Fragment dashboard = new DashboardFragment_();
    private Fragment accounts = new AccountsFragment_();
    private Fragment transactions = new TransactionsFragment_();

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

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
            if (savedInstanceState == null) {
                navigationView.getMenu().performIdentifierAction(R.id.nav_item_dashboard, 0);
            }
        }


        ((TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_header_title)).setText(User.getCurrentUser().getEmail());
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
