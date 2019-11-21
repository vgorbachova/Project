package com.example.project1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btNewBlocks, btOldBlocks;

    public final int BACK2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btNewBlocks = (Button) findViewById(R.id.btNewBlocks);
        btOldBlocks = (Button) findViewById(R.id.btOldBlocks);
        btNewBlocks.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NewBlockActivity.class);
                intent.putExtra("getBack", BACK2);
                startActivity (intent);
            }
        });
        btOldBlocks.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FolderActivity.class);
                startActivity (intent);
            }
        });
    }
}
