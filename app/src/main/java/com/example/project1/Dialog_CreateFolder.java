package com.example.project1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;

public class Dialog_CreateFolder extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.create_folder, null);
        builder.setView(view);


//        builder.setMessage("")
//                .setIcon(R.drawable.ic_tools)
//                .setTitle("Важно! Максимальный перепост")
                builder.setPositiveButton("create", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((FolderActivity) getActivity()).okClicked2();
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
