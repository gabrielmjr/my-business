package com.gabrielMJr.twaire.mybusiness;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.gabrielMJr.twaire.mybusiness.data_manager.ProductDataCenter;
import com.gabrielMJr.twaire.mybusiness.R;

public class AddNewProductActivity extends AppCompatActivity
{

    // Attributes
    // Final int for result of image picker
    private static final int IMAGE_PICKER_CODE = 100;
    private static final int IMAGE_TAKE_CODE = 101;
    
    // Camera permission code
    private static final int CAMERA_PERM_CODE = 102;

    // Intent for picker and taker
    private static Intent pick_take_Image;

    // Boolean with camera permission
    private static Boolean hasCameraPerm;
    
    // Data center variable
    private static ProductDataCenter dataCenter;

    private static ImageView add_new_product_image;
    private static EditText add_new_product_name;
    private static EditText add_new_product_price;
    
    // Buttons of the dialog
    private static Button pick_from_gallery;
    private static Button take_picture;
    private static Button cancel;
    
    // Add product button
    private static Button add_new_product_button;
    
    private static PackageManager pm;

    // Alert dialog of image chooser components
    private static AlertDialog.Builder builder;
    private static AlertDialog dialog;   
    private static View dialog_view;

    //private static String path;
    private static Uri imageUri;

    // Initializing
    private void initialize()
    {
        dataCenter = new  ProductDataCenter(getApplicationContext());
        
        add_new_product_image = findViewById(R.id.add_new_product_image);
        add_new_product_name = findViewById(R.id.add_new_product_name);
        add_new_product_price = findViewById(R.id.add_new_product_price);    
        add_new_product_button = findViewById(R.id.add_new_product_button);
        
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_product);
        initialize();

        add_new_product_image.setOnClickListener(
            new OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    pickImage();
                }
            });
            
        add_new_product_button.setOnClickListener(
            new OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    addProduct();
                }
            });
    }

    // Add product method
    private void addProduct()
    {
        String product = add_new_product_name.getText().toString();
        float price = Float.valueOf(add_new_product_price.getText().toString());
        dataCenter.addProduct(product, price);
    }

    // Take/ pick image method
    private void pickImage()
    {
        pm = getPackageManager();

        // if sdk version >= 23, check and ask camera permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            // Boolean with camera permission value
            hasCameraPerm = getPackageManager().checkPermission(Manifest.permission.CAMERA, getPackageName()) == PackageManager.PERMISSION_GRANTED;
        }
        
        // Else, hasCameraPerm will be true
        else
        {
            hasCameraPerm = true;
        }
        
        // Builder for the dialog
        builder = new AlertDialog.Builder(this);
        dialog_view = getLayoutInflater().inflate(R.layout.image_chooser_custom_dialog, null);
        
        // Initialze the buttons
        initializeChooserPicDialog();
        
        // Setting tittle
        builder.setTitle(R.string.load_picture);
        
        // Setting view and creating the dialog
        builder.setView(dialog_view);
        dialog = builder.create();
        dialog.show();
        
        // OnClick of each button
        // Pick from gallery
        pick_from_gallery.setOnClickListener(
            new OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    dialog.dismiss();
                    pick_take_Image = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pick_take_Image, IMAGE_PICKER_CODE);
                }                          
            });
            
            // Take using camera
            take_picture.setOnClickListener(
                new OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        dialog.dismiss();

                        // Check for camera permissiom
                        if (hasCameraPerm)
                        {
                            pick_take_Image = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(pick_take_Image, IMAGE_TAKE_CODE);
                        }

                        // Ask permission
                        else
                        {
                            askCameraPermission();
                        }
                    }
                });
                
                cancel.setOnClickListener(
                    new OnClickListener()
                    {
                        @Override
                        public void onClick(View view)
                        {
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), getText(R.string.canceled_by_user), Toast.LENGTH_SHORT).show();                       
                        }
                    });              
        }
                  
        
    // Initialize the byttons of image chooser dialog
    private static void initializeChooserPicDialog()
    {
        pick_from_gallery = dialog_view.findViewById(R.id.pick_from_gallery);
        take_picture = dialog_view.findViewById(R.id.take_picture);
        cancel = dialog_view.findViewById(R.id.cancel);
    }

    // Asking camera permission
    private void askCameraPermission()
    {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
    }
    
       
    // On intent result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        
        // Check if it was done
        if (resultCode == RESULT_OK)
        {
            if (requestCode == IMAGE_PICKER_CODE
                || 
                requestCode == IMAGE_TAKE_CODE)
                {
                    add_new_product_image.setImageURI(data.getData());
                }
        }
        
        // Else, show an toast
        else if (resultCode == RESULT_CANCELED)
        {
            Toast.makeText(getApplicationContext(), getText(R.string.canceled_by_user), Toast.LENGTH_SHORT).show();
        }
    }
}
