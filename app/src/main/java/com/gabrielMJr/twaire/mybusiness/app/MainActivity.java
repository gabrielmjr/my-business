package com.gabrielMJr.twaire.mybusiness.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;
import com.gabrielMJr.twaire.mybusiness.R;
import com.gabrielMJr.twaire.mybusiness.app.AddCartActivity;
import com.gabrielMJr.twaire.mybusiness.app.AddNewProductActivity;
import com.gabrielMJr.twaire.mybusiness.data_manager.ProductDataCenter;
import com.gabrielMJr.twaire.mybusiness.data_manager.ProductDatabase;
import com.gabrielMJr.twaire.mybusiness.util.Constants;
import com.gabrielMJr.twaire.mybusiness.util.MainAdapter;
import com.gabrielMJr.twaire.mybusiness.util.RecyclerViewInterface;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements RecyclerViewInterface
{

    // Go to add item button
    private Button add_new_product;
    
    // Products array
    protected ArrayList<String> name;
    protected ArrayList<String> price;
    //private ArrayList<String> amount;
    protected ArrayList<String> image;
    
    private HashMap<Integer, Integer> card_id;

    // Recycler and adapter view
    private RecyclerView productRecycler;
    private MainAdapter productAdapter;

    // Product data center
    private ProductDataCenter dataCenter;
    private ProductDatabase productDB;
    
    // App bar navigation attributes
    private ImageView nav_purchase;
    private ImageView nav_home;
    private ImageView nav_report;

    // Popup menu
    private PopupMenu product_options_menu;

    private void initialize()
    {
        // Initializing main attributes
        add_new_product = findViewById(R.id.add_new_product);
        productRecycler = findViewById(R.id.productsRecyclerView);

        // For app bar navigation
      
        nav_purchase = findViewById(R.id.nav_purchase);
        nav_home = findViewById(R.id.nav_home);
        nav_report = findViewById(R.id.nav_report);
        
        name = new ArrayList<>();
        price = new ArrayList<>();
        //amount = new ArrayList<>();
        image = new ArrayList<>();
        card_id = new HashMap<>();

        productAdapter = new MainAdapter(getApplicationContext(), name, price, /*amount,*/ image, this);
        dataCenter = new ProductDataCenter(getApplicationContext());
        productDB = new ProductDatabase(getApplicationContext());
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
                    //startActivity(new Intent(getApplicationContext(), AddNewProductActivity.class));
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
            
        // Nav Purchase button
        nav_report.setOnClickListener(
            new OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    // Go to report activity
                   // startActivity(new Intent(getApplicationContext(), AddCartActivity.class));
                }
            });

    }

    // Displaying shared preferences into the adapter
    // To display it just add values into the array list
    private void displayDate()
    {
        ArrayList id = dataCenter.getIDs();
        if (dataCenter.isDataCenterEmpty())
        {
            /*productRecycler.setActivated(false);
            add_new_product.setActivated(false);*/
            // Dont do nothing
        }
        else
        {
            name.clear();
            price.clear();
            //amount.clear();
            image.clear();
            card_id.clear();
            card_id.put(0, -1);
            
            for (int i = 0; i < dataCenter.getProductsIndex(); i++)
            {
                name.add(dataCenter.getName((Integer)id.get(i)));
                price.add(String.valueOf(dataCenter.getPrice((Integer)id.get(i))));
                //amount.add(String.valueOf(dataCenter.getAmount((Integer)id.get(i))));
                image.add(String.valueOf(dataCenter.getImage(id.get(i))));
                card_id.put(i + 1, (Integer)id.get(i));
            }
        }
    }

    @Override
    public void onItemClick(int position)
    {
        Toast.makeText(getApplicationContext(), "Clicked on: " + position, Toast.LENGTH_SHORT).show();
    }

    // Product on long click
    @Override
    public void onLongItClick(final int position, View view)
    {
        // Inflate menu
        product_options_menu = new PopupMenu(getApplicationContext(), view);
        product_options_menu.inflate(R.menu.product_options_popup_menu);

        product_options_menu.setOnMenuItemClickListener(
            new PopupMenu.OnMenuItemClickListener()
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
                    return false;
                }                              
            });

        product_options_menu.show();
    }

    // Delete product submethod
    private void deleteProduct(int position)
    {      
        // Get id of the item to be removed
        int id = card_id.get(position + 1);
        
        // Delete from datacenter
        if (productDB.deleteProduct(id) && dataCenter.deleteImage(id))
        {
            // Remove items from arraylist
            name.remove(position);
            price.remove(position);
            //amount.remove(position);
            image.remove(position);
   
            // Remove from actual hashmap
            card_id.remove(position + 1, card_id.get(position));
            
            // Notify on item removed
            productAdapter.notifyItemRemoved(position);
            recreate();
            Toast.makeText(getApplicationContext(), (String)getText(R.string.deleted) + id, Toast.LENGTH_SHORT).show();
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
                //int amount = data.getIntExtra(Constants.AMOUNT, 0);
                String image = data.getStringExtra(Constants.IMAGE);
                
                Uri uri = Uri.parse(image);
                
                this.name.add(name);
                this.price.add(String.valueOf(price));
                //this.amount.add(String.valueOf(amount));
                this.image.add(String.valueOf(uri));
                
                card_id.put(card_id.size() + 1, dataCenter.getLastId());
                productAdapter.notifyItemInserted(productAdapter.getItemCount());
                recreate();
            }
        }
    }
}
