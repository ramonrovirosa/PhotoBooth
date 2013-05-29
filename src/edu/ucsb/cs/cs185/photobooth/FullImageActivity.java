package edu.ucsb.cs.cs185.photobooth;

import java.io.File;
import java.util.Collections;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class FullImageActivity extends Activity {

	ImageView imageView;
	Uri uri;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.full_image);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		Intent i = getIntent();
		Integer position = i.getIntExtra("Position", 0);
		
		uri = Uri.fromFile(StoragePath.getSingleFile(this, position));
		ImageView imageView = (ImageView) findViewById(R.id.full_image_view);
		//imageView.setImageResource(mThumbIds[position]);
		imageView.setImageURI(uri);
	}

	// ...
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate menu resource file.
		getMenuInflater().inflate(R.menu.home, menu);
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
		case android.R.id.home:
			finish();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

}