package com.bnuz.ztx.translateapp.Fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bnuz.ztx.translateapp.R;
import com.bnuz.ztx.translateapp.Ui.GoodsActivity;
import com.bnuz.ztx.translateapp.Ui.PromotionActivity;
import com.bnuz.ztx.translateapp.Util.FontManager;

import com.bnuz.ztx.translateapp.Util.URLUtil;
import com.bnuz.ztx.translateapp.View.LooperTextView;
import com.bnuz.ztx.translateapp.net.MyMQTT;
import com.bnuz.ztx.translateapp.net.MyVolley;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.http.VolleyError;
import com.youth.banner.Banner;


/**
 * Created by ZTX on 2018/5/2.
 * 主Activity->商品Fragment
 */

public class ShoppingFragment extends Fragment implements View.OnClickListener {
    TextView scanningTextView, enterTextView, cameraTextView;//扫码图标，搜索图标，拍照图标
    EditText editText;//输入框
    Banner banner;//商城轮播图
    LooperTextView looperTextView;//自定义文字轮播
    ViewStub view1, view2, view3, view4, viewLove;//子view模块
    TextView more_bt;//更多按钮
    MyVolley myVolley;//volley类，请求数据
    ImageView img1, img2, img3, img4, img5, img6, img7, img8;
    Intent intent;//点击跳转Intent

    //创建View，添加布局
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping, null);
        findView(view);
        return view;
    }

    private void findView(View view) {
        //控件实例化
        editText = view.findViewById(R.id.input_et);
        more_bt = view.findViewById(R.id.more);
        more_bt.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        scanningTextView = view.findViewById(R.id.sao_tv);
        scanningTextView.setTypeface(new FontManager().getALiType(getContext()));
        scanningTextView.setOnClickListener(this);
        enterTextView = view.findViewById(R.id.enter_tv);
        enterTextView.setTypeface(new FontManager().getALiType(getContext()));
        cameraTextView = view.findViewById(R.id.camera_tv);
        cameraTextView.setTypeface(new FontManager().getALiType(getContext()));
        myVolley = new MyVolley(getContext());
        banner = view.findViewById(R.id.myBanner);
        //通过自定义请求类，请求数据
        myVolley.getBanner(new URLUtil().getIP() + "/Carousel", banner);
        looperTextView = view.findViewById(R.id.myLooper);
        myVolley.getLooperTextView(new URLUtil().getIP() + "/NewsCarousel", looperTextView);
        String promotionUrl = new URLUtil().getIP() + "/Promotion?Type=subPromotion&subTitle=null";
        view1 = view.findViewById(R.id.viewOne);
        view1.setOnClickListener(this);
        myVolley.getPromotion(promotionUrl, view1, 0, getContext());
        view2 = view.findViewById(R.id.viewTwo);
        myVolley.getPromotion(promotionUrl, view2, 1, getContext());
        view3 = view.findViewById(R.id.viewThree);
        myVolley.getPromotion(promotionUrl, view3, 2, getContext());
        view4 = view.findViewById(R.id.viewFour);
        myVolley.getPromotion(promotionUrl, view4, 3, getContext());
        viewLove = view.findViewById(R.id.viewLove);
        myVolley.getPromotion(new URLUtil().getIP() + "/marketLove", viewLove, getContext());
        img1 = view.findViewById(R.id.image_bt1);
        img1.setOnClickListener(this);
        img2 = view.findViewById(R.id.image_bt2);
        img2.setOnClickListener(this);
        img3 = view.findViewById(R.id.image_bt3);
        img3.setOnClickListener(this);
        img4 = view.findViewById(R.id.image_bt4);
        img4.setOnClickListener(this);
        img5 = view.findViewById(R.id.image_bt5);
        img5.setOnClickListener(this);
        img6 = view.findViewById(R.id.image_bt6);
        img6.setOnClickListener(this);
        img7 = view.findViewById(R.id.image_bt7);
        img7.setOnClickListener(this);
        img8 = view.findViewById(R.id.image_bt8);
        img8.setOnClickListener(this);
    }

    //点击事件
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.sao_tv:
                Toast.makeText(getActivity(), "消息已发出", Toast.LENGTH_SHORT).show();
                break;
            case R.id.input_et:
                break;
            case R.id.image_bt1:
            case R.id.image_bt2:
            case R.id.image_bt3:
            case R.id.image_bt4:
            case R.id.image_bt5:
            case R.id.image_bt6:
            case R.id.image_bt7:
            case R.id.image_bt8:
                intent = new Intent(getActivity(), PromotionActivity.class);
                intent.putExtra("data", "1");
                startActivity(intent);
                break;
        }
    }
}