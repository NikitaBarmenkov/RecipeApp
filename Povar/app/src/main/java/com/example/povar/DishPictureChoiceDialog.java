package com.example.povar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;

public class DishPictureChoiceDialog extends DialogFragment {

    private OnClickListeners listener;

    public interface OnClickListeners {
        public void openGaleryClick();
        public void openCameraClick();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnClickListeners) context;
        }
        catch (Exception ex) {

        }
    }

    @Override
    public Dialog onCreateDialog(Bundle SavedInstanceSaved) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder
                .setMessage("Выберите действие")
                .setPositiveButton("Галерея", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.openGaleryClick();
                    }
                })
                .setNegativeButton("Камера", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.openCameraClick();
                    }
                })
                .create();
    }
}
