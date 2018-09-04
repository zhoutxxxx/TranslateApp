package com.bnuz.ztx.translateapp.Ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.bnuz.ztx.translateapp.R;
import com.bnuz.ztx.translateapp.Util.FontManager;

public class DownLoadActivity extends AppCompatActivity {
    TextView noDownLoadIcon,noDownLoadLabel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_load);
        noDownLoadIcon = findViewById(R.id.noDownLoad_Icon);
        noDownLoadIcon.setTypeface(new FontManager().getType(getApplicationContext()));
        noDownLoadLabel = findViewById(R.id.noDownLoad_Label);
    }
}
