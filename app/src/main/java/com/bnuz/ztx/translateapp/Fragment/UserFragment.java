package com.bnuz.ztx.translateapp.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bnuz.ztx.translateapp.R;
import com.bnuz.ztx.translateapp.Util.FontManager;


/**
 * Created by ZTX on 2018/3/25.
 */

public class UserFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, null);
        findView(view);
        return view;
    }

    private void findView(View view) {
        //图标实例化
        TextView moneyTextView = (TextView)view.findViewById(R.id.money_tv_icon);
        moneyTextView.setTypeface(new FontManager().getType(getActivity()));
        TextView information1 = (TextView)view.findViewById(R.id.information1);
        information1.setTypeface(new FontManager().getType(getActivity()));
        TextView messageTextView = (TextView)view.findViewById(R.id.message_tv_icon);
        messageTextView.setTypeface(new FontManager().getType(getActivity()));
        TextView information2 = (TextView)view.findViewById(R.id.information2);
        information2.setTypeface(new FontManager().getType(getActivity()));
        TextView downloadTextView = (TextView)view.findViewById(R.id.download_tv_icon);
        downloadTextView.setTypeface(new FontManager().getType(getActivity()));
        TextView information3 = (TextView)view.findViewById(R.id.information3);
        information3.setTypeface(new FontManager().getType(getActivity()));
        TextView settingTextView = (TextView)view.findViewById(R.id.setting_tv_icon);
        settingTextView.setTypeface(new FontManager().getType(getActivity()));
        TextView information4 = (TextView)view.findViewById(R.id.information4);
        information4.setTypeface(new FontManager().getType(getActivity()));
        Button editButton = (Button)view.findViewById(R.id.reserve_bt);
        editButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.reserve_bt:
                Log.d("ztx","i am button!");
        }
    }
}
