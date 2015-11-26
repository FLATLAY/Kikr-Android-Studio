package com.kikr.activity;

import java.net.URL;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kikr.R;
import com.kikr.utility.CommonUtility;
import com.kikrlib.utils.Syso;

public class ImageViewActivity extends Activity{
	ImageView view;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_image_view);
		setImage();
	}

	private void setImage() {
		view = (ImageView) findViewById(R.id.imageView);
		FrameLayout frameLayout=(FrameLayout) findViewById(R.id.overlayView);
//		CommonUtility.setImageNS(this, getIntent().getStringExtra("image"), view, R.drawable.dum_list_item_product);
		LinearLayout layout = (LinearLayout) findViewById(R.id.parantDialog);
		layout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		new GetBitmapFromUrl().execute(getIntent().getStringExtra("image"));
//		try{
//		URL url = new URL(getIntent().getStringExtra("image"));
//		Bitmap b = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//		
////		Bitmap b = ((BitmapDrawable)view.getDrawable()).getBitmap();
//		int w = b.getWidth();
//		int h = b.getHeight();
//		Syso.info("12345678 Image width:"+w+", height:"+h);
//		}catch(Exception e){
//			e.printStackTrace();
//		}
	}
	
	private class GetBitmapFromUrl extends AsyncTask<String, Void, Bitmap>{

		@Override
		protected Bitmap doInBackground(String... params) {
			// TODO Auto-generated method stub
			try{
				URL url = new URL(params[0]);
				return  BitmapFactory.decodeStream(url.openConnection().getInputStream());
			}catch(Exception e){
				return null;
			}
		}
		
		@Override
		protected void onPostExecute(Bitmap result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result!=null){
				int w = result.getWidth();
				int h = result.getHeight();
				Syso.info("12345678 Image width:"+w+", height:"+h);
				view.setImageBitmap(result);
			}
		}
	}

}
