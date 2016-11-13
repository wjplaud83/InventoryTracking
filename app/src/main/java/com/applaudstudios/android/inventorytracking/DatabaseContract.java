package com.applaudstudios.android.inventorytracking;

import android.provider.BaseColumns;

/**
 * Created by wjplaud83 on 11/13/16.
 */

public final class DatabaseContract {
    // to prevent someone from accidentally entering into the contract class,
    // and give it to an emppty constructor.
    public DatabaseContract() {
    }

    // Inner class that defines the table of contents
    public static abstract class Products implements BaseColumns {
        public static final String TABLE_NAME = "products";
        public static final String COLUMN_NAME_NULLABLE = null;
        public static final String COLUMN_NAME_PRODUCT_NAME = "name";
        public static final String COLUMN_NAME_PRODUCT_PRICE = "price";
        public static final String COLUMN_NAME_PRODUCT_QUANTITY = "quantity";
        public static final String COLUMN_NAME_PRODUCT_SUPPLIER = "supplier";
        public static final String COLUMN_NAME_PRODUCT_IMAGEURI = "imageuri";
    }
}
