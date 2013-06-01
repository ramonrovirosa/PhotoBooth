package edu.ucsb.cs.cs185.photobooth;

import java.io.IOException;
import java.io.InputStream;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;

public class ExampleLaunch extends Activity {

	
	private Bitmap testpic;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//for now, get the testpic from the assets
		try {
            InputStream is = getAssets().open("testpic.png");
            testpic = BitmapFactory.decodeStream(is);
            is.close();
            is = null;
        }
        catch(IOException ex) {
            
        }
		setContentView(R.layout.activity_example_launch);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.example_launch, menu);
		return true;
	}
	
	public void Launch(View v){
		//test mod for eclipse
		//this is how you would launch it. the launcher saves the testpics as static to pass
		//to CreateFilmStrip because they are too large to bundle
		CreateFilmStripLauncher.Launch(this,testpic,testpic,testpic);
	}
	
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
