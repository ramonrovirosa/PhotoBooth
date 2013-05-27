package edu.ucsb.cs.cs185.photobooth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class GridGallery extends Activity{
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_layout);
        
        Intent intename = getIntent();
 
        GridView gridView = (GridView) findViewById(R.id.grid_view);
 
        // Instance of ImageAdapter Class
        gridView.setAdapter(new GalleryAdapter(this));
        
        
        gridView.setOnItemClickListener(new OnItemClickListener() {
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

}
