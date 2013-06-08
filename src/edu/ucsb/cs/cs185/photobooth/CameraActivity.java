package edu.ucsb.cs.cs185.photobooth;

import java.io.IOException;
import java.io.InputStream;

import android.R.color;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class CameraActivity extends CreateFilmStripLauncher {

	// now when you extend CreateFilmStripLauncher
	// it will take car of launching CreateFilmStrip
	// with ending the activity or not when you return from it

	private Camera mCamera;
	private CameraPreview mCameraPreview;

	private final String TAG = this.toString();

	Color buttonColor;
	FrameLayout preview;
	RelativeLayout backdrop;
	RelativeLayout cover;
	ImageButton btns [];
	Bitmap imgs[];
	int btnSelect = -1;
	private Bitmap testpic;

	private static boolean frontMode = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// for now, get the testpic from the assets
		try {
			InputStream is = getAssets().open("testpic.png");
			testpic = BitmapFactory.decodeStream(is);
			is.close();
			is = null;
		} catch (IOException ex) {

		}
		setContentView(R.layout.camera_fixed);
		preview = (FrameLayout) findViewById(R.id.camera_preview);
		backdrop = (RelativeLayout) findViewById(R.id.backlayout);
		cover = (RelativeLayout) findViewById(R.id.cover);
		imgs = new Bitmap[3];
		btns = new ImageButton[3];
		btns[0] = (ImageButton)findViewById(R.id.imageButton1);
		btns[0].setBackgroundColor(android.graphics.Color.GRAY);
		btns[1] = (ImageButton)findViewById(R.id.imageButton2);
		btns[1].setBackgroundColor(android.graphics.Color.GRAY);
		btns[2]  =(ImageButton)findViewById(R.id.imageButton3);
		btns[2].setBackgroundColor(android.graphics.Color.GRAY);
		ViewTreeObserver vto = backdrop.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				fixCover();
				ViewTreeObserver obs = backdrop.getViewTreeObserver();
				obs.removeGlobalOnLayoutListener(this);
			}

		});

		initCamera();
		btnPressed(0);
	}

	private void initCamera(){
		preview.removeAllViews();
		mCamera = getCameraInstance();
		mCameraPreview = new CameraPreview(this, mCamera);
		
		preview.addView(mCameraPreview);
		Button captureButton = (Button) findViewById(R.id.button_capture);
		captureButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(btnSelect >= 0 && btnSelect < 3)
					mCamera.takePicture(null, null, mPicture);
			}
		});
	}
	
	
	public static int getFrontCameraId() {
		CameraInfo ci = new CameraInfo();
		for (int i = 0 ; i < Camera.getNumberOfCameras(); i++) {
			Camera.getCameraInfo(i, ci);
			if ((ci.facing == CameraInfo.CAMERA_FACING_FRONT))
			{
				return i;
			}

		}

		return CameraInfo.CAMERA_FACING_BACK; // No front-facing camera found
	}

	/** A safe way to get an instance of the Camera object. */
	public static Camera getCameraInstance() {
		Camera c = null;
		try {
			if(frontMode)
				c = Camera.open(getFrontCameraId()); // attempt to get a Camera instance
			else
				c = Camera.open();
		} catch (Exception e) {
			// Camera is not available (in use or does not exist)
			return null;
		}

		c.setDisplayOrientation(90);
		return c; // returns null if camera is unavailable
	}

	private PictureCallback mPicture = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			Bitmap img = Cropper.crop(data , frontMode);
			if(img == null) return;
			imgs[btnSelect] = img;
			btns[btnSelect].setImageBitmap(Cropper.cropButton(img, (int)(btns[btnSelect].getWidth()*.85)));
			mCameraPreview.reset();
			btnPressed((btnSelect + 1)%btns.length);
		}
	};

	public void fixCover() {
		cover.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		int width = backdrop.getWidth();
		int height = backdrop.getHeight();
		int picheight = FilmStripMaker.getCameraLength(width);
		RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) cover
				.getLayoutParams();
		params.height = height - picheight;
		cover.setLayoutParams(params);

		int bw = btns[0].getWidth();
		btns[0].setMaxHeight(bw);
		btns[0].setMinimumHeight(bw);
		btns[1].setMaxHeight(bw);
		btns[1].setMinimumHeight(bw);
		btns[2].setMaxHeight(bw);
		btns[2].setMinimumHeight(bw);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.camera_menu, menu);
		return true;
	}

	public void LaunchBtnPress(View v) {
		// test mod for eclipse
		// this is how you would launch it. the launcher saves the testpics as
		// static to pass
		// to CreateFilmStrip because they are too large to bundle
		Log.v("launch", "launch button pressed");
		Launch(testpic, testpic, testpic);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mCamera != null) {
			mCamera.release(); // release the camera for other applications
			mCamera = null;
		}
		//finish here
		Intent returnIntent = new Intent();
   	 	returnIntent.putExtra("modified",true);
   	 	setResult(RESULT_OK,returnIntent);     
   	 	finish();
	}

	private void btnPressed(int i){
		if(btnSelect>=0)
			btns[btnSelect].setBackgroundColor(android.graphics.Color.GRAY);
		btnSelect = i;
		btns[i].setBackgroundColor(android.graphics.Color.BLACK);

	}
	public void MakeFilm(View v){
		for(int i=0;i<imgs.length;i++){
			if(imgs[i] == null)
				return;
		}
		Launch(imgs);
	}

	public void btn1(View v){
		btnPressed(0);
	}
	public void btn2(View v){
		btnPressed(1);
	}
	public void btn3(View v){
		btnPressed(2);
	}

	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()) {
		//get which option is pressed and create/set that image
		case R.id.flip:
			frontMode = !frontMode;
			mCamera.release();
			initCamera();
			return true;
		case R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
}
