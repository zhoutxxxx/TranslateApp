package com.bnuz.ztx.translateapp.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bnuz.ztx.translateapp.R;
import com.bnuz.ztx.translateapp.Ui.SettingActivity;
import com.bnuz.ztx.translateapp.Util.FontManager;


/**
 * Created by ZTX on 2018/3/25.
 */

public class UserFragment extends android.support.v4.app.Fragment implements View.OnClickListener {
    TextView information1,information2,information3,information4,information1TextView,information2TextView,information3TextView,information4TextView;
    TextView moneyTextView,messageTextView,downloadTextView,settingTextView;
    Button editButton;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, null);
        findView(view);
        return view;
    }

    private void findView(View view) {
        //图标实例化
        moneyTextView = (TextView)view.findViewById(R.id.money_tv_icon);
        moneyTextView.setTypeface(new FontManager().getType(getActivity()));
        information1 = (TextView)view.findViewById(R.id.information1);
        information1.setTypeface(new FontManager().getType(getActivity()));
        messageTextView = (TextView)view.findViewById(R.id.message_tv_icon);
        messageTextView.setTypeface(new FontManager().getType(getActivity()));
        information2 = (TextView)view.findViewById(R.id.information2);
        information2.setTypeface(new FontManager().getType(getActivity()));
        downloadTextView = (TextView)view.findViewById(R.id.download_tv_icon);
        downloadTextView.setTypeface(new FontManager().getType(getActivity()));
        information3 = (TextView)view.findViewById(R.id.information3);
        information3.setTypeface(new FontManager().getType(getActivity()));
        settingTextView = (TextView)view.findViewById(R.id.setting_tv_icon);
        settingTextView.setTypeface(new FontManager().getType(getActivity()));
        //设置item右边图标点击事件
        information4 = (TextView)view.findViewById(R.id.information4);
        information4.setTypeface(new FontManager().getType(getActivity()));
        information4.setOnClickListener(this);
        information1TextView = (TextView)view.findViewById(R.id.money_tv);
        information2TextView = (TextView)view.findViewById(R.id.message_tv);
        information3TextView = (TextView)view.findViewById(R.id.download_tv);
        //设置item的点击事件
        information4TextView = (TextView)view.findViewById(R.id.setting_tv);
        information4TextView.setOnClickListener(this);
        //编辑资料的按钮事件
        editButton = (Button)view.findViewById(R.id.reserve_bt);
        editButton.setOnClickListener(this);

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.reserve_bt:
                Toast.makeText(getActivity(),"这是编辑按钮",Toast.LENGTH_SHORT).show();
                break;
            case R.id.setting_tv: case R.id.information4:
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
                break;
        }
    }
}
