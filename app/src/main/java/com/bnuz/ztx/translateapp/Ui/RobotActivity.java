package com.bnuz.ztx.translateapp.Ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bnuz.ztx.translateapp.Adapter.ChatAdapter;
import com.bnuz.ztx.translateapp.Entity.ChatMessage;
import com.bnuz.ztx.translateapp.R;
import com.bnuz.ztx.translateapp.Util.FontManager;
import com.bnuz.ztx.translateapp.Util.URLUtil;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.http.VolleyError;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RobotActivity extends AppCompatActivity implements View.OnClickListener {
    static String URL = "http://openapi.tuling123.com/openapi/api/v2";
    List<ChatMessage> list;
    TextView back;
    Button send;
    EditText msg;
    RecyclerView recyclerView;
    ChatMessage chatMessage;
    ChatAdapter chatAdapter;
    HttpCallback callback;
    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robot);
        back = findViewById(R.id.back_tv_robot);
        back.setTypeface(new FontManager().getALiType(getApplicationContext()));
        back.setOnClickListener(this);
        send = findViewById(R.id.send_message_bt);
        send.setOnClickListener(this);
        msg = findViewById(R.id.msg_et);
        recyclerView = findViewById(R.id.chat_recycle);
        list = new ArrayList<>();
        chatMessage = new ChatMessage("您好，请问有没有什么可以为您服务的？",ChatMessage.RECEIVED);
        list.add(chatMessage);

        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        chatAdapter = new ChatAdapter(list);
        recyclerView.setAdapter(chatAdapter);

        callback = new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                Logger.json(t);
                parsingJson(t);
            }

            @Override
            public void onFailure(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_tv_robot:
                finish();
                break;
            case R.id.send_message_bt:
                String s = msg.getText().toString();
                if (s.equals("")) {
                    Toast.makeText(getApplicationContext(), "无法发送空消息", Toast.LENGTH_SHORT).show();
                } else {
                    chatMessage = new ChatMessage(s, ChatMessage.SENT);
                    list.add(chatMessage);
                    //当有新消息时，刷新RecyclerView中的显示
                    chatAdapter.notifyItemChanged(list.size()-1);
                    //将RecyclerView定位到最后一行
                    recyclerView.scrollToPosition(list.size()-1);
                    msg.setText(null);
                    new RxVolley.Builder()
                            .url(URL)
                            .httpMethod(RxVolley.Method.POST)
                            .contentType(RxVolley.ContentType.JSON)
                            .params(new URLUtil().getRobotHttpParams(s))
                            .encoding("utf-8")
                            .callback(callback)
                            .doTask();
                }
                break;
        }
    }

    private void parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            JSONArray array = jsonObject.getJSONArray("results");
            JSONObject json = array.getJSONObject(0);
            JSONObject value = json.getJSONObject("values");
            String s = value.getString("text");
            chatMessage = new ChatMessage(s,ChatMessage.RECEIVED);
            list.add(chatMessage);
            //当有新消息时，刷新RecyclerView中的显示
            chatAdapter.notifyItemChanged(list.size()-1);
            //将RecyclerView定位到最后一行
            recyclerView.scrollToPosition(list.size()-1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
