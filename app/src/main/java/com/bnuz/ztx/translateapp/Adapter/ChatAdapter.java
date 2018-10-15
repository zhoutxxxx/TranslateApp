package com.bnuz.ztx.translateapp.Adapter;

import android.support.v7.widget.ActivityChooserView;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bnuz.ztx.translateapp.Entity.ChatMessage;
import com.bnuz.ztx.translateapp.R;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by ZTX on 2018/9/13.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    List<ChatMessage>list;

    public ChatAdapter(List<ChatMessage> list){
        this.list = list;
    }
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item,parent,false);
        ChatAdapter.ViewHolder viewHolder = new ChatAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ChatAdapter.ViewHolder holder, int position) {
        ChatMessage chatMessage = list.get(position);
        if (chatMessage.getType() == ChatMessage.RECEIVED){
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftMsg.setText(chatMessage.getContent());
        }else {
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightMsg.setText(chatMessage.getContent());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        TextView leftMsg;
        TextView rightMsg;
        public ViewHolder(View itemView) {
            super(itemView);
            leftLayout = itemView.findViewById(R.id.left_linear);
            rightLayout = itemView.findViewById(R.id.right_linear);
            leftMsg = itemView.findViewById(R.id.left_tv);
            rightMsg = itemView.findViewById(R.id.right_tv);
        }
    }
}
