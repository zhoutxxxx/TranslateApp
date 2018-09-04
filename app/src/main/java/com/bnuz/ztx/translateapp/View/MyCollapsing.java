package com.bnuz.ztx.translateapp.View;

import android.content.Context;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ZTX on 2018/8/25.
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


    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        consumed[1] = 380;
        scrollBy(0,dy);
    }
}
