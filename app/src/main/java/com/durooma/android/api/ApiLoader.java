package com.durooma.android.api;

import android.content.Context;
import android.support.v4.content.Loader;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ApiLoader<D> extends Loader<D> implements Observer<D> {

    private D current;
    private Observable<D> call;
    private Subscription connection;

    @Override
    public void onNext(D d) {
        deliverResult(d);
    }

    @Override
    public void onCompleted() {
        connection = null;
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        deliverResult(null);
    }

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
    public ApiLoader(Context context, Observable<D> call) {
        super(context);
        this.call = call;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (current == null) {
            if (connection == null) {
                connection = call.observeOn(AndroidSchedulers.mainThread()).subscribe(this);
            }
        } else {
            deliverResult(current);
        }
    }

    @Override
    public void deliverResult(D data) {
        connection = null;
        super.deliverResult(data);
    }

    @Override
    protected void onReset() {
        super.onReset();
        current = null;
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
        if (connection != null) {
            connection.unsubscribe();
        }
        current = null;
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
        call.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this);
    }
}
