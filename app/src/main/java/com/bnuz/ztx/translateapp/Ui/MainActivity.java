package com.bnuz.ztx.translateapp.Ui;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bnuz.ztx.translateapp.Fragment.TranslateFragment;
import com.bnuz.ztx.translateapp.Fragment.UserFragment;
import com.bnuz.ztx.translateapp.R;
import com.bnuz.ztx.translateapp.Util.FontManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //去掉阴影
        getSupportActionBar().setElevation(0);

        initDate();
        initView();
    }

    private void initDate() {
        //初始化导航标题
        mListTile = new ArrayList<>();
        mListTile.add(R.string.TabLayout_Title_Translate);
        mListTile.add(R.string.TabLayout_Title_User);
        //初始化导航图标
        mListIcon = new ArrayList<>();
        mListIcon.add(R.string.TabLayout_Icon_Translate);
        mListIcon.add(R.string.TabLayout_Icon_User);
        //初始化Fragment
        mFragment = new ArrayList<>();
        mFragment.add(new TranslateFragment());
        mFragment.add(new UserFragment());
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
                Log.i("TAG", "position:" + position);
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
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(getTabView(getResources().getString(mListTile.get(i)), getResources().getString(mListIcon.get(i))));
            }
        }
    }

    public View getTabView(String title, String icon) {
        View v = LayoutInflater.from(getApplication()).inflate(R.layout.tab_item, null);
        TextView textView2= (TextView) v.findViewById(R.id.imageView2);
        textView2.setText(icon);
        textView2.setTypeface(new FontManager().getType(getApplication()));
        TextView textView1 = (TextView) v.findViewById(R.id.textView1);
        textView1.setText(title);
        return v;
    }

}
