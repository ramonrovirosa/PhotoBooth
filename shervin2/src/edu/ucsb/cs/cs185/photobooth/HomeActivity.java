package edu.ucsb.cs.cs185.photobooth;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
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
			startActivity(i);
				
			
				
			}
			});
		
		
		
	}
	
	public void galleryClicked(View view){
		
//		Context context = getApplicationContext();
//		CharSequence text = "Gallery Clicked!";
//		int duration = Toast.LENGTH_SHORT;
//		Toast toast = Toast.makeText(context, text, duration);
//		toast.show();
		
		Intent intObj = new Intent(this, GridGallery.class);
		startActivity(intObj);
		
	}
	
	public void takePhotos(View view){
		Context context = getApplicationContext();
		CharSequence text = "Take Photos!";
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
		
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

//	@Override
//    protected void onDestroy() {
//    super.onDestroy();
//
//    unbindDrawables(findViewById(R.id.RootView));
//    System.gc();
//    }

//    private void unbindDrawables(View view) {
//    	Context context = getApplicationContext();
//		CharSequence text = "unbindDrawables!";
//		int duration = Toast.LENGTH_SHORT;
//		Toast toast = Toast.makeText(context, text, duration);
//		toast.show();
//    	
//    	
//    	if (view.getBackground() != null) {
//        view.getBackground().setCallback(null);
//        }
//        if (view instanceof ViewGroup) {
//            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
//            unbindDrawables(((ViewGroup) view).getChildAt(i));
//            }
//        ((ViewGroup) view).removeAllViews();
//        }
//    }
	
	

}
