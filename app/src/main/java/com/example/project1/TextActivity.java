package com.example.project1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


//Активити просто для хранения текстовой инфы (лекции, стихи и тд). Что-то типа заметок внутри приложения
public class TextActivity extends AppCompatActivity {
    Button btSave;

    EditText edText;

    String buttonText, block_id;

    SQLiteDatabase database;
    DBBlockHelper dbBlockHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        edText = (EditText) findViewById(R.id.edText);
        Intent intent = getIntent();
        block_id = intent.getStringExtra("block_id");
        seek_info();
        btSave = (Button) findViewById(R.id.btSave);
        edText.setTag(edText.getKeyListener());
        edText.setKeyListener(null);
        btSave.setText("edit");

        //Устанавливаем слушатель на кнопку "save". Даем возможность
        //пользователю редактировать и сохранить введенный текст
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonText = ((Button) btSave).getText().toString();
                if (buttonText.equals("save")) {
                    edText.setTag(edText.getKeyListener());
                    edText.setKeyListener(null);
                    update_info();
                    btSave.setText("edit");
                }
                else if (buttonText.equals("edit")) {
                    edText.setKeyListener((KeyListener) edText.getTag());
                    btSave.setText("save");
                }
            }
        });
    }

    //Метод чтения текста из базы данных
    public void seek_info(){
        DBBlockHelper dbBlockHelper = new DBBlockHelper(this);
        database = dbBlockHelper.getWritableDatabase();
        Cursor cursor = database.query(DBBlockHelper.TABLE_BLOCKS, null, "_id=="+block_id, null, null, null, null);

        if(cursor.moveToFirst()) {
            //int nameIndex = cursor.getColumnIndex(DBBlockHelper.KEY_NAME);
            int textIndex = cursor.getColumnIndex(DBBlockHelper.KEY_TEXT);
            edText.setText(cursor.getString(textIndex));
        }
        cursor.close();

    }

    //Метод внесения текста в базу данных блока
    public void update_info(){
        DBBlockHelper dbBlockHelper = new DBBlockHelper(getApplicationContext());
        SQLiteDatabase database = dbBlockHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBBlockHelper.KEY_TEXT, edText.getText().toString());

        int updCount = database.update(DBBlockHelper.TABLE_BLOCKS, contentValues, "_id = " + block_id, null);
    }
}
