package cs185.mousavi.alex.filmstripmaker;

import android.content.Context;
import android.os.Environment;

public class StoragePath {

	//class to hold a single pathname for all activities
	public static String get(Context c){
		return Environment.getExternalStorageDirectory()
        + "/Android/data/"
        + c.getPackageName()
        + "/images";
	}
	
}
