package edu.ucsb.cs.cs185.photobooth;

import java.io.File;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class HomeActivity extends Activity {

	private boolean full_screen_mode = false;
	
	GridView gridView = null;
	ProgressBar progress = null;
	ImageAdapter imgAdptr = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		createDirectory();
		progress = (ProgressBar)findViewById(R.id.progress);
		
		gridView=(GridView) findViewById(R.id.gridView);
		
		//loads threads with async. much cleaner.
		new loadAdapter().execute(this);
	}
	
	private void setListener(){
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
			int position, long id) {
				// Sending image id to FullScreenActivity
				Intent i = new Intent(HomeActivity.this, FullImageActivity.class);
				i.putExtra("Position", position);
				startActivityForResult(i,1);
			}
		});
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_screen, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()) {
		case R.id.full_screen:
			doFullScreen();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private void doFullScreen(){
		full_screen_mode = true;
		Toast.makeText(this, "Full screen mode enabled, press back button to exit.",Toast.LENGTH_LONG).show();
		findViewById(R.id.GalleryButton).setVisibility(View.INVISIBLE);
		findViewById(R.id.CameraButton).setVisibility(View.INVISIBLE);
		ActionBar act = getActionBar();
		act.hide();
	}
	
	@Override
	public void onBackPressed() {
		if(full_screen_mode){
			findViewById(R.id.GalleryButton).setVisibility(View.VISIBLE);
			findViewById(R.id.CameraButton).setVisibility(View.VISIBLE);
			ActionBar act = getActionBar();
			act.show();
			full_screen_mode = false;
		} else {
			super.onBackPressed();
		}
	}
	
	public void galleryClicked(View view){
		Intent intObj = new Intent(this, GridGallery.class);
		startActivityForResult(intObj,1);	
	}
	
	public void takePhotos(View view){		
		//change this to start shervin's
		Intent i = new Intent(this,CameraActivity.class);
		startActivityForResult(i,1);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			if (requestCode == 1) {
				if(resultCode == RESULT_OK){      
			         Boolean modified=data.getBooleanExtra("modified", true); 
			         if(modified){
			        	 new loadAdapter().execute(this);
			         }
			     }
			     if (resultCode == RESULT_CANCELED) {    
			         //do nothing for now
			     }
			}
		}
	
	public void createDirectory() {
		File storageDir = new File(StoragePath.get(this));
		if(!storageDir.exists()){
			storageDir.mkdir();
		}
		sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
	}

	private class loadAdapter extends AsyncTask<Context, ImageAdapter, ImageAdapter>{

		@Override
		protected ImageAdapter doInBackground(Context... c) {
			return new ImageAdapter(c[0]);
		}
		
		@Override
		protected void onProgressUpdate(ImageAdapter... temp){
		}
		
		@Override
	    protected void onPreExecute() {
			progress.setVisibility(View.VISIBLE);
		}
		
		@Override
	    protected void onPostExecute(ImageAdapter result) {
			imgAdptr = result;
			gridView.setAdapter(result);
			setListener();
			progress.setVisibility(View.INVISIBLE);
		}

	}

}
