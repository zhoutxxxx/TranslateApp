package com.bnuz.ztx.translateapp.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileDescriptor;

public class ImageResizer {
    public Bitmap decodeFormStream(FileDescriptor fd, int reqWidth, int reqHeight){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fd,null,options);
        options.inSampleSize = calculateInSampleSize(options,reqWidth,reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFileDescriptor(fd,null,options);
    }
    /**
     * 计算图片采样率
     */
    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth,int reqHeight){
        if(reqWidth == 0 || reqHeight == 0){
            return 1;
        }
        int inSampleSize = 1;
        int width = options.outWidth;
        int height = options.outHeight;
        if(width>reqWidth || height>reqHeight){
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
}
