package com.applaudstudios.android.inventorytracking;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.IOException;

/**
 * Created by wjplaud83 on 11/13/16.
 */

public final class ImageTools {

    private ImageTools() {

    }

    public static Bitmap imageProcess(String imagePath) {

        String subPath = imagePath.substring(5);

        Bitmap inputBitmap = BitmapFactory.decodeFile(subPath);
        int originalWidth = inputBitmap.getWidth();
        int originalHeight = inputBitmap.getHeight();

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(inputBitmap, originalWidth / 4, originalHeight / 4, true);

        Bitmap processedBitmap;

        ExifInterface ei = null;
        try {
            ei = new ExifInterface(subPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                processedBitmap = rotateImage(scaledBitmap, 90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                processedBitmap = rotateImage(scaledBitmap, 180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                processedBitmap = rotateImage(scaledBitmap, 270);
                break;
            case ExifInterface.ORIENTATION_NORMAL:
            default:
                processedBitmap = scaledBitmap;
                break;
        }

        return processedBitmap;

    }

    private static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix,
                true);
    }
}
