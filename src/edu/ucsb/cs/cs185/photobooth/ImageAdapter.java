package edu.ucsb.cs.cs185.photobooth;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {

	private Context mContext;
	private File root = null;
	private File [] fileName;
	private ArrayList<Bitmap> resizedImages;
	private ArrayList<File> validFiles = null;
	private int count;

	public ImageAdapter(Context c) {
		mContext = c;
		validFiles = StoragePath.getFileList(mContext);
		count = validFiles.size();
		resizedImages = new ArrayList<Bitmap>();
		for(int i=0;i<count;i++){
			Bitmap addme = decodeFile(validFiles.get(i));
			if(addme!=null){
				resizedImages.add(addme);
			}
		}

	}

	public File getFile(int i){
		return validFiles.get(i);
	}


	public int getCount() {
		return count;
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Bitmap myBitmap = resizedImages.get(position);

		ImageView imageView = new ImageView(mContext);
		imageView.setImageBitmap(myBitmap);
		imageView.setPadding(0,0,0,0);
		imageView.setScaleType(ImageView.ScaleType.CENTER);
		imageView.setLayoutParams(new GridView.LayoutParams(getViewWidth(), GridView.LayoutParams.WRAP_CONTENT));
		return imageView;
	}

	public int getViewWidth(){
		WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		return (int) (width*.75);
	}

	//decodes image and scales it to reduce memory consumption
	  private Bitmap decodeFile(File f){
	     try {
	        //decode image size
	        BitmapFactory.Options options= new BitmapFactory.Options();
	        options.inJustDecodeBounds = true;
	        BitmapFactory.decodeStream(new FileInputStream(f),null,options);
	        int newWidth = getViewWidth();
	        int newHeight = (int)(((double)newWidth / (double)options.outWidth) * (double)options.outHeight);
	        return Bitmap.createScaledBitmap(BitmapFactory.decodeStream(new FileInputStream(f)), newWidth, newHeight, true);
	   } catch (FileNotFoundException e) {}
	     return null;
	}



}