package com.example.project1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

//Активити для демонстрации карточек пользователя в виде карточки на весь экран с возможностью переворачивать ее. В виде карточек он может хранить иностранные слова, даты, формулы и тд - всю парную информацию.
public class CardActivity_CardView extends AppCompatActivity {

    Button btAnswer;

    TextView tvAnswer, question;

    ArrayList<Card_Class> cards;

    int count = 0;
    boolean answer=false;

    DBCardHelper dbCardHelper;
    SQLiteDatabase database;

    String block_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card__view);
        Intent intent = getIntent();
        block_id = intent.getStringExtra("block_id");
        Log.d("BLOCK_ID", String.format("%s", block_id));
        fillDB();
        tvAnswer = (TextView) findViewById(R.id.answer);
        question = (TextView) findViewById(R.id.question);
        btAnswer = (Button) findViewById(R.id.btAnswer);
        showWord();

        question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answer=!answer;
                showWord();
            }
        });

        btAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count<(cards.size())) {
                    count++;
                    answer=false;
                }
                showWord();
            }
        });
    }


    //Метод демонстрации материала в виде карточек
    public void showWord () {
        if(cards.size()!=0) {
            if(count!=cards.size()) {
                if (answer) question.setText(cards.get(count).getSecond_word());
                else question.setText(cards.get(count).getFirst_word());
            }
            else question.setText("END OF THE BLOCK");
        }
        else question.setText("BLOCK IS EMPTY");
    }

    //Метод заполнения ArrayList
    public void fillDB() {
        cards = new ArrayList<>();

        dbCardHelper = new DBCardHelper(this);
        database = dbCardHelper.getWritableDatabase();
        Cursor cursor = database.query(DBCardHelper.TABLE_CARDS, null, "block=="+block_id, null, null, null, null);
        if(cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(DBCardHelper.KEY_NAME);
            int idIndex = cursor.getColumnIndex(DBCardHelper.KEY_ID);
            int methodIndex = cursor.getColumnIndex(DBCardHelper.KEY_METHOD);
            int blockIndex = cursor.getColumnIndex(DBCardHelper.KEY_BLOCK);
            int first_wordIndex = cursor.getColumnIndex(DBCardHelper.KEY_FIRST_WORD);
            int second_wordIndex = cursor.getColumnIndex(DBCardHelper.KEY_SECOND_WORD);
            do {
                cards.add(new Card_Class(cursor.getString(idIndex), cursor.getString(nameIndex), cursor.getString(blockIndex), cursor.getString(methodIndex), cursor.getString(first_wordIndex), cursor.getString(second_wordIndex)));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

}
