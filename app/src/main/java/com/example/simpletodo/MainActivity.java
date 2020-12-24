package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    ArrayList<String> items;

    Button btnAdd;
    EditText etItem;
    RecyclerView rvItems;

    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        etItem = findViewById(R.id.etItem);
        rvItems = findViewById(R.id.rvItems);

        etItem.setText("I'm doing this from java");

        loadItems();
        //items = new ArrayList<>();
        //items.add("Buy Milk");
        //items.add("Go to Gym");
        //items.add("Have Fun!");

        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                // Delete the item
                items.remove(position);
                // Notify the adapter
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item Removed", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };


        // At this point we know what position where the thing has been long pressed
        itemsAdapter = new ItemsAdapter(items, onLongClickListener);
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        // oOnclick listener for the button implimentation

        btnAdd.setOnClickListener(new View.OnClickListener() {

            // When someone hits our button
            @Override
            public void onClick(View v) {

                // Returns the string version of the user input
                String todoItem = etItem.getText().toString();
                // Add item to the model
                items.add(todoItem);
                // Notify the adapter that an item was added
                itemsAdapter.notifyItemInserted(items.size() - 1);
                // Clears the buffer
                etItem.setText("");

                // A Toast a notification that something has occurred
                Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });

    }
    
    private File getDataFile() {
        return new File(getFilesDir(), "data.txt");
    }
    
    // Reads the data file to load correct items
    private void loadItems() {
        try {
            items = new ArrayList<String>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            //e.printStackTrace();
            Log.e("MainActivity", "Error reading item", e);
            items = new ArrayList<>();
        }
    }
    // This function saves items to the file for future storage
    private void  saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            //e.printStackTrace();
            Log.e("MainActivity", "Error writing items", e);
        }
    }
    
}