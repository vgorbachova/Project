package com.example.project1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;

public class Dialog_CreateFolder extends DialogFragment {
    EditText etName;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.create_folder, null);
        builder.setView(view);

        etName = (EditText) view.findViewById (R.id.etName);

        builder.setPositiveButton("create", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DBFoldersHelper dbFoldersHelper = new DBFoldersHelper(getActivity());
                        SQLiteDatabase database = dbFoldersHelper.getWritableDatabase();
                        ContentValues contentValues = new ContentValues();

                        contentValues.put(DBFoldersHelper.KEY_NAME, etName.getText().toString());
                        database.insert(DBFoldersHelper.TABLE_FOLDERS, null, contentValues);

                        Cursor cursor = database.query(DBFoldersHelper.TABLE_FOLDERS, null, null, null, null, null, null);
                        cursor.moveToFirst();
                        int idIndex = cursor.getColumnIndex(DBFoldersHelper.KEY_ID);
                        int nameIndex = cursor.getColumnIndex(DBFoldersHelper.KEY_NAME);

                        do {
                            Log.d("DATAB", String.format("%s %s", String.valueOf(cursor.getInt(idIndex)), cursor.getString(nameIndex)));
                        } while (cursor.moveToNext());
                        cursor.close();
                        ((FolderActivity) getActivity()).getNewElementToFolder();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((FolderActivity) getActivity()).cancelClicked2();
                        dialog.cancel();
                    }
                });
        return builder.create();
    }
}
