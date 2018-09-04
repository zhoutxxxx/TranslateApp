package com.bnuz.ztx.translateapp.Ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.bnuz.ztx.translateapp.R;
import com.bnuz.ztx.translateapp.Util.FontManager;

public class MessageActivity extends AppCompatActivity {
    TextView noMessageIcon,noMessageLabel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        noMessageIcon = findViewById(R.id.noMessage_Icon);
        noMessageIcon.setTypeface(new FontManager().getALiType(getApplicationContext()));
        noMessageLabel = findViewById(R.id.noMessage_Label);
    }
}
