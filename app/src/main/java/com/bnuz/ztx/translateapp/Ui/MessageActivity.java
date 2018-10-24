package com.bnuz.ztx.translateapp.Ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bnuz.ztx.translateapp.R;
import com.bnuz.ztx.translateapp.Util.FontManager;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener {
    TextView noMessageIcon,noMessageLabel,back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        noMessageIcon = findViewById(R.id.noMessage_Icon);
        noMessageIcon.setTypeface(new FontManager().getALiType(getApplicationContext()));
        noMessageLabel = findViewById(R.id.noMessage_Label);
        back = findViewById(R.id.back_message_tv);
        back.setTypeface(new FontManager().getALiType(getApplicationContext()));
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_message_tv:
                finish();
                break;
        }
    }
}
