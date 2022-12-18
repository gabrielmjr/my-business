package com.gabrielMJr.twaire.mybusiness.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.gabrielMJr.twaire.mybusiness.R;
import com.gabrielMJr.twaire.mybusiness.util.RecyclerViewInterface;
import java.util.ArrayList;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;

// Products adapter view
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyViewHolder>
{
    
    // Attributes
    private Context context;
    private ArrayList names_id;
    private ArrayList prices_id;
    //private ArrayList amount_id;
    private ArrayList images_id;
    
    private View viewHolder;
    private final RecyclerViewInterface RVI;
    
    // Constructor
    public MainAdapter(
                    Context context,
                    ArrayList names_id,
                    ArrayList prices_id,
                    /*ArrayList amount_id, */
                    ArrayList images_id,
                    RecyclerViewInterface RVI)
    {
        this.context = context;
        this.names_id = names_id;
        this.prices_id = prices_id;
        //this.amount_id = amount_id;
        this.images_id = images_id;
        this.RVI = RVI;
    }
    

    // The obligatory overrides from recyclerView.adapter
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int p2)
    {
        // Inflating the card view
        viewHolder = LayoutInflater.from(context).inflate(R.layout.card_view_product, parent, false);
        return new MyViewHolder(viewHolder);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        // Setting text and uri image parsed of each avaliable product
        holder.names_id.setText(" " + names_id.get(position));
        holder.prices_id.setText(" " + prices_id.get(position));
        //holder.amounts_id.setText(" " + amount_id.get(position));
        holder.images_id.setImageURI(Uri.parse((String)images_id.get(position)));
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
        //private TextView amounts_id;
        private ImageView images_id;
        
        public MyViewHolder(View itemView)
        {
            super(itemView);
            
            // Finding the widgets
            names_id = itemView.findViewById(R.id.product_name);
            prices_id = itemView.findViewById(R.id.product_price);
            //amounts_id = itemView.findViewById(R.id.product_amount);
            images_id = itemView.findViewById(R.id.product_image);
            
            // OnClick of item
            itemView.setOnClickListener(
                new OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        // Getting the clicked item position
                        int position = getAdapterPosition();

                        
                        if (position != RecyclerView.NO_POSITION)
                        {
                            // Setting the clicked position
                            RVI.onItemClick(position);
                        }
                    }
                });
                
                // On long click
                itemView.setOnLongClickListener(
                    new OnLongClickListener()
                {
                    @Override
                    public boolean onLongClick(View view)
                    {
                        int position = getAdapterPosition();
                        
                        // If no ppsition
                        if (position != RecyclerView.NO_POSITION)
                        {
                            RVI.onLongItClick(position, view);
                            return true;
                        }
                        else
                        {
                            return false;
                        }
                    }
                                         
                    });
        }
    }
}

