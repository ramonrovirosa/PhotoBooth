package edu.ucsb.cs.cs185.photobooth;

import java.io.IOException;
import java.io.InputStream;

import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class CreateFilmStrip extends Activity {

	Bitmap selectedFilm = null;
	Bitmap [] filmCache = new Bitmap[FilmStripMaker.num_filters];
	Bitmap [] imgs = new Bitmap[3];
	Bitmap film;
	
	ImageView imgview = null;
	ProgressBar progress = null;
	ActionBar act = null;
	
	private boolean actionBarEnabled = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_film_strip);
		initActionBar();
		//initialize UI components
		imgview = (ImageView)findViewById(R.id.imageView1);
		progress = (ProgressBar)findViewById(R.id.progressBar1);
		//load images
		getImages();
		//load an image to start
		initFirst();
	}

	@SuppressLint("NewApi")
	private void initActionBar(){
		//enable action bar back button if in correct api version
		if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB){
			act = getActionBar();
			act.setDisplayHomeAsUpEnabled(true);
		}
	}
	
	private void initFirst(){
		setLoadImage(FilmStripMaker.NORMAL);
	}
	
	private void getImages(){
		//get the filmstrip from the assets
		try {
            InputStream is = getAssets().open("filmstrip.png");
            film = BitmapFactory.decodeStream(is);
            is.close();
            is = null;
        }
        catch(IOException ex) {
        	//do something for error?
        }
		//get img array
		for(int i=0;i<imgs.length;i++){
			imgs[i] = CreateFilmStripLauncher.images[i];
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_film_strip, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		if(!actionBarEnabled)return super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		//get which option is pressed and create/set that image
		case R.id.normal:
			setLoadImage(FilmStripMaker.NORMAL);
			return true;
		case R.id.sepia:
			setLoadImage(FilmStripMaker.SEPIA);
			return true;
		case R.id.retro:
			setLoadImage(FilmStripMaker.RETRO);
			return true;
		case R.id.invert:
			setLoadImage(FilmStripMaker.INVERT);
			return true;
		case android.R.id.home:
			//if back button is pressed, end activity
			finish();
			return true;
		case R.id.save:
			saveImage();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private void saveImage(){
		if(selectedFilm == null){
			//image hasn't loaded yet or something has gone wrong
			//warn the user
			alert("No Image Available","No image is available yet.","Wait","Exit");
            return;
		}
		new StoreImage().execute(this);
		//StorageReadWrite.store(this, selectedFilm);
		
	}
	
	private void setLoadImage(int num){
		if(filmCache[num]==null){
			new MakeFilmStrip().execute(num);
		} else {
			selectedFilm = filmCache[num];
			imgview.setImageBitmap(selectedFilm);
		}
	}
	
	private class MakeFilmStrip extends AsyncTask<Integer, Integer, Bitmap> {

		@Override
		protected Bitmap doInBackground(Integer... args) {
			Bitmap ret = FilmStripMaker.make(film, imgs, args[0]);
			filmCache[args[0]] = ret;
			return ret;
		}
		
		@Override
		protected void onPostExecute(Bitmap result){
			selectedFilm = result;
			imgview.setImageBitmap(selectedFilm);
			progress.setVisibility(View.INVISIBLE);
			actionBarEnabled = true;
		}
		
		@Override
	    protected void onPreExecute() {
			actionBarEnabled = false;
			selectedFilm = null;
			imgview.setImageBitmap(null);
			progress.setVisibility(View.VISIBLE);
		}
		
		@Override
	    protected void onProgressUpdate(Integer... args) {
	    }	
	}
	
	private class StoreImage extends AsyncTask<Context, Void, Boolean>{

		@Override
		protected Boolean doInBackground(Context... params) {
			return StorageReadWrite.store(params[0], selectedFilm);
		}
		
		@Override
	    protected void onPreExecute() {
			actionBarEnabled = false;
			progress.setVisibility(View.VISIBLE);
			imgview.setAlpha(.5f);
		}
		
		@Override
	    protected void onPostExecute(Boolean result) {
			progress.setVisibility(View.INVISIBLE);
			actionBarEnabled = true;
			String msg = "";
			if(result){
				msg = "Film strip saved succesfully.";
			} else {
				msg = "Film strip failed to save.";
			}
			imgview.setAlpha(1f);
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
		}
		
	}
	
	
	private void alert(String... args){
		//args: title, message, negative button, quit button
		if(args.length < 3)
			return;
		boolean quitBtn;
		quitBtn = args.length > 3;
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(args[0]);
        builder.setMessage(args[1]);
        builder.setPositiveButton(args[2],
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog,
                        int which) {
                    dialog.dismiss();
                }
            });
        if(quitBtn){
        	builder.setNegativeButton(args[3],
    			new DialogInterface.OnClickListener() {
	    			@Override
	                public void onClick(DialogInterface dialog,
	                		int which) {
	    				finish();
	                }
	            });
        }
        AlertDialog alert = builder.create();
        alert.show();
	}
}
