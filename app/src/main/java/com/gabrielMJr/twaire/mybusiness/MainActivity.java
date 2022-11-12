package com.gabrielMJr.twaire.mybusiness;

import android.os.Bundle;
import android.util.Log;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.content.Intent;
import com.gabrielMJr.twaire.mybusiness.AddNewProductActivity;
import com.gabrielMJr.twaire.mybusiness.data_manager.ProductDataCenter;
import java.util.ArrayList;
import android.support.v7.widget.RecyclerView;
import com.gabrielMJr.twaire.mybusiness.util.MyAdapter;
import android.support.v7.widget.LinearLayoutManager;

public class MainActivity extends AppCompatActivity {

    // Go to add item button
    private static Button add_new_product;
    
    // Products array
    private static ArrayList<String> name;
    private static ArrayList<String> price;
    
    // Recycler and adapter view
    private static RecyclerView productRecycler;
    private static MyAdapter productAdapter;
    
    // Product data center
    private static ProductDataCenter dataCenter;
    
    private void initialize()
    {
        add_new_product = findViewById(R.id.add_new_product);
        productRecycler = findViewById(R.id.productsRecyclerView);
        
        name = new ArrayList<>();
        price = new ArrayList<>();
        
        productAdapter = new MyAdapter(getApplicationContext(), name, price);
        dataCenter = new ProductDataCenter(getApplicationContext());
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
                    startActivity(new Intent(getApplicationContext(),AddNewProductActivity.class)); 
                }
            });
		
    }

    //Displaying shared preferences into the adapter
    // To display it just add values into the array list
    private void displayDate()
    {
        if (dataCenter.isDataCenterEmpty())
        {
            productRecycler.setActivated(false);
            add_new_product.setActivated(false);
        }
        for (int i = 0; i < dataCenter.getProductsIndex(); i++)
        {
            name.add(dataCenter.getName(i));
            price.add(String.valueOf(dataCenter.getPrice(i)));
        }
    }
}
