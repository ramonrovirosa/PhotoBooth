package edu.ucsb.cs.cs185.photobooth;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

public class CreateFilmStripLauncher {

	public static Bitmap [] images = null;
	
	public static void Launch(Context c , Bitmap... imgs){
		if(imgs.length<3)return;
		
		images = imgs.clone();
		
		Intent intent = new Intent(c,CreateFilmStrip.class);
		
		c.startActivity(intent);
	}
	
}
