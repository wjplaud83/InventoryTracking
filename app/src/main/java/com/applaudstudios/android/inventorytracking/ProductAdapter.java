package com.applaudstudios.android.inventorytracking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wjplaud83 on 11/13/16.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context mContext;
    private List<Product> mProductList = new ArrayList<>();

    public ProductAdapter(Context context, List<Product> productList) {
        mContext = context;
        mProductList = productList;
    }

    //Not sure if this one will work on a Recycler View
    public void setProductList(List<Product> productList) {
        mProductList.clear();
        mProductList.addAll(productList);
        notifyDataSetChanged();
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(com.applaudstudios.android.inventorytracking.R.layout.product_list, parent, false);

        return new ProductViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {

        Product currentProduct = mProductList.get(position);

        /*
        This is way better, and the lost frames warnings are gone. Although, I still think that
        the whole image is getting reloaded every time the Sell button is pressed, only difference
        is that I don't see it slowing down the whole app.
        */
        loadBitmap(currentProduct.getImageUri(), holder.proImage);

        holder.proName.setText(currentProduct.getName());
        holder.proSupplier.setText(mContext.getString(com.applaudstudios.android.inventorytracking.R.string.supplied_by) + currentProduct.getSupplier());
        holder.proQuantity.setText(mContext.getString(com.applaudstudios.android.inventorytracking.R.string.quantity) + String.valueOf(currentProduct.getQuantity()));
        holder.proPrice.setText(mContext.getString(com.applaudstudios.android.inventorytracking.R.string.unit_price) + priceFormat(currentProduct.getPrice()));

    }

    public static String priceFormat(float price) {
        if (price == (long) price)
            return String.format("%d", (long) price);
        else
            return String.format("%s", price);
    }

    @Override
    public int getItemCount() {
        return (null != mProductList ? mProductList.size() : 0);
    }

    private void productSale(int position) {
        Product product = mProductList.get(position);
        int quantity = product.getQuantity();

        if (quantity > 0) {
            DatabaseHelper dbHelp = new DatabaseHelper(mContext);
            dbHelp.updateQuantity(product.getId(), quantity - 1);

            //I hate this so much... all I want to update is one textview...
            mProductList.clear();
            mProductList.addAll(dbHelp.getAllProducts());
            notifyDataSetChanged();

        } else {
            Toast.makeText(mContext, com.applaudstudios.android.inventorytracking.R.string.no_inventory, Toast.LENGTH_SHORT).show();
        }
    }

    private void openDetailView(int position) {
        Intent intent = new Intent(mContext, ProductDetail.class);
        Product product = mProductList.get(position);
        int productId = product.getId();
        intent.putExtra("id", productId);
        ((Activity) mContext).startActivityForResult(intent, 2);
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected ImageView proImage;
        protected TextView proName;
        protected TextView proSupplier;
        protected TextView proQuantity;
        protected TextView proPrice;
        protected Button sellButton;

        public ProductViewHolder(View view) {
            super(view);
            sellButton = (Button) view.findViewById(com.applaudstudios.android.inventorytracking.R.id.sell_button);
            proImage = (ImageView) view.findViewById(com.applaudstudios.android.inventorytracking.R.id.pro_image);
            proName = (TextView) view.findViewById(com.applaudstudios.android.inventorytracking.R.id.pro_name);
            proSupplier = (TextView) view.findViewById(com.applaudstudios.android.inventorytracking.R.id.pro_supplier);
            proQuantity = (TextView) view.findViewById(com.applaudstudios.android.inventorytracking.R.id.pro_quantity);
            proPrice = (TextView) view.findViewById(com.applaudstudios.android.inventorytracking.R.id.pro_price);
            sellButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    productSale(getLayoutPosition());
                }
            });
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            openDetailView(getLayoutPosition());
        }
    }

    public void loadBitmap(String imagePath, ImageView imageView) {
        BitmapWorkerTask task = new BitmapWorkerTask(imageView);
        task.execute(imagePath);
    }

    class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        private String imagePath;

        public BitmapWorkerTask(ImageView imageView) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(String... params) {
            imagePath = params[0];
            return ImageTools.imageProcess(imagePath);
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }

}
