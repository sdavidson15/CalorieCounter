package com.davidsonsc19.caloriecounter;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

public class DetailActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView tv = findViewById(R.id.detail_counter);
        tv.setText("Total: " + getCurrentCount() + " calories");

        TextView detailDate = findViewById(R.id.detail_date);
        detailDate.setText(Util.getDateString(Util.usingDate));

        Button prevButton = findViewById(R.id.detail_previous);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.usingDate = Util.incrementDate(Util.usingDate, -1);
                Intent intent = new Intent(DetailActivity.this, DetailActivity.class);
                startActivity(intent);
            }
        });

        Button nextButton = findViewById(R.id.detail_next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.usingDate = Util.incrementDate(Util.usingDate, 1);
                Intent intent = new Intent(DetailActivity.this, DetailActivity.class);
                startActivity(intent);
            }
        });
        if (Util.datesMatch(Util.usingDate, new Date()))
            nextButton.setVisibility(View.GONE);


        populateTable();
    }

    public boolean populateTable() {
        String existingData = "";
        String filename = Util.getFilename(Util.usingDate);

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

        if (existingData.length() > 0) {
            String[] itemStrings = existingData.split("%20%");
            for (int i = 1; i < itemStrings.length; i++) {
                // The following must be done for backwards compatibility reasons
                if (itemStrings[i].split("@").length == 4)
                    addItemRow(new Item("99@" + itemStrings[i]));
                else
                    addItemRow(new Item(itemStrings[i]));
            }
        }

        return true;
    }

    public void addItemRow(Item item) {
        TextView itemID = new TextView(this);
        TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,1);
        itemID.setLayoutParams(params);
        itemID.setText(String.valueOf(item.itemID));
        itemID.setVisibility(View.GONE);

        TextView itemName = new TextView(this);
        itemName.setLayoutParams(new TableRow.LayoutParams(150, TableRow.LayoutParams.WRAP_CONTENT,1));
        itemName.setTextColor(Color.BLACK);
        itemName.setPadding(5, 5, 5, 5);
        itemName.setText(item.name);

        TextView itemCalories = new TextView(this);
        itemCalories.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,1));
        itemCalories.setTextColor(Color.BLACK);
        itemCalories.setPadding(5, 5, 5, 5);
        itemCalories.setText(String.valueOf(item.calories));

        TextView itemTimestamp = new TextView(this);
        itemTimestamp.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT,1));
        itemTimestamp.setTextColor(Color.BLACK);
        itemTimestamp.setPadding(5, 5, 5, 5);
        itemTimestamp.setText(item.time);

        TableRow tr = new TableRow(this);
        tr.addView(itemID);
        tr.addView(itemName);
        tr.addView(itemCalories);
        tr.addView(itemTimestamp);
        final TableRow newRow = tr;
        if (Util.datesMatch(Util.usingDate, new Date())) {
            newRow.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Util.edittingItemID = Integer.parseInt(((TextView) newRow.getChildAt(0)).getText().toString());
                    Intent intent = new Intent(DetailActivity.this, EditActivity.class);
                    startActivity(intent);
                    return true;
                }
            });
        }

        TableLayout tl = findViewById(R.id.table_layout);
        tl.addView(tr);
    }

    // FIXME: This is duplicated code. Consolidate this somewhere
    public int getCurrentCount() {
        String existingData = "";
        String filename = Util.getFilename(Util.usingDate);

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

        if (existingData.length() > 0) {
            String[] itemStrings = existingData.split("%20%");
            int total = 0;
            for (int i = 1; i < itemStrings.length; i++) {
                Item currentItem;

                // The following must be done for backwards compatibility reasons
                if (itemStrings[i].split("@").length == 4)
                    currentItem = new Item("99@" + itemStrings[i]);
                else
                    currentItem = new Item(itemStrings[i]);

                total += currentItem.calories;
            }
            return total;
        }
        return 0;
    }
}
