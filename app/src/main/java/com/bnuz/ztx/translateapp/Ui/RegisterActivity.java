package com.bnuz.ztx.translateapp.Ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bnuz.ztx.translateapp.R;
import com.bnuz.ztx.translateapp.Util.FontManager;
import com.bnuz.ztx.translateapp.Util.URLUtil;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    TextView phone,mail,password,eye;
    EditText phoneEditText,mailEditText,passwordEditText;
    Button register;
    boolean isLook = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        phone = (TextView)findViewById(R.id.phone_tv);
        phone.setTypeface(new FontManager().getType(getApplicationContext()));
        mail = (TextView)findViewById(R.id.eMail_tv);
        mail.setTypeface(new FontManager().getType(getApplicationContext()));
        password = (TextView)findViewById(R.id.password_tv);
        password.setTypeface(new FontManager().getType(getApplicationContext()));
        register = (Button) findViewById(R.id.register_bt);
        eye = (TextView)findViewById(R.id.eye_tv);
        eye.setTypeface(new FontManager().getType(getApplicationContext()));
        eye.setOnClickListener(this);
        register.setOnClickListener(this);
        phoneEditText = (EditText) findViewById(R.id.phone_register_et);
        mailEditText = (EditText)findViewById(R.id.mail_register_et);
        passwordEditText = (EditText)findViewById(R.id.password_register_et);
        phoneEditText.setOnClickListener(this);
        mailEditText.setOnClickListener(this);
        passwordEditText.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.register_bt:
                String phone = phoneEditText.getText().toString();
                String mail = mailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                new RxVolley.Builder()
                        .url(new URLUtil().getIP() + "/Register")//访问地址
                        .httpMethod(RxVolley.Method.POST)//访问方式POST
                        .params(new URLUtil().getRegisterParams(phone,mail,password))//参数
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
            case R.id.phone_register_et:
            case R.id.mail_register_et:
            case R.id.password_register_et:
                phoneEditText.setCursorVisible(true);
                mailEditText.setCursorVisible(true);
                passwordEditText.setCursorVisible(true);
                break;
            case R.id.eye_tv:
                if (!isLook){//不可见，改为可见状态
                    passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isLook = true;
                }else{//可见，改为隐藏状态
                    passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isLook = false;
                }
                break;
        }
    }

    private void parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            String s = jsonObject.getString("resultCode");
            if(s.equals("200")){
                Toast.makeText(getApplicationContext(),"注册成功",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this,LoginActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(getApplicationContext(),"注册失败",Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
