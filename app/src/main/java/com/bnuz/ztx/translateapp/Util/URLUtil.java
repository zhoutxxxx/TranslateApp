package com.bnuz.ztx.translateapp.Util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * Created by ZTX on 2018/4/8.
 */

public class URLUtil {
        final String appKey = "073c24ee75a4bc9e";
        final String appPassWord = "oCLsRlxIMRAoq5aRTxsDgiZWkgFsRZDs";
        String query = null;
        String salt = String.valueOf(System.currentTimeMillis());
        String from = null;
        String to = null;
        String sign = null;
    public String getTranslateURL(String query , int fromInt ,int toInt) throws Exception {
        switch (fromInt){
            case 0:
                this.from = "zh-CHS";
                break;
            case 1:
                this.from = "EN";
                break;
        }
        switch (toInt){
            case 0:
                this.to = "EN";
                break;
            case 1:
                this.to = "zh-CHS";
                break;
        }
        this.query = query;
        sign = md5(appKey + query + salt+ appPassWord);
        return "https://openapi.youdao.com/api?q="+ query +"&from=" + from + "&to=" + to + "&appKey=" + appKey + "&salt=" + salt + "&sign=" + sign;
    }

    /**
     * 生成32位MD5摘要
     * @param string
     * @return
     */
    public String md5(String string) {
        if(string == null){
            return null;
        }
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F'};

        try{
            byte[] btInput = string.getBytes("utf-8");
            /** 获得MD5摘要算法的 MessageDigest 对象 */
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            /** 使用指定的字节更新摘要 */
            mdInst.update(btInput);
            /** 获得密文 */
            byte[] md = mdInst.digest();
            /** 把密文转换成十六进制的字符串形式 */
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        }catch(NoSuchAlgorithmException | UnsupportedEncodingException e){
            return null;
        }
    }

    /**
     * 根据api地址和参数生成请求URL
     * @param url
     * @param params
     * @return
     */
    public String getUrlWithQueryString(String url, Map params) {
        if (params == null) {
            return url;
        }

        StringBuilder builder = new StringBuilder(url);
        if (url.contains("?")) {
            builder.append("&");
        } else {
            builder.append("?");
        }

        int i = 0;
        for (Object key : params.keySet()) {
            String value = (String) params.get(key);
            if (value == null) { // 过滤空的key
                continue;
            }

            if (i != 0) {
                builder.append('&');
            }

            builder.append(key);
            builder.append('=');
            builder.append(encode(value));

            i++;
        }

        return builder.toString();
    }
    /**
     * 进行URL编码
     * @param input
     * @return
     */
    public String encode(String input) {
        if (input == null) {
            return "";
        }

        try {
            return URLEncoder.encode(input, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return input;
    }
}