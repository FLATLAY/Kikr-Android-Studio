package com.kikr.utility;


import java.io.FileNotFoundException;
import java.io.InputStream;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;

import com.kikr.ui.ProgressBarDialog;

public class PhotosImageTask extends AsyncTask<Void, Void, Bitmap> {

	ProgressBarDialog pleaseWaitDialog;
		Uri imageUrl;
		Bitmap bmp;
		FragmentActivity context;
		
		public PhotosImageTask(FragmentActivity context, Uri imageUrl) {
			super();
			this.context = context;
			this.imageUrl = imageUrl;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pleaseWaitDialog = new ProgressBarDialog(context);
			pleaseWaitDialog.show();   //("Select Image");
		    pleaseWaitDialog.setOnCancelListener(new OnCancelListener() {
			  @Override
			  public void onCancel(DialogInterface dialog) {
				cancel(true);
			  }
		    });
		}

		@Override
	protected void onCancelled() {
		super.onCancelled();
		pleaseWaitDialog.dismiss();
	}
	
		@Override
		protected Bitmap doInBackground(Void... params) {

			Bitmap bitmap = null;
			InputStream stream;
		    try {
			 stream = context.getContentResolver().openInputStream(imageUrl);
			 bitmap =  BitmapFactory.decodeStream(stream);
		    } catch (FileNotFoundException e) {
			  e.printStackTrace();
			  bitmap = null;
		    }
			return bitmap;
		  }

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);

			if (result != null)
			{
				bmp =  PictureUtils.scaleDownBitmap(result, 600, context);	
				PictureUtils.setImageData(context, bmp);
		    } 
			pleaseWaitDialog.dismiss();
	    }
		
	}