package com.gabrielMJr.twaire.mybusiness.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.gabrielMJr.twaire.mybusiness.R;
import com.gabrielMJr.twaire.mybusiness.adapter.MainAdapter;
import com.gabrielMJr.twaire.mybusiness.util.RecyclerViewInterface;
import java.util.ArrayList;

// Products adapter view
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyViewHolder>
{
    
    // Attributes
    private Context context;
    private ArrayList<String> names_id;
    private ArrayList<String> prices_id;
    private ArrayList<Bitmap> images_id;
    
    private View viewHolder;
    private final RecyclerViewInterface RVI;
    
    // Constructor
    public MainAdapter(
                    Context context,
                    ArrayList<String> names_id,
                    ArrayList<String> prices_id,
                    ArrayList<Bitmap> images_id,
                    RecyclerViewInterface RVI)
    {
        this.context = context;
        this.names_id = names_id;
        this.prices_id = prices_id;
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
        holder.images_id.setImageBitmap(images_id.get(position));
    }

    // returning total index of adapted views
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
        private ImageView images_id;
        
        public MyViewHolder(View itemView)
        {
            super(itemView);
            
            // Finding the widgets
            names_id = itemView.findViewById(R.id.product_name);
            prices_id = itemView.findViewById(R.id.product_price);
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
                        
                        // If has position on the pressed card
                        if (position != RecyclerView.NO_POSITION)
                        {
                            RVI.onLongClick(position, view);
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

