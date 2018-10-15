package com.bnuz.ztx.translateapp.View;

/**
 * Created by ZTX on 2018/5/20.
 * 自定义文字轮播
 * 主要功能：实现商城界面的文字轮播效果
 */

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bnuz.ztx.translateapp.R;

import java.util.List;
import java.util.Random;

public class LooperTextView extends FrameLayout{
    private List<String> tipList;//String类型的List，用来存放轮播文字
    private int curTipIndex = 0;//默认当前的轮播位置
    private long lastTimeMillis ;
    private static final int ANIM_DELAYED_MILLIONS = 3 * 1000;//静止时间3秒
    /**  动画持续时长  */
    private static final int ANIM_DURATION = 1* 1000;
    private static final String DEFAULT_TEXT_COLOR = "#2F4F4F";//默认文字颜色
    private static final int DEFAULT_TEXT_SIZE = 13;//默认显示的文字长度，超出部分使用...显示
    private Drawable head_boy,head_girl;
    private TextView tv_tip_out,tv_tip_in;
    private Animation anim_out, anim_in;//动画效果
    //带有上下文的构造器
    public LooperTextView(Context context) {
        super(context);
        initTipFrame();
        initAnimation();
    }

    public LooperTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTipFrame();
        initAnimation();
    }

    public LooperTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTipFrame();
        initAnimation();
    }
    //初始化文本，只有两个一出，一入
    private void initTipFrame() {
        tv_tip_out = newTextView();
        tv_tip_in = newTextView();
        addView(tv_tip_in);
        addView(tv_tip_out);
    }
    private TextView newTextView(){
        //创建TextView
        TextView textView = new TextView(getContext());
        //layout参数设置
        LayoutParams lp = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.CENTER_VERTICAL);
        //将layout参数放进textView里
        textView.setLayoutParams(lp);
        //padding=10
        textView.setCompoundDrawablePadding(10);
        //垂直居中
        textView.setGravity(Gravity.CENTER_VERTICAL);
        //设置行数
        textView.setLines(1);
        //文字间隔
        textView.setEllipsize(TextUtils.TruncateAt.END);
        //文字颜色
        textView.setTextColor(Color.parseColor(DEFAULT_TEXT_COLOR));
        //文字大小
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_TEXT_SIZE);
        return textView;
    }

    //初始化动画
    private void initAnimation() {
        anim_out = newAnimation(0, -1);
        anim_in = newAnimation(1, 0);
        anim_in.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                updateTipAndPlayAnimationWithCheck();
            }
        });
    }
    //创建动画，根据传值得出动画的移入或移出
    private Animation newAnimation(float fromYValue, float toYValue) {
        Animation anim = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,0,
                Animation.RELATIVE_TO_SELF,fromYValue,Animation.RELATIVE_TO_SELF, toYValue);
        anim.setDuration(ANIM_DURATION);
        anim.setStartOffset(ANIM_DELAYED_MILLIONS);
        anim.setInterpolator(new DecelerateInterpolator());
        return anim;
    }
    private void updateTipAndPlayAnimationWithCheck() {
        if (System.currentTimeMillis() - lastTimeMillis < 1000 ) {
            return ;
        }
        lastTimeMillis = System.currentTimeMillis();
        updateTipAndPlayAnimation();
    }
    private void updateTipAndPlayAnimation() {
        if (curTipIndex % 2 == 0) {
            updateTip(tv_tip_out);
            tv_tip_in.startAnimation(anim_out);
            tv_tip_out.startAnimation(anim_in);
            this.bringChildToFront(tv_tip_in);
        } else {
            updateTip(tv_tip_in);
            tv_tip_out.startAnimation(anim_out);
            tv_tip_in.startAnimation(anim_in);
            this.bringChildToFront(tv_tip_out);
        }
    }
    private void updateTip(TextView tipView) {
        if (new Random().nextBoolean()) {
            tipView.setCompoundDrawables(head_boy, null, null, null);
        } else {
            tipView.setCompoundDrawables(head_girl, null, null, null);
        }
        String tip = getNextTip();
        if(!TextUtils.isEmpty(tip)) {
            tipView.setText(Html.fromHtml(tip));
        }
    }
    /**
     *  获取下一条消息
     * @return
     */
    private String getNextTip() {
        if (isListEmpty(tipList)) return null;
        return tipList.get(curTipIndex++ % tipList.size());
    }
    public static boolean isListEmpty(List list) {
        return list == null || list.isEmpty();
    }
    public void setTipList(List<String> tipList) {
        this.tipList = tipList;
        curTipIndex = 0;
        updateTip(tv_tip_out);
        updateTipAndPlayAnimation();
    }
}