package com.bnuz.ztx.translateapp.Ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bnuz.ztx.translateapp.Entity.Promotion;
import com.bnuz.ztx.translateapp.R;
import com.bnuz.ztx.translateapp.Util.FontManager;
import com.bnuz.ztx.translateapp.Util.PicassoImageLoader;
import com.bnuz.ztx.translateapp.View.MyScroll;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ZTX on 2018/8/18.
 */

public class GoodsActivity extends AppCompatActivity {
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    AppBarLayout appBarLayout;
    Button backGray, shareGary, shoppingGary;
    TextView backTool, shoppingTool, shareTool, contactService, stories, shopping, goodRemark, question, questionIcon1, questionIcon2, picture;
    TextView moneyIcon, downPriceIcon, careIcon;
    TextView goodsPrice, goodsTitle, goodsSubTitle, goodsExpressPrice, goodsExpressSum, goodsPromotion, goodsFare, goodsWhiteBar, goodsSelect, goodsWeight, goodsRemarkSum, goodsRemarkRate, remarkName1, remarkBody1, remarkTime1, remarkName2, remarkBody2, remarkTime2;
    ImageView appBarImage;
    TabLayout tabLayout;
    List<String> listTitle;
    List<LinearLayout> linearLayoutList;
    MyScroll myScroll;
    Promotion promotion;
    Intent intent;
    LinearLayout linearLayout;
    //判读是否是scrollview主动引起的滑动，true-是，false-否，由tablayout引起的
    private boolean isScroll;
    //记录上一次位置，防止在同一内容块里滑动 重复定位到tablayout
    private int lastPos;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);
        initData();
        initView();

    }

    private void initData() {
        listTitle = new ArrayList<>();
        listTitle.add("商品");
        listTitle.add("评价");
        listTitle.add("详情");

        tabLayout = findViewById(R.id.goods_tab);
        for (int i = 0; i < listTitle.size(); i++) {
            tabLayout.addTab(tabLayout.newTab().setText(listTitle.get(i)));
        }

        intent = getIntent();
        promotion = (Promotion) intent.getSerializableExtra("data");

    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        collapsingToolbarLayout = findViewById(R.id.collapsing);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        collapsingToolbarLayout.setTitle(null);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.RED);
        collapsingToolbarLayout.setExpandedTitleColor(Color.BLUE);
        backGray = findViewById(R.id.back_gray_bt);
        backGray.setTypeface(new FontManager().getALiType(getApplicationContext()));
        shareGary = findViewById(R.id.share_gray_bt);
        shareGary.setTypeface(new FontManager().getALiType(getApplicationContext()));
        shoppingGary = findViewById(R.id.shopping_gray_bt);
        shoppingGary.setTypeface(new FontManager().getALiType(getApplicationContext()));
        backTool = findViewById(R.id.back_tools_tv);
        backTool.setTypeface(new FontManager().getALiType(getApplicationContext()));
        shoppingTool = findViewById(R.id.shopping_tools_tv);
        shoppingTool.setTypeface(new FontManager().getALiType(getApplicationContext()));
        shareTool = findViewById(R.id.share_tools_tv);
        shareTool.setTypeface(new FontManager().getALiType(getApplicationContext()));
        contactService = findViewById(R.id.contact_service_icon_goods);
        contactService.setTypeface(new FontManager().getALiType(getApplicationContext()));
        stories = findViewById(R.id.stories_icon_goods);
        stories.setTypeface(new FontManager().getALiType(getApplicationContext()));
        shopping = findViewById(R.id.shopping_icon_goods);
        shopping.setTypeface(new FontManager().getALiType(getApplicationContext()));
        goodRemark = findViewById(R.id.goodRemark_icon);
        goodRemark.setTypeface(new FontManager().getALiType(getApplicationContext()));
        question = findViewById(R.id.question_and_answer_icon);
        question.setTypeface(new FontManager().getALiType(getApplicationContext()));
        questionIcon1 = findViewById(R.id.question_1);
        questionIcon1.setTypeface(new FontManager().getALiType(getApplicationContext()));
        questionIcon2 = findViewById(R.id.question_2);
        questionIcon2.setTypeface(new FontManager().getALiType(getApplicationContext()));
        picture = findViewById(R.id.picture_Icon);
        picture.setTypeface(new FontManager().getALiType(getApplicationContext()));
        //////////////////
        moneyIcon = findViewById(R.id.money_icon_goods);
        moneyIcon.setTypeface(new FontManager().getType(getApplicationContext()));
        downPriceIcon = findViewById(R.id.downPrice_icon_goods);
        downPriceIcon.setTypeface(new FontManager().getALiType(getApplicationContext()));
        careIcon = findViewById(R.id.care_icon_goods);
        careIcon.setTypeface(new FontManager().getALiType(getApplicationContext()));
