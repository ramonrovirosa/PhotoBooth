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
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ShareActionProvider;
import android.widget.Toast;

public class HomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		createDirectory();
		
		GridView gridView=(GridView) findViewById(R.id.gridView);
		gridView.setAdapter(new ImageAdapter(this));
		
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
			int position, long id) {
				
			//System.out.println("position is: " + position);

				
				
			// Sending image id to FullScreenActivity
			Intent i = new Intent(HomeActivity.this, FullImageActivity.class);
			
			Integer pos  = position;
			
			// passing array index
			i.putExtra("Position", position);
//			setResult(RESULT_OK, i);
			//finish();
			startActivity(i);
				
//				Context context = getApplicationContext();
//				CharSequence text = "Hello toast!";
//				int duration = Toast.LENGTH_SHORT;
//				Toast toast = Toast.makeText(context, text, duration);
//				toast.show();
//			
				
				
			}
			});
		
		
		
	}

//	private ShareActionProvider mShareActionProvider;
//
//	// ...
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate menu resource file.
//		getMenuInflater().inflate(R.menu.home, menu);
//		// Locate MenuItem with ShareActionProvider
//		MenuItem item = menu.findItem(R.id.menu_item_share);
//		// Fetch and store ShareActionProvider
//		mShareActionProvider = (ShareActionProvider) item.getActionProvider();
//		// Return true to display menu
//		return true;
//	}
//
//	// Call to update the share intent
//	private void setShareIntent(Intent shareIntent) {
//		if (mShareActionProvider != null) {
//			mShareActionProvider.setShareIntent(shareIntent);
//		}
//	}
	
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

}
