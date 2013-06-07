package edu.ucsb.cs.cs185.photobooth;

import java.io.IOException;
import java.io.InputStream;

import android.R.color;
import android.R.drawable;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
		btns[0].setBackgroundColor(getResources().getColor(color.holo_blue_dark));
		btns[1] = (ImageButton)findViewById(R.id.imageButton2);
		btns[1].setBackgroundColor(getResources().getColor(color.holo_blue_dark));
		btns[2]  =(ImageButton)findViewById(R.id.imageButton3);
		btns[2].setBackgroundColor(getResources().getColor(color.holo_blue_dark));
		ViewTreeObserver vto = backdrop.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				fixCover();
				ViewTreeObserver obs = backdrop.getViewTreeObserver();
				obs.removeGlobalOnLayoutListener(this);
			}

		});

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


	
	/** A safe way to get an instance of the Camera object. */
	public static Camera getCameraInstance() {
		Camera c = null;
		try {
			c = Camera.open(); // attempt to get a Camera instance
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
			Bitmap img = Cropper.crop(data);
			if(img == null) return;
			imgs[btnSelect] = img;
			btns[btnSelect].setImageBitmap(Cropper.cropButton(img, (int)(btns[btnSelect].getWidth()*.85)));
			mCameraPreview.reset();
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
		getMenuInflater().inflate(R.menu.example_launch, menu);
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
		finish();
	}

	@SuppressLint("NewApi")
	private void btnPressed(int i){
		if(btnSelect>=0)
			btns[btnSelect].setBackgroundColor(getResources().getColor(color.holo_blue_dark));
		btnSelect = i;
		btns[i].setBackgroundColor(getResources().getColor(color.holo_blue_bright));
		
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
	
}
