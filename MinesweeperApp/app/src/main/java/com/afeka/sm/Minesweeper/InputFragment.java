package com.afeka.sm.Minesweeper;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;
import android.widget.Toast;
import com.example.mineswipper.R;


public class InputFragment extends DialogFragment {
    private EditText editText;
    private Toast toast;
    OnDataPass dataPasser;

    public interface OnDataPass {
        void onDataPass(String data);
    }

    public InputFragment() {
    }

    public static InputFragment newInstance() {
        return new InputFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage(R.string.PleaseEnterYourName);
        alertDialogBuilder.setTitle(R.string.CongratulationsMessage);
        editText = new EditText(this.getActivity());
        alertDialogBuilder.setView(editText);
        //alertDialogBuilder.setIcon(R.drawable.winner);
        alertDialogBuilder.setPositiveButton(R.string.ConfirmationButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Pass the Name to GameOverActivity
                dataPasser.onDataPass(editText.getText().toString());
                toast.makeText(getActivity(),
                        R.string.SavedConfirmation, Toast.LENGTH_LONG)
                        .show();
            }
        });
        return alertDialogBuilder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataPasser = (OnDataPass) context;
    }
}