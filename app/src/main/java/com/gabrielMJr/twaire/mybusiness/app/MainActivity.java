package com.gabrielMJr.twaire.mybusiness.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.gabrielMJr.twaire.mybusiness.R;
import com.gabrielMJr.twaire.mybusiness.app.AddCartActivity;
import com.gabrielMJr.twaire.mybusiness.app.AddNewProductActivity;
import com.gabrielMJr.twaire.mybusiness.data_manager.ProductDataCenter;
import com.gabrielMJr.twaire.mybusiness.data_manager.ProductDatabase;
import com.gabrielMJr.twaire.mybusiness.util.Constants;
import com.gabrielMJr.twaire.mybusiness.util.CustomToast;
import com.gabrielMJr.twaire.mybusiness.util.MainAdapter;
import com.gabrielMJr.twaire.mybusiness.util.RecyclerViewInterface;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecyclerViewInterface
{

    // Go to add item button
    private Button add_new_product;

    // Products array
    protected ArrayList<String> name;
    protected ArrayList<String> price;
    protected ArrayList<String> image;

    private ArrayList<Integer> card_id;

    // Recycler and adapter view
    private RecyclerView productRecycler;
    private MainAdapter productAdapter;

    // Product data center
    private ProductDataCenter dataCenter;
    private ProductDatabase productDB;

    // App bar navigation attributes
    private ImageView nav_purchase;
    private ImageView nav_home;

    // Popup menu
    private PopupMenu product_options_menu;

    // Custom toast object
    private CustomToast custom_toast;

    private void initialize()
    {
        // Initializing main attributes
        add_new_product = findViewById(R.id.add_new_product);
        productRecycler = findViewById(R.id.productsRecyclerView);

        // For app bar navigation

        nav_purchase = findViewById(R.id.nav_purchase);
        nav_home = findViewById(R.id.nav_home);

        name = new ArrayList<>();
        price = new ArrayList<>();
        image = new ArrayList<>();
        card_id = new ArrayList<>();

        productAdapter = new MainAdapter(getApplicationContext(), name, price, /*amount,*/ image, this);
        dataCenter = new ProductDataCenter(getApplicationContext());
        productDB = new ProductDatabase(getApplicationContext());
        custom_toast = new CustomToast(getApplicationContext());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();

        // Setting adapter and column numbers into the recycler
        productRecycler.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        productRecycler.setAdapter(productAdapter);
        displayDate();

        // Add new product button
        add_new_product.setOnClickListener(
            new OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    startActivityForResult(new Intent(getApplicationContext(), AddNewProductActivity.class), Constants.ADD_PRODUCT_ACTIVITY);
                }
            });

        // App bar navigation on click
        // Nav Purchase button
        nav_purchase.setOnClickListener(
            new OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    // Is not be possible to add cart while doent have product
                    // Check if has product
                    if (name.size() < 1)
                    {
                        // Doesnt have product
                        // Set background, text and icon
                        // Set and show toast
                        custom_toast.setBackground(R.drawable.ic_error_toast_1)
                            .setDrawable(R.drawable.ic_alert_circle_outline)
                            .setDuration(Toast.LENGTH_SHORT)
                            .setText(R.string.doesnt_have_product)
                            .show();



                        // Return void
                        return;
                    }

                    // If arrive here, it means that has product
                    // Go to add card activity
                    startActivity(new Intent(getApplicationContext(), AddCartActivity.class));
                }
            });

        // Home button
        nav_home.setOnClickListener(
            new OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    // Dont do nothing
                }
            });
    }

    // Displaying shared preferences into the adapter
    // To display it just add values into the array list
    private void displayDate()
    {
        // Get total ids
        card_id = dataCenter.getIDs();
        
        if (dataCenter.isDataCenterEmpty())
        {
            // Dont do nothing
        }
        else
        {
            name.clear();
            price.clear();
            image.clear();

            for (int i = 0; i < dataCenter.getProductsIndex(); i++)
            {
                name.add(dataCenter.getName(card_id.get(i)));
                price.add(String.valueOf(dataCenter.getPrice(card_id.get(i))));
                image.add(String.valueOf(dataCenter.getImage(card_id.get(i))));
            }
        }
    }

    @Override
    public void onItemClick(int position)
    {
        // Do nothing

    }

    // Product on long click
    @Override
    public void onLongClick(final int position, View view)
    {
        // Inflate menu
        product_options_menu = new PopupMenu(getApplicationContext(), view);
        product_options_menu.inflate(R.menu.product_options_popup_menu);

        product_options_menu.setOnMenuItemClickListener(
            new OnMenuItemClickListener()
            {
                @Override
                public boolean onMenuItemClick(MenuItem item)
                {
                    switch (item.getItemId())
                    {
                            // Delete product
                        case R.id.delete_product:
                            deleteProduct(position);
                            return true;
                    }

                    // Nothing was clicked
                    return false;
                }                              
            });

        product_options_menu.show();
    }

    // Delete product submethod
    private void deleteProduct(int position)
    {      
        // Get id of the item to be removed
        int id = card_id.get(position);

        // Delete from datacenter
        if (productDB.deleteProduct(id) && dataCenter.deleteImage(id))
        {
            // Remove items from arraylist
            name.remove(position);
            price.remove(position);
            image.remove(position);

            // Remove from card id arraylist
            card_id.remove(position);

            // Notify on item removed
            productAdapter.notifyItemRemoved(position);
        }
        // Something went wrong
        else
        {
            Toast.makeText(getApplicationContext(), getText(R.string.failed_on_delete), Toast.LENGTH_SHORT).show();
        }
    }


    // On activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        /* Check the request code to determine what is being called*/

        // Add product activity
        if (requestCode == Constants.ADD_PRODUCT_ACTIVITY)
        {
            /* Check if it was successful or no*/
            // Sucessfull
            if (resultCode == RESULT_OK)
            {
                String name = data.getStringExtra(Constants.NAME);
                float price = data.getFloatExtra(Constants.PRICE, 0f);
                String image = data.getStringExtra(Constants.IMAGE);

                Uri uri = Uri.parse(image);

                this.name.add(name);
                this.price.add(String.valueOf(price));
                this.image.add(String.valueOf(uri));
                
                card_id = dataCenter.getIDs();
                productAdapter.notifyItemInserted(card_id.size());
            }
        }
    }
}
