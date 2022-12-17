package com.gabrielMJr.twaire.mybusiness.app;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.gabrielMJr.twaire.mybusiness.R;
import com.gabrielMJr.twaire.mybusiness.util.AddCartAdapter;
import java.util.ArrayList;

public class AddCartActivity extends Activity {
    
    // Attributes
    // Card arraylist
    private ArrayList<Drawable> product_image;
    private ArrayList<String> product_name;
    private ArrayList<Integer> product_amount;
    private ArrayList<Float> product_price;
    
    private Button add_item;
    
    // Recycler View and adapter
    private RecyclerView recycler;
    private AddCartAdapter adapter;
    int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cart);
        
        // Call initialize method
        initialize();
        
        add_item.setOnClickListener(
            new OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    //"product_image.add(getDrawable(R.mipmap.ic_launcher));
                    product_name.add("Product " + count);
                    product_amount.add(36);
                    product_price.add(12f);
                    adapter.notifyAll();
                    count++;
                }
            });
        
        // Setting layout manager and adapter
        recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recycler.setAdapter(adapter);
    }
    
    // Initializing the activity
    private void initialize()
    {
        // Finding view
        recycler = findViewById(R.id.add_cart_recycler);
        
        // Initializing card attributes
        product_image = new ArrayList<>();
        product_name = new ArrayList<>();
        product_amount = new ArrayList<>();
        product_price = new ArrayList<>();
        add_item = findViewById(R.id.add_item);
        // Add cart adapter
        adapter = new AddCartAdapter(getApplicationContext(),
                product_image,
                product_name,
                product_amount,
                product_price);
    }
    
}
