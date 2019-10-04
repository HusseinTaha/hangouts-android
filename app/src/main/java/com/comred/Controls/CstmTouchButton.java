package com.comred.Controls;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import com.comred.Glob.Global;

/**
 * Created by husseintaha on 11/22/14.
 * custom button with a custom typeface.
 */
public class CstmTouchButton extends Button {


    public CstmTouchButton(Context context) {
        super(context);
        init();
    }

    public CstmTouchButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CstmTouchButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        this.setTypeface(Global.getTypeFace());
    }

}
