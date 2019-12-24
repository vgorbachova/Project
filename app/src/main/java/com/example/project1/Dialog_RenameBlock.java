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

public class Dialog_RenameBlock extends DialogFragment {
    EditText etName;
    int elementId;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.create_folder, null);
        builder.setView(view);

        etName = (EditText) view.findViewById(R.id.etName);
        elementId = getArguments().getInt("elementId");

        builder.setPositiveButton("rename", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                DBBlockHelper dbBlockHelper = new DBBlockHelper(getActivity());
                SQLiteDatabase database = dbBlockHelper.getWritableDatabase();
                ContentValues contentValues = new ContentValues();

                contentValues.put(DBBlockHelper.KEY_NAME, etName.getText().toString());

                int updCount = database.update(DBBlockHelper.TABLE_BLOCKS, contentValues, "_id = " + String.valueOf(elementId), null);
                ((BlockActivity) getActivity()).renameBlock2();
            }
        })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((BlockActivity) getActivity()).cancelClicked3();
                        dialog.cancel();
                    }
                });
        return builder.create();
    }
}

