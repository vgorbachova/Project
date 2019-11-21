package com.example.project1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class NewBlockActivity extends AppCompatActivity {
    Button btCardBlock, btTextBlock, btPimsleur, btEbbinghaus, btOldFolder, btNewFolder;
    ImageButton btGetBack;
    EditText etBlockName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_block);
        etBlockName = (EditText) findViewById(R.id.etBlockName);

        btGetBack = (ImageButton) findViewById(R.id.btGetBack);
        btGetBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                int back = intent.getIntExtra("getBack", 3);
                switch (back){
                    case 1:
                        Intent intent1 = new Intent(getApplicationContext(), FolderActivity.class);
                        startActivity (intent1);
                        break;
                    case 0:
                        Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity (intent2);
                        break;
                    }
                }
            });
        }
    }
