package edu.ucsb.cs.cs185.photobooth;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class StorageReadWrite {

	public static final String fileHeader = "PhotoBooth_";
	
	//returns true of successful, false if not
	public static boolean store(Context c,Bitmap img){
		File pictureFile = createFile(c);
	    if (pictureFile == null) {
	        Log.d(c.toString(),"Error creating media file, check storage permissions: ");// e.getMessage());
	        return false;
	    } 
	    try {
	        FileOutputStream fos = new FileOutputStream(pictureFile);
	        img.compress(Bitmap.CompressFormat.PNG, 90, fos);
	        fos.close();
	        c.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
	        return true;
	    } catch (FileNotFoundException e) {
	        Log.d(c.toString(), "File not found: " + e.getMessage());
	        return false;
	    } catch (IOException e) {
	        Log.d(c.toString(), "Error accessing file: " + e.getMessage());
	        return false;
	    } 
	}
	
	public static void deleteImg(Context c,int position){
		File file=StoragePath.getSingleFile(c, position);
		boolean deleted = file.delete();
		c.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
		Toast.makeText(c, "Image Deleted",Toast.LENGTH_LONG).show();
	}
	
	public static void changeImgName(Context c,int position){
		File file=StoragePath.getSingleFile(c, position);
		String origPath = file.getPath();
		String filename = origPath.substring(origPath.lastIndexOf('/')+1);
		origPath = origPath.substring(0,origPath.lastIndexOf('/')+1);
		String newFileName = "Img"+filename.substring(filename.indexOf('_'));
		
		File oldFile = new File(origPath + filename);
		File newFile = new File(origPath + newFileName);
		
		oldFile.renameTo(newFile);
		
		c.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
	}
	
	//lint suppressed because we don't really care about the date's format, we just want a filename
	@SuppressLint("SimpleDateFormat")
	private static File createFile(Context c){
	  
	    File mediaStorageDir = new File(StoragePath.get(c)); 

	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            return null;
	        }
	    } 
	    // Create a media file name with a timestamp
	    String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
	    File mediaFile;
	    String mImageName= fileHeader + timeStamp +".png";
	    mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);  
	    return mediaFile;
	} 
	
	
	
}
