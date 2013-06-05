package edu.ucsb.cs.cs185.photobooth;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;

public abstract class CreateFilmStripLauncher extends Activity {

	public static Bitmap [] images = null;
	
	public void Launch(Bitmap... imgs){
		//validity check
		if(imgs.length<3)return;
		//validity check #2
		for(int i=0;i<imgs.length;i++){
			if(imgs[i] == null || FilmStripMaker.checkDimensions(imgs[i].getWidth(), imgs[i].getHeight()) )
				return;
		}
		
		images = imgs.clone();
		
		Intent intent = new Intent(this,CreateFilmStrip.class);
		
		startActivityForResult(intent,1);
	}
	
	@Override
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
}
