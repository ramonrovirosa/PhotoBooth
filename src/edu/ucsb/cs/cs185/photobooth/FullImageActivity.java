package edu.ucsb.cs.cs185.photobooth;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

public class FullImageActivity extends Activity {

	ImageView imageView;
	Uri uri;
	int position;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.full_image);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		Intent i = getIntent();
		Integer pos = i.getIntExtra("Position", 0);
		position = pos;
		uri = Uri.fromFile(StoragePath.getSingleFile(this, pos));
		ImageView imageView = (ImageView) findViewById(R.id.full_image_view);
		//imageView.setImageResource(mThumbIds[position]);
		imageView.setImageURI(uri);
	}

	// ...
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate menu resource file.
		getMenuInflater().inflate(R.menu.home, menu);
		MenuItem item=menu.findItem(R.id.menu_item_share);
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
			
		case R.id.menu_item_delete:
			deleteImage();
<<<<<<< HEAD
			finish();
=======
<<<<<<< HEAD
			finish();
=======
			Intent intObj=new Intent(this, HomeActivity.class);
			startActivity(intObj);
			return true;
>>>>>>> d5997fcc5ebca7fbd4ea0aa32b77ee67b3286aef
>>>>>>> 00e723761f97751ec99c559a62630438ec0991cc
		case android.R.id.home:
			finish();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	public void deleteImage(){
		File file=StoragePath.getSingleFile(this, position);
		boolean deleted = file.delete();
		
		sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
		Toast.makeText(this, "Image Deleted",Toast.LENGTH_LONG).show();

		
		
	}

}