package com.applaudstudios.android.inventorytracking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wjplaud83 on 11/13/16.
 */

public class ProductActivity extends AppCompatActivity {

    private List<Product> mProductList = new ArrayList<>();
    private DatabaseHelper dbHelp;
    private RecyclerView mRecyclerView;
    private ProductAdapter mAdapter;
    private TextView productCountTextView;
    TextView emptyListTextView;
    private int productCount;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.applaudstudios.android.inventorytracking.R.layout.activity_product);

        dbHelp = new DatabaseHelper(this);

        //dbHelp.deleteDatabase();

        //Funny how the database was not regenerated everytime... delete everything! (while testing)
        //dbHelp.deleteAllData();

        mProductList = dbHelp.getAllProducts();

        emptyListTextView = (TextView) findViewById(com.applaudstudios.android.inventorytracking.R.id.empty_list_text_view);
        checkEmptyList();

        productCountTextView = (TextView) findViewById(com.applaudstudios.android.inventorytracking.R.id.pro_count);
        updateProductCount();

        // Initialize recycler view
        mRecyclerView = (RecyclerView) findViewById(com.applaudstudios.android.inventorytracking.R.id.recycler_view);
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new ProductAdapter(ProductActivity.this, mProductList);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                mAdapter.setProductList(dbHelp.getAllProducts());
                checkEmptyList();
                updateProductCount();
            case 2:
                mAdapter.setProductList(dbHelp.getAllProducts());
                checkEmptyList();
                updateProductCount();
        }
    }

    public void updateProductCount() {
        productCountTextView.setText(getString(R.string.products) + Integer.toString(mProductList.size()));
    }

    public void checkEmptyList() {
        TextView emptyListTextView = (TextView) findViewById(com.applaudstudios.android.inventorytracking.R.id.empty_list_text_view);
        if (mProductList.size() > 0){
            emptyListTextView.setVisibility(View.GONE);
        }
        else {
            emptyListTextView.setVisibility(View.VISIBLE);
        }
    }

    public void openNewProductActivity(View view) {
        /*
        The Add Product button in the other activity adds the product to the db and also, closes
        that activity. That triggers the onActivityResult here, which updates the data in the
        Adapter... it works, not sure if it is the best way to do it though. I wanted to trigger
        it straight from the other activity... but no idea how to get a reference to the adapter
        over there.
        */
        startActivityForResult(new Intent(ProductActivity.this, ProductCreation.class), 1);
    }

}
