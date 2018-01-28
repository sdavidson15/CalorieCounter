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

public class EditActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Item editItem = getItemForEditting();
        ((EditText) findViewById(R.id.edit_name)).setHint(editItem.name);
        ((EditText) findViewById(R.id.edit_calories)).setHint(String.valueOf(editItem.calories));
        ((EditText) findViewById(R.id.edit_timestamp)).setHint(editItem.time);

        Button submit = findViewById(R.id.edit_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (storeNewItem(getItemFromInput())) {
                    Intent intent = new Intent(EditActivity.this, DetailActivity.class);
                    startActivity(intent);
                } else {
                    Snackbar.make(view, "Something went wrong.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });

        Button delete = findViewById(R.id.edit_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (deleteItem()) {
                    Intent intent = new Intent(EditActivity.this, DetailActivity.class);
                    startActivity(intent);
                } else {
                    Snackbar.make(view, "Something went wrong.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });
    }

    public Item getItemForEditting() {
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
                return null;
            }
        }

        String[] itemStrings = existingData.split("%20%");
        for (int i = 1; i < itemStrings.length; i++) {
            String[] itemFields = itemStrings[i].split("@");
            if (Integer.parseInt(itemFields[0]) == Util.edittingItemID)
                return new Item(itemStrings[i]);
        }

        return null;
    }

    public Item getItemFromInput() {
        String newItemName = ((EditText) findViewById(R.id.edit_name)).getText().toString();
        int newItemCalories = Integer.parseInt(((EditText) findViewById(R.id.edit_calories)).getText().toString());
        String timeStr = ((EditText) findViewById(R.id.edit_timestamp)).getText().toString();

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
            String[] itemStrings = existingData.split("%20%");
            for (int i = 1; i < itemStrings.length; i++) {
                String[] itemFields = itemStrings[i].split("@");
                if (Integer.parseInt(itemFields[0]) == Util.edittingItemID) {
                    itemStrings[i] = newItem.toString();
                    break;
                }
            }

            String newData = "";
            for (int i = 1; i < itemStrings.length; i++)
                newData += "%20%" + itemStrings[i];

            fos.write(newData.getBytes());
            fos.close();

            Util.edittingItemID = -1;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean deleteItem() {
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
            String[] itemStrings = existingData.split("%20%");
            String newData = "";
            for (int i = 1; i < itemStrings.length; i++) {
                String[] itemFields = itemStrings[i].split("@");
                if (Integer.parseInt(itemFields[0]) != Util.edittingItemID)
                    newData += "%20%" + itemStrings[i];
            }
            fos.write(newData.getBytes());
            fos.close();

            Util.edittingItemID = -1;
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
