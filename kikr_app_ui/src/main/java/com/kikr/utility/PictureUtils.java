package com.kikr.utility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

import com.kikr.activity.EditProfileActivity;
import com.kikr.activity.HomeActivity;
import com.kikr.fragment.FragmentInspirationImage;
import com.kikrlib.db.UserPreference;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.Syso;

public class PictureUtils {
	
	public static final int REQUEST_CODE_CAMERA = 1;
	public static final int REQUEST_CODE_GALLERY = 2;
	public static final int REQUEST_CODE_DEFAULT_LIST = 3;
	public static final int REQUEST_CODE_VIDEO = 4;
	
	public static final String APP_DIR_MAIN = "SpotPog/images/";
	public static final String FILE_NAME = "pic_temp";
	
	
	
	public static void showAddPictureAlert(final FragmentActivity context,final EditProfileActivity editProfileActivity) {
		
	//	final CharSequence[] Type = { "Take New Photo", "Browse Gallery", "Import from Facebook", "Import from Twitter","Cancel" };
		final CharSequence[] Type = { "Browse Gallery", "Import from Facebook", "Import from Twitter","Import from Instagram","Cancel" };

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Add picture");
		builder.setItems(Type, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int pos) {
			//	if (pos == 0) {
			//		PictureUtils.openCamera(context);
			//	} 
				 if (pos == 0) {
					PictureUtils.openGallery(context);
				} 
				else if (pos == 1) {
					editProfileActivity.getFBProfilePic();
				} 
				else if (pos == 2) {
					editProfileActivity.twitterLoogedIn();
				}else if (pos == 3) {
					editProfileActivity.instagramLoogedIn();
				}  
			}
		});
		builder.show();
	}
	
	public static void showAddPictureAlert2(final FragmentActivity context,final EditProfileActivity editProfileActivity) {
		
		//final CharSequence[] Type = { "Choose from default Images","Take New Photo", "Browse Gallery","Cancel" };
		final CharSequence[] Type = { "Choose from default Images", "Browse Gallery","Import from Instagram","Cancel" };

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Add picture");
		builder.setItems(Type, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int pos) {
				if (pos == 0) {
					editProfileActivity.gotoDefaultList();
				//} else if (pos == 1) {
			//		PictureUtils.openCamera(context);
				} else if(pos==1){
					PictureUtils.openGallery(editProfileActivity);
				}else if(pos==2){
					editProfileActivity.gotoInstgramList();
				}
			}
		});
		builder.show();
	}
	
	public static void showAddPictureAlert(final FragmentActivity context,final FragmentInspirationImage fragmentUploadInspiration) {
		
		final CharSequence[] Type = { "Take New Photo", "Browse Gallery","Import from Instagram", "Cancel" };

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Add profile picture");
		builder.setItems(Type, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int pos) {
				if (pos == 0) {
					fragmentUploadInspiration.openCamera();
				} else if(pos==1){
					fragmentUploadInspiration.openGallery();
				}else if(pos==2){
					fragmentUploadInspiration.loadInstagramPic();
				}
			}
		});
		builder.show();
	}
	
	public static void openCamera(FragmentActivity context) {
		
		Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
		if(isSDPresent){
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

				Random r = new Random();
				UserPreference.getInstance().saveChatImage(String.valueOf(r.nextInt()));
				Syso.info("Random:"+String.valueOf(UserPreference.getInstance().getChatImage()));
			    File file = new File(createDirectory(), FILE_NAME+ String.valueOf(UserPreference.getInstance().getChatImage()) + ".png");
			    Syso.info("file name>>"+file.getName());
			    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
				context.startActivityForResult(intent, REQUEST_CODE_CAMERA);
		}
		else{
			AlertUtils.showToast(context, "SD Card not available.");
		}
	}
	
	private static void openGallery(FragmentActivity context){
		
		Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		context.startActivityForResult(intent, REQUEST_CODE_GALLERY);
	}
	
	public static byte[] getByteArray(Bitmap bitmap) {
		
		if(bitmap == null){
			return null;
		}

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
	    //create resized image and display
		Log.e("image size bye aray","width: " + bitmap.getWidth() + "height: " + bitmap.getHeight());
//		int newwidth = (int) (bitmap.getWidth() * 0.20);
//		int newheight = (int) (bitmap.getHeight() * 0.20);
		int newwidth=(int)bitmap.getWidth();
		int newheight=(int)bitmap.getHeight();
		
		if(newwidth > 400 || newheight > 400) {
			newwidth = (int) (bitmap.getWidth() * 0.70);
			newheight = (int) (bitmap.getHeight() * 0.70);
		}
		
		Log.e("image size new byte aray","width: " + newwidth + "height: " + newheight);
	    Bitmap resizedImage = Bitmap.createScaledBitmap(bitmap, newwidth , newheight, true);
	    resizedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] byteArray = stream.toByteArray();
		return byteArray;
	}

	public static byte[] getByteArray2(Bitmap bitmap) {
		
		if(bitmap == null){
			return null;
		}

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
	    Bitmap resizedImage = Bitmap.createScaledBitmap(bitmap,  bitmap.getWidth() , bitmap.getHeight(), true);
	    resizedImage.compress(Bitmap.CompressFormat.PNG, 90, stream);
		byte[] byteArray = stream.toByteArray();
		try {
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return byteArray;
	}




	
	public static Bitmap createScaledImage(File mFile) {

		Bitmap bitmap = null;
		ExifInterface exif;
		try {
			
			Syso.info("1111===="+mFile.getPath());
			exif = new ExifInterface(mFile.getPath());
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			int angle = 0;

			if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
				angle = 90;
			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
				angle = 180;
			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
				angle = 270;
			}

			Matrix mat = new Matrix();
			mat.postRotate(angle);
			BitmapFactory.Options options = new BitmapFactory.Options();
//			options.inJustDecodeBounds=false;
			options.inSampleSize = calculateInSampleSize2(options, 800, 600);
			Syso.info("2222===="+mFile.getPath());
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			Bitmap bmp = BitmapFactory.decodeStream(new FileInputStream(mFile), null, options);
			
			Syso.info("123456789  2222==== Width:"+bmp.getWidth()+", Height:"+bmp.getHeight()+", ww:"+options.outWidth+", hh:"+options.outHeight);
			bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), mat, true);
			Syso.info("123456789  2222==== Width:"+bitmap.getWidth()+", Height:"+bitmap.getHeight());
			Syso.info("===="+bitmap);
		} 
		catch (Exception e) {
			Syso.error(e);
		}
		if (bitmap != null)
			return bitmap;

		else
			return null;
	}
	
	public static File createDirectory(){
		Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);

		if(isSDPresent &&isSDCardWritable())
		{
			File mainDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), APP_DIR_MAIN);
	        if(!mainDir.exists()) {
	        	mainDir.mkdirs();
	        }
	        return mainDir;
		}
		else
		{
			File mainDir = new File(Environment.getRootDirectory().getAbsolutePath(), APP_DIR_MAIN);
	        if(!mainDir.exists()) {
	        	mainDir.mkdirs();
	        }
	        return mainDir;
		}
	}
	
	// Checks if SD Card is available for read and write
	public static boolean isSDCardWritable() {
	  String status = Environment.getExternalStorageState();
	  if (Environment.MEDIA_MOUNTED.equals(status)) {
	    return true;
	   }
	  return false;
	}
	
	public static File getFileFromUri(Context context, Uri uri){
		
		if (uri == null) {
			return null;
		}
		
		String[] filePath = { MediaStore.Images.Media.DATA };
		Cursor c = context.getContentResolver().query(uri, filePath, null, null, null);
		c.moveToFirst();
		int columnIndex = c.getColumnIndex(filePath[0]);
		String picturePath = c.getString(columnIndex);
		File file = new File(picturePath);
		c.close();
		Syso.debug("image Path", picturePath + "");
		return file;
	}
	
	public static void showGalleryImage(FragmentActivity context, Uri uri) {
		System.out.println("uri>>>>"+uri.toString());
		if (uri.toString().startsWith("content://com.google.android.apps.photos.content")) {
//			if (DeviceUtils.isInternetOn(context)) {
				PhotosImageTask photoImageTask = new PhotosImageTask(context,uri);
				photoImageTask.execute();
//			} else {
//				AlertUtils.showToast(context,
//						"Please Check Your Internet Connection");
//			}

		} else {
			File file = PictureUtils.getFileFromUri(context, uri);
			if (file != null) {
				Bitmap galleryBitmap = PictureUtils.createScaledImage(file);
				setImageData(context, galleryBitmap);
			}
		}
	}
	
	 public static String SaveImageToDevice(Bitmap mImage) {
		 String mImagePath = null;
	      File mediaStorageDir = new File(android.os.Environment
	                    .getExternalStorageDirectory(), APP_DIR_MAIN);
	              
	               if (!mediaStorageDir.exists()) {
	          if (!mediaStorageDir.mkdirs()) {
	           Log.d("/kikr/Profile Image", "Oops! Failed create "
	             +   " directory");
	          }
	               }
	       String timeStamp = new SimpleDateFormat("yyyyMMdd HHmmss",
	         Locale.getDefault()).format(new Date());
	       
	             File mediaFile;
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator
	          + timeStamp
	          +".jpg");
	        FileOutputStream out;
	        try {
	         out = new FileOutputStream(mediaFile);
	         mImage.compress(Bitmap.CompressFormat.JPEG, 90, out);
	         out.flush();
	         out.close();
	         mImage.recycle();
	         
	         mImagePath = mediaFile.getAbsolutePath();
	      return mImagePath;
	         
	        } catch (Exception e) {
	         e.printStackTrace();
	        }
			return mImagePath;
	       
	    }
	 
	 
	 public static Bitmap compressImage(String filePath) {

			Bitmap scaledBitmap = null;
			String filename = null;

			try {
				BitmapFactory.Options options = new BitmapFactory.Options();

				// by setting this field as true, the actual bitmap pixels are not
				// loaded in the memory. Just the bounds are loaded. If
				// you try the use the bitmap here, you will get null.
				options.inJustDecodeBounds = true;
				Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

				int actualHeight = options.outHeight;
				int actualWidth = options.outWidth;

				if (actualHeight == 0) {
					actualHeight = 3120;
				}
				if (actualWidth == 0) {
					actualWidth = 3120;
				}

				// max Height and width values of the compressed image is taken as
				// 816x612

				float maxHeight = actualHeight/2;
				float maxWidth = actualWidth/2;

				System.out.println("heig actualWidth" + actualWidth);
				System.out.println("heig actualHeight" + actualHeight);
				float imgRatio = actualWidth / actualHeight;
				float maxRatio = maxWidth / maxHeight;

				// width and height values are set maintaining the aspect ratio of
				// the
				// image

				if (actualHeight > maxHeight || actualWidth > maxWidth) {
					if (imgRatio < maxRatio) {
						imgRatio = maxHeight / actualHeight;
						actualWidth = (int) (imgRatio * actualWidth);
						actualHeight = (int) maxHeight;
					} else if (imgRatio > maxRatio) {
						imgRatio = maxWidth / actualWidth;
						actualHeight = (int) (imgRatio * actualHeight);
						actualWidth = (int) maxWidth;
					} else {
						actualHeight = (int) maxHeight;
						actualWidth = (int) maxWidth;
					}
				}

				// setting inSampleSize value allows to load a scaled down version
				// of
				// the original image

				options.inSampleSize = calculateInSampleSize(options, actualWidth,actualHeight);

				// inJustDecodeBounds set to false to load the actual bitmap
				options.inJustDecodeBounds = false; 

				// this options allow android to claim the bitmap memory if it runs
				// low
				// on memory
				options.inPurgeable = true;
				options.inInputShareable = true;
				options.inTempStorage = new byte[16 * 1024];

				// load the bitmap from its path
				bmp = BitmapFactory.decodeFile(filePath, options);
				scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,Bitmap.Config.ARGB_8888);

				float ratioX = actualWidth / (float) options.outWidth;
				float ratioY = actualHeight / (float) options.outHeight;
				float middleX = actualWidth / 2.0f;
				float middleY = actualHeight / 2.0f;

				Matrix scaleMatrix = new Matrix();
				scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

				Canvas canvas = new Canvas(scaledBitmap);
				canvas.setMatrix(scaleMatrix);
				canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2,middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

				// check the rotation of the image and display it properly
				ExifInterface exif;
				exif = new ExifInterface(filePath);

				int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
				Log.d("EXIF", "Exif: " + orientation);
				Matrix matrix = new Matrix();
				if (orientation == 6) {
					matrix.postRotate(90);
					Log.d("EXIF", "Exif: " + orientation);
				} else if (orientation == 3) {
					matrix.postRotate(180);
					Log.d("EXIF", "Exif: " + orientation);
				} else if (orientation == 8) {
					matrix.postRotate(270);
					Log.d("EXIF", "Exif: " + orientation);
				}
				scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
						true);

				FileOutputStream out = null;
				filename = filePath;
				out = new FileOutputStream(filename);

				// write the compressed bitmap at the destination specified by
				// filename.
				scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return scaledBitmap;

		}
		
		public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
			final int height = options.outHeight;
			final int width = options.outWidth;
			int inSampleSize = 1;

			if (height > reqHeight || width > reqWidth) {
				final int heightRatio = Math.round((float) height / (float) reqHeight);
				final int widthRatio = Math.round((float) width / (float) reqWidth);
				inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
			}
			final float totalPixels = width * height;
			final float totalReqPixelsCap = reqWidth * reqHeight * 2;
			while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap)
			{
				inSampleSize++;
			}
			Syso.info("Sample size: "+inSampleSize);
			return inSampleSize;
		}
		
		
		public static int calculateInSampleSize2( BitmapFactory.Options options, int reqWidth, int reqHeight) {
	    // Raw height and width of image
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;

	    if (height > reqHeight || width > reqWidth) {

	        final int halfHeight = height / 2;
	        final int halfWidth = width / 2;

	        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
	        // height and width larger than the requested height and width.
	        while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
	            inSampleSize *= 2;
	        }
	    }
	    Syso.info("12345678 inSampleSize:"+inSampleSize+", height:"+height+", width:"+width);
	    return inSampleSize;
	}
		
		
		public static Bitmap convertFileIntoBitmap(FragmentActivity context, File file)
		 {
		  if(PictureUtils.getImageSize(file.length()) > 1)
		  {
			 System.out.println("Large size");
		   return  createScaledImage(file);
		  }
		  else
		  {
			  System.out.println("Small size");
		   return  getSampledBitmap(context,file); 
		  }  
		 }
		
		public static Bitmap getSampledBitmap(FragmentActivity context, File file) 
		 {
		  Bitmap bitmap = null;
		  String filename = null;
		  ExifInterface exif;
		  try {
		   exif = new ExifInterface(file.getPath());
		   int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
		   int angle = 0;
/*
		   if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
		    angle = 90;
		   } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
		    angle = 180;
		   } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
		    angle = 270;
		   }
		   */
		   switch (orientation) {
           case ExifInterface.ORIENTATION_NORMAL:
        	   angle = 0;
               break;
           case ExifInterface.ORIENTATION_ROTATE_90:
        	   angle = 90;
               break;
           case ExifInterface.ORIENTATION_ROTATE_180:
        	   angle = 180;
               break;
           case ExifInterface.ORIENTATION_ROTATE_270:
        	   angle = 270;
               break;
           case ExifInterface.ORIENTATION_UNDEFINED:
        	   angle = 0;
               break;
           default:
        	   angle = 90;
		   }

		  Matrix mat = new Matrix();
		  mat.postRotate(angle);
		  BitmapFactory.Options opts = new Options();
		  // Don't read the pixel array into memory, only read the picture
		  // information
		  opts.inSampleSize = 1;
		  opts.inJustDecodeBounds = true;
		  BitmapFactory.decodeFile(file.getAbsolutePath(), opts);
		  // Get a picture from the Options resolution
		  int imageHeight = opts.outHeight;
		  int imageWidth = opts.outWidth;

		  DisplayMetrics displaymetrics = new DisplayMetrics();
		     context.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		     int windowHeight = displaymetrics.heightPixels;
		     int windowWidth = displaymetrics.widthPixels;

		  // Calculation of sampling rate
		  int scaleX = imageWidth / windowWidth;
		  int scaleY = imageHeight / windowHeight;
		  int scale = 1;
		  // The sampling rate in accordance with the direction of maximum prevail
		  if (scaleX > scaleY && scaleY >= 1) {
		   scale = scaleX;
		  }
		  if (scaleX < scaleY && scaleX >= 1) {
		   scale = scaleY;
		  }

		  // False read the image pixel array into memory, in accordance with the
		  // sampling rate set
		  opts.inJustDecodeBounds = false;
		  // Sampling rate
		  opts.inSampleSize = scale;
		  Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath(),opts);
		  bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), mat, true);
		  FileOutputStream out = null;
			filename = file.getAbsolutePath();
			out = new FileOutputStream(filename);

			// write the compressed bitmap at the destination specified by
			// filename.
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
		  return bitmap;
		  } 
		  catch (Exception e) {
			  e.printStackTrace();
			Syso.error(context.getClass().getSimpleName());
		   return bitmap;
		  }
		  
		 }
		
		public static Bitmap getBitmap(FragmentActivity context, File file){

			  Bitmap bitmap = null;
			  String filename = null;
			  ExifInterface exif;
			  try {
			   exif = new ExifInterface(file.getPath());
			   int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			   int angle = 0;

			   if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
			    angle = 90;
			   } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
			    angle = 180;
			   } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
			    angle = 270;
			   }

			  Matrix mat = new Matrix();
			  mat.postRotate(angle);
			  BitmapFactory.Options opts = new Options();
			  // Don't read the pixel array into memory, only read the picture
			  // information
			  opts.inSampleSize = 1;
			  opts.inJustDecodeBounds = true;
			  BitmapFactory.decodeFile(file.getAbsolutePath(), opts);
			  // Get a picture from the Options resolution
			  int imageHeight = opts.outHeight;
			  int imageWidth = opts.outWidth;

			  DisplayMetrics displaymetrics = new DisplayMetrics();
			     context.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
			     int windowHeight = displaymetrics.heightPixels;
			     int windowWidth = displaymetrics.widthPixels;

			  // Calculation of sampling rate
			  int scaleX = imageWidth / windowWidth;
			  int scaleY = imageHeight / windowHeight;
			  int scale = 1;
			  // The sampling rate in accordance with the direction of maximum prevail
			  if (scaleX > scaleY && scaleY >= 1) {
			   scale = scaleX;
			  }
			  if (scaleX < scaleY && scaleX >= 1) {
			   scale = scaleY;
			  }

			  // False read the image pixel array into memory, in accordance with the
			  // sampling rate set
			  opts.inJustDecodeBounds = false;
			  // Sampling rate
			  opts.inSampleSize = 1;
			  Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath(),opts);
			  bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), mat, true);
			  FileOutputStream out = null;
				filename = file.getAbsolutePath();
				out = new FileOutputStream(filename);

				// write the compressed bitmap at the destination specified by
				// filename.
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
			  return bitmap;
			  } 
			  catch (Exception e) {
				  e.printStackTrace();
				Syso.error(context.getClass().getSimpleName());
			   return bitmap;
			  }
		}
		 
		 public static long getImageSize(long bytes)
		 {
		  long sizeInMb = bytes / (1024 * 1024);
		  return sizeInMb;
		 }
		
		
	@SuppressWarnings("deprecation")
	public static void setChatImageBackground(ImageView layout, Drawable drawable) {
		if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.JELLY_BEAN) {
			layout.setBackgroundDrawable(drawable);
		} else {
			layout.setBackground(drawable);
		}
	}
	
	
	public static byte[] convertFileIntoByte(File file)
	 {
	  FileInputStream fileInputStream = null;
	  byte[] bFile = new byte[(int) file.length()];

	  try {
	   // convert file into array of bytes
	   fileInputStream = new FileInputStream(file);
	   fileInputStream.read(bFile);
	   fileInputStream.close();
	  } catch (Exception e) {
	   e.printStackTrace();
	  }
	  return bFile;
	 }
	
	public static Bitmap scaleDownBitmap(Bitmap photo, int newHeight, FragmentActivity context) 
	 {
	   final float densityMultiplier = context.getResources().getDisplayMetrics().density;        
	   int h = (int) (newHeight*densityMultiplier);
	   int w = (int) (h * photo.getWidth()/((double) photo.getHeight()));
	   photo  = Bitmap.createScaledBitmap(photo, w, h, true);
	   return photo;
	 }
	
	public static void setImageData(FragmentActivity context, Bitmap bitmap) {
		if (context instanceof EditProfileActivity)
			((EditProfileActivity) context).setImage(bitmap);
		else if (context instanceof HomeActivity)
			((HomeActivity) context).setimage(bitmap);
	}
	
	public static Bitmap scaleImage(Bitmap bitmap)
	{
	    // Get current dimensions AND the desired bounding box
	    int width = bitmap.getWidth();
	    int height = bitmap.getHeight();
	    int bounding = 800;
	    Log.i("Test", "original width = " + Integer.toString(width));
	    Log.i("Test", "original height = " + Integer.toString(height));
	    Log.i("Test", "bounding = " + Integer.toString(bounding));

	    // Determine how much to scale: the dimension requiring less scaling is
	    // closer to the its side. This way the image always stays inside your
	    // bounding box AND either x/y axis touches it.  
	    float xScale = ((float) bounding) / width;
	    float yScale = ((float) bounding) / height;
	    float scale = (xScale <= yScale) ? xScale : yScale;
	    Log.i("Test", "xScale = " + Float.toString(xScale));
	    Log.i("Test", "yScale = " + Float.toString(yScale));
	    Log.i("Test", "scale = " + Float.toString(scale));

	    // Create a matrix for the scaling and add the scaling data
	    Matrix matrix = new Matrix();
	    matrix.postScale(scale, scale);

	    // Create a new bitmap and convert it to a format understood by the ImageView 
	    Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
	    width = scaledBitmap.getWidth(); // re-use
	    height = scaledBitmap.getHeight(); // re-use
	    Log.i("Test", "scaled width = " + Integer.toString(width));
	    Log.i("Test", "scaled height = " + Integer.toString(height));
	    return scaledBitmap;
	}
}
