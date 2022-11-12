package com.gabrielMJr.twaire.mybusiness.util;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import java.util.ArrayList;
import android.view.View;
import android.widget.TextView;
import com.gabrielMJr.twaire.mybusiness.R;
import android.view.ViewGroup;
import android.view.LayoutInflater;

// Products adapter view
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>
{
    
    // Attributes
    private static Context context;
    private static ArrayList names_id;
    private static ArrayList prices_id;
    
    private static View viewHolder;

    // Constructor
    public MyAdapter(Context context, ArrayList names_id, ArrayList prices_id)
    {
        this.context = context;
        this.names_id = names_id;
        this.prices_id = prices_id;
    }
    

    // The obligatory overrides from recyclerView.adapter
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int p2)
    {
        // Inflating the card view
        viewHolder = LayoutInflater.from(context).inflate(R.layout.products_card, parent, false);
        return new MyViewHolder(viewHolder);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        // Setting text of each avaliable product
        holder.names_id.setText(String.valueOf(names_id.get(position)));
        holder.prices_id.setText(String.valueOf(prices_id.get(position)));
    }

    // Setting total index of adapted views
    @Override
    public int getItemCount()
    {
        return names_id.size();
    }
  
    
    // My view holder class
    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        
        // Attributes
        private TextView names_id;
        private TextView prices_id;
        
        public MyViewHolder(View itemView)
        {
            super(itemView);
            
            // Finding the widgets
            names_id = itemView.findViewById(R.id.name);
            prices_id = itemView.findViewById(R.id.price);
        }
    }
}
