package com.bnuz.ztx.translateapp.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bnuz.ztx.translateapp.R;
import com.bnuz.ztx.translateapp.Util.FontManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by ZTX on 2018/5/25.
 */

public class applyFragment extends Fragment {
    @BindView(R.id.service_bt) Button service;
    @BindView(R.id.video_bt) Button video;
    Unbinder unbinder;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_apply, null);
        unbinder = ButterKnife.bind(this,view);
        findView();
        return view;
    }

    private void findView() {
        video.setTypeface(new FontManager().getALiType(getContext()));
        service.setTypeface(new FontManager().getALiType(getContext()));
    }
}
