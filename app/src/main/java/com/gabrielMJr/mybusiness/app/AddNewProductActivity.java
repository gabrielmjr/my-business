package com.gabrielMJr.mybusiness.app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.gabrielMJr.mybusiness.R;
import com.gabrielMJr.mybusiness.data_manager.ProductDataCenter;
import com.gabrielMJr.mybusiness.util.Constants;
import com.gabrielMJr.twaire.tools.Tools;

public class AddNewProductActivity extends AppCompatActivity
{

    // Attributes
    // Camera permission code
    private final int CAMERA_PERM_CODE = 102;

    // Intent for picker and taker
    private Intent pick_take_Image;

    // Boolean with camera permission
    private Boolean hasCameraPerm;

    // Data managers variable
    private ProductDataCenter dataCenter;

    // Activity widgets
    private ImageView add_new_product_image;
    private EditText add_new_product_name;
    private EditText add_new_product_price;
    private EditText add_new_product_initial_amount;

    // Boolean verifier
    // Does have image on the image field?
    private Boolean hasImage = false;

    // Buttons of the dialog
    private Button pick_from_gallery;
    private Button take_picture;
    private Button cancel;

    // Add product button
    private Button add_new_product_button;

    // Package manager
    private PackageManager pm;

    // Alert dialog of image chooser components
    private AlertDialog.Builder builder;
    private AlertDialog dialog;   
    private View dialog_view;

    // Custom toast object
    private Toast toast;

    // Custom toast view && them components
    private View custom_toast;
    private TextView toast_status;
    private ImageView toast_icon;

    // BitmapDrawable (converteble drawable)
    private static BitmapDrawable imageS;

    // Intent with result data from request
    private Intent returnData;

    /*
     My tools package
     I used him instead of java.lang.isEmpty because:
     my tools consider space as empty
     */
    private static Tools tools;

    // Initializing
    private void initialize()
    {
        // New datacenter object
        dataCenter = new  ProductDataCenter(getApplicationContext());
        tools = new Tools();

        // New toast object, view and them attributes
        toast = new Toast(getApplicationContext());
        custom_toast = getLayoutInflater().inflate(R.layout.toast_add_item_status, null);
        toast_status = custom_toast.findViewById(R.id.toast_status);
        toast_icon = custom_toast.findViewById(R.id.toast_icon);

        // Creating neccessary folders 
        dataCenter.createHome();
        dataCenter.createImgDir();

        // Initializing return data intent
        returnData = new Intent();

        // Getting activity components
        add_new_product_name = findViewById(R.id.add_new_product_name);
        add_new_product_price = findViewById(R.id.add_new_product_price);
        add_new_product_initial_amount = findViewById(R.id.add_new_product_initial_amount);
        add_new_product_image = findViewById(R.id.add_new_product_image);
        add_new_product_button = findViewById(R.id.add_new_product_button);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_product);
        initialize();

