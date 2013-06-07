package edu.ucsb.cs.cs185.photobooth;

import java.io.IOException;
import java.io.InputStream;

import android.os.Bundle;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
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
import android.widget.RelativeLayout;
import android.widget.Toast;

public class CameraActivity extends CreateFilmStripLauncher {

	// now when you extend CreateFilmStripLauncher
	// it will take car of launching CreateFilmStrip
	// with ending the activity or not when you return from it

	private Camera mCamera;
	private CameraPreview mCameraPreview;

	private final String TAG = this.toString();

	FrameLayout preview;
	RelativeLayout backdrop;
	RelativeLayout cover;

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
			Launch(img, img, img);
			
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
		Log.v("camera_debug", "width: " + width + " height: " + height
				+ " picheight: " + picheight);
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

}
