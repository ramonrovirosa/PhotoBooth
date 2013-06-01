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

public class ExampleLaunch extends CreateFilmStripLauncher {

	
	//now when you extend CreateFilmStripLauncher
	//it will take car of launching CreateFilmStrip
	//with ending the activity or not when you return from it
	
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
	
	public void LaunchBtnPress(View v){
		//test mod for eclipse
		//this is how you would launch it. the launcher saves the testpics as static to pass
		//to CreateFilmStrip because they are too large to bundle
		Launch(testpic,testpic,testpic);
	}
	
	
	
	

}
