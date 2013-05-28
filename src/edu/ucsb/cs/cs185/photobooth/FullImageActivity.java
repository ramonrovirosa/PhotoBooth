package edu.ucsb.cs.cs185.photobooth;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.Toast;

public class FullImageActivity extends Activity {

	ImageView imageView;
	Uri uri;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.full_image);
//
//		// get intent data
		Intent i = getIntent();
		
		Integer position = i.getIntExtra("Position", 0);
		

		File root = new File(
			    Environment.getExternalStoragePublicDirectory(
			        Environment.DIRECTORY_PICTURES
			    ), 
			    "/PhotoBooth"
			);
		
		File[] fileName = root.listFiles();
		
		uri = Uri.fromFile(fileName[position]);
		
		ImageView imageView = (ImageView)findViewById(R.id.full_image_view);
		//imageView.setImageResource(mThumbIds[position]);
		imageView.setImageURI(uri);
//		imageView.setPadding(0,0,0,0);
//		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		
		//imageView.setLayoutParams(new GridView.LayoutParams(500, 975));
		
		
		
	}


	// ...
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate menu resource file.
		getMenuInflater().inflate(R.menu.home, menu);
		// Locate MenuItem with ShareActionProvider
		MenuItem item = menu.findItem(R.id.menu_item_share);
	
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_item_share:
			// app icon in action bar clicked; go home
			Intent share = new Intent(Intent.ACTION_SEND);
			share.setType("image/*");
			share.putExtra(Intent.EXTRA_STREAM, uri);
			startActivity(Intent.createChooser(share, "Share PhotoBooth Image"));
			
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}


	
}
