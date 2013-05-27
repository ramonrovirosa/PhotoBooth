package cs185.mousavi.alex.filmstripmaker;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

public class FilmStripMaker {

	//hardcoded to values of high res image
	static int x = 96;
	static int [] ys = {32,417,802};
	static final int length = 345;
	static final int width = 461;
	static final int film_height = 1164;
	static final int film_width = 657;
	//filters
	static final public int NORMAL = 0;
	static final public int SEPIA = 1;
	static final public int RETRO = 2;
	static final public int INVERT = 3;
	static final public int num_filters = 4;
	
	static Bitmap make(Bitmap filmstrip,Bitmap [] imgs, int filter){
		//validity check
		if(imgs==null||imgs.length!=3||filmstrip==null){
			return null;
		}
		if(filmstrip.getHeight()!=film_height || filmstrip.getWidth()!=film_width){
			Log.v("Bitmap size error", "Height: "+film_height+" Width: "+film_width);
			return null;
		}
		for(int i=0;i<3;i++){
			if(imgs[i].getHeight()!=length || imgs[i].getWidth()!=width){
				Log.v("Bitmap size error", "Img "+i+" Height: "+film_height+" Width: "+film_width);
				return null;
			}
		}
		
		//copy bitmaps to mutable version
		//and apply filters here to mutImgs
		Bitmap mutFilmstrip = filmstrip.copy(Bitmap.Config.ARGB_8888,true);
		Bitmap [] mutImgs = new Bitmap[3];
		for(int i=0;i<3;i++){
			mutImgs[i] = filter(imgs[i].copy(Bitmap.Config.ARGB_8888, true),filter);
		}
		
		//place each bitmap into filmstrip
		//could be done with int arrays as buffer for performance later
		for(int index=0;index<3;index++){
			Bitmap img = mutImgs[index];
			int y = ys[index];
			for(int j=0;j<width;j++){
				for(int k=0;k<length;k++){
					mutFilmstrip.setPixel(x+j,y+k,img.getPixel(j,k));
				}
			}
		}
		return mutFilmstrip;
	}
	
	
	static Bitmap filter(Bitmap img,int filtNum){
		if(filtNum == NORMAL)return img;//slight performance boost for no filter
		//put pixels in a buffer, filter each pixel in buffer, return new bitmap from buffer
		int [] buff = new int[img.getWidth()*img.getHeight()];
		img.getPixels(buff,0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());
		for (int i=0;i<buff.length;i++){
			buff[i] = doPixel(filtNum,buff[i]);
		}
		return Bitmap.createBitmap(buff, img.getWidth(), img.getHeight(), Bitmap.Config.ARGB_8888);
	}
	
	private static int fixRange(int num){
		//set pixel back to range if filter pushes it out
		return Math.min(Math.max(num, 0), 255);
	}
	
	private static double fixRange(double num){
		//set pixel back to range if filter pushes it out
		return Math.min(Math.max(num, 0), 255);
	}
	
	static int doPixel(int filtnum,int color){
		//apply filter to individual pixels
		int r,g,b;
		int inputRed = Color.red(color);
		int inputGreen = Color.green(color);
		int inputBlue = Color.blue(color);
		switch(filtnum){
		case SEPIA:
			r = fixRange((int)((inputRed * .393) + (inputGreen *.769) + (inputBlue * .189)));
			g = fixRange((int)((inputRed * .349) + (inputGreen *.686) + (inputBlue * .168)));
			b = fixRange((int)((inputRed * .272) + (inputGreen *.534) + (inputBlue * .131)));
			break;
		case RETRO:
			double contrastValue = 2;
			double redIn = ((double)inputRed)/255.0;
		    double greenIn = ((double)inputGreen)/255.0;
		    double blueIn = ((double)inputBlue)/255.0;
		    redIn -= 0.5;
		    redIn *= contrastValue;
		    redIn += 0.5;
		    redIn *= 255.0;
		    redIn = fixRange(redIn);
		    greenIn -= 0.5;
		    greenIn *= contrastValue;
		    greenIn += 0.5;
		    greenIn *= 255.0;
		    greenIn = fixRange(greenIn);
		    blueIn -= 0.5;
		    blueIn *= contrastValue;
		    blueIn += 0.5;
		    blueIn *= 255.0;
		    blueIn = fixRange(blueIn);
		    r = (int)redIn;
		    g = (int)greenIn;
		    b = (int)blueIn;
		    break;
		case INVERT:
			r = fixRange(255-inputRed);
			g = fixRange(255-inputGreen);
			b = fixRange(255-inputBlue);
			break;
		default:
			return color;
			
		}
		return Color.rgb(r, g, b);
	}
	
	
}
