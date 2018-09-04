package com.bnuz.ztx.translateapp.View;

import android.content.Context;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;

/**
 * Created by ZTX on 2018/8/23.
 */

public class MyScroll extends NestedScrollView implements NestedScrollingChild{

    private NestScrollCallbacks nestScrollCallbacks;
    private NestedScrollingChildHelper nestedScrollingChildHelper;


    public MyScroll(Context context) {
        super(context);
    }

    public MyScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScroll(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public NestScrollCallbacks getNestScrollCallbacks() {
        return nestScrollCallbacks;
    }

    public void setNestScrollCallbacks(NestScrollCallbacks nestScrollCallbacks) {
        this.nestScrollCallbacks = nestScrollCallbacks;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (nestScrollCallbacks != null) {
            nestScrollCallbacks.onNestScrollChanged(l, t, oldl, oldt);
        }
    }

    //定义接口用于回调
    public interface NestScrollCallbacks {
        void onNestScrollChanged(int x, int y, int oldx, int oldy);
    }


    @Override
    public boolean startNestedScroll(int axes) {
        return nestedScrollingChildHelper.startNestedScroll(axes);
    }

}
