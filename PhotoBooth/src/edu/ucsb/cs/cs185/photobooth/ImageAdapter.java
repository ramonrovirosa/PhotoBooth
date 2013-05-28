package edu.ucsb.cs.cs185.photobooth;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Environment;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
	private Context mContext;

	File root = new File(
		    Environment.getExternalStoragePublicDirectory(
		        Environment.DIRECTORY_PICTURES
		    ), 
		    "/PhotoBooth"
		);
	public File[] fileName = root.listFiles();
	int count = fileName.length;

	public ImageAdapter(Context c) {
		mContext = c;
	}

	public int getCount() {
		return fileName.length;
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		Uri uri = Uri.fromFile(fileName[position]);
		
		Bitmap myBitmap=decodeFile(fileName[position]);
		
		
		
		ImageView imageView = new ImageView(mContext);
		//imageView.setImageResource(mThumbIds[position]);
		//imageView.setImageURI(uri);
		imageView.setImageBitmap(myBitmap);
		imageView.setPadding(0,0,0,0);
		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		
		imageView.setLayoutParams(new GridView.LayoutParams(getViewWidth(), 975));
		//convertView.setBackgroundResource(R.drawable.redstage);
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
	        BitmapFactory.Options o = new BitmapFactory.Options();
	        o.inJustDecodeBounds = true;
	        BitmapFactory.decodeStream(new FileInputStream(f),null,o);

	        //Find the correct scale value. It should be the power of 2.
	        final int REQUIRED_SIZE=70;
	        int width_tmp=o.outWidth, height_tmp=o.outHeight;
	        int scale=1;
	        while(true){
	            if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
	                break;
	            width_tmp/=8;
	            height_tmp/=8;
	            scale*=2;
	        }

	        BitmapFactory.Options o2 = new   BitmapFactory.Options();
	        o2.inSampleSize=scale;
	        return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
	   } catch (FileNotFoundException e) {}
	    return null;
	}
	  
	  


}