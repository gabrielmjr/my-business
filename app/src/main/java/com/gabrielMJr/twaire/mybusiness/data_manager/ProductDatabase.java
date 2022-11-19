package com.gabrielMJr.twaire.mybusiness.data_manager;

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import com.gabrielMJr.twaire.mybusiness.data_manager.ProductDataCenter;

public class ProductDatabase extends SQLiteOpenHelper
{
    // DataBase components
    private static final String DB_NAME = "products";
     
    // Table name
    private static String TBN = null;
    
    // Primary key
    private static final String ID = "id";
    
    // Shared preferences id
    private static final String SPID = "spid";
    
    // Actual opened database index
    private static int intex;
    
    // Constructor
    public ProductDatabase(Context context, int product_id)
    {
        super(context, DB_NAME, null, 1);
        
        this.intex = product_id;
        
        // Table name
        this.TBN = ProductDataCenter.PRODUCT +  product_id;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE IF NOT EXISTS " 
                  + TBN
                  + "("
                  + ID 
                  + " TNTEGER PRIMARY KEY AUTOINCREMENT, "
                  + SPID
                  + " INTEGER"
                  + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TBN);
        onCreate(db);
    }
    
    // Data getters
    
}
