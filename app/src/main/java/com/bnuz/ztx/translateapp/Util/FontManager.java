package com.bnuz.ztx.translateapp.Util;

import android.content.Context;
import android.graphics.Typeface;

import java.lang.reflect.Type;

/**
 * Created by ZTX on 2018/3/25.
 * 文字处理类
 */

public class FontManager {
    //字体设置
    public Typeface getType(Context context){
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
        return font;
    }

    public Typeface getALiType(Context context){
        Typeface font = Typeface.createFromAsset(context.getAssets(), "iconfont.ttf");
        return font;
    }
}
