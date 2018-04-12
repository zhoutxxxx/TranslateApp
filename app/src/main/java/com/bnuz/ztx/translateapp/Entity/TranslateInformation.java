package com.bnuz.ztx.translateapp.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZTX on 2018/4/10.
 */

public class TranslateInformation {
    List<String> explains = new ArrayList<>();
    String us_phonetic = "";
    String phonetic = "";
    String uk_phonetic = "";
    String query = "";

    public String getUs_phonetic() {
        return us_phonetic;
    }

    public void setUs_phonetic(String us_phonetic) {
        this.us_phonetic = us_phonetic;
    }

    public String getPhonetic() {
        return phonetic;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setPhonetic(String phonetic) {
        this.phonetic = phonetic;
    }

    public String getUk_phonetic() {
        return uk_phonetic;
    }

    public void setUk_phonetic(String uk_phonetic) {
        this.uk_phonetic = uk_phonetic;
    }

    public List<String> getExplains() {
        return explains;
    }

    public void setExplains(List<String> explains) {
        this.explains = explains;
    }

    public String getENPhonetictoString() {
        String s = "英 /" + uk_phonetic + "/     美/" + us_phonetic + "/";
        return s;
    }
    public String getPhonetictoString() {
        return "/" + phonetic + "/";
    }
}