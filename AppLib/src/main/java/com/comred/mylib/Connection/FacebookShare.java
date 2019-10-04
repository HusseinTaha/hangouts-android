package com.comred.mylib.Connection;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

/**
 * Created by Administrator on 29/08/13.
 */
public class FacebookShare {

    private Context mContext;
    private static Facebook facebook; // App ID For the App
    private String PATH ; // Put Your Video Link Here
    private ProgressDialog mDialog ;
    String TAG="facebook";
    private Handler mPostHandler;
    private Activity activity;


    public FacebookShare(Activity a,Context context,Facebook facebook,ProgressDialog mDialog,Handler mPostHandler){

        this.mContext=context;
        this.mDialog=mDialog;
        this.mPostHandler=mPostHandler;
        this.activity=a;
        this.facebook=facebook;

    }



        public void SetVideoPath(String path){
        this.PATH=path;
    }

    public void Share(){


        facebook.authorize(activity, new String[]{ "user_photos,publish_checkins,publish_actions,publish_stream"}, Facebook.FORCE_DIALOG_AUTH,new Facebook.DialogListener() {
            @Override
            public void onComplete(Bundle values) {
                postVideoonWall();

            }
            @Override
            public void onFacebookError(FacebookError error) {}
            @Override
            public void onError(DialogError e) {}
            @Override
            public void onCancel() {}
        });

    }

    public void postVideoonWall() {
        mDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {

                byte[] data = null;
                InputStream is = null;
                String dataMsg = "Posted By Nanomedia";
                Bundle param;

                try {
                    is = new FileInputStream(PATH);
                    data = readBytes(is);
                    param = new Bundle();
                    param.putString("message", dataMsg);
                    param.putString("filename", "test1.mp4");
                    //param.putString("method", "video.upload");
                    param.putByteArray("video", data);
                    AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner(facebook);
                    mAsyncRunner.request("me/videos", param, "POST", new SampleUploadListener(), null);
                }
                catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public byte[] readBytes(InputStream inputStream) throws IOException {
        // This dynamically extends to take the bytes you read.
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        // This is storage overwritten on each iteration with bytes.
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        // We need to know how may bytes were read to write them to the byteBuffer.
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }

        // And then we can return your byte array.
        return byteBuffer.toByteArray();
    }

    public class SampleUploadListener implements AsyncFacebookRunner.RequestListener {

        public void onComplete(final String response, final Object state) {
            try {
                Log.d("Facebook-Example", "Response: " + response.toString());
                JSONObject json = Util.parseJson(response);
                mPostHandler.sendEmptyMessage(0);
                // then post the processed result back to the UI thread
                // if we do not do this, an runtime exception will be generated
                // e.g. "CalledFromWrongThreadException: Only the original
                // thread that created a view hierarchy can touch its views."
            } catch (JSONException e) {
                mPostHandler.sendEmptyMessage(1);
                Log.w("Facebook-Example", "JSON Error in response");
            } catch (FacebookError e) {
                mPostHandler.sendEmptyMessage(2);
                Log.w("Facebook-Example", "Facebook Error: " + e.getMessage());
            }
        }

        @Override
        public void onIOException(IOException e, Object state) {

        }

        @Override
        public void onFileNotFoundException(FileNotFoundException e, Object state) {

        }

        @Override
        public void onMalformedURLException(MalformedURLException e, Object state) {

        }

        @Override
        public void onFacebookError(FacebookError e, Object state) {
            // TODO Auto-generated method stub
        }



    }


}
