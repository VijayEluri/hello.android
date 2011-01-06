package pparkkin.android;

import java.io.IOException;
import java.net.MalformedURLException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageDisplay extends Activity {
	private static final int errorImage = R.drawable.bsod;
	private static String url = "nourl";
	private ImageView iV;
	private TextView tV; 
	
	private static ProgressDialog spinner;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image); // Can't access views before calling setContentView
        
        iV = (ImageView) findViewById(R.id.imageview);
        tV = (TextView) findViewById(R.id.image_textview);
        
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
        	setImage(null);
        } else {
        	url = extras.getString("url");
    		new LoadImageTask().execute(url);
        }
    }
    
	private class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
		private Exception error;
		protected void onPreExecute() {
			ImageDisplay.this.showSpinner();
		}

		@Override
		protected Bitmap doInBackground(String... urls) {
        	Bitmap b = null;
        	
			try {
				b = HttpGET.fetchBitmap(urls[0]);
			} catch (MalformedURLException e) {
				error = e;
				Log.e("Hello.Android", e.getMessage());
			} catch (IOException e) {
				error = e;
				Log.e("Hello.Android", e.getMessage());
			}
			
        	return b;
		}
		
		protected void onPostExecute(Bitmap b) {
			ImageDisplay.this.setImage(b);
			if (error != null)
				ImageDisplay.this.setText(error.getMessage());
			ImageDisplay.this.dismissSpinner();
		}
	}

	public void setImage(Bitmap b) {    	
		if (b == null) {
	        iV.setImageResource(errorImage);
	        return;
		}
		
		iV.setImageBitmap(b);
	}

	public void setText(String message) {
    	tV.setText(url);
	}

	public void showSpinner() {
		spinner = ProgressDialog.show(this, "",
				  "Loading Image. Please wait...", true);
	}

	public void dismissSpinner() {
		spinner.dismiss();
	}
}
