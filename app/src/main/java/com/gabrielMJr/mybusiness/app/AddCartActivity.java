package com.gabrielMJr.mybusiness.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import com.gabrielMJr.mybusiness.R;
import com.gabrielMJr.mybusiness.data_manager.ProductDataCenter;
import com.gabrielMJr.mybusiness.util.AddCartAdapter;
import com.gabrielMJr.mybusiness.util.AddCartInterface;
import com.gabrielMJr.mybusiness.util.Constants;
import java.util.ArrayList;

public class AddCartActivity extends AppCompatActivity implements AddCartInterface
{

    // Attributes
    // Card arraylist
    private ArrayList<Uri> product_image;
    private ArrayList<String> product_name;
    private ArrayList<Integer> product_amount;
    private ArrayList<Float> product_price;

    // ArrayList of added item id
    private ArrayList<Integer> card_id;
    
    // Value of total price for added item
    private Float total_price_value;
    
    // Add item button
    private Button add_item;
    
    // Total price text view
    private TextView total_price_text_view;

    // Recycler View and adapter
    private RecyclerView recycler;
    private AddCartAdapter adapter;
    
    // Popup menu
    private PopupMenu add_to_cart_card_popup_menu;

    // Product database center object
    private ProductDataCenter dataCenter;

    // Products added to cart counter
    private int count;
    
    // On long click card position
    private static int position;

    // Intent for go to choose product activity
    private Intent intent;

    
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
        
        // Initializing card item ids and total price
        card_id = new ArrayList<>();
        total_price_value = 0f;
        
        // Get add item button and total price text view
        add_item = findViewById(R.id.add_item);
        total_price_text_view = findViewById(R.id.total_price);
        
        // Set $0 as initial price for total
        total_price_text_view.setText(getText(R.string.product_total) + " " + getText(R.string.initial_price));

        // Add cart adapter
        adapter = new AddCartAdapter(getApplicationContext(),
                                     product_image,
                                     product_name,
                                     product_amount,
                                     product_price,
                                     card_id,
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
                    // Initializing intent
                    intent = new Intent(getApplicationContext(), ChooseProductActivity.class);
                    
                    // Insert id array list to intent
                    intent.putExtra(Constants.ID, card_id);
                  
                    // Go to choose product activity
                    startActivityForResult(intent, Constants.CHOOSE_PRODUCT_ACTIVITY);
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
            }
        }
        return;
    }

    // On item click
    @Override
    public void onItemClick(int position)
    {
        // Dont do nothing
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
    
    
    // On item incremented
    @Override
    public void onItemIncremented(int position)
    {
        // total_price += (price stored on database
        total_price_value += dataCenter.getPrice(card_id.get(position));
        
        // Set new total price to text view
        total_price_text_view.setText(getText(R.string.product_total) + " $" + total_price_value.toString());
    }

    // On item decremented
    @Override
    public void onItemDecremented(int position)
    {
        // total_price -= (price stored on database)
        total_price_value -= dataCenter.getPrice(card_id.get(position));

        // Set new total price to text view
        total_price_text_view.setText(getText(R.string.product_total) + " $" +total_price_value.toString());
    }
    
    // Remove product method
    private void removeProduct()
    {
        // Removing values from array lists to this card
        product_image.remove(position);
        product_name.remove(position);
        product_amount.remove(position);
        product_price.remove(position);
        
        // Remove the id of added item from arrayList
        card_id.remove(position);
        
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

        // Adding the added item to card id
        card_id.add(id);
        
        // Notifying added product
        adapter.notifyItemInserted(count);

        // Incrementing the counter
        count++;
        return;
    }
}
