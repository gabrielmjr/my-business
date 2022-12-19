package com.gabrielMJr.twaire.mybusiness.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SearchView;
import com.gabrielMJr.twaire.mybusiness.R;
import com.gabrielMJr.twaire.mybusiness.data_manager.ProductDataCenter;
import com.gabrielMJr.twaire.mybusiness.util.ChooseProductAdapter;
import com.gabrielMJr.twaire.mybusiness.util.Constants;
import com.gabrielMJr.twaire.mybusiness.util.RecyclerViewChooseProductInterface;
import java.util.ArrayList;
import java.util.HashMap;

public class ChooseProductActivity extends AppCompatActivity implements RecyclerViewChooseProductInterface
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

    // Search view
   // private SearchView search;
    
    // This hash contains id of recycler view sycronized with card position
    private HashMap<Integer, Integer> card_id;
    
    // Return data intent
    private Intent returnData;
    
    // Initializing the activity
    private void initialize()
    {
        // Initializing the card array list
        image = new ArrayList<>();
        name = new ArrayList<>();
        amount = new ArrayList<>();
        price = new ArrayList<>();
        card_id = new HashMap<>();
        
        // Getting recycler view from layout
        recycler = findViewById(R.id.choose_product_recycler_view);
        
        // Initializing new choose product adapter
        adapter = new ChooseProductAdapter(
                getApplicationContext(),
                image,
                name,
                amount,
                price,
                this);
                
        // Initializing data center object
        dataCenter = new ProductDataCenter(getApplicationContext());
        
        // Initializing search view
      //  search = findViewById(R.id.choose_product_search);
        
        // Remove focus, when the activity start, keyboard will not be showed
       // search.clearFocus();
        
        // Initializing return data intent
        returnData = new Intent();
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
                card_id.put(i, (Integer)id.get(i));
            }
        }
    }

    // On item clicked
    @Override
    public void onItemClick(int position)
    {
        // Set id into intentent and return them
        returnData.putExtra(Constants.ID, card_id.get(position));
        setResult(RESULT_OK, returnData);
   
        // Finish the activity
        finish();
    }
}
