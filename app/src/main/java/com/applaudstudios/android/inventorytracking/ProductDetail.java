package com.applaudstudios.android.inventorytracking;

import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by wjplaud83 on 11/13/16.
 */

public class ProductDetail extends AppCompatActivity implements BasicDialogFragment.BasicDialogListener {

    EditText addItemsField;
    EditText subtractItemsField;

    int productId;
    String productName;
    String productImagePath;
    float productPrice;
    String productSupplier;
    int productQuantity;

    TextView detailProductQuantity;

    Product detailProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        int queryId = getIntent().getIntExtra("id", -1);

        productAssignment(queryId);

        addItemsField = (EditText) findViewById(R.id.dt_plus_text);
        subtractItemsField = (EditText) findViewById(R.id.dt_sub_text);

        ImageView detailProductImage = (ImageView) findViewById(R.id.dt_pro_image);
        TextView detailProductName = (TextView) findViewById(R.id.dt_pro_name);
        TextView detailProductPrice = (TextView) findViewById(R.id.dt_pro_price);
        detailProductQuantity = (TextView) findViewById(R.id.dt_pro_quantity);
        TextView detailProductSupplier = (TextView) findViewById(R.id.dt_pro_supplier);

        detailProductImage.setImageBitmap(ImageTools.imageProcess(productImagePath));
        detailProductName.setText(productName);
        detailProductSupplier.setText(this.getString(R.string.supplied_by) + productSupplier);
        detailProductQuantity.setText(this.getString(R.string.quantity) + String.valueOf(productQuantity));
        detailProductPrice.setText(getString(R.string.unit_price) + priceFormat(productPrice));
    }

    public static String priceFormat(float price) {
        if (price == (long) price)
            return String.format("%d", (long) price);
        else
            return String.format("%s", price);
    }

    private void productAssignment(int queryId) {
        DatabaseHelper dbHelp = new DatabaseHelper(this);
        detailProduct = dbHelp.getProduct(queryId);

        productId = detailProduct.getId();
        productName = detailProduct.getName();
        productImagePath = detailProduct.getImageUri();
        productPrice = detailProduct.getPrice();
        productSupplier = detailProduct.getSupplier();
        productQuantity = detailProduct.getQuantity();
    }

    public void deleteProduct(View view) {
        DialogFragment newFragment = new BasicDialogFragment();
        newFragment.show(getFragmentManager(), "delete");
    }

    public void orderMore(View view) {
        String[] emails = new String[]{getString(R.string.email_address)};
        String subject = getString(R.string.request_for_a_new) + productName
                + getString(R.string.shipment);
        String text = getString(R.string.hello) + getString(R.string.request_main)
                + productName + getString(R.string.units) + productSupplier + ".";

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, emails);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void backToList(View view) {
        this.finish();
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        DatabaseHelper dbHelp = new DatabaseHelper(this);
        dbHelp.deleteProduct(detailProduct.getId());
        this.finish();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        //Nothing here...
    }

    public void addItems(View view) {
        int quantity = productQuantity;
        String input = addItemsField.getText().toString();

        if (input.isEmpty()) {
            Toast.makeText(this, R.string.empty_add, Toast.LENGTH_LONG).show();
        } else {

            int extraQuantity = Integer.valueOf(input);
            DatabaseHelper dbHelp = new DatabaseHelper(this);
            dbHelp.updateQuantity(productId, quantity + extraQuantity);
            productAssignment(productId);
            detailProductQuantity.setText(this.getString(R.string.quantity) + String.valueOf(productQuantity));

        }

    }

    public void removeItems(View view) {
        int quantity = productQuantity;
        String input = subtractItemsField.getText().toString();

        if (input.isEmpty()) {
            Toast.makeText(this, R.string.empty_sub, Toast.LENGTH_LONG).show();
        } else {
            int extraQuantity = Integer.valueOf(input);
            if (extraQuantity > quantity) {
                Toast.makeText(this, R.string.larger_than_current, Toast.LENGTH_LONG).show();
                return;
            }

            DatabaseHelper dbHelp = new DatabaseHelper(this);
            dbHelp.updateQuantity(productId, quantity - extraQuantity);
            productAssignment(productId);
            detailProductQuantity.setText(this.getString(R.string.quantity) + String.valueOf(productQuantity));

        }
    }
}
