package com.davidsonsc19.caloriecounter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class AddActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Util.usingDate = new Date();

        Button b = findViewById(R.id.add_submit);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Item newItem = getItemFromInput();
                boolean success = storeNewItem(newItem);

                if (success) {
                    Intent intent = new Intent(AddActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Snackbar.make(view, "Something went wrong.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });
    }

    public Item getItemFromInput() {
        String newItemName = ((EditText) findViewById(R.id.add_name)).getText().toString();
        int newItemCalories = Integer.parseInt(((EditText) findViewById(R.id.add_calories)).getText().toString());
        String timeStr = ((EditText) findViewById(R.id.add_timestamp)).getText().toString();

        return new Item(newItemName, newItemCalories, timeStr, new Date());
    }

    public boolean storeNewItem(Item newItem) {
        String filename = Util.getFilename(new Date());
        String existingData = "";

        File todaysFile = getBaseContext().getFileStreamPath(filename);
        if (todaysFile.exists()) {
            try {
                FileInputStream fis = openFileInput(filename);
                byte[] bytes = new byte[(int)todaysFile.length()];
                fis.read(bytes);
                existingData = new String(bytes);
                fis.close();
            } catch (IOException e) {
                return false;
            }
        }

        try {
            FileOutputStream fos = openFileOutput(filename, Context.MODE_PRIVATE);
            String data = existingData + "%20%" + newItem.toString();
            fos.write(data.getBytes());
            fos.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
