package com.bnuz.ztx.translateapp.Ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bnuz.ztx.translateapp.R;
import com.bnuz.ztx.translateapp.Util.FontManager;
import com.bnuz.ztx.translateapp.Util.ShareUtils;
import com.bnuz.ztx.translateapp.Util.URLUtil;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Locale;
import java.util.regex.Pattern;

import butterknife.ButterKnife;

/**
 * Created by ZTX on 2018/4/3.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button loginButton;
    TextView user, password, forgetPsw, register, passLogin, eye;
    EditText userEdit, pswEdit;
    boolean isLook = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {

        loginButton = (Button) findViewById(R.id.login_bt);
        loginButton.setOnClickListener(this);
        user = (TextView) findViewById(R.id.user_icon_tv);
        user.setTypeface(new FontManager().getType(getApplicationContext()));
        password = (TextView)findViewById(R.id.psw_icon_tv);
        password.setTypeface(new FontManager().getType(getApplicationContext()));
        forgetPsw = (TextView)findViewById(R.id.forgetPsw_tv);
        forgetPsw.setOnClickListener(this);
        register = (TextView)findViewById(R.id.register_tv);
        register.setOnClickListener(this);
        passLogin = (TextView)findViewById(R.id.passLogin_tv);
        passLogin.setOnClickListener(this);
        userEdit = (EditText) findViewById(R.id.user_et);
        pswEdit = (EditText) findViewById(R.id.password_et);
        userEdit.setOnClickListener(this);
        pswEdit.setOnClickListener(this);
        eye = (TextView)findViewById(R.id.eye_tv);
        eye.setTypeface(new FontManager().getType(getApplicationContext()));
        eye.setOnClickListener(this);


        String user = ShareUtils.getString(getApplicationContext(), "user", null);
        String psw = ShareUtils.getString(getApplicationContext(), "psw", null);
        if (user != null && psw != null){
            Intent intent = new Intent(this,TabLayoutViewPager_Activity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_bt:
                String isPhone;
                String userStr = userEdit.getText().toString();
                if (checkEmail(userStr)) {
                    isPhone = "false";
                } else {
                    isPhone = "true";
                }
                String pswStr = pswEdit.getText().toString();
                new RxVolley.Builder()
                        .url(new URLUtil().getIP() + "/Login")//访问地址
                        .httpMethod(RxVolley.Method.POST)//访问方式POST
                        .params(new URLUtil().getLoginParams(userStr, pswStr, isPhone))//参数
                        .encoding("UTF-8")//UTF-8编码
                        .timeout(3000)
                        .callback(new HttpCallback() {
                            @Override
                            public void onSuccess(String t) {
                                Logger.json(t);
                                parsingJson(t);
                            }
                            @Override
                            public void onFailure(int errorNo, String strMsg) {
                                Logger.d(strMsg + "errorCode:" + errorNo);
                                Toast.makeText(getApplicationContext(), "请求超时，请检查网络", Toast.LENGTH_SHORT).show();
                            }
                        })//响应
                        .doTask();//执行请求
                break;
            case R.id.passLogin_tv:
                IntentActivity();
                break;
            case R.id.eye_tv:
                if (!isLook) {//不可见，改为可见状态
                    pswEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isLook = true;
                } else {//可见，改为隐藏状态
                    pswEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isLook = false;
                }
                break;
            case R.id.user_et:
            case R.id.password_et:
                userEdit.setCursorVisible(true);
                pswEdit.setCursorVisible(true);
                break;
            case R.id.register_tv:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void parsingJson(String t) {
        try {
            JSONObject json = new JSONObject(t);
            String code = json.getString("resultCode").toString();
            if (code.equals("200")) {
                ShareUtils.putString(getApplicationContext(), "user", userEdit.getText().toString().trim());
                ShareUtils.putString(getApplicationContext(), "psw", pswEdit.getText().toString().trim());
                IntentActivity();
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.check_Information), Toast.LENGTH_LONG).show();
                pswEdit.setText(null);
                ShareUtils.putString(getApplicationContext(), "psw", null);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void IntentActivity() {
        Resources resources = LoginActivity.this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        // 应用用户选择语言
        switch (ShareUtils.getInt(LoginActivity.this, "language", 0)) {
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
    }

    public boolean checkEmail(String email) {
        String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        return Pattern.matches(regex, email);
    }

}
