package com.gabrielMJr.twaire.mybusiness.data_manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import com.gabrielMJr.twaire.mybusiness.data_manager.DatabaseManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import android.graphics.drawable.Drawable;

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
    private static final String IMAGE = "uri_image";

    // Shared preferences editor
    private static SharedPreferences.Editor editor;

    // Database manager class
    private static DatabaseManager dbm;

    // Home folder
    public static final String home = "files";

    // Images folder
    public static final String image_dir = "products_pics";

    // pwd = actual directory
    public static String pwd; 

    // Constructor
    public ProductDataCenter(Context context)
    {
        this.context = context;
        dbm = new DatabaseManager(context);
        pwd = context.getApplicationInfo().dataDir;
    }

    // Creating if not exists local private dir for the app
    public boolean createHome()
    {
        File file = new File(pwd + "/" + home);

        if (!file.exists())
        {
            // Return true if dir was created
            return file.mkdir();
        }
        else
        {
            return false;
        }
    }

    // Folder for the picked images
    public boolean createImgDir()
    {
        String pwd = context.getApplicationInfo().dataDir;
        File file = new File(pwd + "/" + home + "/" + image_dir);

        // If the folder already exists
        if (file.exists())
        {
            return false;
        }

        // If not exists
        else
        {
            file.mkdir();
            return true;
        }
    }


    // add product and return boolean value
    public boolean addProduct(String name, float price, BitmapDrawable image)
    {
        // Total index of avaliable products
        int lastIndex = dbm.getTotalIndex();

        product_id = context.getSharedPreferences(PRODUCT + lastIndex + ".bmp", 0);    
        editor = product_id.edit();

        // Adding into sharedPreferences
        editor.putString(NAME, name);
        editor.putFloat(PRICE, price);

        // Saving the image
        addImage(image, lastIndex);

        // If data center stored the value
        if (editor.commit())
        {
            // Total avaliable + 1
            lastIndex++;

            // If database stored return true, else false
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

    // Save image method
    public void addImage(BitmapDrawable drawable, int lastIndex)
    {
        // Converting from bitmalDrawable to bitmal
        Bitmap bitmap = drawable.getBitmap();

        // File contains imageDir/imageFile
        File file = new File(pwd + "/" + home + "/" + image_dir, PRODUCT + lastIndex + ".bmp");
        try
        {        
            // Compressing the image
            FileOutputStream output = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);

            try
            {
                output.flush();
            }
            catch (IOException e)
            {}

            try
            {
                output.close();
            }
            catch (IOException e)
            {}
        }
        catch (FileNotFoundException e)
        {}
    }



    // Getting product name using index
    public String getName(int index)
    {
        product_id = context.getSharedPreferences(PRODUCT + index, 0);
        return product_id.getString(NAME, null);
    }

    // Getting product price using index
    public float getPrice(int index)
    {
        product_id = context.getSharedPreferences(PRODUCT + index, 0);
        return product_id.getFloat(PRICE, 0.0f);
    }

    // Getting product image using index
    public Bitmap getImage(int index)
    {
        File path = new File( + "/" + home + "/" + image_dir + "/" + PRODUCT + index);

        //InputStream inputStream = new FileInputStream(path);
        Bitmap bmp = BitmapFactory.decodeFile(path, BitmapFactory;

        //Drawable bmp = Drawable.createFromPath(path);
        return bmp;
    }

    // Getting total avaliable product on data
    public int getProductsIndex()
    {
        return dbm.getTotalIndex();
    }

    // Is data empty method
    public Boolean isDataCenterEmpty()
    {
        return dbm.isDataCenterEmpty();
    }
}
