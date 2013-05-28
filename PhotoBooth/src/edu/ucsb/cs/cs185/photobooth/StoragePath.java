package edu.ucsb.cs.cs185.photobooth;

import android.content.Context;
import android.os.Environment;

public class StoragePath {

	//class to hold a single pathname for all activities
	public static String get(Context c){
		return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + 
				"/PhotoBooth";
				
		/*		
		Environment.getExternalStorageDirectory()
        + "/Android/data/"
        + c.getPackageName()
        + "/images";
        */
	}
	
	
	
}
