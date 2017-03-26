package com.durooma.android;

import android.accounts.Account;
import com.andretietz.retroauth.AndroidToken;
import com.andretietz.retroauth.AndroidTokenType;
import com.andretietz.retroauth.Provider;
import com.andretietz.retroauth.TokenStorage;
import okhttp3.Request;
import okhttp3.Response;

public class TokenProvider implements Provider<Account, AndroidTokenType, AndroidToken> {

    @Override
    public Request authenticateRequest(Request request, AndroidToken androidToken) {
        return request.newBuilder()
                .addHeader("Authorization", "Bearer " + androidToken.token)
                .build();
    }

    @Override
    public boolean retryRequired(int i, Response response, TokenStorage<Account, AndroidTokenType, AndroidToken> tokenStorage, Account account, AndroidTokenType androidTokenType, AndroidToken androidToken) {
        // this is an optional (sample) implementation
        if (!response.isSuccessful()) {
            if (response.code() == 401) {
                tokenStorage.removeToken(account, androidTokenType, androidToken);
            }
        }
        return false;
    }

}
