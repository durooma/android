package com.durooma.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import com.durooma.android.api.Api;
import com.durooma.android.model.Session;
import com.durooma.android.model.User;
import com.durooma.android.util.DialogUtil;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

@EActivity(R.layout.activity_splash)
public class SplashActivity extends AppCompatActivity implements Observer<Session> {

    @AfterViews
    public void init() {
        Api.get().getSession()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this);
    }

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
        DialogUtil.showError(this, e);
    }

    @Override
    public void onNext(Session session) {
        User.setCurrentUser(session.getUser());
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
