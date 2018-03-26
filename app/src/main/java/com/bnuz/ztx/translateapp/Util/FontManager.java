package com.bnuz.ztx.translateapp.Util;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by ZTX on 2018/3/25.
 */

public class FontManager {
    //字体设置
    public Typeface getType(Context context){
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
        return font;
    }
}
