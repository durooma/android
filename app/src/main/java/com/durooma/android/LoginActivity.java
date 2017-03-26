package com.durooma.android;

import android.accounts.Account;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.andretietz.retroauth.AuthenticationActivity;
import com.durooma.android.api.Api;
import com.durooma.android.model.Session;
import com.durooma.android.model.UserCredentials;
import com.durooma.android.util.DialogUtil;
import com.durooma.android.util.TextInputLayoutAdapter;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.mobsandgeeks.saripaar.exception.ConversionException;
import org.androidannotations.annotations.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@EActivity(R.layout.activity_login)
@OptionsMenu(R.menu.activity_login)
public class LoginActivity extends AuthenticationActivity implements GoogleApiClient.OnConnectionFailedListener, Callback<Session> {

    @ViewById(R.id.email)
    TextInputLayout emailInput;

    @ViewById(R.id.password)
    TextInputLayout passwordInput;

    @ViewById(R.id.sign_in_button)
    SignInButton signInButton;

    private static final int RC_SIGN_IN = 1;
    private GoogleApiClient mGoogleApiClient;
    private TextInputLayoutAdapter adapter = new TextInputLayoutAdapter();

    @AfterViews
    void init() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestServerAuthCode("318487213973-llmhckcpa3stg6rog9un2idp31o6ndmi.apps.googleusercontent.com", false)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @OptionsItem
    void login() {
        try {
            String email = adapter.getData(emailInput);
            String password = adapter.getData(passwordInput);

            Api.get().login(new UserCredentials(email, password))
                    .enqueue(this);
        } catch (ConversionException e) {
            e.printStackTrace();
        }
    }

    @Click(R.id.sign_in_button)
    void googleLogin() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @OnActivityResult(RC_SIGN_IN)
    void handleSignInResult(Intent data) {
        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        Log.d("LOGIN", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            String authCode = acct.getServerAuthCode();
        } else {
            // Signed out, show unauthenticated UI.
        }
    }

    @Override
    public void onResponse(Call<Session> call, Response<Session> response) {
        if (response.isSuccessful()) {
            Session session = response.body();
            Account account = createOrGetAccount(session.getUser().getEmail());
            storeToken(account, getString(R.string.authentication_token), session.getToken());
            finalizeAuthentication(account);
        } else {
            DialogUtil.showError(this, response);
        }
    }

    @Override
    public void onFailure(Call<Session> call, Throwable t) {
        t.printStackTrace();
        DialogUtil.showError(this, t.getLocalizedMessage());
    }
}
