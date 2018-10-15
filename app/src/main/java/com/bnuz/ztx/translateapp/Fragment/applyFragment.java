package com.bnuz.ztx.translateapp.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bnuz.ztx.translateapp.R;
import com.bnuz.ztx.translateapp.Ui.RobotActivity;
import com.bnuz.ztx.translateapp.Ui.VideoActivity;
import com.bnuz.ztx.translateapp.Util.FontManager;



/**
 * Created by ZTX on 2018/5/25.
 * 主APP->小应用Fragment
 */

public class applyFragment extends Fragment implements View.OnClickListener {
    Button service, video;//视频通话按钮，在线客服按钮
    Intent intent;

    //创建View，添加布局
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_apply, null);
        findView(view);
        return view;
    }

    private void findView(View view) {
        //实例化
        video = (Button) view.findViewById(R.id.video_bt);
        video.setTypeface(new FontManager().getALiType(getContext()));
        video.setOnClickListener(this);
        service = (Button) view.findViewById(R.id.service_bt);
        service.setTypeface(new FontManager().getALiType(getContext()));
        service.setOnClickListener(this);
    }
    //点击事件
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.video_bt:
                intent = new Intent(getActivity(), VideoActivity.class);
                startActivity(intent);
                break;
            case R.id.service_bt:
                intent = new Intent(getActivity(), RobotActivity.class);
                startActivity(intent);
                break;
        }

    }

}
