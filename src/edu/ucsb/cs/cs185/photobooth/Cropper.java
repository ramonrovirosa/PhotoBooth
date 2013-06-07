package edu.ucsb.cs.cs185.photobooth;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

public class Cropper {

	public static Bitmap crop(byte [] data){
		
		Bitmap original = BitmapFactory.decodeByteArray(data , 0, data.length);
		
		//these two are backwards from what you expect
		int origHeight = original.getHeight();
		int origWidth = original.getWidth();
		Log.v("camera_debug","Original: height: "+origHeight+" Width: "+origWidth);
		
		double resize_scale = ((double)origHeight)/((double)FilmStripMaker.width);
		
		Bitmap scaled = Bitmap.createScaledBitmap(original,(int) (origWidth/resize_scale),FilmStripMaker.width,false);
		original = null;
		
		Log.v("camera_debug","Scaled: height: "+scaled.getHeight()+" Width: "+scaled.getWidth());
		
		Matrix matrix = new Matrix();
		matrix.postRotate(90);
		
		Bitmap cropped = Bitmap.createBitmap(scaled,0,0,
				FilmStripMaker.length,FilmStripMaker.width,null,false);
		scaled = null;
		cropped = Bitmap.createBitmap(cropped,0,0,
				FilmStripMaker.length,FilmStripMaker.width,matrix,true);
		Log.v("camera_debug","Cropped: height: "+cropped.getHeight()+" Width: "+cropped.getWidth());
		
		return cropped;
	}
	
}
