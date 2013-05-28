package edu.ucsb.cs.cs185.photobooth;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class GridGallery extends Activity {

    public class ImageAdapter extends BaseAdapter {
     
     private Context mContext;
     ArrayList<String> itemList = new ArrayList<String>();
     
     public ImageAdapter(Context c) {
      mContext = c; 
     }
     
     void add(String path){
      itemList.add(path); 
     }

  @Override
  public int getCount() {
   return itemList.size();
  }

  @Override
  public Object getItem(int arg0) {
   // TODO Auto-generated method stub
   return null;
  }

  @Override
  public long getItemId(int position) {
   // TODO Auto-generated method stub
   return 0;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
   ImageView imageView;
         if (convertView == null) {  // if it's not recycled, initialize some attributes
             imageView = new ImageView(mContext);
             imageView.setLayoutParams(new GridView.LayoutParams(140, 280));
             imageView.setPadding(10, 10, 10, 10);
             imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
             
         } else {
             imageView = (ImageView) convertView;
             imageView.setPadding(10, 10, 10, 10);
         }

         //Bitmap bm = decodeSampledBitmapFromUri(itemList.get(position), 220, 220);
         
         // Use the path as the key to LruCache
         final String imageKey = itemList.get(position);
           final Bitmap bm = getBitmapFromMemCache(imageKey);
           
         if (bm == null){
          BitmapWorkerTask task = new BitmapWorkerTask(imageView);
          task.execute(imageKey);
         };

         imageView.setImageBitmap(bm);
         return imageView;
  }
  
  public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth, int reqHeight) {
   
   Bitmap bm = null;
   // First decode with inJustDecodeBounds=true to check dimensions
   final BitmapFactory.Options options = new BitmapFactory.Options();
   options.inJustDecodeBounds = true;
   BitmapFactory.decodeFile(path, options);
       
   // Calculate inSampleSize
   options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
       
   // Decode bitmap with inSampleSize set
   options.inJustDecodeBounds = false;
   bm = BitmapFactory.decodeFile(path, options); 
       
   return bm;   
  }
  
  public int calculateInSampleSize(
    
   BitmapFactory.Options options, int reqWidth, int reqHeight) {
   // Raw height and width of image
   final int height = options.outHeight;
   final int width = options.outWidth;
   int inSampleSize = 1;
   
   if (height > reqHeight || width > reqWidth) {
    if (width > height) {
     inSampleSize = Math.round((float)height / (float)reqHeight);    
    } else {
     inSampleSize = Math.round((float)width / (float)reqWidth);    
    }   
   }
   
   return inSampleSize;    
  }
  
  class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap>{
   
   private final WeakReference<ImageView> imageViewReference;
   
   public BitmapWorkerTask(ImageView imageView) {
    // Use a WeakReference to ensure the ImageView can be garbage collected
    imageViewReference = new WeakReference<ImageView>(imageView); 
   }
   
   @Override
   protected Bitmap doInBackground(String... params) {
    final Bitmap bitmap = decodeSampledBitmapFromUri(params[0], 200, 200);
    addBitmapToMemoryCache(String.valueOf(params[0]), bitmap);
    return bitmap;
   }

   @Override
   protected void onPostExecute(Bitmap bitmap) {
    if (imageViewReference != null && bitmap != null) {
     final ImageView imageView = (ImageView)imageViewReference.get();
     if (imageView != null) {
      imageView.setImageBitmap(bitmap); 
     } 
    } 
   }
  }

 }
    
    ImageAdapter myImageAdapter;
    
    private LruCache<String, Bitmap> mMemoryCache;

 @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_layout);
        
        GridView gridview = (GridView) findViewById(R.id.grid_view);
        myImageAdapter = new ImageAdapter(this);
        gridview.setAdapter(myImageAdapter);
        
//        String ExternalStorageDirectoryPath = Environment
//          .getExternalStorageDirectory()
//          .getAbsolutePath();
        
        String targetPath = Environment.getExternalStoragePublicDirectory(
		        Environment.DIRECTORY_PICTURES
		    )+
		    "/PhotoBooth";
        
//        Toast.makeText(getApplicationContext(), targetPath, Toast.LENGTH_LONG).show();
        File targetDirector = new File(targetPath);
        
        File[] files = targetDirector.listFiles();
        for (File file : files){
         myImageAdapter.add(file.getAbsolutePath());
        } 
        
        // Get memory class of this device, exceeding this amount will throw an
        // OutOfMemory exception.
        final int memClass 
         = ((ActivityManager)getSystemService(Context.ACTIVITY_SERVICE))
          .getMemoryClass();
        
        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = 1024 * 1024 * memClass / 8;
        
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
         
         @Override
         protected int sizeOf(String key, Bitmap bitmap) {
          // The cache size will be measured in bytes rather than number of items.
          return bitmap.getByteCount(); 
         }
        };
        
        gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
			int position, long id) {

			//System.out.println("position is: " + position);



			// Sending image id to FullScreenActivity
			Intent i = new Intent(GridGallery.this, FullImageActivity.class);

			Integer pos  = position;

			// passing array index
			i.putExtra("Position", position);
//			setResult(RESULT_OK, i);
			//finish();
			startActivity(i);

//				Context context = getApplicationContext();
//				CharSequence text = "Hello toast!";
//				int duration = Toast.LENGTH_SHORT;
//				Toast toast = Toast.makeText(context, text, duration);
//				toast.show();
//			


			}
			});
        
    }
 
 public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
  if (getBitmapFromMemCache(key) == null) {
   mMemoryCache.put(key, bitmap); 
  } 
 }
 
 public Bitmap getBitmapFromMemCache(String key) {
  return (Bitmap) mMemoryCache.get(key); 
 }

}