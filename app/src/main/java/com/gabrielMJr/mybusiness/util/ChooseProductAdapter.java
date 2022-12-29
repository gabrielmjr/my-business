package com.gabrielMJr.mybusiness.util;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.gabrielMJr.mybusiness.R;
import com.gabrielMJr.mybusiness.util.ChooseProductAdapter;
import java.util.ArrayList;

public class ChooseProductAdapter extends RecyclerView.Adapter<ChooseProductAdapter.MyViewHolder>
{
    
    // Attributes
    // Card array list
    private ArrayList<Uri> image;
    private ArrayList<String> name;
    private ArrayList<Integer> amount;
    private ArrayList<Float> price;
    
    // Context
    private Context context;
    
    // View holder
    private View viewHolder;
    
    // Recycler view choose product interface object
    private final RecyclerViewInterface RVI;
    
    // Constructor
    public ChooseProductAdapter(Context context,
           ArrayList<Uri> image,
           ArrayList<String> name,
           ArrayList<Integer> amount,
           ArrayList<Float> price,
           RecyclerViewInterface RVI)
    {
        // Initializing the context
        this.context = context;
        
        // Initializing the lists
        this.image = image;
        this.name = name;
        this.amount = amount;
        this.price = price;
        
        // Initializing recycler view product interface from constructor
        this.RVI = RVI;
    }

    // Create new card
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int p2)
    {
        // Inflating layout
        viewHolder = LayoutInflater.from(context).inflate(R.layout.card_view_choose_product, parent, false);
        
        // Returning new view holder inflated
        return new MyViewHolder(viewHolder);
    }

    // Set values to the cards
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        // Setting the values into the layout component
        holder.choose_product_image.setImageURI(image.get(position));
        holder.choose_product_name.setText(name.get(position));
        holder.choose_product_amount.setText("Amount: " + String.valueOf(amount.get(position)));
        holder.choose_product_price.setText("$" + String.valueOf(price.get(position)));
    }

    // Get item count
    @Override
    public int getItemCount()
    {
        // Returning the size from name arrayList
        return name.size();
    }
   
    
    // My view holder class
    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        // Attributes
        // Card components
        private ImageView choose_product_image;
        private TextView choose_product_name;
        private TextView choose_product_amount;
        private TextView choose_product_price;
        
        // Constructor
        public MyViewHolder(View itemView)
        {
            super(itemView);
            
            // Getting the widgets from layout
            choose_product_image = itemView.findViewById(R.id.choose_product_image);
            choose_product_name = itemView.findViewById(R.id.choose_product_name);
            choose_product_amount = itemView.findViewById(R.id.choose_product_amount);
            choose_product_price = itemView.findViewById(R.id.choose_product_price);
            
            // On item click
            itemView.setOnClickListener(
                new OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        // Get item position 
                        int position = getAdapterPosition();
                        
                        if (position != RecyclerView.NO_POSITION)
                        {
                            // Setting the clicked position
                            RVI.onItemClick(position);
                        }
                    }
                });
        }
    }
}
