package com.oscode.gatherthis;

import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Log.d("DwnlImgTask", urldisplay);
            Bitmap mIcon11 = null;
            try {
                Log.d("DwnlImgTask", "Trying to download image...");
                InputStream in = new java.net.URL(urldisplay).openStream();
                if (in == null)
                Log.d("DwnlImgTask", "InputSream null!");
              mIcon11 = BitmapFactory.decodeStream(in);
              if (mIcon11 == null) Log.d("DwnlImgTask", "Decode failed!"); 
            } catch (Exception e) {
                Log.d("DwnlImgTask", "Download failed.");
                Log.e("Error", e.getMessage());
                Log.d("DwnlImgTask", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            if (bmImage != null) bmImage.setImageBitmap(result);
        }
      }