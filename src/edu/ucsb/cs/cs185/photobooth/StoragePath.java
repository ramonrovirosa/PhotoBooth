package edu.ucsb.cs.cs185.photobooth;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class StoragePath {

	/**
	 * Returns a single filepath to the film strip directory.
	 * To be used by all activities.
	 * 
	 * todo: change path tail to '/' + string_app_name
	 * 
	 * @param context
	 * @return path to storage directory
	 */
	public static String get(Context c){
		return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + 
				"/PhotoBooth";		
	}
	
	/**
	 * Returns an array list of all pngs in reverse order.
	 * Reverse order because we want newest images first.
	 * 
	 * @param context
	 * @return ArrayList<File> of probably valid film strips
	 */
	public static ArrayList<File> getFileList(Context c){
		File root = new File(StoragePath.get(c));
		File [] fileNames = root.listFiles();
		
		//this check is needed because some s return null if there are no files in the path
		if(fileNames == null) return new ArrayList<File>();
		
		ArrayList<File> validFiles = new ArrayList<File>();
		
		//place all pngs into validFiles
		for(int i=0;i<fileNames.length;i++){
			String name = fileNames[i].getName();
			if(isValidImage(name)){
				validFiles.add(fileNames[i]);
			}
		}
		Collections.reverse(validFiles);
		return validFiles;
	}
	
	/**
	 * DO NOT iterate through this.
	 * It creates the entire list first then indexes the array list.
	 * If you need multiple files use getFileList and iterate on that
	 * 
	 * @param context
	 * @param index of file
	 * @return file at index
	 */
	public static File getSingleFile(Context c,int index){
		return getFileList(c).get(index);
	}
	
	/**
	 * Verify the file is close enough to the correct format.
	 * If the user intentionally puts files in the directory that are not pngs but still follow the format...
	 * They might cause crashes but fuck 'em for now because there's nothing we can do.
	 * 
	 * @param name of file
	 * @return boolean if valid
	 */
	@SuppressLint("DefaultLocale")
	private static boolean isValidImage(String name){
		Log.d("StoragePath",name);
		return (
				name.startsWith(StorageReadWrite.fileHeader) &&
				name.toLowerCase().endsWith(".png")
			);
	}
	
}
