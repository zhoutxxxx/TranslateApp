package com.bnuz.ztx.translateapp.Ui;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bnuz.ztx.translateapp.Fragment.ShoppingFragment;
import com.bnuz.ztx.translateapp.Fragment.TranslateFragment;
import com.bnuz.ztx.translateapp.Fragment.UserFragment;
import com.bnuz.ztx.translateapp.Fragment.applyFragment;
import com.bnuz.ztx.translateapp.R;
import com.bnuz.ztx.translateapp.Util.FontManager;
import com.bnuz.ztx.translateapp.net.MyMQTT;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

//
public class TabLayoutViewPager_Activity extends AppCompatActivity {
    //滑动
    private TabLayout mTabLayout;
    //底部滑动标题
    private List<Integer> mListTile;
    //底部滑动图标
    private List<Integer> mListIcon;
    //Fragment的存放
    private List<Fragment> mFragment;
    //View的容器用来放Fragment
    private ViewPager mViewPager;
    protected MyMQTT myMQTT;


    //导航栏
    TextView nav_title;
    TextView nav_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDate();
        initView();
        initEvent();
    }


    private void initDate() {
        //初始化导航标题
        mListTile = new ArrayList<>();
        mListTile.add(R.string.TabLayout_Title_Translate);
        mListTile.add(R.string.TabLayout_Title_shopping);
        mListTile.add(R.string.TabLayout_Title_apply);
        mListTile.add(R.string.TabLayout_Title_User);
        //初始化导航图标
        mListIcon = new ArrayList<>();
        mListIcon.add(R.string.TabLayout_Icon_Translate);
        mListIcon.add(R.string.TabLayout_Icon_shopping);
        mListIcon.add(R.string.TabLayout_Icon_Apply);
        mListIcon.add(R.string.TabLayout_Icon_User);
        //初始化Fragment
        mFragment = new ArrayList<>();
        mFragment.add(new TranslateFragment());
        mFragment.add(new ShoppingFragment());
        mFragment.add(new applyFragment());
        mFragment.add(new UserFragment());

        myMQTT = new MyMQTT(getApplicationContext());
        myMQTT.init();
    }
    //初始化View
    private void initView() {
        mTabLayout = (TabLayout) findViewById(R.id.tablayout);
        mViewPager = (ViewPager) findViewById(R.id.viewpage);

        //预加载
        mViewPager.setOffscreenPageLimit(mFragment.size());
        //mViewPager滑动监听
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
                return mFragment.get(position);
            }

            //返回item的个数
            @Override
            public int getCount() {
                return mFragment.size();
            }

            //滑动标题
            @Override
            public CharSequence getPageTitle(int position) {
                return getResources().getString(mListTile.get(position));
            }
        });
        //绑定
        mTabLayout.setupWithViewPager(mViewPager);
        setupTabIcons();
    }

    private void setupTabIcons() {
        mTabLayout.getTabAt(0).setCustomView(getTabView(0));
        mTabLayout.getTabAt(1).setCustomView(getTabView(1));
        mTabLayout.getTabAt(2).setCustomView(getTabView(2));
        mTabLayout.getTabAt(3).setCustomView(getTabView(3));
    }

    public View getTabView(int position) {
        View v = LayoutInflater.from(getApplication()).inflate(R.layout.tab_item, null);
        nav_icon = (TextView) v.findViewById(R.id.nav_icon);
        nav_icon.setText(mListIcon.get(position));
        nav_icon.setTypeface(new FontManager().getType(getApplication()));
        nav_title = (TextView) v.findViewById(R.id.nav_title);
        nav_title.setText(mListTile.get(position));
        if (position == 0) {
            nav_title.setTextColor(getResources().getColor(R.color.colorPrimary));
            nav_icon.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else {
            nav_title.setTextColor(getResources().getColor(R.color.gray_navigation_bar));
            nav_icon.setTextColor(getResources().getColor(R.color.gray_navigation_bar));
        }
        return v;
    }

    private void initEvent() {
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                changeTabSelect(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                changeTabUnSelect(tab);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void changeTabUnSelect(TabLayout.Tab tab) {
        View view = tab.getCustomView();
        nav_title = view.findViewById(R.id.nav_title);
        nav_icon = view.findViewById(R.id.nav_icon);
        if (nav_title.getText().toString().equals(getString(R.string.TabLayout_Title_Translate))) {
            nav_title.setTextColor(getResources().getColor(R.color.gray_navigation_bar));
            nav_icon.setTextColor(getResources().getColor(R.color.gray_navigation_bar));
            mViewPager.setCurrentItem(0);
        } else if (nav_title.getText().toString().equals(getString(R.string.TabLayout_Title_shopping))) {
            nav_title.setTextColor(getResources().getColor(R.color.gray_navigation_bar));
            nav_icon.setTextColor(getResources().getColor(R.color.gray_navigation_bar));
            mViewPager.setCurrentItem(1);
        } else if (nav_title.getText().toString().equals(getString(R.string.TabLayout_Title_apply))) {
            nav_title.setTextColor(getResources().getColor(R.color.gray_navigation_bar));
            nav_icon.setTextColor(getResources().getColor(R.color.gray_navigation_bar));
            mViewPager.setCurrentItem(2);
        } else if (nav_title.getText().toString().equals(getString(R.string.TabLayout_Title_User))) {
            nav_title.setTextColor(getResources().getColor(R.color.gray_navigation_bar));
            nav_icon.setTextColor(getResources().getColor(R.color.gray_navigation_bar));
            mViewPager.setCurrentItem(3);
        }
    }

    private void changeTabSelect(TabLayout.Tab tab) {
        View view = tab.getCustomView();
        nav_title = view.findViewById(R.id.nav_title);
        nav_icon = view.findViewById(R.id.nav_icon);
        if (nav_title.getText().toString().equals(getString(R.string.TabLayout_Title_Translate))) {
            nav_title.setTextColor(getResources().getColor(R.color.colorPrimary));
            nav_icon.setTextColor(getResources().getColor(R.color.colorPrimary));
            mViewPager.setCurrentItem(0);
        } else if (nav_title.getText().toString().equals(getString(R.string.TabLayout_Title_shopping))) {
            nav_title.setTextColor(getResources().getColor(R.color.colorPrimary));
            nav_icon.setTextColor(getResources().getColor(R.color.colorPrimary));
            mViewPager.setCurrentItem(1);
        } else if (nav_title.getText().toString().equals(getString(R.string.TabLayout_Title_apply))) {
            nav_title.setTextColor(getResources().getColor(R.color.colorPrimary));
            nav_icon.setTextColor(getResources().getColor(R.color.colorPrimary));
            mViewPager.setCurrentItem(2);
        } else if (nav_title.getText().toString().equals(getString(R.string.TabLayout_Title_User))) {
            nav_title.setTextColor(getResources().getColor(R.color.colorPrimary));
            nav_icon.setTextColor(getResources().getColor(R.color.colorPrimary));
            mViewPager.setCurrentItem(3);
        }
    }
}
