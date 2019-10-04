package com.comred.mylib.Connection;

import com.comred.mylib.Utility;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;


import java.io.InputStream;

/**
 * Created by Administrator on 02/09/13.
 */
public class DownloadImageTask  extends AsyncTask<String, Void, Bitmap> {



        private ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
            Utility.clearAllResources();
        }

        public Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Utility.clearAllResources();
//            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
//                mIcon11 = BitmapFactory.decodeStream(in);
                return BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
//            return mIcon11;
            Utility.clearAllResources();
            return null;
        }

        protected void onPostExecute(Bitmap result) {
            Utility.clearAllResources();
            bmImage.setImageBitmap(result);
            Utility.clearAllResources();
        }

}