//        //将fragment在NestScroll中显示出来
        myScroll = findViewById(R.id.myNest);
        myScroll.setFillViewport(true);
        myScroll.setFocusable(false);


        //数据填充
        appBarImage = findViewById(R.id.myImg);
        new PicassoImageLoader().displayImage(getApplicationContext(), promotion.getImgUrl(), appBarImage);
        goodsPrice = findViewById(R.id.money_tv_goods);
        goodsPrice.setText(promotion.getMoney());
        goodsTitle = findViewById(R.id.title_tv_goods);
        goodsTitle.setText(promotion.getTitle());
        goodsSubTitle = findViewById(R.id.subTitle_tv_goods);
        goodsSubTitle.setText(promotion.getSubTitle());
        goodsExpressPrice = findViewById(R.id.express_free_goods_label);
        goodsExpressPrice.setText(promotion.getExpressPrice());
        goodsExpressSum = findViewById(R.id.saleSum_goods_label);
        goodsExpressSum.setText(promotion.getExpressSum());
        goodsPromotion = findViewById(R.id.promotion_goods_label);
        goodsPromotion.setText(promotion.getPromotion());
        goodsFare = findViewById(R.id.feedBack_goods_label);
        goodsFare.setText(promotion.getFare());
        goodsWhiteBar = findViewById(R.id.whiteBar_goods_label);
        goodsWhiteBar.setText(promotion.getWhiteBar());
        goodsSelect = findViewById(R.id.select_goods_label);
        goodsSelect.setText(promotion.getSelect());
        goodsWeight = findViewById(R.id.weight_goods_label);
        goodsWeight.setText(promotion.getWeight());
        goodsRemarkSum = findViewById(R.id.remarkSum_tv_goods);
        goodsRemarkSum.setText("(" + promotion.getRemarkSum() + ")");
        goodsRemarkRate = findViewById(R.id.goodRate_tv_goods);
        goodsRemarkRate.setText(promotion.getGood() + "%");
        remarkName1 = findViewById(R.id.custom_goods_name);
        remarkName1.setText(promotion.getRemarks().get(0).getRemarkName());
        remarkBody1 = findViewById(R.id.custom_goods_body);
        remarkBody1.setText(promotion.getRemarks().get(0).getRemarkBody());
        remarkTime1 = findViewById(R.id.custom_goods_time);
        remarkTime1.setText(promotion.getRemarks().get(0).getRemarkTime());
        remarkName2 = findViewById(R.id.custom2_goods_name);
        remarkName2.setText(promotion.getRemarks().get(1).getRemarkName());
        remarkBody2 = findViewById(R.id.custom2_goods_body);
        remarkBody2.setText(promotion.getRemarks().get(1).getRemarkBody());
        remarkTime2 = findViewById(R.id.custom2_goods_time);
        remarkTime2.setText(promotion.getRemarks().get(1).getRemarkTime());


        linearLayout = findViewById(R.id.image_Linear);
        for (int i = 0; i < promotion.getImageInformation().size(); i++) {
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            int screenWidth = dm.widthPixels;
            ImageView imageView = new ImageView(getApplicationContext());
            new PicassoImageLoader().loadMaxBitmap(getApplicationContext(), promotion.getImageInformation().get(i).toString(), screenWidth, imageView);
            linearLayout.addView(imageView);
        }


        linearLayoutList = new ArrayList<>();
        linearLayoutList.add((LinearLayout) findViewById(R.id.goods_Linear));
        linearLayoutList.add((LinearLayout) findViewById(R.id.text_Linear));
        linearLayoutList.add((LinearLayout) findViewById(R.id.img_Linear));

        myScroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //当由scrollview触发时，isScroll 置true
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    isScroll = true;
                }
                return false;
            }
        });

        myScroll.setNestScrollCallbacks(new MyScroll.NestScrollCallbacks() {
            @Override
            public void onNestScrollChanged(int x, int y, int oldx, int oldy) {
                if (isScroll) {
                    for (int i = listTitle.size() - 1; i >= 0; i--) {
                        //根据滑动距离，对比各模块距离父布局顶部的高度判断
                        if (y > linearLayoutList.get(i).getTop() - 1) {
                            setScrollPos(i);
                            break;
                        }
                    }
                }
            }
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //点击标签，使scrollview滑动，isScroll置false
                isScroll = false;
                int pos = tab.getPosition();
                int top = linearLayoutList.get(pos).getTop();
                myScroll.fling(0);
                myScroll.smoothScrollTo(0, top);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //透明度监听
        appBarLayout = findViewById(R.id.appBar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int scrollRange = appBarLayout.getTotalScrollRange();
                //初始verticalOffset为0，不能参与计算。
                if (verticalOffset == 0) {
                    backTool.setAlpha(0.0f);
                    shoppingTool.setAlpha(0.0f);
                    shareTool.setAlpha(0.0f);
                    tabLayout.setAlpha(0.0f);
                } else {
                    //保留一位小数
                    float alpha = Math.abs(Math.round(1.00f * verticalOffset / scrollRange) * 10) / 10;
                    backTool.setAlpha(alpha);
                    shoppingTool.setAlpha(alpha);
                    shareTool.setAlpha(alpha);
                    tabLayout.setAlpha(alpha);
                }
            }
        });
    }

    //tablayout对应标签的切换
    private void setScrollPos(int newPos) {
        if (lastPos != newPos) {
            //该方法不会触发tablayout 的onTabSelected 监听
            tabLayout.setScrollPosition(newPos, 0, true);
        }
        lastPos = newPos;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