        // Add new product image (visible while product.isEmpty())
        add_new_product_image.setOnClickListener(
            new OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    // Pick image
                    pickImage();
                }
            });

        // Add new product button 
        add_new_product_button.setOnClickListener(
            new OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    String product = add_new_product_name.getText().toString();
                    String price = add_new_product_price.getText().toString();
                    String initial_amount = add_new_product_initial_amount.getText().toString();

                    if (checkFieldsValues(product, price, initial_amount))
                    {
                        // Checks if the name already exists
                        if (dataCenter.checkByName(product))
                        {
                            Toast.makeText(getApplicationContext(), getText(R.string.product_exist), Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            // If the number is greater than 9999999, java will show you "E" ans will split the value
                            // To dont do that, actually, the limit is 9999999

                            // For price
                            if (Long.valueOf(add_new_product_price.getText().toString()) > 9999999)
                            {
                                // Show the error
                                add_new_product_price.setError(getText(R.string.very_high_value));
                                return;
                            }

                            // For amount
                            else if (Long.valueOf(add_new_product_initial_amount.getText().toString()) > 9999999)
                            {
                                // Show the error
                                add_new_product_initial_amount.setError(getText(R.string.very_high_value));
                                return;
                            }
                            
                            // Else
                            // Add product
                            addProduct(product, Float.valueOf(price), Integer.valueOf(initial_amount));
                            toast_status.setText(R.string.added_successfully);
                            toast_icon.setImageDrawable(getDrawable(R.drawable.ic_checkbox_marked_circle_outline));
                            custom_toast.setBackground(getDrawable(R.drawable.ic_done_add_product_toast));

                            // Set view, duration and show the toast
                            toast.setView(custom_toast);
                            toast.setDuration(Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                }
            });
    }

    // Check if the fields are empty
    private Boolean checkFieldsValues(String name, String price, String amount)
    {
        // Boolean verifiers
        Boolean verifyName;
        Boolean verifyPrice;
        Boolean verifyAmount;

        // Check for name
        if (tools.isNull(name))
        {
            add_new_product_name.setError(getText(R.string.empty_name));
            verifyName = false;
        }
        else
        {
            verifyName = true;
        }

        // Check for price
        if (tools.isNull(price))
        {
            add_new_product_price.setError(getText(R.string.empty_price));
            verifyPrice = false;
        }
        else
        {
            verifyPrice = true;
        }

        // Check amount
        if (tools.isNull(amount))
        {
            add_new_product_initial_amount.setError(getText(R.string.empty_amount));
            verifyAmount = false;
        }
        else
        {
            verifyAmount = true;
        }

        if (!hasImage)
        {
            // Set some values into the custom view toast
            toast_status.setText(R.string.empty_image);
            toast_icon.setImageDrawable(getDrawable(R.drawable.ic_error_outline));
            custom_toast.setBackground(getDrawable(R.drawable.ic_error_toast_1));

            // Show empty image warning
            toast.setView(custom_toast);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        }

        // Checking and returning the boolean
        return (verifyName && verifyPrice && verifyAmount && hasImage); // ? true: false;
    }

    // Add product method
    private void addProduct(String product, float price, int initial_amount)
    {
        // If data.wasAdded, finish the activity
        if (dataCenter.addProduct(product, price, initial_amount, imageS))
        {
            // Else add the product to db
            // Return datas
            returnData.putExtra(Constants.NAME, product);
            returnData.putExtra(Constants.PRICE, price);
            returnData.putExtra(Constants.AMOUNT, initial_amount);
            returnData.putExtra(Constants.IMAGE, dataCenter.getImagePath(dataCenter.getLastId()));
            setResult(RESULT_OK, returnData);
            finish();
            }

        // Else, finish activity and show message Toast
        else
        {
        // Return datas
            setResult(RESULT_CANCELED, returnData);
            finish();
            Toast.makeText(getApplicationContext(), getString(R.string.an_error_occurred_on_db), Toast.LENGTH_SHORT).show();
            }
        }

    // Take/ pick image method
    private void pickImage()
    {
    pm = getPackageManager();

        // if sdk version >= 23, check and ask camera permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
        // Boolean with camera permission boolean
            hasCameraPerm = getPackageManager().checkPermission(Manifest.permission.CAMERA, getPackageName()) == PackageManager.PERMISSION_GRANTED;
            }

        // Else, hasCameraPerm will be true
        else
        {
        hasCameraPerm = true;
            }

        // Builder for the dialog
        builder = new AlertDialog.Builder(this);
        dialog_view = getLayoutInflater().inflate(R.layout.custom_dialog_image_chooser, null);

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
                    startActivityForResult(pick_take_Image, Constants.IMAGE_PICKER_CODE);
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
                        startActivityForResult(pick_take_Image, Constants.IMAGE_TAKE_CODE);
                        }

                    // request permission
                    else
                    {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
                        }
                    }
                });

        // Cancel field on dialog
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
    private void initializeChooserPicDialog()
    {
    pick_from_gallery = dialog_view.findViewById(R.id.pick_from_gallery);
        take_picture = dialog_view.findViewById(R.id.take_picture);
        cancel = dialog_view.findViewById(R.id.cancel);
        }

    // On intent result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
    super.onActivityResult(requestCode, resultCode, data);

        // Check if it was done
        if (resultCode == RESULT_OK)
        {
        if (requestCode == Constants.IMAGE_PICKER_CODE)
            {
            add_new_product_image.setImageURI(data.getData());
                imageS = (BitmapDrawable)add_new_product_image.getDrawable();

                // Setting has image to reuse when verify values
                hasImage = true;
                }
            else if (requestCode == Constants.IMAGE_TAKE_CODE)
            {
            add_new_product_image.setImageBitmap((Bitmap)data.getExtras().get("data"));
                imageS = (BitmapDrawable)add_new_product_image.getDrawable();
                hasImage = true;
                }
            }

        // Else, show an toast
        else if (resultCode == RESULT_CANCELED)
        {
        Toast.makeText(getApplicationContext(), getText(R.string.canceled_by_user), Toast.LENGTH_SHORT).show();
            hasImage = false;
            }
        }
    }
