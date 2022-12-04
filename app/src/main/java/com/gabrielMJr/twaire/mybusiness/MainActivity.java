package com.gabrielMJr.twaire.mybusiness;

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
import android.widget.Toast;
import com.gabrielMJr.twaire.mybusiness.AddNewProductActivity;
import com.gabrielMJr.twaire.mybusiness.data_manager.ProductDataCenter;
import com.gabrielMJr.twaire.mybusiness.data_manager.ProductDatabase;
import com.gabrielMJr.twaire.mybusiness.util.MyAdapter;
import com.gabrielMJr.twaire.mybusiness.util.RecyclerViewInterface;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements RecyclerViewInterface
{

    // Go to add item button
    private static Button add_new_product;

    // Products array
    protected ArrayList<String> name;
    protected ArrayList<String> price;
    private ArrayList<String> amount;
    protected ArrayList<Uri> image;
    
    private HashMap<Integer, Integer> card_id;

    // Recycler and adapter view
    private RecyclerView productRecycler;
    private MyAdapter productAdapter;

    // Product data center
    private ProductDataCenter dataCenter;
    private ProductDatabase productDB;

    // Popup menu
    private PopupMenu product_options_menu;

    private void initialize()
    {
        add_new_product = findViewById(R.id.add_new_product);
        productRecycler = findViewById(R.id.productsRecyclerView);

        name = new ArrayList<>();
        price = new ArrayList<>();
        amount = new ArrayList<>();
        image = new ArrayList<>();
        card_id = new HashMap<>();

        productAdapter = new MyAdapter(getApplicationContext(), name, price, amount, image, this);
        dataCenter = new ProductDataCenter(getApplicationContext());
        productDB = new ProductDatabase(getApplicationContext());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();

        // Setting adapter into the recycler
        productRecycler.setAdapter(productAdapter);
        productRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        displayDate();

        // Add new product button
        add_new_product.setOnClickListener(
            new OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    startActivity(new Intent(getApplicationContext(), AddNewProductActivity.class)); 
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
            productRecycler.setActivated(false);
            add_new_product.setActivated(false);
        }
        else
        {
            for (int i = 0; i < dataCenter.getProductsIndex(); i++)
            {
                name.add(dataCenter.getName((Integer)id.get(i)));
                price.add(String.valueOf(dataCenter.getPrice((Integer)id.get(i))));
                amount.add(String.valueOf(dataCenter.getAmount((Integer)id.get(i))));
                image.add(Uri.parse(String.valueOf(dataCenter.getImage(id.get(i)))));
                card_id.put(i, (Integer)id.get(i));
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

    private void deleteProduct(int position)
    {      
         
        int i = position;     
        card_id.remove(i + 1);
        
        int id = card_id.get(position);
    
        if (productDB.deleteProduct(id) && dataCenter.deleteImage(id))
        {
            for (int j = position; j < this.name.size(); j++)
            {
                card_id.put(i, card_id.get(i + 1));
                i++;
            }
            Toast.makeText(getApplicationContext(), getText(R.string.deleted) , Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "didnt work", Toast.LENGTH_SHORT).show();
        }
    }
}
