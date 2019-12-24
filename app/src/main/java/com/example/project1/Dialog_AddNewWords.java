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

public class Dialog_AddNewWords extends DialogFragment {
    EditText frontSide, backSide;

    String block_id, method, blockName;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_new_words, null);
        builder.setView(view);

        frontSide = (EditText) view.findViewById (R.id.frontSide);
        backSide = (EditText) view.findViewById (R.id.backSide);

        block_id = getArguments().getString("block_id");
        method = getArguments().getString("method");
        blockName = getArguments().getString("blockName");



//        builder.setMessage("")
//                .setIcon(R.drawable.ic_tools)
//                .setTitle("Важно! Максимальный перепост")
        builder.setPositiveButton("add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                DBCardHelper dbCardHelper = new DBCardHelper(getActivity());
                SQLiteDatabase database = dbCardHelper.getWritableDatabase();
                ContentValues contentValues = new ContentValues();

                contentValues.put(DBCardHelper.KEY_FIRST_WORD , frontSide.getText().toString());
                contentValues.put(DBCardHelper.KEY_SECOND_WORD , backSide.getText().toString());
                contentValues.put(DBCardHelper.KEY_BLOCK , block_id);
                contentValues.put(DBCardHelper.KEY_METHOD, method);
                contentValues.put(DBCardHelper.KEY_NAME, blockName);
                long idx =database.insert(DBCardHelper.TABLE_CARDS, null, contentValues);

                ((CardActivity_Recycler) getActivity()).getNewWordToBlock();
            }
        })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((CardActivity_Recycler) getActivity()).cancelClicked();
                        dialog.cancel();
                    }
                });
        return builder.create();
    }
}
