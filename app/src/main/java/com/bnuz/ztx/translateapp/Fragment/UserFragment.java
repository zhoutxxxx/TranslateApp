package com.bnuz.ztx.translateapp.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bnuz.ztx.translateapp.R;


/**
 * Created by ZTX on 2018/3/25.
 */

public class UserFragment extends android.support.v4.app.Fragment {

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, null);
        findView(view);
        return view;
    }

    private void findView(View view) {
    }
}
