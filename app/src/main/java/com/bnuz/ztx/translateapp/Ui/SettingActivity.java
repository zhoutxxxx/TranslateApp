package com.bnuz.ztx.translateapp.Ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.bnuz.ztx.translateapp.R;
import com.bnuz.ztx.translateapp.Util.ShareUtils;

import org.angmarch.views.NiceSpinner;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Created by ZTX on 2018/4/2.
 */

public class SettingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    NiceSpinner niceSpinner;
    List<String> data;
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
        niceSpinner.setSelectedIndex(ShareUtils.getInt(SettingActivity.this,"language",0));
        niceSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l){
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
        Toast.makeText(getApplicationContext(),"成功修改语言为：" + data.get(i).toString(),Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, TabLayoutViewPager_Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Toast.makeText(SettingActivity.this,"NiceSpinner index is null" , Toast.LENGTH_SHORT).show();
    }
}
