package com.comred.mylib.Connection;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


import android.content.Context;
import android.content.SharedPreferences;

public class DownloadPics  extends Thread{
	
	Context context;
	
	String prefExistingName= "Existingpics";
	String prefUnsuccessfulName= "UnsuccessfulDownloadedPics";
	String us="";
	String s= "";
	SharedPreferences.Editor editor;
	SharedPreferences sharedPref;
	
	File Dir = new File(android.os.Environment.getExternalStorageDirectory(),"AllNewPharma");
	List<String> UrlPicsToDownload =new ArrayList<String>();
	List<String> PicsName =new ArrayList<String>();
	List<String> ImagesPaths=new ArrayList<String>();
	List<String> ImagesName=new ArrayList<String>();
	List<String> ExistingIDpics=new ArrayList<String>();
	List<String> unsIDpics=new ArrayList<String>();
	/*
		public DownloadPics(Context context ) {
			// TODO Auto-generated constructor stub
			this.context=context;
			
			if (!Dir.exists())
  	 			Dir.mkdirs();
			sharedPref= PreferenceManager.getDefaultSharedPreferences(context);
			
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			
			prepareData();
			DivideResponse();
			prepareToDownload();
			//retryToDownload();
			ImagesName=imagesName();
			ImagesPaths=imagesPaths();
		}
		private void  prepareData(){
			s= sharedPref.getString(prefExistingName, "");  //copy of the string saved in preferences that contains all the IDs of images already downloaded and saved in sdcard, IDs are separated by the delimiter -*-      
			us= sharedPref.getString(prefUnsuccessfulName, ""); //copy of the string saved in preferences that contains all the urls of images that didn't succeed to be downloaded earlier 
        	Log.d("uns",us);
           	
  	      
  	        String t=s;
  	        Log.d("tag", t);
  	        ExistingIDpics.clear();
  	        while(t.indexOf("-*-")>0){
  	        	ExistingIDpics.add(t.substring(0, t.indexOf("-*-")));
  	        	t=t.substring(t.indexOf("-*-")+3);        	
  	        }
  	        t=us;
  	        unsIDpics.clear();
	  	    while(t.indexOf("-*-")>0){
	  	    	unsIDpics.add(t.substring(0, t.indexOf("-*-")));
		        	t=t.substring(t.indexOf("-*-")+3);        	
	        }
		}
 	    private void DivideResponse(){
 	   
 	        	UrlPicsToDownload.clear();
 	        	PicsName.clear();
 	        	System.out.println("the urls of pics to download are: ");
 	        	List<JsonProduct> temp=JsonProduct.data;
 	        	if (ExistingIDpics.size()==0)
 	        	{
	 	        	for(int i=0;i<temp.size();i++){
	 	        		
	 	        		try{	
	 	        			//String url=(String) img.get();
	 	        			UrlPicsToDownload.add(temp.get(i).Brochure);
	 	        			PicsName.add("D_"+String.valueOf(temp.get(i).ID)+".jpg");
	 	        			Log.d("pics" ,PicsName.get(i) );
		 	        			
	 	        			System.out.println(UrlPicsToDownload.get(i));			
	 	        		}
	 	        		catch (Exception e) {
	 	    				// TODO: handle exception
	 	        			System.out.println("I catched an error while retrieving the picsToDownload "+ e);
	 	        			break;
	 	    			}
	 	        	}
 	        	}else
 	        	{
 	        		if(unsIDpics.size()!=0){
 	        			for(int i=0;i<unsIDpics.size();i++){
 	        				try{	
 		 	        			//String url=(String) img.get();
 	        					JsonProduct prod=getProduct(temp,unsIDpics.get(i));
 		 	        			UrlPicsToDownload.add(prod.Brochure);
 		 	        			PicsName.add("D_"+String.valueOf(prod.ID)+".jpg");
 		 	        			Log.d("pics" ,PicsName.get(i) );
 		 	        			System.out.println(UrlPicsToDownload.get(i));			
 		 	        		}
 		 	        		catch (Exception e) {
 		 	    				// TODO: handle exception
 		 	        			System.out.println("I catched an error while retrieving the picsToDownload "+ e);
 		 	        			break;
 		 	    			}
 	        			}
 	        		}
 	        		else return;
 	        	}
 	        	
 	    }
 	    private void prepareToDownload(){
	    	if(!UrlPicsToDownload.isEmpty()){
	    	for(int i=0; i<UrlPicsToDownload.size();i++){
	    		try{
	    			String downloadFile=Dir.toString()+ "/" + PicsName.get(i);
	    			Log.d("image",downloadFile);
	    		if(!DownloadFromUrl(UrlPicsToDownload.get(i),downloadFile)){
	    			
	    			 try{
	    				 String pic=PicsName.get(i);
	    				 String id=pic.substring(pic.indexOf("_")+1,pic.indexOf(".jpg"));
	 	    				us = us+id+"-*-";
	 	                 	editor.putString(prefUnsuccessfulName, us);
	 	                 	editor.commit();
	 	                 	File f= new File(downloadFile);
	 	                 	f.delete();
	 	                 	f=null;
	                 }
	                 catch (Exception e2) {
	 					// TODO: handle exception
	 				}
	    		}
	    		}
	    		catch (Exception e) {
					// TODO: handle exception
	    			System.out.println("error in preparetoDownload function");
	    		}
	    	}
	    	}
 	    }

 	   private boolean DownloadFromUrl(String imageURL, String fileName) {
 		
 		   if(imageURL==null)return false;
	        try {
	        	    URL url = new URL(imageURL); //you can write here any link
	                File file = new File(fileName);
	                long startTime = System.currentTimeMillis();
	                URLConnection ucon = url.openConnection();

	                InputStream is = ucon.getInputStream();
	                BufferedInputStream bis = new BufferedInputStream(is);
	                //InputStream  bis=  new BufferedInputStream(url.openStream(), 8192);

	                ByteArrayBuffer baf = new ByteArrayBuffer(50);
	                int current = 0;
	                while ((current = bis.read()) != -1) {
	                        baf.append((byte) current);
	                }

	                FileOutputStream fos = new FileOutputStream(file);
	                fos.write(baf.toByteArray());
	                fos.close();
	                Log.d("ImageManager", "download ready in"
	                                + ((System.currentTimeMillis() - startTime) / 1000)
	                                + " sec");
	                
	                Log.d("download",fileName);
	                String id= fileName.substring(fileName.indexOf("_")+1,fileName.indexOf(".jpg"));
	  		       JsonProduct prod=getProduct(JsonProduct.data, id);
	  		       ImagesName.add(prod.Description);
	 		       ImagesPaths.add(fileName);
	  		       ExistingIDpics.add(id); 
	                if(s.equals("")){Log.d("big","s is empty ");
	                		s=id+"-*-";
	                	}
	                else{
	 	               	Log.d("big","s is not empty ");
	 	               	s= s+ id+"-*-";
	                	}
	                editor.putString(prefExistingName, s);
	  		        return true;

	        	
	        } catch (IOException e) {
	                Log.d("ImageManager", "Error: " + e);
	                
	               
	                return false;
	        }
		

	}
 *//*	  private void retryToDownload(){
	    
	    		try {
	    			prepareData();
	    			DivideResponse();
	    			prepareToDownload();
				} catch (Exception e) {
					// TODO: handle exception
				}
	    
	    		
 	  }*//*
 	 private JsonProduct getProduct(List<JsonProduct> temp, String id){
 		
 		for(int i=0;i<temp.size();i++){
 			JsonProduct prod=temp.get(i);
 			if(prod.ID==Integer.parseInt(id))
 				return prod;
 		}
 		return null;
 	}
 	public List<String> imagesName(){
 		return ImagesName;
 	}
 	public List<String> imagesPaths(){
 		return ImagesPaths;
 	}*/
}
