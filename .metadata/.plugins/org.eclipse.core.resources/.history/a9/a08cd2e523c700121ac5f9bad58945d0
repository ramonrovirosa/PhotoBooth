package cs185.mousavi.alex.filmstripmaker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

public class StorageReadWrite {

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
	        return true;
	    } catch (FileNotFoundException e) {
	        Log.d(c.toString(), "File not found: " + e.getMessage());
	        return false;
	    } catch (IOException e) {
	        Log.d(c.toString(), "Error accessing file: " + e.getMessage());
	        return false;
	    } 
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
	    String mImageName="Film_"+ timeStamp +".png";
	    mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);  
	    return mediaFile;
	} 
	
}
