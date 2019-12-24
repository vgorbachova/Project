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
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;

public class Dialog_CreateBlock extends DialogFragment {
    EditText etBlockName;

    CheckBox cbCardBlock, cbTextBlock, cbPimsleur, cbEbbinghaus;

    String folder_id_for_block;

    public final String CARD = "CARD";
    public final String TEXT = "TEXT";

    public final String PIMS = "PIMS";
    public final String EBBI = "EBBI";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    LayoutInflater inflater = getActivity().getLayoutInflater();
    View view = inflater.inflate(R.layout.create_block, null);
        builder.setView(view);
        etBlockName = (EditText) view.findViewById (R.id.etBlockName);

        cbCardBlock = (CheckBox) view.findViewById(R.id.cbCardBlock);
        cbTextBlock = (CheckBox) view.findViewById(R.id.cbTextBlock);
        cbPimsleur = (CheckBox) view.findViewById(R.id.cbPimsleur);
        cbEbbinghaus = (CheckBox) view.findViewById(R.id.cbEbbinghaus);
        folder_id_for_block=getArguments().getString("folder_id_for_block");

        builder.setPositiveButton("create", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {
            DBBlockHelper dbBlockHelper = new DBBlockHelper(getActivity());
            SQLiteDatabase database = dbBlockHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put(DBBlockHelper.KEY_NAME, etBlockName.getText().toString());
            contentValues.put(DBBlockHelper.KEY_FOLDER, folder_id_for_block);
            if (cbCardBlock.isChecked()) {
                contentValues.put(DBBlockHelper.KEY_KIND_OF_BLOCK, CARD);
            }
            else if (cbTextBlock.isChecked()) {
                contentValues.put(DBBlockHelper.KEY_KIND_OF_BLOCK, TEXT);
            }
            if (cbPimsleur.isChecked()) {
                contentValues.put(DBBlockHelper.KEY_METHOD, PIMS);
            }
            else if (cbEbbinghaus.isChecked()) {
                contentValues.put(DBBlockHelper.KEY_METHOD, EBBI);
            }
            contentValues.put(DBBlockHelper.KEY_LEARNING, 0);
            database.insert(DBBlockHelper.TABLE_BLOCKS, null, contentValues);

            Cursor cursor = database.query(DBBlockHelper.TABLE_BLOCKS, null, null, null, null, null, null);
            cursor.moveToFirst();
            int idIndex = cursor.getColumnIndex(DBBlockHelper.KEY_ID);
            int nameIndex = cursor.getColumnIndex(DBBlockHelper.KEY_NAME);
            int folderIndex = cursor.getColumnIndex(DBBlockHelper.KEY_FOLDER);
            int methodIndex = cursor.getColumnIndex(DBBlockHelper.KEY_METHOD);
            int kindIndex = cursor.getColumnIndex(DBBlockHelper.KEY_KIND_OF_BLOCK);


            do {
                Log.d("BLOCKDB", String.format("%s %s %s %s %s", cursor.getString(idIndex), cursor.getString(nameIndex), cursor.getString(folderIndex), cursor.getString(methodIndex), cursor.getString(kindIndex)));
            } while (cursor.moveToNext());
            cursor.close();
            ((BlockActivity) getActivity()).createBlock();
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
