package com.gabrielMJr.twaire.mybusiness.util;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.gabrielMJr.twaire.mybusiness.R;
import java.util.ArrayList;

public class AddCartAdapter extends RecyclerView.Adapter<AddCartAdapter.MyViewHolder>
{

    // Attributes
    private Context context;
    private ArrayList<Uri> product_image;
    private ArrayList<String> product_name;
    private ArrayList<Integer> product_amount;
    private ArrayList<Float> product_price;
    private ArrayList<Integer> amount;
    
    // View holder
    private View holder;
    
    // Constructor
    public AddCartAdapter(Context context,
           /*ArrayList<Uri> product_image,*/
           ArrayList<String> product_name,
           ArrayList<Integer> product_amount,
           ArrayList <Float> product_price)
    {
        
        // Setting up the attributes
        this.context = context;
        /*this.product_image = product_image;*/
        this.product_name = product_name;
        this.product_amount = product_amount;
        this.product_price = product_price;
    }
    
    // On create
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int p2)
    {
        // Inflating the layout
        holder = LayoutInflater.from(context).inflate(R.layout.card_view_add_cart, parent, false);
        
        // Returning new layout
        return new MyViewHolder(holder);
    }

    
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        // Setting values to card
        //holder.product_image.setImageURI(product_image.get(position));
        holder.product_name.setText(product_name.get(position));
        holder.product_amount.setText(String.valueOf(product_amount.get(position)));
        holder.product_price.setText(String.valueOf(product_price.get(position)));
    }

    @Override
    public int getItemCount()
    {
        // Returning the size of cards
        return product_name.size();
    }
    
    
    
    
    // My ViewHolder class
    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        
        // Card components
        private ImageView product_image;
        private TextView product_name;
        private TextView product_amount;
        private TextView product_price;

        private EditText amount;
        
        // Constructor
        public MyViewHolder(View itemView)
        {
            super(itemView);
            
            // Initializing card components
            product_image = itemView.findViewById(R.id.add_cart_product_image);
            product_name = itemView.findViewById(R.id.add_cart_product_name);
            product_amount = itemView.findViewById(R.id.add_cart_product_amount);
            product_price = itemView.findViewById(R.id.add_cart_product_price);
            
            amount = itemView.findViewById(R.id.add_cart_amount);
        }
    }
}
