package edu.ucsb.cs.cs185.photobooth;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

public class HomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		createDirectory();
		
		GridView gridView=(GridView) findViewById(R.id.gridView);
		gridView.setAdapter(new ImageAdapter(this));
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
	
	public void createDirectory() {
		File storageDir = new File(
			    Environment.getExternalStoragePublicDirectory(
			        Environment.DIRECTORY_PICTURES
			    ), 
			    "/PhotoBooth"
			);
		
		System.out.println(storageDir);
		
		if(!storageDir.exists()){
				storageDir.mkdir();
				System.out.println("making photoBooth directory");
		}
		
		//sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+storageDir)));
		sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
		
	}

    public void Camera(View view){
        Intent myIntent = new Intent(HomeActivity.this, CameraActivity.class);
        //myIntent.putExtra("key", value);
        HomeActivity.this.startActivity(myIntent);
    }

}
