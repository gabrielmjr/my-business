package com.gabrielMJr.twaire.mybusiness.data_manager;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import com.gabrielMJr.twaire.mybusiness.data_manager.DatabaseManager;

public class ProductDataCenter extends AppCompatActivity
{
    // Context
    private static Context context;

    // Shared preferences attr
    private static SharedPreferences product_id;

    // product preffix
    private static final String PRODUCT = "product_";

    // Product descriptions
    private static final String NAME = "name";
    private static final String PRICE = "price";

    // Shared preferences editor
    private static SharedPreferences.Editor editor;

    // Database manager class
    private static DatabaseManager dbm;

    // Constructor
    public ProductDataCenter(Context context)
    {
        this.context = context;
        dbm = new DatabaseManager(context);
    }

    // add product and return boolean value
    public boolean addProduct(String name, float price)
    {
        int lastIndex = dbm.getTotalIndex();

        product_id = context.getSharedPreferences(PRODUCT + lastIndex, 0);    
        editor = product_id.edit();
        editor.putString(NAME, name);
        editor.putFloat(PRICE, price);

        // If data center stored the value
        if (editor.commit())
        {
            lastIndex++;
            // If database stored
            if (dbm.addTotalIndex(lastIndex))
            {
                return true;
            }
            else
            {
                return false;
            }
           
        }
        else
        {
            return false;
        }
    }
}
