package b.lf.triviaquiz.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import b.lf.triviaquiz.R;


public class NoticeDialogFragment extends DialogFragment {

    String Message;
    String Title;

    public interface NoticeDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogNegativeClick(DialogFragment dialog);
        String getDialogMessage();
        String getDialogTitle();
    }

    NoticeDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (NoticeDialogListener) activity;

            Message = mListener.getDialogMessage();
            Title   = mListener.getDialogTitle();

        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + getString(R.string.must_implement));
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_Light_Dialog_Alert);
        builder.setMessage(Message)
                .setIcon(R.drawable.ic_quiz)
                .setTitle(Title)
                .setPositiveButton(R.string.yes,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the positive button event back to the host activity
                        mListener.onDialogPositiveClick(NoticeDialogFragment.this);
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the negative button event back to the host activity
                        mListener.onDialogNegativeClick(NoticeDialogFragment.this);
                    }
                });
        return builder.create();
    }
}