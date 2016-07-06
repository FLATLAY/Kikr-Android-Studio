package com.kikr.circle_crop.circle_crop;

import android.net.Uri;

import java.io.File;

/**
 * @author albin
 * @date 24/6/15
 */
public class Utils {

    public static  Uri path;

//    public static Uri getImageUri(URI path) {
//        return this.path;
//    }
        public static Uri getImageUri(String path) {
        return Uri.fromFile(new File(path));
    }

    public static void setImageUri(Uri uri) {
        path=uri;
    }
}
