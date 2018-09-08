package com.bnuz.ztx.translateapp.Ui;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bnuz.ztx.translateapp.Entity.Promotion;
import com.bnuz.ztx.translateapp.Fragment.PromotionFragmentOne;
import com.bnuz.ztx.translateapp.Fragment.PromotionFragmentThree;
import com.bnuz.ztx.translateapp.Fragment.PromotionFragmentTwo;
import com.bnuz.ztx.translateapp.Fragment.ShoppingFragment;
import com.bnuz.ztx.translateapp.R;
import com.bnuz.ztx.translateapp.Util.FontManager;
import com.bnuz.ztx.translateapp.Util.URLUtil;
import com.bnuz.ztx.translateapp.View.CustomDialog;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.http.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PromotionActivity extends AppCompatActivity implements View.OnClickListener {
    TextView back, share, title;
    List<String> titleList;
    List<Fragment> fragmentList;
    TabLayout mTab;
    ViewPager mViewPager;
    Intent intent;
    CustomDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion);
        initDate();
        initView();
    }

    private void initDate() {
        titleList = new ArrayList<>();
        titleList.add(" ");
        titleList.add(" ");
        titleList.add(" ");
        fragmentList = new ArrayList<>();
        fragmentList.add(new PromotionFragmentOne());
        fragmentList.add(new PromotionFragmentTwo());
        fragmentList.add(new PromotionFragmentThree());
    }

    private void parsingJson(String s) {
        try {
            titleList.clear();
            JSONObject jsonObject = new JSONObject(s);
            String topTitle = jsonObject.getString("topTitle");
            title.setText(topTitle);
            JSONArray jsonArray = jsonObject.getJSONArray("childTitle");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                titleList.add(object.getString("childTitle"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        back = findViewById(R.id.back_tv);
        back.setTypeface(new FontManager().getALiType(getApplicationContext()));
        back.getPaint().setFakeBoldText(true);
        back.setOnClickListener(this);
        share = findViewById(R.id.share_tv);
        share.setTypeface(new FontManager().getALiType(getApplicationContext()));
        share.getPaint().setFakeBoldText(true);
        share.setOnClickListener(this);
        dialog = new CustomDialog(this, 100, 100, R.layout.dialog_share, R.style.Theme_dialog, Gravity.BOTTOM, R.style.pop_anim_style);
        TextView qqShare = dialog.findViewById(R.id.qq_share_icon);
        qqShare.setTypeface(new FontManager().getALiType(getApplicationContext()));
        TextView wxShare = dialog.findViewById(R.id.wx_share_icon);
        wxShare.setTypeface(new FontManager().getALiType(getApplicationContext()));
        TextView friendShare = dialog.findViewById(R.id.friend_share_icon);
        friendShare.setTypeface(new FontManager().getALiType(getApplicationContext()));
        TextView wbShare = dialog.findViewById(R.id.wb_share_icon);
        wbShare.setTypeface(new FontManager().getALiType(getApplicationContext()));
        dialog.setCancelable(true);

        title = findViewById(R.id.title_promotion);
        intent = getIntent();
        String s = intent.getStringExtra("data");
        RxVolley.get(new URLUtil().getIP() + "/goods?goodsIndex=" + s, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                parsingJson(t);
                Bundle bundle = new Bundle();
                bundle.putString("Json", t);
                for (int i = 0; i < fragmentList.size(); i++) {
                    fragmentList.get(i).setArguments(bundle);
                }
                mTab = findViewById(R.id.Promotion_tab);
                mViewPager = findViewById(R.id.Promotion_vp);

                mViewPager.setOffscreenPageLimit(fragmentList.size());

                mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    }

                    @Override
                    public void onPageSelected(int position) {
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                    }
                });

                //设置适配器
                mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
                    //选中的item
                    @Override
                    public Fragment getItem(int position) {
                        return fragmentList.get(position);
                    }

                    //返回item的个数
                    @Override
                    public int getCount() {
                        return fragmentList.size();
                    }

                    //滑动标题
                    @Override
                    public CharSequence getPageTitle(int position) {
                        return titleList.get(position);
                    }
                });
                //绑定
                mTab.setupWithViewPager(mViewPager);
            }

            @Override
            public void onFailure(VolleyError error) {
                Toast.makeText(getApplicationContext(), "网络错误，请检查", Toast.LENGTH_SHORT).show();
            }
        });


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_tv:
                finish();
                break;
            case R.id.share_tv:
                dialog.show();
                break;
        }
    }
}
