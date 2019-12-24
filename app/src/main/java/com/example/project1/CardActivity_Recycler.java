package com.example.project1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

//Активити для демонстрации карточек пользователя в виде списка. В виде карточек он может хранить иностранные слова, даты, формулы и тд - всю парную информацию.
//Эту же информацию есть возможность повторять по предложенным двум методам интервального повторения, если нажать на кнопку "Start learning"
public class CardActivity_Recycler extends AppCompatActivity {

    ArrayList<Card_Class> cards;

    int elementPosition, delete_id;

    RecyclerView rvCards;

    DBCardHelper dbCardHelper;
    DBBlockHelper dbBlockHelper;
    SQLiteDatabase database;

    Button btAppearance, btStartCards, btStopCards;
    ImageButton btAdd_New_Card;

    String block_id, method, blockName;

    Dialog_DeleteCard deleteCard;

    Dialog_AddNewWords addNewWords;

    int learn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_recycler);

        Intent intent = getIntent();
        block_id = intent.getStringExtra("block_id");
        method = intent.getStringExtra("method");
        blockName = intent.getStringExtra("blockName");

        cards = new ArrayList<>();

        rvCards = (RecyclerView) findViewById(R.id.rvCards);
        CardActivity_Recycler.MyAdapter adapter = new CardActivity_Recycler.MyAdapter();
        rvCards.setAdapter(adapter);
        rvCards.setLayoutManager(new LinearLayoutManager(this));

        btAppearance = (Button) findViewById(R.id.btAppearance);
        btAppearance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CardActivity_CardView.class);
                intent.putExtra("block_id", block_id);
                startActivity(intent);
            }
        });

        btAdd_New_Card = (ImageButton) findViewById(R.id.btAdd_New_Card);
        btAdd_New_Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewWords = new Dialog_AddNewWords();
                Bundle args = new Bundle();
                args.putString("block_id", block_id);
                args.putString("method", method);
                args.putString("blockName", blockName);
                addNewWords.setArguments(args);
                addNewWords.show(getFragmentManager(), "dialog");
            }
        });

        btStartCards = (Button) findViewById(R.id.btStartCards);
        btStartCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    learn = 1;
                    DBStartLearning();
                    fillCurrentTimeandStep();
            }
        });

        btStopCards = (Button) findViewById (R.id.btStopCards);
        btStopCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBStopLearning();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fillDB();
    }


    //Создаем адаптер RecyclerView для карточек
    public class MyAdapter extends RecyclerView.Adapter <CardActivity_Recycler.MyAdapter.ViewHolder> {

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

            TextView text1, text2;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                itemView.setOnLongClickListener(this);
                text1 = itemView.findViewById(R.id.first_word);
                text2 = itemView.findViewById(R.id.second_word);
            }

            //Добавляем переход в диалоговое окно по длинному табу на элемент RecyclerView
            @Override
            public boolean onLongClick(View view) {
                elementPosition = getAdapterPosition();
                Card_Class card = cards.get(elementPosition);
                delete_id=Integer.valueOf(card.getId());
                Bundle args = new Bundle();
                args.putInt("elementId", delete_id);
                deleteCard = new Dialog_DeleteCard();
                deleteCard.setArguments(args);
                deleteCard.show(getFragmentManager(), "dialog");
                return true;
            }
        }


        @NonNull
        @Override
        public CardActivity_Recycler.MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_for_recycler, parent, false);
            return new CardActivity_Recycler.MyAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CardActivity_Recycler.MyAdapter.ViewHolder holder, int position) {
            Card_Class card = cards.get(position);
            String wordFirst = card.getFirst_word();
            String wordSecond = card.getSecond_word();
            holder.text1.setText(wordFirst);
            holder.text2.setText(wordSecond);
        }

        @Override
        public int getItemCount() {
            return cards.size();
        }
    }

    //Методы
    public void getNewWordToBlock() {
        fillDB();
        rvCards.getAdapter().notifyItemInserted(cards.size() - 1);
    }

    public void cancelClicked() {
    }

    public void deleteCard() {
        int delCount = database.delete(DBCardHelper.TABLE_CARDS, "_id = " + delete_id, null);
        fillDB ();
        rvCards.getAdapter().notifyItemRemoved(elementPosition);
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

    //Метод запуска блока на изучение
    public void DBStartLearning () {
        dbBlockHelper = new DBBlockHelper(getApplicationContext());
        database = dbBlockHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBBlockHelper.KEY_LEARNING, learn);

        int updCount = database.update(DBBlockHelper.TABLE_BLOCKS, contentValues, "_id=="+block_id, null);
    }

    //Метод прекращения обучения
    public void DBStopLearning () {
        dbBlockHelper = new DBBlockHelper(getApplicationContext());
        database = dbBlockHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBBlockHelper.KEY_LEARNING, 0);

        int updCount = database.update(DBBlockHelper.TABLE_BLOCKS, contentValues, "_id=="+block_id, null);
    }

    public void fillCurrentTimeandStep () {
        dbBlockHelper = new DBBlockHelper(getApplicationContext());
        database = dbBlockHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();


        contentValues.put(DBBlockHelper.KEY_PREVIOUS_LESSON, System.currentTimeMillis());
        contentValues.put(DBBlockHelper.KEY_NEXT_LESSON, 1);

        int updCount = database.update(DBBlockHelper.TABLE_BLOCKS, contentValues, "_id=="+block_id, null);

        Cursor cursor = database.query(DBBlockHelper.TABLE_BLOCKS, null, "_id=="+block_id, null, null, null, null);
        if(cursor.moveToFirst()) {
            int previous = cursor.getColumnIndex(DBBlockHelper.KEY_PREVIOUS_LESSON);
            int next = cursor.getColumnIndex(DBBlockHelper.KEY_NEXT_LESSON);
            int pre = cursor.getInt(previous);
            int ne = cursor.getInt(next);
            Log.d("fillCurrent", String.format("%s %s", String.valueOf(pre), String.valueOf(ne)));
            }
    }
}
