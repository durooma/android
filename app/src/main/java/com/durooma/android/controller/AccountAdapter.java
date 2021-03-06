package com.durooma.android.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.durooma.android.R;
import com.durooma.android.model.Account;

import java.util.Locale;

public class AccountAdapter extends ArrayAdapter<Account> {

    private LayoutInflater inflater;

    public AccountAdapter(Context context) {
        super(context, R.layout.item_account);
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Account account = getItem(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_account, parent, false);
        }
        ViewHolder vh = (ViewHolder)convertView.getTag();
        if (vh == null) {
            vh = new ViewHolder();
            vh.name = (TextView)convertView.findViewById(R.id.name);
            vh.balance = (TextView)convertView.findViewById(R.id.balance);
        }

        vh.name.setText(account.getName());
        vh.balance.setText(String.format(Locale.getDefault(), "%.2f", account.getBalance()));

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        Account account = getItem(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_account_dropdown, parent, false);
        }
        ((TextView)convertView).setText(account.getName());
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private static class ViewHolder {

        private TextView name;
        private TextView balance;

    }

}
