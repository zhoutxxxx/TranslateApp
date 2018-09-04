package com.bnuz.ztx.translateapp.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.youth.banner.loader.ImageLoader;

import java.io.IOException;

/**
 * Created by ZTX on 2017/12/7.
 */

public class PicassoImageLoader extends ImageLoader {

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //第三方Picasso加载图片的简单方法
        Picasso.with(context).load(String.valueOf(path)).into(imageView);
    }

    //默认加载图片(指定大小)
    public void loadImageViewSize(Context mContext, String url, int width, int height, ImageView imageView) {
        Picasso.with(mContext).load(url).config(Bitmap.Config.RGB_565).resize(width, height).centerCrop().into(imageView);
    }

    public void loadMaxBitmap(final Context mContext, final String url, final int newWidth, final ImageView imageView) {
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Integer width = bitmap.getWidth();
                Integer height = bitmap.getHeight();
                float rate = (float)width / height;
                Integer newHeight = Float.valueOf(String.valueOf((float) newWidth / rate)).intValue();

                float scaleWidth = ((float) newWidth) / width;
                float scaleHeight = ((float) newHeight) / height;

                Matrix matrix = new Matrix();
                matrix.postScale(scaleWidth,scaleHeight);
                Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
                imageView.setImageBitmap(newBitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }

        };
        Picasso.with(mContext).load(url).into(target);
    }
}
