package com.durooma.android.controller;

import android.accounts.Account;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.durooma.android.R;

public class UserAccountAdapter extends ArrayAdapter<Account> {

    private LayoutInflater inflater;

    public UserAccountAdapter(Context context) {
        super(context, R.layout.item_account);
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Account account = getItem(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_user_account_dropdown, parent, false);
        }
        ((TextView)convertView).setText(account.name);
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}