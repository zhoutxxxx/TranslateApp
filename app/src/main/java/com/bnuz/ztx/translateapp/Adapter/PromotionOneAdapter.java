package com.bnuz.ztx.translateapp.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bnuz.ztx.translateapp.Entity.Promotion;
import com.bnuz.ztx.translateapp.Interface.OnItemClickListener;
import com.bnuz.ztx.translateapp.R;
import com.bnuz.ztx.translateapp.Util.FontManager;
import com.bnuz.ztx.translateapp.Util.PicassoImageLoader;

import java.util.List;

/**
 * Created by ZTX on 2018/8/11.
 */

public class PromotionOneAdapter extends RecyclerView.Adapter<PromotionOneAdapter.ViewHolder> implements View.OnClickListener {
    List<Promotion> list;
    Context context;
    private OnItemClickListener mItemClickListener;
    public PromotionOneAdapter(Context context,List<Promotion> list){
        this.context = context;
        this.list = list;
    }


    @Override
    public PromotionOneAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_promotion_item_one,parent,false);
        PromotionOneAdapter.ViewHolder viewHolder = new PromotionOneAdapter.ViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PromotionOneAdapter.ViewHolder holder, int position) {
        new PicassoImageLoader().displayImage(context,list.get(position).getImgUrl(),holder.imageView);
        holder.title.setText(list.get(position).getTitle());
        holder.money.setText(list.get(position).getMoney());
        holder.good.setText(list.get(position).getGood());
        holder.activityIcon.setImageResource(addBitmap(position));
        holder.itemView.setTag(position);
    }


    private int addBitmap(int position) {
        if (list.get(position).getAct().equals("1")){
            return R.mipmap.recommed_icon;
        }else if (list.get(position).getAct().equals("2")){
            return R.mipmap.new_icon;
        }else if (list.get(position).getAct().equals("3")){
            return R.mipmap.manjian;
        }else {
            return R.mipmap.procket;
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView,activityIcon;
        TextView title,moneyIcon,money,goodIcon,good,shoppingIcon;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.promotion_img);
            activityIcon = itemView.findViewById(R.id.activity_icon);
            title = itemView.findViewById(R.id.title_goods);
            moneyIcon = itemView.findViewById(R.id.money_icon);
            moneyIcon.setTypeface(new FontManager().getALiType(context));
            money = itemView.findViewById(R.id.price_tv);
            goodIcon = itemView.findViewById(R.id.good_icon);
            goodIcon.setTypeface(new FontManager().getALiType(context));
            good = itemView.findViewById(R.id.good_tv);
            shoppingIcon = itemView.findViewById(R.id.shopping_tv);
            shoppingIcon.setTypeface(new FontManager().getALiType(context));
        }
    }

    @Override
    public void onClick(View v) {
        if (mItemClickListener!=null){
            mItemClickListener.onItemClick((Integer) v.getTag());
        }
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }
}
