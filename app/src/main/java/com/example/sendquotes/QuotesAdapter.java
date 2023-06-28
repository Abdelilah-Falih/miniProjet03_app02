package com.example.sendquotes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class QuotesAdapter extends RecyclerView.Adapter<QuotesAdapter.MyViewHolder> {
    ArrayList<Quote> quotes;
    Context context;
    int position_item;

    public QuotesAdapter(Context context, ArrayList<Quote> quotes){
        this.context = context;
        this.quotes = quotes;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@Nullable ViewGroup parent , int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.item_pager, parent, false);
        return new MyViewHolder(view);
    };

    @Override
    public int getItemCount(){
        return quotes.size();
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position){
        Quote quote = quotes.get(position);
        holder.tv_quote.setText(quote.getQuote());
        holder.tv_author.setText(quote.getAuthor());
        position_item = position;
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_quote, tv_author;
        public MyViewHolder (View item){
            super(item);
            tv_quote = item.findViewById(R.id.tv_quote);
            tv_author = item.findViewById(R.id.tv_author);
        }
    }
    public int getPosition(){
        return this.position_item;
    }

}
