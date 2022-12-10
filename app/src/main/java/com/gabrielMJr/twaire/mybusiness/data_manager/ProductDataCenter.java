package com.gabrielMJr.twaire.mybusiness.data_manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import com.gabrielMJr.twaire.mybusiness.data_manager.ProductDatabase;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class ProductDataCenter extends AppCompatActivity
{
    // Context
    private Context context;

    // Product database
    private ProductDatabase product_data_base;

    // product preffix
    protected static final String PRODUCT = "product_";

    // Home folder
    public static final String home = "files";

    // Images folder
    public static final String image_dir = "products_pics";

    // pwd = actual directory
    public String pwd;
    
    // Format of stored images
    private String IMAGE_SUFFIX = ".bmp";
    
    private int lastId;

    // Constructor
    public ProductDataCenter(Context context)
    {
        this.context = context;
        pwd = context.getApplicationInfo().dataDir;
        product_data_base = new ProductDatabase(context);
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
    public boolean addProduct(String name, float price, int initial_amount, BitmapDrawable image)
    {
        // last Id of avaliable products
        int lastId = product_data_base.getLastID();
        
        // If data center stored the value
        if (product_data_base.addNewProduct(name, price, initial_amount))
        {
            // Total avaliable + 1
            lastId = product_data_base.getLastID();
            this.lastId = lastId;

            // Saving the image
            addImage(image, lastId);

            // If database stored return true, else false
            return true;
        }
        else
        {
            return false;
        }
    }

    // Save image method
    public void addImage(BitmapDrawable drawable, int lastId)
    {
        // Converting from bitmalDrawable to bitmal
        Bitmap bitmap = drawable.getBitmap();

        // File contains imageDir/imageFile
        File file = new File(pwd + "/" + home + "/" + image_dir, PRODUCT + lastId + IMAGE_SUFFIX);
        try
        {        
            // Compressing the image
            FileOutputStream output = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 10, output);
           
            try
            {
                output.flush();
                output.close();
            }
            catch (IOException e)
            {}
        }
        catch (FileNotFoundException e)
        {}
    }
    
    // Get image path as String 
    public String getImagePath(int Id)
    {
        return pwd + "/" + home + "/" + image_dir + "/" + PRODUCT + Id + IMAGE_SUFFIX;
    }
    
    // Delete image method
    public Boolean deleteImage(int id)
    {
        File file = new File(pwd + "/" + home + "/" + image_dir + "/" + PRODUCT + id + IMAGE_SUFFIX);
        
        return file.delete();
    }

    // Check for product existing using name
    public boolean checkByName(String name)
    {
        if (product_data_base.getID(name) > 0)
        {
            return true;
        }

        return false;
    }


    // Getting product name using index
    public String getName(int id)
    {
        return product_data_base.getName(id);
    }

    // Getting product price using id
    public float getPrice(int id)
    {
        return product_data_base.getPrice(id);
    }

    // Getting the product amount using id
    public int getAmount(int id)
    {
        return product_data_base.getAmount(id);
    }

    // Getting product image using index
    public Uri getImage(int id)
    {
        File file = new File(pwd + "/" + home + "/" + image_dir, PRODUCT + id + ".bmp");

        //Drawable bmp = Drawable.createFromPath(path);
        return Uri.fromFile(file);

    }

    // Getting total ids from products
    public ArrayList<Integer> getIDs()
    {
        return product_data_base.getIDs();
    }


    // Getting total avaliable product on data
    public int getProductsIndex()
    {
        int indexes = product_data_base.getTotalIndex();     
        return (indexes == -1) ? 0: indexes;
    }

    // Is data empty method
    public Boolean isDataCenterEmpty()
    {
        return product_data_base.isProductDBEmpty();
    }

    // Setting index before initialize product db
    public void setIndex(Context context, int index)
    {
        initializePDB(context, index);
    }

    // Initializing product database
    public void initializePDB(Context context, int index)
    {
        product_data_base = new ProductDatabase(context);
    }
    
    // Get product database class
    public ProductDatabase getProductDB()
    {
        return product_data_base;
    }
    
    // Get last id
    public int getLastId()
    {
        return this.lastId;
    }
}
