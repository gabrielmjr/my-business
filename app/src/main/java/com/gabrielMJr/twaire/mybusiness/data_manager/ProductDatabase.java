package com.gabrielMJr.twaire.mybusiness.data_manager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

public class ProductDatabase
{
    // Attributes
    // DataBase components
    private final String DB_NAME = "products.db";

    // Table name
    private String TBN = "actual_products";
    
    // Primary key && anothers
    private final String COL_1 = "id";
    private final String COL_2 = "name";
    private final String COL_3 = "price";
    private final String COL_4 = "amount";
  
    // Actual opened database index
    private int index;
    
    private Context context;
    
    private SQLiteDatabase db;

    // Constructor
    public ProductDatabase(Context context)
    {
        this.context = context;
        
        try 
        {
            db = context.openOrCreateDatabase(DB_NAME, context.MODE_PRIVATE, null); 
            createTable();
        }
        catch (Exception e)
        {
            Log.e("database_error", e.getMessage());
            Toast.makeText(context, "Something is wrong", Toast.LENGTH_SHORT).show();
        }
    }
    
    // Create new table
    private void createTable()
    {
        db.execSQL("CREATE TABLE IF NOT EXISTS "
                  + TBN + "("
                  + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                  + COL_2 + " TEXT, "
                  + COL_3 + " REAL, "
                  + COL_4 + " INTEGER)");
    }

    // Add products
    protected Boolean addNewProduct(String name, float price, int amount)
    {
        try
        {
            db.execSQL("INSERT INTO " 
                      + TBN + "("
                      + COL_2 + ","
                      + COL_3 + ","
                      + COL_4 + ") VALUES ('"
                      + name + "', "
                      + price + ", "
                      + amount + ")");
            return true;
        }
        catch (Exception error)
        {
            Toast.makeText(context, "Wrong thing", Toast.LENGTH_SHORT).show();
            Log.e("database_error", error.getMessage());
            return false;
        }
    }   
    
    // Check product by name
    public Boolean checkByName(String name)
    {
        return false;
    }
    
    // setters and getters
    public int getIndex()
    {
        return index;
    }
    
    public void setIndex(int index)
    {
        this.index = index;
    }
    
    // Get total availables products
    protected int getTotalIndex()
    {
        Cursor cursor = db.rawQuery("SELECT * FROM " + TBN, null);
        return cursor.getCount();
    }
    
    // Is database empty
    protected Boolean isProductDBEmpty()
    {
        Cursor cursor = db.rawQuery("SELECT * FROM " + TBN, null);
        
        Boolean isEmpty = true;
        
        while (cursor.moveToNext())
        {
            isEmpty = false;
        }
        
        return isEmpty;
    }
}
