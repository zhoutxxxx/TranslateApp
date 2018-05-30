package com.bnuz.ztx.translateapp.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;

import com.bnuz.ztx.translateapp.R;
import com.bnuz.ztx.translateapp.Util.FontManager;


/**
 * Created by ZTX on 2018/5/25.
 */

public class applyFragment extends Fragment implements View.OnClickListener {
    Button service,video,link;
    WebView webView;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_apply, null);
        findView(view);
        return view;
    }

    private void findView(View view) {

        webView = (WebView) view.findViewById(R.id.webView);
        video = (Button)view.findViewById(R.id.video_bt);
        video.setTypeface(new FontManager().getALiType(getContext()));
        service = (Button)view.findViewById(R.id.service_bt);
        service.setTypeface(new FontManager().getALiType(getContext()));
        link = (Button) view.findViewById(R.id.link);
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
