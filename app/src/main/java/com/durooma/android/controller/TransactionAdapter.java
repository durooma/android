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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class TransactionAdapter extends ArrayAdapter<Transaction> {

    private LayoutInflater inflater;
    private DateFormat dateFormat = SimpleDateFormat.getDateInstance();

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
            vh.targetAccount = (TextView)convertView.findViewById(R.id.target_account);
            vh.arrow = convertView.findViewById(R.id.arrow);
            vh.amount = (TextView)convertView.findViewById(R.id.amount);
            vh.account = (TextView)convertView.findViewById(R.id.account);
            vh.date = (TextView)convertView.findViewById(R.id.date);
            vh.description = (TextView)convertView.findViewById(R.id.description);
        }

        vh.amount.setText(String.format(Locale.getDefault(), "%.2f", transaction.getAmount()));
        vh.description.setText(transaction.getDescription());
        vh.date.setText(dateFormat.format(transaction.getDate()));
        vh.targetAccount.setVisibility(View.GONE);
        vh.arrow.setVisibility(View.GONE);
        if (transaction.getSource() != null && transaction.getTarget() == null) {
            // income:
            vh.account.setText(Account.get(transaction.getSource()).getName());
        } else if (transaction.getSource() == null && transaction.getTarget() != null) {
            // expense:
            vh.account.setText(Account.get(transaction.getTarget()).getName());
        } else {
            // transfer:
            vh.targetAccount.setVisibility(View.VISIBLE);
            vh.arrow.setVisibility(View.VISIBLE);
            vh.targetAccount.setText(Account.get(transaction.getTarget()).getName());
            vh.account.setText(Account.get(transaction.getSource()).getName());
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

        private TextView targetAccount;
        private View arrow;
        private TextView amount;
        private TextView account;
        private TextView date;
        private TextView description;


    }

}
