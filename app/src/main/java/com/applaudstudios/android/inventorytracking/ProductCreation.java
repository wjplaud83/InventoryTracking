package com.applaudstudios.android.inventorytracking;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wjplaud83 on 11/13/16.
 */

public class ProductCreation extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    String mCurrentPhotoPath;

    private EditText productNameView;
    private EditText productPriceView;
    private EditText productQuantityView;
    private EditText productSupplierView;
    private ImageView productImage;
    private boolean imageAdded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_creation);

        imageAdded = false;

        productNameView = (EditText) findViewById(R.id.edit_pro_name);
        productPriceView = (EditText) findViewById(R.id.edit_pro_price);
        productQuantityView = (EditText) findViewById(R.id.edit_pro_quantity);
        productSupplierView = (EditText) findViewById(R.id.edit_pro_supplier);

        productImage = (ImageView) findViewById(R.id.add_image);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            imageAdded = true;
            productImage.setImageBitmap(ImageTools.imageProcess(mCurrentPhotoPath));

        }
    }

    public void addProduct(View view) {

        String productName = productNameView.getText().toString();
        String productPrice = productPriceView.getText().toString();
        String productQuantity = productQuantityView.getText().toString();
        String productSupplier = productSupplierView.getText().toString();

        if (productName.isEmpty() || productPrice.isEmpty() || productQuantity.isEmpty() || productSupplier.isEmpty() || !imageAdded) {
            Toast.makeText(this, R.string.empty_warning, Toast.LENGTH_LONG).show();
        } else {
            DatabaseHelper dbHelp = new DatabaseHelper(this);

            dbHelp.addProduct(
                    -1,
                    productName,
                    Float.valueOf(productPrice),
                    Integer.valueOf(productQuantity),
                    productSupplier,
                    mCurrentPhotoPath);

            this.finish();
        }

    }

    public void addImage(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }
}
