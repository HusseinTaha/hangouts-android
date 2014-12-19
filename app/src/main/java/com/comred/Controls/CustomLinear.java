package com.comred.Controls;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.androidquery.AQuery;
import com.comred.Glob.Global;
import com.comred.Listeners.MenuClickListener;
import com.comred.hangout.R;

/**
 * Created by HTaha on 11/22/13.
 * the view that manage the menu control in the slider control of the main activity.
 */
public class CustomLinear extends RelativeLayout implements View.OnClickListener {


    /**
     * variables Declarations
     */


    private String[] menu_titles = new String[]{"About Us", "My Schedule", "Friends Schedule", "Upload Photos",
            "Photo Wall", "Settings"};

    private int[] drawables_ids = new int[]{R.drawable.aboutus_dark, R.drawable.schedule_dark, R.drawable.contacts_dark,
            R.drawable.upload_dark
            , R.drawable.photowall_dark, R.drawable.settings_dark};

    private int[] drawablesOn_ids = new int[]{R.drawable.aboutus_dark_on, R.drawable.schedule_dark_on, R.drawable.contacts_dark_on,
            R.drawable.upload_dark_on
            , R.drawable.photowall_dark_on, R.drawable.settings_dark_on};

    private int[] layout_ids = new int[]{R.id.ll_menu_aboutus, R.id.ll_menu_myschedule, R.id.ll_menu_friendsSchedule, R.id.ll_menu_uploadPhotos
            , R.id.ll_menu_photoWall, R.id.ll_menu_settings};

    private int[] textView_ids = new int[]{R.id.tv_menu_aboutus, R.id.tv_menu_myschedule, R.id.tv_menu_friendsSchedule, R.id.tv_menu_uploadPhotos
            , R.id.tv_menu_photoWall, R.id.tv_menu_settings};

    private int[] imageView_ids = new int[]{R.id.iv_menu_aboutus, R.id.iv_menu_myschedule, R.id.iv_menu_friendsSchedule, R.id.iv_menu_uploadPhotos
            , R.id.iv_menu_photoWall, R.id.iv_menu_settings};

    private AQuery ajax;
    private MenuClickListener menuClickListener;

    /**
     * Constructor
     *
     * @param context context of the view .
     * @param attrs   attribute set of the view.
     */
    public CustomLinear(Context context, AttributeSet attrs) {
        super(context, attrs);

        int height = Global.getheight();
        int width = Global.getwidth();
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = layoutInflater.inflate(R.layout.menu_list, null);
        if (root != null) {
            ajax = new AQuery(root);
            for (int i = 0; i < textView_ids.length; i++) {
                ajax.id(textView_ids[i]).typeface(Global.getTypeFace());
                ajax.id(layout_ids[i]).clicked(this);
                try {
                    ajax.id(layout_ids[i]).getView().getLayoutParams().height = height * 14 / 100;
                    ajax.id(textView_ids[i]).getTextView().setTextSize(TypedValue.COMPLEX_UNIT_PX, width * 5 / 100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            this.addView(root);
        }
    }


    public void ChangeLayoutToDefault(int withoutID) {


        for (int i = 0; i < layout_ids.length; i++) {
            ajax.id(layout_ids[i]).enabled(true).backgroundColor(getResources().getColor(R.color.white));
            ajax.id(textView_ids[i]).textColor(getResources().getColor(R.color.DarkBlue_bottom_top));
            ajax.id(imageView_ids[i]).image(drawables_ids[i]);
        }
        if (withoutID < 0 || withoutID > 5)
            return;
        ajax.id(layout_ids[withoutID]).enabled(false).backgroundColor(getResources().getColor(R.color.DarkBlue_bottom_top));
        ajax.id(textView_ids[withoutID]).textColor(getResources().getColor(R.color.white));
        ajax.id(imageView_ids[withoutID]).image(drawablesOn_ids[withoutID]);

    }

    @Override
    public void onClick(View view) {

        int clickedIndex = -1;
        switch (view.getId()) {
            case R.id.ll_menu_aboutus:
                clickedIndex = 0;
                break;
            case R.id.ll_menu_myschedule:
                clickedIndex = 1;
                break;
            case R.id.ll_menu_friendsSchedule:
                clickedIndex = 2;
                break;
            case R.id.ll_menu_uploadPhotos:
                clickedIndex = 3;
                break;

            case R.id.ll_menu_photoWall:
                clickedIndex = 4;
                break;
            case R.id.ll_menu_settings:
                clickedIndex = 5;
                break;
        }
        if (clickedIndex == -1) return;
        this.menuClickListener.setFragmentFromMenu(layout_ids[clickedIndex], menu_titles[clickedIndex]);
        ChangeLayoutToDefault(clickedIndex);
    }


    public void setOnItemMenuClickListener(MenuClickListener menu) {
        this.menuClickListener = menu;
    }


}
