package com.comred.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.comred.Controls.CstmTextView;
import com.comred.Glob.Global;
import com.comred.JsonDATA.JsonWall;
import com.comred.hangout.R;
import com.comred.mylib.Crypto.StringXORer;
import com.comred.urlimageviewhelper.UrlImageViewCallback;
import com.comred.urlimageviewhelper.UrlImageViewHelper;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by HTaha on 12/19/13.
 * adapter used to manage the wall fragment.
 * contains photo, name and date.
 */
public class WallAdapters extends BaseAdapter {


    private Context mContext;
    private static long ID=0;
    private LinkedList<JsonWall> data;

    private int width;
    private int height;

    private String url;


    public WallAdapters(Context context, List<JsonWall> data){
        this.mContext=context;
        this.data=new LinkedList<JsonWall>();
        this.data.addAll(data);

        this.width= Global.getwidth();
        this.height=Global.getheight();
        StringXORer stringXORer = new StringXORer();
        String x = stringXORer.decode(StringXORer.ReverseS(Global.URL_PHOTO()), context.getPackageName());
        url=x;
    }


    public void add(List<JsonWall> dat){
        this.data.addAll(dat);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        View row = convertView;
        CstmTextView tv_name,tv_date;
        ImageView imageView;
        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(R.layout.wall_item, viewGroup, false);
        }
        tv_name = (CstmTextView) row.findViewById(R.id.tv_name);
        tv_date = (CstmTextView) row.findViewById(R.id.tv_date);
        imageView = (ImageView) row.findViewById(R.id.iv_photo);

        tv_name.setText(data.get(i).Name);
        tv_date.setText(data.get(i).Date);

        try{
            int clID=data.get(i).ClientID;
            imageView.setAnimation(null);
            // yep, that's it. it handles the downloading and showing an interstitial image automagically.
            // load the image automatically.
            UrlImageViewHelper.setUrlDrawable(imageView,url+clID+"/"+ data.get(i).Photo, R.drawable.loading, new UrlImageViewCallback() {
                @Override
                public void onLoaded(ImageView imageView, Bitmap loadedBitmap, String url, boolean loadedFromCache) {
                    if (!loadedFromCache) {
                        ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1, ScaleAnimation.RELATIVE_TO_SELF, .5f, ScaleAnimation.RELATIVE_TO_SELF, .5f);
                        scale.setDuration(300);
                        scale.setInterpolator(new OvershootInterpolator());
                        imageView.startAnimation(scale);
                    }
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        return row;
    }


}
