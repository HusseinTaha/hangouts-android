package com.comred.Controls;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.comred.Glob.Global;

/**
 * Created by husseintaha on 11/20/14.
 * custom text view with a custom typeface
 */
public class CstmTextView extends TextView {

    public CstmTextView(Context context) {
        super(context);
        setFont();
    }
    public CstmTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }
    public CstmTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        setTypeface(Global.getTypeFace());
    }


}
