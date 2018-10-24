package com.bnuz.ztx.translateapp.Ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.bnuz.ztx.translateapp.R;
import com.bnuz.ztx.translateapp.Util.FontManager;
import com.bnuz.ztx.translateapp.Util.ShareUtils;

import org.angmarch.views.NiceSpinner;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Created by ZTX on 2018/4/2.
 */

public class SettingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    NiceSpinner niceSpinner;//下拉列表
    List<String> data;//String类型的List 用来存放修改语言的选项
    TextView back;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);
        initView();
    }

    private void initView() {
        niceSpinner = (NiceSpinner) findViewById(R.id.exchangLanguage_np);
        data = new LinkedList<>();
        data.add(getResources().getString(R.string.Exchange_language_Chinese_item));
        data.add(getResources().getString(R.string.Exchange_language_English_item));
        niceSpinner.attachDataSource(data);
        //记住当前的选项
        niceSpinner.setSelectedIndex(ShareUtils.getInt(SettingActivity.this,"language",0));
        //下拉列表的点击事件
        niceSpinner.setOnItemSelectedListener(this);

        back = findViewById(R.id.back_setting_tv);
        back.setTypeface(new FontManager().getALiType(getApplicationContext()));
        back.setOnClickListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l){
        //获取APP的配置信息，更改语言。
        Resources resources = SettingActivity.this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        // 应用用户选择语言
        switch (i){
            case 0:
                config.locale = Locale.SIMPLIFIED_CHINESE;
                ShareUtils.putInt(getApplicationContext(),"language",i);

                break;
            case 1:
                config.locale = Locale.ENGLISH;
                ShareUtils.putInt(getApplicationContext(),"language",i);
                break;
            default:
                config.locale = Locale.SIMPLIFIED_CHINESE;
                ShareUtils.putInt(getApplicationContext(),"language",i);
                break;
        }
        resources.updateConfiguration(config, dm);
        Toast.makeText(getApplicationContext(),getResources().getString(R.string.change_Language_Log) + data.get(i).toString(),Toast.LENGTH_SHORT).show();
        //成功修改后需重新启动Activity
        Intent intent = new Intent(this, TabLayoutViewPager_Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Toast.makeText(SettingActivity.this,"NiceSpinner index is null" , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_setting_tv:
                finish();
                break;
        }
    }
}
