package edu.ucsb.cs.cs185.photobooth;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
	private Context mContext;

	// Keep all Images in array
	public Integer[] mThumbIds = { R.drawable.filmstrip, 
			R.drawable.filmstrip, R.drawable.filmstrip, R.drawable.filmstrip,
			R.drawable.filmstrip, R.drawable.filmstrip, R.drawable.filmstrip,
			R.drawable.filmstrip, R.drawable.filmstrip, R.drawable.filmstrip,
			R.drawable.filmstrip, R.drawable.filmstrip, R.drawable.filmstrip,
			R.drawable.filmstrip 
	};

	// Constructor
	public ImageAdapter(Context c) {
		mContext = c;
	}

	@Override
	public int getCount() {
		return mThumbIds.length;
	}

	@Override
	public Object getItem(int position) {
		return mThumbIds[position];
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView = new ImageView(mContext);
		imageView.setImageResource(mThumbIds[position]);
		imageView.setPadding(0,-11,0,-11);
		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		imageView.setLayoutParams(new GridView.LayoutParams(getViewWidth(), 1600));
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
