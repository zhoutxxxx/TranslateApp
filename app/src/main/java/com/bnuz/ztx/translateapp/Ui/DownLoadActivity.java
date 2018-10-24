package com.bnuz.ztx.translateapp.Ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bnuz.ztx.translateapp.R;
import com.bnuz.ztx.translateapp.Util.FontManager;

public class DownLoadActivity extends AppCompatActivity implements View.OnClickListener {
    TextView noDownLoadIcon,noDownLoadLabel,back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_load);
        noDownLoadIcon = findViewById(R.id.noDownLoad_Icon);
        noDownLoadIcon.setTypeface(new FontManager().getType(getApplicationContext()));
        noDownLoadLabel = findViewById(R.id.noDownLoad_Label);
        back = findViewById(R.id.back_download_tv);
        back.setTypeface(new FontManager().getALiType(getApplicationContext()));
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_download_tv:
                finish();
                break;
        }
    }
}
