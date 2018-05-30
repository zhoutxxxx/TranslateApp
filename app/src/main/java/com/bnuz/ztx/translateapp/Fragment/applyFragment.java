package com.bnuz.ztx.translateapp.Fragment;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.MediaController;

import com.bnuz.ztx.translateapp.R;
import com.bnuz.ztx.translateapp.Util.FontManager;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by ZTX on 2018/5/25.
 */

public class applyFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.service_bt) Button service;
    @BindView(R.id.video_bt) Button video;
    @BindView(R.id.webView) WebView webView;
    @BindView(R.id.link) Button link;
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
        link.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String url = "https://scaledrone.github.io/webrtc/index.html#9438e6";
        webView.loadUrl(url);
        //支持JS
        webView.getSettings().setJavaScriptEnabled(true);
        //支持缩放
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
    }
}
