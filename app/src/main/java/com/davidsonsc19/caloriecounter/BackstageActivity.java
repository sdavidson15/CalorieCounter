package com.davidsonsc19.caloriecounter;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class BackstageActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backstage);
        refreshBackstageText();

        TextView usingDate = findViewById(R.id.backstage_using_date);
        usingDate.setText("Details Using Date: " + Util.usingDate.toString());

        Button clear = findViewById(R.id.backstage_clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String filename = Util.getFilename(new Date());
                try {
                    FileOutputStream fos = openFileOutput(filename, Context.MODE_PRIVATE);
                    fos.write(new byte[0]);
                    fos.close();
                } catch (Exception e) {
                    Snackbar.make(view, "Something went wrong.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                refreshBackstageText();
            }
        });
    }

    public void refreshBackstageText() {
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
                // TODO: Alert
            }
        }

        TextView tv = findViewById(R.id.backstage_text);
        tv.setText(existingData);
    }
}
