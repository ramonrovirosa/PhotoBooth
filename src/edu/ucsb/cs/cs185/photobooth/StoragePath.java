package edu.ucsb.cs.cs185.photobooth;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;
import android.os.Environment;

public class StoragePath {

	//returns a single pathname for all activities
	public static String get(Context c){
		return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + 
				"/PhotoBooth";
				
	}
	
	//returns an array list of all pngs in reverse order
	public static ArrayList<File> getFileList(Context c){
		File root = new File(StoragePath.get(c));
		File [] fileNames = root.listFiles();
		ArrayList<File> validFiles = new ArrayList<File>();
		
		//place all pngs into validFiles
		for(int i=0;i<fileNames.length;i++){
			String path = fileNames[i].getAbsolutePath();
			if(path.substring(path.lastIndexOf(".")+1).equalsIgnoreCase("png")){
				validFiles.add(fileNames[i]);
			}
		}
		Collections.reverse(validFiles);
		return validFiles;
	}
	
	public static File getSingleFile(Context c,int index){
		return getFileList(c).get(index);
	}
	
}
