package com.comred.Controls;

/**
 * Created by HTaha on 1/14/14.
 */


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.os.Handler;
import android.view.KeyEvent;

/**
 * this is a dialog box that show a message and one button to dismiss.
 */
public class ErrorAlert implements OnKeyListener {

    private final Context mContext;

    public ErrorAlert(final Context context) {
        mContext = context;
    }
    AlertDialog aDialog;
    public void showErrorDialog(final String title, final String message) {
         aDialog = new AlertDialog.Builder(mContext).setMessage(message).setTitle(title)
                .setNeutralButton("Close", new OnClickListener() {
                    public void onClick(final DialogInterface dialog,
                                        final int which) {
                        aDialog.dismiss();
                    }
                }).create();
        aDialog.setOnKeyListener(this);
        aDialog.show();
    }

    public void DismisAndFinish(){
        if(aDialog==null)
            return;
        aDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                ((Activity)mContext).finish();
            }
        });
    }

    public void DismisAndFinish(final Handler handler){
        if(aDialog==null)
            return;
        aDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.sendEmptyMessage(0);
            }
        });
    }
    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            //disable the back button
             aDialog.dismiss();
        }
        return true;
    }


}

