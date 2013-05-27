package edu.ucsb.cs.cs185.photobooth;

import java.io.File;

import android.content.Context;
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
		
		ImageView imageView = new ImageView(mContext);
		//imageView.setImageResource(mThumbIds[position]);
		imageView.setImageURI(uri);
		imageView.setPadding(0,0,0,0);
		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		
		imageView.setLayoutParams(new GridView.LayoutParams(getViewWidth(), 975));
		//convertView.setBackgroundResource(R.drawable.redstage);
		return imageView;
	}
	int width;
	public int getViewWidth(){
		WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = size.x;

		return (int) (width*.75);
	}


}