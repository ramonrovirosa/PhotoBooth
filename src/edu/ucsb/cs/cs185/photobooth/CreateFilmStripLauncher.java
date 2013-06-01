package edu.ucsb.cs.cs185.photobooth;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

public class CreateFilmStripLauncher {

	public static Bitmap [] images = null;
	
	public static void Launch(Context c , Bitmap... imgs){
		if(imgs.length<3)return;
		
		images = imgs.clone();
		
		Intent intent = new Intent(c,CreateFilmStrip.class);
		
		((Activity) c).startActivityForResult(intent,1);
	}
	
}

/*	
 * 	put this in class that launches
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  if (requestCode == 1) {
	     if(resultCode == RESULT_OK){      
	         boolean home = data.getBooleanExtra("GoHome",false);
	         if(home){
	        	 finish();
	         }
	     }
	     if (resultCode == RESULT_CANCELED) {    
	         //do nothing
	    	 //back button is pressed
	     }
	  }
	}
 */
