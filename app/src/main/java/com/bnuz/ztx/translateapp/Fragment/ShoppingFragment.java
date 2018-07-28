package com.bnuz.ztx.translateapp.Fragment;

import android.graphics.Paint;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bnuz.ztx.translateapp.R;
import com.bnuz.ztx.translateapp.Util.FontManager;

import com.bnuz.ztx.translateapp.Util.URLUtil;
import com.bnuz.ztx.translateapp.View.LooperTextView;
import com.bnuz.ztx.translateapp.net.MyMQTT;
import com.bnuz.ztx.translateapp.net.MyVolley;
import com.youth.banner.Banner;


/**
 * Created by ZTX on 2018/5/2.
 */

public class ShoppingFragment extends Fragment implements View.OnClickListener {
    TextView scanningTextView, enterTextView, cameraTextView;
    EditText editText;
    Banner banner;
    LooperTextView looperTextView;
    ViewStub view1, view2, view3, view4, viewLove;
    TextView more_bt;
    MyVolley myVolley;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping, null);
        findView(view);
        return view;
    }

    private void findView(View view) {
        editText = view.findViewById(R.id.input_et);
        //初始化，订阅推送
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
        myVolley.getBanner(new URLUtil().getIP() + "/Carousel", banner);
        looperTextView = view.findViewById(R.id.myLooper);
        myVolley.getLooperTextView(new URLUtil().getIP() + "/NewsCarousel", looperTextView);
        String promotionUrl = new URLUtil().getIP() + "/Promotion?Type=subPromotion&subTitle=null";
        view1 = view.findViewById(R.id.viewOne);
        myVolley.getPromotion(promotionUrl, view1, 0, getContext());
        view2 = view.findViewById(R.id.viewTwo);
        myVolley.getPromotion(promotionUrl, view2, 1, getContext());
        view3 = view.findViewById(R.id.viewThree);
        myVolley.getPromotion(promotionUrl, view3, 2, getContext());
        view4 = view.findViewById(R.id.viewFour);
        myVolley.getPromotion(promotionUrl, view4, 3, getContext());
        viewLove = view.findViewById(R.id.viewLove);
        myVolley.getPromotion(new URLUtil().getIP() + "/marketLove", viewLove, getContext());

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sao_tv:
                Toast.makeText(getActivity(),"消息已发出",Toast.LENGTH_SHORT).show();
                break;
            case R.id.input_et:
                break;
        }
    }
}