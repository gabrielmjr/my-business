package com.gabrielMJr.mybusiness.util;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.gabrielMJr.mybusiness.R;
import com.gabrielMJr.mybusiness.data_manager.ProductDataCenter;
import com.gabrielMJr.mybusiness.util.AddCartAdapter;
import java.util.ArrayList;

public class AddCartAdapter extends RecyclerView.Adapter<AddCartAdapter.MyViewHolder>
{

    // Attributes
    private Context context;
    private ArrayList<Uri> product_image;
    private ArrayList<String> product_name;
    private ArrayList<Integer> product_amount;
    private ArrayList<Float> product_price;
    
    private Integer actual_amount;

    // Ids of added products
    private ArrayList<Integer> card_id;

    // View holder
    private View holder;

    // Add cart interface
    private final AddCartInterface ACI;

    // Data center object
    private ProductDataCenter dataCenter;

    // Constructor
    public AddCartAdapter(Context context,
                          ArrayList<Uri> product_image,
                          ArrayList<String> product_name,
                          ArrayList<Integer> product_amount,
                          ArrayList <Float> product_price,
                          ArrayList<Integer> card_id,
                          AddCartInterface ACI)
    {

        // Setting up the attributes
        this.context = context;
        this.product_image = product_image;
        this.product_name = product_name;
        this.product_amount = product_amount;
        this.product_price = product_price;
        this.card_id = card_id;
        this.ACI = ACI;

        // Initializing datacenter
        this.dataCenter = new ProductDataCenter(context);

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
        holder.product_image.setImageURI(product_image.get(position));
        holder.product_name.setText(context.getText(R.string.product_name) + " " + product_name.get(position));
        holder.product_amount.setText(context.getText(R.string.product_amount) + " " + String.valueOf(product_amount.get(position)));
        holder.product_price.setText(context.getText(R.string.product_price) + " " + String.valueOf(product_price.get(position)));
        holder.amount.setText("0");
    }

    // Get item count
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

        // Increment and decrement image view
        private ImageView increment;
        private ImageView decrement;

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
            increment = itemView.findViewById(R.id.add_cart_amount_plus);
            decrement = itemView.findViewById(R.id.add_cart_amount_minus);

            amount = itemView.findViewById(R.id.add_cart_amount);

            // Remove focus from amount
            amount.clearFocus();
            

            // On increment click
            increment.setOnClickListener(
                new OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        actual_amount = Integer.valueOf(amount.getText().toString());
                        
                        // Check if actual amount is greater that stored on db
                        if (actual_amount > dataCenter.getAmount(card_id.get(getAdapterPosition())) - 1)
                        {
                            // The amount ended
                            amount.setError(context.getText(R.string.enough_amount));
                            return;
                        }
                        else
                        {              
                            // Else increment actual amount
                            actual_amount++;
                            
                            // Set actual amount to the widget
                            amount.setText(actual_amount.toString());

                            // Call increment method to add the added item's price to add cart activity
                            ACI.onItemIncremented(getAdapterPosition(), actual_amount);
                        }
                    }
                });

            // On decrement click
            decrement.setOnClickListener(
                new OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        // Amount value
                        actual_amount = Integer.valueOf(amount.getText().toString());

                        // The amount cant be negative
                        if (actual_amount < 1)
                        {
                            // Show the error
                            amount.setError(context.getText(R.string.negative_amount));
                        }
                        else
                        {
                            // Decrement actual amount
                            actual_amount--;
                            
                            // Set actual amount to the widget
                            amount.setText(actual_amount.toString());

                            // Call increment method to add the added item's price to add cart activity
                            ACI.onItemDecremented(getAdapterPosition(), actual_amount);
                        }
                    }
                });

            // On product long click
            itemView.setOnLongClickListener(
                new OnLongClickListener()
                {
                    @Override
                    public boolean onLongClick(View view)
                    {
                        // Get adapter position
                        int position = getAdapterPosition();

                        // Check if its null
                        if (position != RecyclerView.NO_POSITION)
                        {
                            // Setting the index to the activity and calling on long click
                            ACI.onLongClick(getAdapterPosition(), view);
                            return true;
                        }

                        // Else return fale
                        return false;
                    }                
                });
        }
    }
}
