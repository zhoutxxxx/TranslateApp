package com.bnuz.ztx.translateapp.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bnuz.ztx.translateapp.R;
import com.bnuz.ztx.translateapp.Util.FontManager;

import org.angmarch.views.NiceSpinner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by ZTX on 2018/3/25.
 */

public class TranslateFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_translate, null);
        findView(view);
        return view;
    }

    private void findView(View view) {
        NiceSpinner niceSpinner1 = (NiceSpinner) view.findViewById(R.id.nice_spinner_1);
        List<String> dataset1 = new LinkedList<>(Arrays.asList("自动检测","中文", "英语", "日语", "韩语", "葡萄牙语"));
        niceSpinner1.attachDataSource(dataset1);
        NiceSpinner niceSpinner2 = (NiceSpinner) view.findViewById(R.id.nice_spinner_2);
        List<String> dataset2 = new LinkedList<>(Arrays.asList("中文","自动检测", "英语", "日语", "韩语", "葡萄牙语"));
        niceSpinner2.attachDataSource(dataset2);
        TextView exchange = (TextView)view.findViewById(R.id.exchange_tv);
        exchange.setText(getResources().getString(R.string.exchange_Icon));
        exchange.setTypeface(new FontManager().getType(getActivity()));
        TextView photo = (TextView)view.findViewById(R.id.photo_tv);
        photo.setText(getResources().getString(R.string.camera_Icon));
        photo.setTypeface(new FontManager().getType(getActivity()));
        TextView microphone = (TextView)view.findViewById(R.id.voice_tv);
        microphone.setText(getResources().getString(R.string.microphone_Icon));
        microphone.setTypeface(new FontManager().getType(getActivity()));
    }
}
