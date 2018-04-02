package com.bnuz.ztx.translateapp.Ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bnuz.ztx.translateapp.R;

import org.angmarch.views.NiceSpinner;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by ZTX on 2018/4/2.
 */

public class SettingActivity extends AppCompatActivity {
    NiceSpinner niceSpinner;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);
        initView();
    }

    private void initView() {
        niceSpinner = (NiceSpinner) findViewById(R.id.exchangLanguage_np);
        List<String> data = new LinkedList<>();
        data.add(getResources().getString(R.string.Exchange_language_Chinese_item));
        data.add(getResources().getString(R.string.Exchange_language_English_item));
        niceSpinner.attachDataSource(data);
    }
}
