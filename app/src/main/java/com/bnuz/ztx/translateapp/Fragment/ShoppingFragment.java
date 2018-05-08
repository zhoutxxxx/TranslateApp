package com.bnuz.ztx.translateapp.Fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bnuz.ztx.translateapp.R;

/**
 * Created by ZTX on 2018/5/2.
 */

public class ShoppingFragment extends Fragment{
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping, null);
        findView(view);
        return view;
    }

    private void findView(View view) {
    }
}
