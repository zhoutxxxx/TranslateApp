package com.bnuz.ztx.translateapp.Ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bnuz.ztx.translateapp.R;
import com.bnuz.ztx.translateapp.Util.FontManager;

public class MoneyActivity extends AppCompatActivity implements View.OnClickListener {
    TextView money, see, packet;
    TextView moneySum;
    Boolean isLook;
    String moneyStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);
        isLook = false;
        money = findViewById(R.id.money_icon_my_money);
        money.setTypeface(new FontManager().getType(getApplicationContext()));
        see = findViewById(R.id.see_money_Icon);
        see.setTypeface(new FontManager().getType(getApplicationContext()));
        see.setOnClickListener(this);
        packet = findViewById(R.id.more_packet_icon);
        packet.setTypeface(new FontManager().getType(getApplicationContext()));
        moneySum = findViewById(R.id.money_sum);
        moneyStr = moneySum.getText().toString();
        moneySum.setText("***");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.see_money_Icon:
                if (isLook) {//当前状态余额可见，改为不可见
                    moneyStr = moneySum.getText().toString();
                    moneySum.setText("***");
                    isLook = false;
                } else {//当前状态余额不可见，改为可见
                    moneySum.setText(moneyStr);
                    isLook = true;
                }
                break;
        }
    }
}
