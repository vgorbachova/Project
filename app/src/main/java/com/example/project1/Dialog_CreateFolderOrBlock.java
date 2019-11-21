package com.example.project1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;

public class Dialog_CreateFolderOrBlock extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("Create new")
//                .setIcon(R.drawable.ic_tools)
//                .setTitle("Важно! Максимальный перепост")
                .setPositiveButton("folder", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((FolderActivity) getActivity()).okClicked();
                    }
                })
                .setNegativeButton("block", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((FolderActivity) getActivity()).cancelClicked();
                    }
                });
        return builder.create();
    }

}
