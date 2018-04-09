package com.bnuz.ztx.translateapp.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bnuz.ztx.translateapp.R;
import com.bnuz.ztx.translateapp.Util.FontManager;
import com.bnuz.ztx.translateapp.Util.URLUtil;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import org.angmarch.views.NiceSpinner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by ZTX on 2018/3/25.
 */

public class TranslateFragment extends Fragment implements View.OnClickListener {
    TextView enter,microphone,photo,exchange,translateInformation;
    NiceSpinner niceSpinner1,niceSpinner2;
    EditText input;
    String url;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_translate, null);
        findView(view);
        return view;
    }

    private void findView(View view) {
        //左下拉列表
        niceSpinner1 = (NiceSpinner) view.findViewById(R.id.nice_spinner_1);
        List<String> dataset1 = new LinkedList<>();
        dataset1.add(getResources().getString(R.string.Chinese_language));
        dataset1.add(getResources().getString(R.string.English_language));
//        dataset1.add(getResources().getString(R.string.Japanese_language));
//        dataset1.add(getResources().getString(R.string.Korean_language));
//        dataset1.add(getResources().getString(R.string.Portugal_language));
        niceSpinner1.attachDataSource(dataset1);
        //右下拉列表
        niceSpinner2 = (NiceSpinner) view.findViewById(R.id.nice_spinner_2);
        List<String> dataset2 = new LinkedList<>();
        dataset2.add(getResources().getString(R.string.English_language));
        dataset2.add(getResources().getString(R.string.Chinese_language));
//        dataset2.add(getResources().getString(R.string.Portugal_language));
//        dataset2.add(getResources().getString(R.string.Japanese_language));
//        dataset2.add(getResources().getString(R.string.Korean_language));
        niceSpinner2.attachDataSource(dataset2);
        //顶部交换按钮
        exchange = (TextView)view.findViewById(R.id.exchange_tv);
        exchange.setText(getResources().getString(R.string.exchange_Icon));
        exchange.setTypeface(new FontManager().getType(getActivity()));
        //拍照按钮
        photo = (TextView)view.findViewById(R.id.photo_tv);
        photo.setText(getResources().getString(R.string.camera_Icon));
        photo.setTypeface(new FontManager().getType(getActivity()));
        //麦克风按钮
        microphone = (TextView)view.findViewById(R.id.voice_tv);
        microphone.setText(getResources().getString(R.string.microphone_Icon));
        microphone.setTypeface(new FontManager().getType(getActivity()));
        //翻译按钮
        enter = (TextView)view.findViewById(R.id.enter_tv);
        enter.setText(getResources().getString(R.string.enter_Icon));
        enter .setTypeface(new FontManager().getType(getActivity()));
        enter.setOnClickListener(this);
        //输入框
        input = (EditText)view.findViewById(R.id.input_et);
        //Json数据显示框
        translateInformation = (TextView)view.findViewById(R.id.translate_tv);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.enter_tv:
                String s = input.getText().toString();
                int fromInt = niceSpinner1.getSelectedIndex();
                int toInt = niceSpinner2.getSelectedIndex();
                try {
                    url = new URLUtil().getTranslateURL(s,fromInt,toInt);
                    Log.d("ztx","url is --------->" + url);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                RxVolley.get(url, new HttpCallback() {
                    @Override
                    public void onSuccess(String t) {
                        parsingJson(t);
                    }
                });
        }
    }

    private void parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            JSONObject json = jsonObject.getJSONObject("basic");
            String explains = json.getJSONArray("explains").get(0).toString();
            translateInformation.setText(explains);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
