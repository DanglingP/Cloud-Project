package com.el.cloudproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.TypedValue;

/**
 * Created by Omar Sheikh on 4/15/2018.
 */
public class HelperFunctions {
    public static int DpToPx(float dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
    public static Bitmap RecizeBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);
        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }
    public static ClassUser GetCurrentUser(Context context){
        SharedPreferences prefs = context.getSharedPreferences("UserPrefs", context.MODE_PRIVATE);
        String tempUsername = prefs.getString("username", null);
        String tempPassword = prefs.getString("password", null);
        String tempPicURL = prefs.getString("picture", null);
        int tempUserType = prefs.getInt("type", -1);
        return new ClassUser(tempUsername,tempPassword,tempPicURL,tempUserType);
    }
    public static void UpdateCurrentUser(Context context, ClassUser currentUser){
        SharedPreferences.Editor editor = context.getSharedPreferences("UserPrefs", context.MODE_PRIVATE).edit();
        editor.putString("username", currentUser.getUsername());
        editor.putString("password", currentUser.getPassword());
        editor.putString("picture", currentUser.getImageUrl());
        editor.putInt("type", currentUser.getType());
        editor.apply();


    }
}
