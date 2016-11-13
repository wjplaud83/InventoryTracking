package com.applaudstudios.android.inventorytracking;

/**
 * Created by wjplaud83 on 11/13/16.
 */


public class Product {

    private int mId;
    private String mName;
    private float mPrice;
    private int mQuantity;
    private String mSupplier;
    private String mImageUri;

    public Product(String name, float price, int quantity, String supplier, String imageUri) {
        mId = 0;
        mName = name;
        mPrice = price;
        mQuantity = quantity;
        mSupplier = supplier;
        mImageUri = imageUri;
    }

    public Product(int id, String name, float price, int quantity, String supplier, String imageUri) {
        mId = id;
        mName = name;
        mPrice = price;
        mQuantity = quantity;
        mSupplier = supplier;
        mImageUri = imageUri;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public float getPrice() {
        return mPrice;
    }

    public void setPrice(float price) {
        mPrice = price;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public void setQuantity(int quantity) {
        mQuantity = quantity;
    }

    public String getSupplier() {
        return mSupplier;
    }

    public void setSupplier(String supplier) {
        mSupplier = supplier;
    }

    public String getImageUri() {
        return mImageUri;
    }

    public void setImageId(String imageUri) {
        mImageUri = imageUri;
    }


}
