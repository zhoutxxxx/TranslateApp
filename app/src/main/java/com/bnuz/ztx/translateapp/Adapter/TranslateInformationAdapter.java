package com.bnuz.ztx.translateapp.Adapter;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bnuz.ztx.translateapp.Entity.TranslateInformation;
import com.bnuz.ztx.translateapp.R;

import java.util.List;

/**
 * Created by ZTX on 2018/4/10.
 */

public class TranslateInformationAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private List<TranslateInformation> mList;
    private TranslateInformation translateInformation;
    private int width, height;
    private WindowManager wm;

    public TranslateInformationAdapter(Context mContext, List<TranslateInformation> mList) {
        this.mContext = mContext;
        this.mList = mList;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.simpleitem, null);
            viewHolder.tv_title = (TextView) view.findViewById(R.id.information_tv);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        translateInformation = mList.get(i);
        viewHolder.tv_title.setText(Html.fromHtml(translateInformation.getExplains()));
        return view;
    }

    class ViewHolder {
        private TextView tv_title;
    }
}
