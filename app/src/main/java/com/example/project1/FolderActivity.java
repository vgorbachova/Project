package com.example.project1;

import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.util.Log;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class FolderActivity extends AppCompatActivity {

    ImageButton btDelete, btAddNew;

    Dialog_CreateFolderOrBlock CreateFolderOrBlock;

    Dialog_CreateFolder createFolder;

    public final int BACK1 = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);


        btDelete = (ImageButton) findViewById(R.id.btDelete);
        btAddNew = (ImageButton) findViewById(R.id.btAddNew);
        CreateFolderOrBlock= new Dialog_CreateFolderOrBlock();
        createFolder = new Dialog_CreateFolder();


        btDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

            }
        });
        btAddNew.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                CreateFolderOrBlock.show(getFragmentManager(), "create");
            }
        });

    }

    public void okClicked() {
        createFolder.show(getFragmentManager(), "create1");

    }

    public void cancelClicked() {
        Intent intent = new Intent(this, NewBlockActivity.class);
        intent.putExtra("getBack", BACK1);
        startActivity(intent);
    }

    public void okClicked2() {

    }

    public void cancelClicked2() {

    }
}
