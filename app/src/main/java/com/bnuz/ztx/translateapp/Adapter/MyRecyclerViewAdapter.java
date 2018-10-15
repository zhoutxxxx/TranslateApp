package com.bnuz.ztx.translateapp.Adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bnuz.ztx.translateapp.R;

import java.util.List;

/**
 * Created by ZTX on 2018/5/13.
 * Adapter类
 * 主要功能：实现翻译查询后每个词条的展示
 */

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>{
    //实例化String类型的List用于放置每个词条的内容
    private List<String> list;

    //带参构造器
    public MyRecyclerViewAdapter(List<String> list) {
        this.list = list;
    }

    //创建Holder，添加布局
    @Override
    public MyRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //创建View，引入布局
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        MyRecyclerViewAdapter.ViewHolder viewHolder = new MyRecyclerViewAdapter.ViewHolder(view);
        return viewHolder;
    }

    //绑定Holder
    @Override
    public void onBindViewHolder(MyRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.mText.setText(Html.fromHtml(list.get(position)));
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
}
