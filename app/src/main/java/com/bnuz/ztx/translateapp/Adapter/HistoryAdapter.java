package com.bnuz.ztx.translateapp.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bnuz.ztx.translateapp.Interface.OnItemClickListener;
import com.bnuz.ztx.translateapp.R;

import java.util.List;

/**
 * Created by ZTX on 2018/5/13.
 * 翻译Fragment
 * 历史纪录模块
 * 主要功能：主要通过数据获取后，将数据展示在recycleView中，最少0条最多5条
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> implements View.OnClickListener {
    //创建String类型的List
    private List<String> list;
    //list点击事件监听器
    private OnItemClickListener mItemClickListener;

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
    //带list的构造器
    public HistoryAdapter(List<String> list) {
        this.list = list;
    }

    //创建holder，添加界面进来放在每个Item里
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //创建View，引入布局
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        //将View放进Holder里
        HistoryAdapter.ViewHolder viewHolder = new HistoryAdapter.ViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    //绑定每个Holder
    @Override
    public void onBindViewHolder(HistoryAdapter.ViewHolder holder, int position) {
        //设置每个Holder控件的属性
        holder.mText.setText(list.get(position));
        holder.itemView.setTag(position);
    }

    //返回Item个数
    @Override
    public int getItemCount() {
        return list.size();
    }

    //Holder类
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mText;

        ViewHolder(View itemView) {
            super(itemView);
            mText = itemView.findViewById(R.id.information_tv);
        }
    }
    //点击事件
    @Override
    public void onClick(View view) {

    }

    //recycleView监听器
    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

}
