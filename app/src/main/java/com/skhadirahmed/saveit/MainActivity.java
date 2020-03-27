package com.skhadirahmed.saveit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button mbutton = findViewById(R.id.geturls);
        mTextView = findViewById(R.id.textView);
        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        mbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urls = readFromFile(MainActivity.this);
                mTextView.setText(urls);
            }
        });
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
//                Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                handleSendText(intent); // Handle text being sent
            } else if (type.startsWith("image/")) {
//                handleSendImage(intent); // Handle single image being sent
            }
        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
            if (type.startsWith("image/")) {
//                handleSendMultipleImages(intent); // Handle multiple images being sent
            }
        } else {
            // Handle other intents, such as being started from the home screen
        }
    }

    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            // Update UI to reflect text being shared
            Toast.makeText(this, sharedText+"from khadir", Toast.LENGTH_SHORT).show();
            writeToFile(sharedText, this);
            finish();
        }
    }

    private void writeToFile(String data, Context context) {
        try {
            File file = new File(context.getFilesDir(), "shared_urls.txt");
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);

//            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("shared_urls.txt", Context.MODE_PRIVATE));
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
//            outputStreamWriter.write(data);
            outputStreamWriter.append(data);
            outputStreamWriter.append("\n");
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private String readFromFile(Context context) {
        String ret = "";
        try {
            InputStream inputStream = context.openFileInput("shared_urls.txt");
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString;
                StringBuilder stringBuilder = new StringBuilder();
                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append("\n").append(receiveString);
                }
                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
            Log.e("login activity", "File not found : " + e.toString());

        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        return ret;
    }
}
