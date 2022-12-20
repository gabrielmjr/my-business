package com.gabrielMJr.twaire.mybusiness.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import com.gabrielMJr.twaire.mybusiness.R;
import com.gabrielMJr.twaire.mybusiness.data_manager.ProductDataCenter;
import com.gabrielMJr.twaire.mybusiness.util.AddCartAdapter;
import com.gabrielMJr.twaire.mybusiness.util.Constants;
import com.gabrielMJr.twaire.mybusiness.util.RecyclerViewInterface;
import java.util.ArrayList;
import android.view.MenuItem;

public class AddCartActivity extends AppCompatActivity implements RecyclerViewInterface
{

    // Attributes
    // Card arraylist
    private ArrayList<Uri> product_image;
    private ArrayList<String> product_name;
    private ArrayList<Integer> product_amount;
    private ArrayList<Float> product_price;

    // Add item button
    private Button add_item;

    // Recycler View and adapter
    private RecyclerView recycler;
    private AddCartAdapter adapter;

    // Product database center object
    private ProductDataCenter dataCenter;

    // Products added to cart counter
    private int count;
   
    // Popup menu
    private PopupMenu add_to_cart_card_popup_menu;
    
    // On long click card position
    private static int position;

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

        // Get add item button
        add_item = findViewById(R.id.add_item);

        // Add cart adapter
        adapter = new AddCartAdapter(getApplicationContext(),
                                     product_image,
                                     product_name,
                                     product_amount,
                                     product_price,
                                     this);

        // Initializing datacenter
        dataCenter = new ProductDataCenter(getApplicationContext());

        // Initializing added products to cart with 0
        count = 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cart);

        // Call initialize method
        initialize();

        // Setting layout manager and adapter
        recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recycler.setAdapter(adapter);

        // Go to choose product activity on add item on click
        add_item.setOnClickListener(
            new OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    startActivityForResult(new Intent(getApplicationContext(), ChooseProductActivity.class), Constants.CHOOSE_PRODUCT_ACTIVITY);
                    return;
                }
            });
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
                
                // The adapter isnt empty and the button will stay on bottom
                
            }
        }
        return;
    }

    // On item click
    @Override
    public void onItemClick(int position)
    {
        
    }

    // On ling click
    @Override
    public void onLongClick(int position, View view)
    {
        // Passing the position to the class atribute
        this.position = position;
        
        // Initializing popup menu
        add_to_cart_card_popup_menu = new PopupMenu(getApplicationContext(), view);
        
        // Inflating menu
        add_to_cart_card_popup_menu.inflate(R.menu.add_to_cart_popup_menu);
        
        // Setting on click listener
        add_to_cart_card_popup_menu.setOnMenuItemClickListener(
            new OnMenuItemClickListener()
            {
                @Override
                public boolean onMenuItemClick(MenuItem item)
                {
                    // If clicked, check what was clicked
                    switch (item.getItemId())
                    {
                        // If remove button was clicked
                        case R.id.remove:
                            // Call remove product method
                            removeProduct();
                            
                            return true;
                    }
                    
                    // Nothing was clicked
                    return false;
                }            
            });
            
            // Showing the menu
            add_to_cart_card_popup_menu.show();
    }
    
    // Remove product method
    private void removeProduct()
    {
        // Removing values from array lists to this card
        product_image.remove(position);
        product_name.remove(position);
        product_amount.remove(position);
        product_price.remove(position);
        
        // Notifying removed card
        adapter.notifyItemRemoved(position);
    }

    private void displayData(int id)
    {
        // Adding the added product to ui
        product_image.add(dataCenter.getImage(id));
        product_name.add(dataCenter.getName(id));
        product_amount.add(dataCenter.getAmount(id));
        product_price.add(dataCenter.getPrice(id));

        // Notifying added product
        adapter.notifyItemInserted(count);

        // Incrementing the counter
        count++;
        return;
    }
}
