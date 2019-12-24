package com.example.project1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;



public class FolderActivity extends AppCompatActivity {

    ImageButton btAddNew;

    Button dlDB;

    Dialog_CreateFolder createFolder;
    Dialog_RenameFolder rename ;


    RecyclerView rv;

    ArrayList <Folder_NameAndId> folders;

    DBFoldersHelper dbFoldersHelper;
    SQLiteDatabase database;

    int elementPosition;
    int delete_id;
    String folder_id_for_block;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);


        btAddNew = (ImageButton) findViewById(R.id.btAddNew);


        createFolder = new Dialog_CreateFolder();
        rename = new Dialog_RenameFolder();


        btAddNew.setOnClickListener(new View.OnClickListener() {
            //Переходим в дилоговое окно, внутри которого даем имя папке и заполняем базу данных
            @Override
            public void onClick(View view) {
                createFolder.show(getFragmentManager(), "create1");
            }
        });

        folders = new ArrayList<>();

        rv = (RecyclerView) findViewById(R.id.rv);
        MyAdapter adapter = new MyAdapter();
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

        dlDB = (Button) findViewById(R.id.dlDB);
        dlDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.delete(DBFoldersHelper.TABLE_FOLDERS, null, null);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        fillDB();
    }

    //Создаем адаптер для RecyclerView с папками
    public class MyAdapter extends RecyclerView.Adapter <MyAdapter.ViewHolder> {

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

            TextView text1;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                //Добавляем переход на активити с блоками по клику на элемент RecyclerView
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        elementPosition = getAdapterPosition();
                        Folder_NameAndId neededFolder=folders.get(elementPosition);
                        folder_id_for_block=String.valueOf(neededFolder.getId());
                        Intent intent = new Intent(getApplicationContext(), BlockActivity.class);
                        intent.putExtra("folder_id_for_block", folder_id_for_block);
                        startActivity(intent);
                    }
                });
                itemView.setOnLongClickListener(this);
                text1 = itemView.findViewById(android.R.id.text1);
            }

            //Добавляем переход в диалоговое окно Dialog_DeleteAndRenameFolder по длинному табу на элемент RecyclerView
            @Override
            public boolean onLongClick(View view) {
                elementPosition = getAdapterPosition();
                FragmentManager manager = getSupportFragmentManager();
                Dialog_DeleteAndRenameFolder myDialogFragment = new Dialog_DeleteAndRenameFolder();
                Folder_NameAndId folder=folders.get(elementPosition);
                Log.d("DELETE", String.format("%s %s", folder.getId(), folder.getName()));
                delete_id=Integer.valueOf(folder.getId());
                Bundle args = new Bundle();
                args.putInt("elementId", delete_id);
                rename.setArguments(args);
                myDialogFragment.show(getFragmentManager(), "dialog");
                return true;
            }
        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Folder_NameAndId folder =folders.get(position);
            String folderName = folder.getName();
            holder.text1.setText(folderName);
        }


        @Override
        public int getItemCount() {

            return folders.size();
        }
    }


    //Методы кнопок диалоговых окон
    public void getNewElementToFolder() {
        fillDB();
        rv.getAdapter().notifyItemInserted(folders.size() - 1);
    }

    public void cancelClicked2() {
    }

    public void deleteElementFromFolder() {
        int delCount = database.delete(DBFoldersHelper.TABLE_FOLDERS, "_id = " + delete_id, null);
        fillDB ();
        rv.getAdapter().notifyItemRemoved(elementPosition);
    }

    public void renameFolder() {
        rename.show(getFragmentManager(), "dialog");
        fillDB();
    }

    public void renameFolder2() {
        fillDB ();
        rv.getAdapter().notifyItemChanged(elementPosition);
    }

    //Заполнение ArrayList для Recycler
    public void fillDB() {
            folders = new ArrayList<>();

            dbFoldersHelper = new DBFoldersHelper(this);
            database = dbFoldersHelper.getWritableDatabase();
            Cursor cursor = database.query(DBFoldersHelper.TABLE_FOLDERS, null, null, null, null, null, null);
            if(cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(DBFoldersHelper.KEY_NAME);
                int idIndex = cursor.getColumnIndex(DBFoldersHelper.KEY_ID);
                do {
                    folders.add(new Folder_NameAndId(cursor.getString(idIndex), cursor.getString(nameIndex)));
                } while (cursor.moveToNext());
                for (int i = 0; i < folders.size(); i++) {
                    Folder_NameAndId folder_nameAndId = folders.get(i);
                    String name = folder_nameAndId.getName();
                    String id = folder_nameAndId.getId();
                    Log.d("FOLDERS", String.format("%s %s", id, name));
                }
            }
            cursor.close();
    }
}