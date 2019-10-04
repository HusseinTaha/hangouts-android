package com.comred.Controls;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioButton;

import com.comred.Glob.Global;

/**
 * Created by husseintaha on 11/20/14.
 * custom radio button with a custom type face.
 */
public class CstmRadioButton extends RadioButton {

    public CstmRadioButton(Context context) {
        super(context);
        setFont();
    }
    public CstmRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }
    public CstmRadioButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        setTypeface(Global.getTypeFace());
    }


}
