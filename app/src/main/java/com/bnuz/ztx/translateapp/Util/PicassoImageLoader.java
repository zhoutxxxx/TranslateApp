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
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;
import com.youth.banner.loader.ImageLoader;

import java.io.IOException;

/**
 * Created by ZTX on 2017/12/7.
 */

public class PicassoImageLoader extends ImageLoader{
    @Override
    public void displayImage(final Context context, Object path, final ImageView imageView) {
        //第三方Picasso加载图片的简单方法
        Picasso.with(context).load(String.valueOf(path)).into(imageView);
    }

    //默认加载图片(指定大小)
    public void loadImageViewSize(Context mContext, String url, int width, int height, ImageView imageView) {
        Picasso.with(mContext).load(url).config(Bitmap.Config.RGB_565).resize(width, height).centerCrop().into(imageView);
    }

    public Bitmap getBitmap(Context context, Object path){
        try {
            return Picasso.with(context).load(String.valueOf(path)).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
