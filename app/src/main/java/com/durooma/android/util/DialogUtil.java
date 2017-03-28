package com.durooma.android.util;

import android.app.AlertDialog;
import android.content.Context;
import com.durooma.android.R;
import retrofit2.Response;

import java.io.IOException;

public class DialogUtil {

    public static void showError(Context context, String message) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setNeutralButton(R.string.ok, null)
                .create()
                .show();
    }

    public static <T> void showError(Context context, Response<T> response) {
        try {
            showError(context, response.errorBody().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showError(Context context, Throwable t) {
        t.printStackTrace();
        showError(context, t.getLocalizedMessage());
    }

}
