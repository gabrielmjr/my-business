package com.gabrielMJr.twaire.mybusiness.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.gabrielMJr.twaire.mybusiness.R;
import com.gabrielMJr.twaire.mybusiness.data_manager.ProductDataCenter;
import com.gabrielMJr.twaire.mybusiness.util.AddCartAdapter;
import com.gabrielMJr.twaire.mybusiness.util.AddCartInterface;
import com.gabrielMJr.twaire.mybusiness.util.Constants;
import com.gabrielMJr.twaire.mybusiness.util.CustomToast;
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

    // ArrayList of each amount of added product
    private ArrayList<Integer> added_amount;

    // ArrayList of actual amount each product
    private ArrayList<Integer> actual_amount;

    // Value of total price for added item
    private Float total_price_value;

    // Add item button
    private Button add_item;

    // Confirm purchase button
    private Button confirm_purchase;

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

    // Card position
    private static int position;

    // Intent for go to choose product activity
    private Intent intent;

    // Custom toast object
    private CustomToast custom_toast;

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
        actual_amount = new ArrayList<>();

        // Initializing card item ids, added amounts ans total price
        card_id = new ArrayList<>();
        added_amount = new ArrayList<>();
        total_price_value = 0f;

        // Get add item button, confirm purchase and total price text view
        add_item = findViewById(R.id.add_item);
        confirm_purchase = findViewById(R.id.confirm_purchase);
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
        
        // Initializing custom toast
        custom_toast = new CustomToast(getApplicationContext());

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

        // Confirm the cart and update databade
        confirm_purchase.setOnClickListener(
            new OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    // Check if has added products
                    if (card_id.size() < 1)
                    {
                        custom_toast.setBackground(R.drawable.ic_error_toast_1)
                        .setDrawable(R.drawable.ic_alert_circle_outline)
                        .setDuration(Toast.LENGTH_SHORT)
                        .setText(R.string.empty_cart)
                        .show();    
                        
                    }
                    else
                    {
                        // Update database in amount for each added product
                        for (int i = 0; i < card_id.size(); i++)
                        {
                            int product_amount = dataCenter.getAmount(card_id.get(i));
                            // Update database
                            // If products amounts were updated
                            if (dataCenter.getProductDB().updateAmount(card_id.get(i), product_amount - added_amount.get(i)))
                            {
                                // Set and show toast
                                custom_toast.setBackground(R.drawable.ic_done_add_product_toast)
                                    .setDrawable(R.drawable.ic_checkbox_marked_circle_outline)
                                    .setDuration(Toast.LENGTH_SHORT)
                                    .setText(R.string.add_cart_successful)
                                    .show();
                            }
                            else
                            {
                                // If product amounts were not updated
                                custom_toast.setBackground(R.drawable.ic_error_toast_1)
                                    .setDrawable(R.drawable.ic_error_outline)
                                    .setDuration(Toast.LENGTH_SHORT)
                                    .setText(R.string.add_cart_unsuccessful)
                                    .show();
                            }
                        }
                        
                        // Finish the activity
                        finish();
                    }
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

    // On long click
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
    public void onItemIncremented(int position, int new_amount)
    {
        // total_price += (price stored on database
        total_price_value += dataCenter.getPrice(card_id.get(position));

        // Increment from added products amounts
        added_amount.add(position, new_amount);

        // Set new total price to text view
        total_price_text_view.setText(getText(R.string.product_total) + " $" + total_price_value.toString());
    }

    // On item decremented
    @Override
    public void onItemDecremented(int position, int new_amount)
    {  
        // total_price -= (price stored on database)
        total_price_value -= dataCenter.getPrice(card_id.get(position));

        // Decrement from added products amounts
        added_amount.add(position, new_amount);

        // Set new total price to text view
        total_price_text_view.setText(getText(R.string.product_total) + " $" + total_price_value.toString());
    }

    // Remove product method
    private void removeProduct()
    {
        // Removing values from array lists to this card
        product_image.remove(position);
        product_name.remove(position);
        product_amount.remove(position);
        product_price.remove(position);

        // Remove the id and amount of added item from arrayList
        card_id.remove(position);
        added_amount.remove(position);

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

        // Adding the added item to card id and added products amounts
        card_id.add(id);

        /*
         The added amount will add 0 as initial amount for the added products,
         so, if added_amount is empty, the size method will return 0,
         the first index in programming is 0,
         and it need to add the initial value into 0,
         then, added_amount.add("position:" added_amount.size() "which will return "0", "value:" 0)
         the logic will continue so for every each added product
         */
        added_amount.add(added_amount.size(), 0);


        // Notifying added product
        adapter.notifyItemInserted(count);

        // Incrementing the counter
        count++;
        return;
    }
}
