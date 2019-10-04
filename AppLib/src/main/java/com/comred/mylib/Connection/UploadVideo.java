package com.comred.mylib.Connection;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
/**
 * Created by Administrator on 03/09/13.
 */
public class UploadVideo extends  Thread {
    Context context;
    String videoPath;
    String URLString="";
    Handler Uihandler;
    Message msg = new Message();
    String json=null;
    private String Resp;

    public UploadVideo(Context context,String path,Handler Uihandler){

        this.context = context;
        this.videoPath=path;
        this.Uihandler=Uihandler;
    }
    public void setURL(String url){
        this.URLString=url;
    }
    public void setJson(String js){
        this.json=new String();
        this.json=js;
    }


    @Override
    public void run() {
        super.run();

        doFileUpload();

        Uihandler.sendMessage(msg);
    }

    private void doFileUpload(){

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        DataInputStream inStream = null;


        String exsistingFileName = videoPath;
        // Is this the place are you doing something wrong.

        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary =  "*****";


        int bytesRead, bytesAvailable, bufferSize;

        byte[] buffer;

        int maxBufferSize = 1*1024*1024;




        try
        {


            Log.e("MediaPlayer", "Inside second Method");

            FileInputStream fileInputStream = new FileInputStream(new File(exsistingFileName) );



            URL url = new URL(URLString);

            conn = (HttpURLConnection) url.openConnection();

            conn.setDoInput(true);

            // Allow Outputs
            conn.setDoOutput(true);

            // Don't use a cached copy.
            conn.setUseCaches(false);

            // Use a post method.
            conn.setRequestMethod("POST");

            conn.setRequestProperty("Connection", "Keep-Alive");

            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);


            dos = new DataOutputStream( conn.getOutputStream() );

            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"File\";filename=\"" + exsistingFileName +"\"" + lineEnd);
            dos.writeBytes(lineEnd);

            Log.e("MediaPlayer","Headers are written");



            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];



            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0)
            {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }



            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos
                    .writeBytes("Content-Disposition: form-data; name=\"json\"" + lineEnd);
            dos
                    .writeBytes("Content-Type: text/plain ; charset=utf-8"
                            + lineEnd);
            dos.writeBytes(lineEnd);

            dos.write(json.getBytes("utf-8"));
            // outputStream.writeBytes(decodeUTF8(kv[1].getBytes()));
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            /*BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            conn.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null)
                tv.append(inputLine);*/




            // close streams
            Log.e("MediaPlayer","File is written");
            fileInputStream.close();
            dos.flush();
            dos.close();


        }
        catch (MalformedURLException ex)
        {
            Log.e("MediaPlayer", "error: " + ex.getMessage(), ex);
        }

        catch (IOException ioe)
        {
            Log.e("MediaPlayer", "error: " + ioe.getMessage(), ioe);
        }


        //------------------ read the SERVER RESPONSE


        Bundle b = new Bundle();

        try {
            inStream = new DataInputStream ( conn.getInputStream() );
            String str="";
            Resp="";

            while (( str = inStream.readLine()) != null)
            {
                Log.e("MediaPlayer","Server Response"+str);
                Resp=Resp+str;
            }
                /*while((str = inStream.readLine()) !=null ){

                }*/
            if (Uihandler != null) {
                b.putString("Response", Resp);
                Log.d("resp",Resp);
                b.putBoolean("hasError", false);
                msg.setData(b);

            }
            inStream.close();


        }
        catch (IOException ioex){
            Log.e("MediaPlayer", "error: " + ioex.getMessage(), ioex);
        }



    }


}