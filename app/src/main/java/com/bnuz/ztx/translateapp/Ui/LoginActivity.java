package com.bnuz.ztx.translateapp.Ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bnuz.ztx.translateapp.R;
import com.bnuz.ztx.translateapp.Util.ShareUtils;

import java.util.Locale;

/**
 * Created by ZTX on 2018/4/3.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button loginButton;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        loginButton = (Button)findViewById(R.id.login_bt);
        loginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_bt:
                Resources resources = LoginActivity.this.getResources();
                DisplayMetrics dm = resources.getDisplayMetrics();
                Configuration config = resources.getConfiguration();
                // 应用用户选择语言
                switch (ShareUtils.getInt(LoginActivity.this,"language",0)){
                    case 0:
                        config.locale = Locale.SIMPLIFIED_CHINESE;
                        break;
                    case 1:
                        config.locale = Locale.ENGLISH;
                        break;
                    default:
                        config.locale = Locale.SIMPLIFIED_CHINESE;
                        break;
                }
                resources.updateConfiguration(config, dm);
                Intent intent = new Intent(this, TabLayoutViewPager_Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }
    }
}
