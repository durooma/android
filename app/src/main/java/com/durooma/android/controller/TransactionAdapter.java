package com.durooma.android.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.durooma.android.R;
import com.durooma.android.model.Account;
import com.durooma.android.model.Transaction;

import java.util.Locale;

public class TransactionAdapter extends ArrayAdapter<Transaction> {

    private LayoutInflater inflater;

    public TransactionAdapter(Context context) {
        super(context, R.layout.item_transaction);
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Transaction transaction = getItem(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_transaction, parent, false);
        }
        ViewHolder vh = (ViewHolder)convertView.getTag();
        if (vh == null) {
            vh = new ViewHolder();
            vh.sourceAccount = (TextView)convertView.findViewById(R.id.source_account);
            vh.transfer = convertView.findViewById(R.id.transfer);
            vh.amount = (TextView)convertView.findViewById(R.id.amount);
            vh.account = (TextView)convertView.findViewById(R.id.account);
        }

        vh.amount.setText(String.format(Locale.getDefault(), "%.2f", transaction.getAmount()));
        vh.transfer.setVisibility(View.GONE);
        if (transaction.getSource() != null && transaction.getTarget() == null) {
            // income:
            vh.account.setText(Account.get(transaction.getSource()).getName());
        } else if (transaction.getSource() == null && transaction.getTarget() != null) {
            // expense:
            vh.account.setText(Account.get(transaction.getTarget()).getName());
        } else {
            // transfer:
            vh.transfer.setVisibility(View.VISIBLE);
            vh.sourceAccount.setText(Account.get(transaction.getSource()).getName());
            vh.account.setText(Account.get(transaction.getTarget()).getName());
        }

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

        private TextView sourceAccount;
        private View transfer;
        private TextView amount;
        private TextView account;


    }

}
