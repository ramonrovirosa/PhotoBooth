package edu.ucsb.cs.cs185.photobooth;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

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
		imageView = (ImageView) findViewById(R.id.full_image_view);
		getPhoto();
		
	}

	private void getPhoto(){
		uri = Uri.fromFile(StoragePath.getSingleFile(this, position));
		imageView.setImageURI(uri);
	}
	
	// ...
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate menu resource file.
		getMenuInflater().inflate(R.menu.home, menu);
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

		case R.id.menu_item_delete:
			deleteImage();
			return true;
		case android.R.id.home:
			finishResult(false);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onBackPressed() {
		finishResult(false);
	}
	
	private void finishResult(boolean result){
		Intent returnIntent = new Intent();
		returnIntent.putExtra("modified",result);
		setResult(RESULT_OK,returnIntent);     
		finish();
	}
	
	public void deleteImage(){
		
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
	    alertDialog.setTitle("Delete Image?");
	    String message = "";
	    message += "Press \"Delete\" to permanently erase the image.\n";
	    message += "Press \"Remove\" to no longer display the image in the app.";
	    alertDialog.setMessage(message);
	    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Delete", new DialogInterface.OnClickListener() {
	    	public void onClick(DialogInterface dialog, int id) {
	    		StorageReadWrite.deleteImg(getApplicationContext(), position);
	    		finishResult(true);
	    	} 
	    }); 
	    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
	    	public void onClick(DialogInterface dialog, int id) {
	    	dialog.dismiss();
	    	}
	    }); 
	    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Remove", new DialogInterface.OnClickListener() {
	    	public void onClick(DialogInterface dialog, int id) {
	    		StorageReadWrite.changeImgName(getApplicationContext(), position);
	    		finishResult(true);
	    	}
	    });
	    alertDialog.show();
	}
}