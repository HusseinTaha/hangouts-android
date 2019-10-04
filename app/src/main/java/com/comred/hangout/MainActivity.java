package com.comred.hangout;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.TypedValue;
import android.view.InflateException;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.comred.Controls.CustomLinear;
import com.comred.Controls.SlideHolder;
import com.comred.Fragments.AboutUs_Fragment;
import com.comred.Fragments.CalendarFragment;
import com.comred.Fragments.HangoutFragment;
import com.comred.Fragments.HomeFragment;
import com.comred.Fragments.SettingsFragment;
import com.comred.Fragments.UploadPhotosFragment;
import com.comred.Fragments.WallFragment;
import com.comred.Glob.Global;
import com.comred.Listeners.HomeClickListener;
import com.comred.Listeners.MenuClickListener;
import com.comred.mylib.Utility;


/*
* the main activity class that contains all the functionality
* here all the controls are been inflated.*/
public class MainActivity extends FragmentActivity  implements MenuClickListener,HomeClickListener {



    // decalration des variables
    private static SlideHolder mSlideHolder;
    private CustomLinear menuView;
    private AQuery ajax;
    private ImageView iv_home;
    private Animation animZoomIn;
    private boolean isHome=true;
    private UploadPhotosFragment photosFragment;
    private int height,width;

    public static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utility.clearAllResources();
        Global.SetContext(this);
        width= Global.getwidth();
        height=Global.getheight();
        ajax =  new AQuery(this);
        mSlideHolder = (SlideHolder)findViewById(R.id.slideHolder);
        ajax.id(R.id.tv_fragmentTitle).typeface(Global.getTypeFace());
        menuView= (CustomLinear) findViewById(R.id.expanded_menu);
        iv_home= (ImageView)findViewById(R.id.iv_home);

        ajax.id(R.id.tv_fragmentTitle).getTextView().setTextSize(TypedValue.COMPLEX_UNIT_PX,width*5/100);
        ajax.id(R.id.ib_mneu).getView().getLayoutParams().width=width*20/100;
        ajax.id(R.id.ib_mneu).getView().getLayoutParams().height=height*7/100;

        ajax.id(R.id.viewBottom).getView().getLayoutParams().height=height*3/100;

        animZoomIn = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.trans_zoom);
        ajax.id(R.id.ib_mneu).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ajax.id(R.id.ib_mneu).animate(animZoomIn);
                mSlideHolder.toggle();
            }
        });

        menuView.getLayoutParams().width= Utility.getWidth(MainActivity.this)*90/100;
        menuView.setOnItemMenuClickListener(this);
        iv_home.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                       iv_home.setColorFilter(0xffffffff, PorterDuff.Mode.SRC_ATOP);
                        iv_home.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                        goHome(iv_home);
                    case MotionEvent.ACTION_CANCEL: {
                       iv_home.clearColorFilter();
                        iv_home.invalidate();
                        break;
                    }
                }
                return true;
            }
        });




        if (savedInstanceState == null) {
            try {
                ajax.id(R.id.tv_fragmentTitle).text("Home");
                HomeFragment home_fragment = new HomeFragment();
                home_fragment.setHomeClickListener(this);
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.App_container,home_fragment )
                        .commit();
                isHome=true;

            } catch (InflateException e) {
                System.out.println(e.getMessage());
            }

        }
        menuView.setOnItemMenuClickListener(this);



    }

    @Override
    public void setFragmentFromMenu(int id, String title) {

        mSlideHolder.toggle();
        ajax.id(R.id.tv_fragmentTitle).text(title);
        isHome=false;
        try {

        switch(id){
            case R.id.ll_menu_aboutus:
                    getSupportFragmentManager().popBackStack();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.App_container, new AboutUs_Fragment())
                            .commit();
                break;
            case R.id.ll_menu_myschedule:
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.App_container, new HangoutFragment())
                        .commit();
                break;
            case R.id.ll_menu_friendsSchedule:
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.App_container, new CalendarFragment())
                        .commit();
                break;
            case R.id.ll_menu_uploadPhotos:
                photosFragment=new UploadPhotosFragment();
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.App_container,photosFragment)
                        .commit();
                break;
            case R.id.ll_menu_photoWall:
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.App_container,new WallFragment())
                        .commit();
                break;
            case R.id.ll_menu_settings:
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.App_container,new SettingsFragment())
                        .commit();
                break;
        }
        } catch (InflateException e) {
            System.out.println(e.getMessage());
        }
    }



    //method pour afficher le contenu de la page principale.
    public void goHome(View v){
        if(isHome)
            return;
        ajax.id(R.id.tv_fragmentTitle).text("Home");
        menuView.ChangeLayoutToDefault(-1);
        HomeFragment home_fragment = new HomeFragment();
        home_fragment.setHomeClickListener(this);
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.App_container, home_fragment)
                .commit();
        isHome=true;
    }

    @Override
    public void setFragmentFromHome(String title,View v) {
        ajax.id(R.id.tv_fragmentTitle).text(title);
        isHome=false;
        int index=-1;
        try {

            switch(v.getId()){
                case R.id.iv_schedule_home:
                    index=1;
                    getSupportFragmentManager().popBackStack();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.App_container, new HangoutFragment())
                            .commit();
                    break;
                case R.id.iv_contacts_home:
                    index=2;
                    getSupportFragmentManager().popBackStack();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.App_container, new CalendarFragment())
                            .commit();
                    break;

                case R.id.iv_upload_home:
                    index=3;
                    photosFragment= new UploadPhotosFragment();
                    getSupportFragmentManager().popBackStack();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.App_container,photosFragment)
                            .commit();
                    break;
                case R.id.iv_photowall_home:
                    getSupportFragmentManager().popBackStack();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.App_container,new WallFragment())
                            .commit();
                    index=4;
                    break;

            }
            menuView.ChangeLayoutToDefault(index);
        } catch (InflateException e) {
            System.out.println(e.getMessage());
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICK_IMAGE && data != null && data.getData() != null) {
            Uri _uri = data.getData();

            //User had pick an image.
            Cursor cursor = getContentResolver().query(_uri, new String[] { android.provider.MediaStore.Images.ImageColumns.DATA }, null, null, null);
            cursor.moveToFirst();

            //Link to the image
            final String imageFilePath = cursor.getString(0);
            cursor.close();
            if(photosFragment!=null)
                photosFragment.setImage(imageFilePath);
            System.out.println(imageFilePath);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private int counter=0;
    @Override
    public void onBackPressed() {
        if(counter>=1){
            finish();
        }else{
            Utility.showtoast(MainActivity.this,"Press Another time to quit");
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    counter=0;
                }
            }, 3 * 1000);
        }
        counter++;

    }



}
