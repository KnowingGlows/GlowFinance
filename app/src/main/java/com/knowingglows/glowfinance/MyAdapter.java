package com.knowingglows.glowfinance;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>
{

    Context context;
    ArrayList<transaction_records> transactionRecords;

    public MyAdapter (Context context, ArrayList<transaction_records> transactionRecords)
    {
        this.context = context;
        this.transactionRecords = transactionRecords;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
       View view = LayoutInflater.from(context).inflate(R.layout.records_design, parent, false);

        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position)
    {

        transaction_records transactionRecord = transactionRecords.get(position);
        holder.transaction_src.setText(transactionRecord.getSource()); // Access from the object
        holder.transaction_amount.setText(transactionRecord.getAmount().toString()); // Access from the object
        holder.transaction_date.setText(transactionRecord.getDate());
        if (transactionRecord.getType().equals("Income"))
        {
            holder.transaction_type.setImageResource(R.drawable.income_icon);
        } else if (transactionRecord.getType().equals("Expense"))
        {
            holder.transaction_type.setImageResource(R.drawable.expense_icon);
        }

    }

    @Override
    public int getItemCount() {
        return transactionRecords.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView transaction_src, transaction_amount, transaction_date;
        ImageView transaction_type;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            transaction_src = itemView.findViewById(R.id.Src);
            transaction_amount = itemView.findViewById(R.id.Amount);
            transaction_date = itemView.findViewById(R.id.Date);
            transaction_type = itemView.findViewById(R.id.Type);

        }
    }
}
