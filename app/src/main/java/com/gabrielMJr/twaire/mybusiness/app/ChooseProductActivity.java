package com.gabrielMJr.twaire.mybusiness.app;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.gabrielMJr.twaire.mybusiness.R;
import com.gabrielMJr.twaire.mybusiness.data_manager.ProductDataCenter;
import com.gabrielMJr.twaire.mybusiness.util.ChooseProductAdapter;
import java.util.ArrayList;

public class ChooseProductActivity extends AppCompatActivity
{
    // Attributes
    // Card array list
    private ArrayList<Uri> image;
    private ArrayList<String> name;
    private ArrayList<Integer> amount;
    private ArrayList<Float> price;
    
    // Data center object
    private ProductDataCenter dataCenter;
    
    // Recycler view and Choose product adapter objects
    private RecyclerView recycler;
    private ChooseProductAdapter adapter;

    
    // Initializing the activity
    private void initialize()
    {
        // Initializing the card array list
        image = new ArrayList<>();
        name = new ArrayList<>();
        amount = new ArrayList<>();
        price = new ArrayList<>();
        
        // Getting recycler view from layout
        recycler = findViewById(R.id.choose_product_recycler_view);
        
        // Initializing new choose product adapter
        adapter = new ChooseProductAdapter(
                getApplicationContext(),
                image,
                name,
                amount,
                price);
                
        // Initializing data center object
        dataCenter = new ProductDataCenter(getApplicationContext());
    }
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_product);
        
        // Initialize the activity
        initialize();
        
        // Setting layout manager into recycler vire
        recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        
        // Setting adapter into recycler view
        recycler.setAdapter(adapter);
        
        // Display items
        displayDate();
    }
    
    
    // Displaying the items
    private void displayDate()
    {
        // Getting all ids from db
        ArrayList id = dataCenter.getIDs();
        
        // If data.isEmpty
        if (dataCenter.isDataCenterEmpty())
        {
            /*
             The algorithm doesnt allow to arrive here when data is empty
             If you arrive here when is empty, the app will close propositally
            */
        }
        else
            // Else, display
        {
            for (int i = 0; i < dataCenter.getProductsIndex(); i++)
            {
                name.add(dataCenter.getName((Integer)id.get(i)));
                price.add(dataCenter.getPrice((Integer)id.get(i)));
                amount.add(dataCenter.getAmount((Integer)id.get(i)));
                image.add(dataCenter.getImage(id.get(i)));
            }
        }
    }

}
