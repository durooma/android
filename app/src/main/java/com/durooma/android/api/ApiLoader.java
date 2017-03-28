package com.durooma.android.api;

import android.content.Context;
import android.support.v4.content.Loader;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiLoader<D> extends Loader<D> implements Callback<D> {

    private Call<D> call;
    private Call<D> currentCall;

    private D current;

    /**
     * Stores away the application context associated with context.
     * Since Loaders can be used across multiple activities it's dangerous to
     * store the context directly; always use {@link #getContext()} to retrieve
     * the Loader's Context, don't use the constructor argument directly.
     * The Context returned by {@link #getContext} is safe to use across
     * Activity instances.
     *
     * @param context used to retrieve the application context.
     */
    public ApiLoader(Context context, Call<D> call) {
        super(context);
        this.call = call;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (current == null) {
            if (currentCall == null) {
                currentCall = call.clone();
                currentCall.enqueue(this);
            }
        } else {
            deliverResult(current);
        }
    }

    @Override
    public void deliverResult(D data) {
        currentCall = null;
        super.deliverResult(data);
    }

    @Override
    protected void onReset() {
        super.onReset();
        current = null;
        currentCall = null;
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
        if (currentCall != null) {
            currentCall.cancel();
            currentCall = null;
        }
        current = null;
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
        currentCall = call.clone();
        currentCall.enqueue(this);
    }

    @Override
    public void onResponse(Call<D> call, Response<D> response) {
        if (response.isSuccessful()) {
            current = response.body();
            deliverResult(current);
        } else {
            deliverResult(null);
        }
    }

    @Override
    public void onFailure(Call<D> call, Throwable t) {
        t.printStackTrace();
        deliverResult(null);
    }
}
