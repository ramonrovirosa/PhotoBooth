package edu.ucsb.cs.cs185.photobooth;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

public class Cropper {

	public static Bitmap crop(byte [] data, boolean isFront){
		
		Bitmap original = BitmapFactory.decodeByteArray(data , 0, data.length);
		if(original==null)return null;
		//these two are backwards from what you expect
		int origHeight = original.getHeight();
		int origWidth = original.getWidth();
		Log.v("camera_debug","Original: height: "+origHeight+" Width: "+origWidth);
		
		double resize_scale = ((double)origHeight)/((double)FilmStripMaker.width);
		
		Bitmap scaled = Bitmap.createScaledBitmap(original,(int) (origWidth/resize_scale),FilmStripMaker.width,false);
		original = null;
		
		Log.v("camera_debug","Scaled: height: "+scaled.getHeight()+" Width: "+scaled.getWidth());
		
		
		Matrix matrix = new Matrix();
		
		
		
		if(isFront){
			Matrix fmatrix = new Matrix();
			fmatrix.postRotate(180);
			scaled = Bitmap.createBitmap(scaled,0,0,
					scaled.getWidth(),scaled.getHeight(),fmatrix,true);
		}
			
		matrix.postRotate(90);
		
			
		
		
		Bitmap cropped = Bitmap.createBitmap(scaled,0,0,
				FilmStripMaker.length,FilmStripMaker.width,null,false);
		scaled = null;
		cropped = Bitmap.createBitmap(cropped,0,0,
				FilmStripMaker.length,FilmStripMaker.width,matrix,true);
		Log.v("camera_debug","Cropped: height: "+cropped.getHeight()+" Width: "+cropped.getWidth());
		
		isFront = false;
		if(isFront){
			matrix = new Matrix();
			matrix.postRotate(180);
			cropped = Bitmap.createBitmap(cropped,0,0,
					cropped.getWidth(),cropped.getHeight(),matrix,true);
		}
		
		return cropped;
	}
	
	public static Bitmap cropButton(Bitmap image, int width){
		
		double scale = (double)image.getWidth()/width;
		int height = (int)(image.getHeight() / scale);
		
		return Bitmap.createScaledBitmap(image, width, height, false);
	}
	
}
