package com.gabrielMJr.twaire.mybusiness.app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.gabrielMJr.twaire.mybusiness.R;
import com.gabrielMJr.twaire.mybusiness.data_manager.ProductDataCenter;
import com.gabrielMJr.twaire.mybusiness.util.AddCartAdapter;
import com.gabrielMJr.twaire.mybusiness.util.Constants;
import java.util.ArrayList;

public class AddCartActivity extends Activity {
    
    // Attributes
    // Card arraylist
    private ArrayList<Uri> product_image;
    private ArrayList<String> product_name;
    private ArrayList<Integer> product_amount;
    private ArrayList<Float> product_price;
    
    // Centralized add item button
    private Button centralized_add_item;
    
    // Bottom add item button
    private Button bottom_add_item;
    
    // Final add item button
    private Button add_item;
    
    // Recycler View and adapter
    private RecyclerView recycler;
    private AddCartAdapter adapter;
    
    // Product database center object
    private ProductDataCenter dataCenter;
    
    // Products added to cart counter
    private int count;
    
    // Initializing the activity
    private void initialize()
    {
        // Finding view
        recycler = findViewById(R.id.add_cart_recycler_view);

        // Initializing card attributes
        product_image = new ArrayList<>();
        product_name = new ArrayList<>();
        product_amount = new ArrayList<>();
        product_price = new ArrayList<>();
        
        // Get add item buttons
        centralized_add_item = findViewById(R.id.centralized_add_item);
        bottom_add_item = findViewById(R.id.bottom_add_item);
      
        // Add cart adapter
        adapter = new AddCartAdapter(getApplicationContext(),
                                    /* product_image,*/
                                     product_name,
                                     product_amount,
                                     product_price);
                                    
        // Initializing datacenter
        dataCenter = new ProductDataCenter(getApplicationContext());
        
        // Initializing added products to cart with 0
        count = 0;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cart);
        
        // Call initialize method
        initialize();
        
        // Setting layout manager and adapter
        recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recycler.setAdapter(adapter);
        
        initializeAddItemButton();
        
        // Go to choose product activity on add item on click
        add_item.setOnClickListener(
            new OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    startActivityForResult(new Intent(getApplicationContext(), ChooseProductActivity.class), Constants.CHOOSE_PRODUCT_ACTIVITY);
                }
            });
    }

    /* 
     This method initialize the add item button
     If the array is empty, the button should be on center
     else, on botton
    */
    private void initializeAddItemButton()
    {
        // Check if the array is empty
        if (product_name.size() < 1)
        {
            // Is empty, main button should be centralized
            add_item = centralized_add_item; 
        }
        else
        {
            // Isnt empty, main button should be bottom
            add_item = bottom_add_item;
        }

        // Show the add_item;
        add_item.setVisibility(View.VISIBLE);

        // Set text  to add_item
        add_item.setText(R.string.add_to_cart);
    }

    
    // On activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        
        // Check from where is comming the result
        // For choose product activity
        if (requestCode == Constants.CHOOSE_PRODUCT_ACTIVITY)
        {
            // If the result was done
            if (resultCode == RESULT_OK)
            {
                // Get id and display data
                displayData(data.getIntExtra(Constants.ID, -1));
                initializeAddItemButton();
            }
        }
    }

    private void displayData(int id)
    {
        // Adding the added product to ui
        product_image.add(dataCenter.getImage(id));
        product_name.add(dataCenter.getName(id));
        product_amount.add(dataCenter.getAmount(id));
        product_price.add(dataCenter.getPrice(id));
        
        // Notifying added product
        adapter.notifyItemInserted(count + 1);
        
        // Incrementing the counter
        count++;
    }
}
