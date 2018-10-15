package com.bnuz.ztx.translateapp.View;

import android.content.Context;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ZTX on 2018/8/25.
 * 自定义折叠布局
 * 主要功能：运用于上面详细界面的滑动，出现一个折叠效果
 */

public class MyCollapsing extends CollapsingToolbarLayout implements NestedScrollingParent {
    private NestedScrollingParentHelper nestedScrollingParentHelper;

    public MyCollapsing(Context context) {
        super(context);
    }

    public MyCollapsing(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyCollapsing(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    //view是子布局，dx子布局滑动的X轴方向距离，dy子布局滑动y轴方距离。consumed数组表示父布局消化掉的距离。长度为2，0表示x轴，1表示y轴
    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        consumed[1] = 380;
        scrollBy(0,dy);
    }
}
