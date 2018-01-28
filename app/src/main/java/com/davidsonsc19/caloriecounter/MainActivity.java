package com.davidsonsc19.caloriecounter;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView tv = findViewById(R.id.counter);
        tv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Util.usingDate = new Date();
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                startActivity(intent);
                return true;
            }
        });
        tv.setText(getCurrentCount() + " calories");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, BackstageActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // TODO: Pull item retrieval into Util
    public int getCurrentCount() {
        String existingData = "";
        String filename = Util.getFilename(new Date());

        File todaysFile = getBaseContext().getFileStreamPath(filename);
        if (todaysFile.exists()) {
            try {
                FileInputStream fis = openFileInput(filename);
                byte[] bytes = new byte[(int)todaysFile.length()];
                fis.read(bytes);
                existingData = new String(bytes);
                fis.close();
            } catch (IOException e) {
                return -1;
            }
        }

        // Note: this also finds the maxItemID.
        int total = 0;
        int maxItemID = -1;
        if (existingData.length() > 0) {
            String[] itemStrings = existingData.split("%20%");
            for (int i = 1; i < itemStrings.length; i++) {
                Item currentItem = new Item(itemStrings[i]);
                total += currentItem.calories;

                if (maxItemID < currentItem.itemID)
                   maxItemID = currentItem.itemID;
            }
        }
        Util.setCurrentItemID(maxItemID);
        return total;
    }
}
