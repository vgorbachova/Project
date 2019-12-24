package com.example.project1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class BlockActivity extends AppCompatActivity {
    ImageButton btAddNewBlock;

    RecyclerView rvBlock;

    String folder_id_for_block, block_id, kind_of_block, method, blockName;

    Dialog_RenameBlock renameBlock;

    DBBlockHelper dbBlockHelper;
    SQLiteDatabase database;

    int elementPosition,  delete_id;

    ArrayList<Block_Class> blocks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block);
        Intent intent = getIntent();
        folder_id_for_block = intent.getStringExtra("folder_id_for_block");

        renameBlock = new Dialog_RenameBlock();

        btAddNewBlock = (ImageButton) findViewById(R.id.btAddNewBlock);
        btAddNewBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Переходим в дилоговое окно, внутри которого даем имя блоку и заполняем базу данных
                Bundle args = new Bundle();
                args.putString("folder_id_for_block", folder_id_for_block);
                Dialog_CreateBlock createBlock = new Dialog_CreateBlock();
                createBlock.setArguments(args);
                createBlock.show(getFragmentManager(), "create");
            }
        });

        rvBlock = (RecyclerView) findViewById(R.id.rvBlock);
        BlockActivity.MyAdapter adapter = new BlockActivity.MyAdapter();
        rvBlock.setAdapter(adapter);
        rvBlock.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onResume() {
        super.onResume();
        fillDB();
    }

    //Создаем адаптер RecyclerView для блоков
    public class MyAdapter extends RecyclerView.Adapter <BlockActivity.MyAdapter.ViewHolder> {

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

            TextView text1;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                //Добавляем переход внутрь блока по клику на элемент RecyclerView
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        elementPosition = getAdapterPosition();
                        Block_Class neededBlock = blocks.get(elementPosition);
                        block_id = String.valueOf(neededBlock.getId());
                        kind_of_block = neededBlock.getKind();
                        blockName = neededBlock.getName();
                        method = neededBlock.getMethod();
                        if (kind_of_block.equals("TEXT")) {
                            Intent intent = new Intent(getApplicationContext(), TextActivity.class);
                            intent.putExtra("method", method);
                            intent.putExtra("block_id", block_id);
                            intent.putExtra("blockName", blockName);
                            startActivity(intent);
                        }
                        else if (kind_of_block.equals("CARD")) {
                            Intent intent = new Intent(getApplicationContext(), CardActivity_Recycler.class);
                            intent.putExtra("method", method);
                            intent.putExtra("block_id", block_id);
                            intent.putExtra("blockName", blockName);
                            startActivity(intent);
                        }
                    }
                });
                itemView.setOnLongClickListener(this);
                text1 = itemView.findViewById(android.R.id.text1);
            }

            //Добавляем переход в диалоговое окно Dialog_DeleteAndRenameBlock по длинному табу на элемент RecyclerView
            @Override
            public boolean onLongClick(View view) {
                elementPosition = getAdapterPosition();
                FragmentManager manager = getSupportFragmentManager();
                Dialog_DeleteAndRenameBlock myDialogFragment = new Dialog_DeleteAndRenameBlock();
                Block_Class block=blocks.get(elementPosition);
                Log.d("DELETE", String.format("%s %s", block.getId(), block.getName()));
                delete_id=Integer.valueOf(block.getId());
                Bundle args = new Bundle();
                args.putInt("elementId", delete_id);
                renameBlock.setArguments(args);
                myDialogFragment.show(getFragmentManager(), "dialog");
                return true;
            }
        }


        @NonNull
        @Override
        public BlockActivity.MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            return new BlockActivity.MyAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull BlockActivity.MyAdapter.ViewHolder holder, int position) {
            Block_Class folder = blocks.get(position);
            String folderName = folder.getName();
            holder.text1.setText(folderName);
        }


        @Override
        public int getItemCount() {
            return blocks.size();
        }
    }

    //Методы кнопок диалоговых окон
    public void deleteElementFromBlock() {
        int delCount = database.delete(DBBlockHelper.TABLE_BLOCKS, "_id = " + delete_id, null);
        fillDB ();
        rvBlock.getAdapter().notifyItemRemoved(elementPosition);
    }

    public void renameBlock() {
        renameBlock.show(getFragmentManager(), "dialog");
    }

    public void renameBlock2() {
        fillDB ();
        rvBlock.getAdapter().notifyItemChanged(elementPosition);
    }

    public void createBlock() {
        fillDB();
    }

    public void cancelClicked3() {
    }

    //Заполнение ArrayList для Recycler
    public void fillDB() {
        blocks = new ArrayList<>();

        dbBlockHelper = new DBBlockHelper(this);
        database = dbBlockHelper.getWritableDatabase();
        Cursor cursor = database.query(DBBlockHelper.TABLE_BLOCKS, null, "folder=="+folder_id_for_block, null, null, null, null);
        if(cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(DBBlockHelper.KEY_NAME);
            int idIndex = cursor.getColumnIndex(DBBlockHelper.KEY_ID);
            int methodIndex = cursor.getColumnIndex(DBBlockHelper.KEY_METHOD);
            int folderIndex = cursor.getColumnIndex(DBBlockHelper.KEY_FOLDER);
            int kindIndex = cursor.getColumnIndex(DBBlockHelper.KEY_KIND_OF_BLOCK);
            do {
                blocks.add(new Block_Class(cursor.getString(idIndex), cursor.getString(nameIndex), cursor.getString(folderIndex), cursor.getString(methodIndex), cursor.getString(kindIndex)));
            } while (cursor.moveToNext());
            for (int i = 0; i < blocks.size(); i++) {
                Block_Class folder_nameAndId = blocks.get(i);
                String name = folder_nameAndId.getName();
                String id = folder_nameAndId.getId();
                Log.d("BLOCKS", String.format("%s %s", id, name));
            }
        }
        cursor.close();
    }
}
