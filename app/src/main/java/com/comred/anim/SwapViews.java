package com.comred.anim;

/**
 * Created by HTaha on 4/
 */


import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;


public final class SwapViews implements Runnable {
    
    private boolean mIsFirstView;
    private LinearLayout ll_shareword,ll_share;

            
    public SwapViews(boolean isFirstView, LinearLayout ll1, LinearLayout ll2) {
        mIsFirstView = isFirstView;
        this.ll_shareword = ll1;
        this.ll_share = ll2;
    }
    

            
    public void run() {
        final float centerX = ll_shareword.getWidth() / 2.0f;
        final float centerY = ll_shareword.getHeight() / 2.0f;
        Flip3dAnimation rotation;

        if (mIsFirstView) {
            ll_shareword.setVisibility(View.GONE);
            ll_share.setVisibility(View.VISIBLE);
            ll_share.requestFocus();
            rotation = new Flip3dAnimation(-90,0, centerX, centerY);
            
        } else {
            ll_share.setVisibility(View.GONE);
            ll_shareword.setVisibility(View.VISIBLE);
            ll_shareword.requestFocus();
            rotation = new Flip3dAnimation( 90,0, centerX, centerY);
            
        }
        
        rotation.setDuration(500);
        rotation.setFillAfter(true);
        rotation.setInterpolator(new DecelerateInterpolator());

        if (mIsFirstView) {
            ll_share.startAnimation(rotation);
        } else {
            ll_shareword.startAnimation(rotation);
        }
        
    }
    
}