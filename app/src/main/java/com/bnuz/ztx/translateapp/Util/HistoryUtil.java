package com.bnuz.ztx.translateapp.Util;

import android.content.Context;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZTX on 2018/5/13.
 */

public class HistoryUtil {
    String s;
    int[] list = new int[10];
    String[] strings;
    List<String> history;

    public void putWord(Context context, int start, String word) {
        strings = new String[6];
        boolean exit = false;
        s = ShareUtils.getString(context, "history", ".1.2.3.4.5.");
        Logger.d("this s is ------------------>" + s);
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '.') {
                list[count] = i;
                count++;
            }
        }
        for (int i = 0 ; i < 5 ; i++){
            strings[i] = s.substring(list[i] + 1, list[i + 1]);
        }
        for (int i = 0 ; i < 5 ; i++){
            if (word.equals(strings[i])){
                exit = true;
            }
        }
        if (!exit) {
            Logger.d("this start is ------------------>" + list[start] + "    end is ------->" + list[start + 1]);
            String previous = s.substring(list[start] + 1, list[start + 1]);
            Logger.d("this previous is --------->" + previous);
            s = s.replace(previous, word);
            ShareUtils.putString(context, "history", s);
            start = (start + 1) % 5;
            ShareUtils.putInt(context,"wordIndex",start);
            Logger.d(ShareUtils.getString(context,"history","null"));
        }
    }

    public List<String> getHistoryList(String str) {
        history = new ArrayList<>();
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '.') {
                list[count] = i;
                count++;
            }
        }
        for (int i = 0; i < 5; i++) {
            String inStr = str.substring(list[i] + 1, list[i + 1]);
            if (!inStr.equals("1") && !inStr.equals("2") && !inStr.equals("3") && !inStr.equals("4") && !inStr.equals("5")) {
                history.add(i, inStr);
            }
        }
        return history;
    }

    public String getPointString(String str){
        String s = "";
        for (int i = 0; i < str.length(); i++){
            if (i != str.length() - 1){
                s = s + str.charAt(i);
                s = s + "·";
            }else{
                s = s + str.charAt(i);
            }
        }
        return s;
    }
}
