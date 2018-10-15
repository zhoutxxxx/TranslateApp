package com.bnuz.ztx.translateapp.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bnuz.ztx.translateapp.Adapter.PromotionOneAdapter;
import com.bnuz.ztx.translateapp.Entity.Promotion;
import com.bnuz.ztx.translateapp.Entity.Remark;
import com.bnuz.ztx.translateapp.Interface.OnItemClickListener;
import com.bnuz.ztx.translateapp.R;
import com.bnuz.ztx.translateapp.Ui.GoodsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZTX on 2018/8/11.
 */

public class PromotionFragmentTwo extends Fragment implements OnItemClickListener {
    //商品显示的列表
    RecyclerView recyclerView;
    //Promotion类的List
    List<Promotion> list;
    //自定义Adapter
    PromotionOneAdapter promotionOneAdapter;
    //点击跳转的Intent
    Intent intent;

    //创建View，添加布局
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_promotion_one, null);
        //fragment获取父Activity传过来的bundle数据
        Bundle bundle = getArguments();
        String json = bundle.getString("Json");
        findView(view, json);
        return view;
    }

    private void findView(View view, String json) {
        list = new ArrayList<>();
        parsingJson(json);
        recyclerView = view.findViewById(R.id.Promotion_recycle);
        //设置管理器
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        //初始化适配器
        promotionOneAdapter = new PromotionOneAdapter(getActivity(), list);
        //设置添加或删除item时的动画，这里使用默认动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //设置适配器
        recyclerView.setAdapter(promotionOneAdapter);
        //item点击事件
        promotionOneAdapter.setItemClickListener(this);
    }

    private void parsingJson(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = jsonObject.getJSONArray("childTitle");
            JSONObject object = jsonArray.getJSONObject(1);
            JSONArray goods = object.getJSONArray("goods");
            for (int i = 0; i < goods.length(); i++) {
                Promotion promotion = new Promotion();
                JSONObject json = goods.getJSONObject(i);
                promotion.setImgUrl(json.getString("imgUrl"));
                promotion.setTitle(json.getString("goodsTitle"));
                promotion.setMoney(json.getString("goodsPrice"));
                promotion.setAct(json.getString("goodsActivity"));
                promotion.setSubTitle(json.getString("goodsSubTitle"));
                promotion.setExpressPrice(json.getString("goodsExpressPrice"));
                promotion.setExpressSum(json.getString("goodsExpressSum"));
                promotion.setPromotion(json.getString("goodsPromotion"));
                promotion.setFare(json.getString("goodsFare"));
                promotion.setWhiteBar(json.getString("goodsWhiteBar"));
                promotion.setSelect(json.getString("goodsSelect"));
                promotion.setWeight(json.getString("goodsWeight"));
                promotion.setGood(json.getString("goodsPrefect"));
                promotion.setRemarkSum(json.getString("goodsRemarkSum"));
                String remark = json.getString("goodsRemark");
                parsingRemark(remark, promotion);
                String imageInformation = json.getString("imageInformation");
                parsingImage(imageInformation, promotion);
                list.add(promotion);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parsingImage(String imageInformation, Promotion promotion) {
        String[] a = imageInformation.split("\\;");
        List<String> url = new ArrayList<>();
        for (int i = 0; i < a.length; i++) {
            url.add(a[i]);
        }
        promotion.setImageInformation(url);
    }

    private void parsingRemark(String remark, Promotion promotion) {
        String[] a = remark.split("\\;");
        List<Remark> remarks = new ArrayList<>();
        Remark remark1 = new Remark();
        remark1.setRemarkName(a[0]);
        remark1.setRemarkBody(a[1]);
        remark1.setRemarkTime(a[2]);
        remarks.add(remark1);
        Remark remark2 = new Remark();
        remark2.setRemarkName(a[3]);
        remark2.setRemarkBody(a[4]);
        remark2.setRemarkTime(a[5]);
        remarks.add(remark2);
        promotion.setRemarks(remarks);
    }

    @Override
    public void onItemClick(int position) {
        intent = new Intent(getContext(), GoodsActivity.class);
        intent.putExtra("data", list.get(position));
        startActivity(intent);
    }
}
