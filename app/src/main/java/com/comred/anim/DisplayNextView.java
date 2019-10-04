package com.comred.anim;

/**
 * Created by HTaha on 4/
 * used to show next view in a flip animation between two views.
 */


import android.view.animation.Animation;
import android.widget.LinearLayout;


public final class DisplayNextView implements Animation.AnimationListener {
    
    private boolean mCurrentView;
    private LinearLayout ll_shareWord;
    private LinearLayout ll_share;
            
    public DisplayNextView(boolean currentView, LinearLayout ll1, LinearLayout ll2) {
        
        mCurrentView = currentView;
        this.ll_shareWord = ll1;
        this.ll_share = ll2;
    }
    

            
    public void onAnimationStart(Animation animation) {
        
    }
    

            
    public void onAnimationEnd(Animation animation) {
        ll_shareWord.post(new SwapViews(mCurrentView, ll_shareWord, ll_share));
    }
    

            
    public void onAnimationRepeat(Animation animation) {
        
    }
    
}
