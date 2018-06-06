package com.bnuz.ztx.translateapp.Util;

import com.kymjs.rxvolley.client.HttpParams;
import com.youdao.sdk.app.Language;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;


/**
 * Created by ZTX on 2018/4/8.
 * url访问类
 */

public class URLUtil {
    final String appKey = "073c24ee75a4bc9e";//应用实例
    final String appPassWord = "ERyzY0e4nQbP60AGvBOLF6xp45hQ896A";//应用密钥
    String query = null;//查询文本
    String salt = String.valueOf(System.currentTimeMillis());//随机数
    String from = null;//初始语言
    String to = null;//目标语言
    String sign = null;//签名
    //OCR模块参数
    final String OCRUrl = "https://openapi.youdao.com/ocrapi";
    final String OCRAppKey = "073c24ee75a4bc9e";
    final String OCRAppPassWord = "ERyzY0e4nQbP60AGvBOLF6xp45hQ896A";
    String OCRDetectType = "10012";//10012整行翻译  10011按字翻译
    String OCRImageType = "1";//图片类型 只支持base64
    String OCRLangType = "zh-en";//语言支持中英混合
    String OCRDocType = "json";//返回数据类型
    String OCRSalt = String.valueOf(System.currentTimeMillis());
    String OCRImg = "";//图片参数  仅支持base64
    String OCRSign = "";//签名
    //ASR模块
    final String ASRUrl = "https://openapi.youdao.com/asrapi";
    final String ASRAppKey = "073c24ee75a4bc9e";
    final String ASRAppPassWord = "ERyzY0e4nQbP60AGvBOLF6xp45hQ896A";
    String q = "";
    String ASRLangType = "";
    String ASRFormat = "wav";
    String ASRRate = "16000";
    String ASRChannel = "1";
    String ASRSalt = String.valueOf(System.currentTimeMillis());
    String ASRType = "1";
    String ASRSign = "";//签名
    HttpParams httpParams;
    //ASR将参数放入，并作UTF-8编码

    public  String getIP() {
        return IP;
    }

    //IP
    final  String IP = "http://192.168.31.117:8080";
    public HttpParams getRegisterParams(String phone,String mail,String password){
        httpParams = new HttpParams();
        try {
            httpParams.put("phone",URLEncoder.encode(phone,"utf-8"));
            httpParams.put("mail",URLEncoder.encode(mail,"utf-8"));
            httpParams.put("password",URLEncoder.encode(password,"utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return httpParams;
    }
    public HttpParams getLoginParams(String user,String password,String isPhone){
        httpParams = new HttpParams();
        try {
            httpParams.put("user",URLEncoder.encode(user,"utf-8"));
            httpParams.put("isPhone",URLEncoder.encode(isPhone,"utf-8"));
            httpParams.put("password",URLEncoder.encode(password,"utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return httpParams;
    }
    public HttpParams getASRHttpParams(String base64qVoice , String language) throws UnsupportedEncodingException {
        httpParams = new HttpParams();
        this.q = base64qVoice;
        this.ASRLangType = language;
        httpParams.put("appKey",URLEncoder.encode(ASRAppKey,"utf-8"));
        httpParams.put("q",URLEncoder.encode(q,"utf-8"));
        httpParams.put("langType",URLEncoder.encode(ASRLangType,"utf-8"));
        httpParams.put("format",URLEncoder.encode(ASRFormat,"utf-8"));
        httpParams.put("rate",URLEncoder.encode(ASRRate,"utf-8"));
        httpParams.put("salt",URLEncoder.encode(ASRSalt,"utf-8"));
        httpParams.put("channel",URLEncoder.encode(ASRChannel,"utf-8"));
        httpParams.put("type",URLEncoder.encode(ASRType,"utf-8"));
        ASRSign = md5(ASRAppKey + q + ASRSalt + ASRAppPassWord);
        httpParams.put("sign",URLEncoder.encode(ASRSign,"utf-8"));
        return httpParams;
    }
    public String getASRUrl(){
        return ASRUrl;
    }
    //OCR将参数放入，并作UTF-8编码
    public HttpParams getHttpParams(String base64Image) throws UnsupportedEncodingException {
        httpParams = new HttpParams();
        this.OCRImg = base64Image;
        httpParams.put("appKey",URLEncoder.encode(OCRAppKey,"utf-8"));
        httpParams.put("img",URLEncoder.encode(OCRImg,"utf-8"));
        httpParams.put("detectType",URLEncoder.encode(OCRDetectType,"utf-8"));
        httpParams.put("imageType",URLEncoder.encode(OCRImageType,"utf-8"));
        httpParams.put("langType",URLEncoder.encode(OCRLangType,"utf-8"));
        httpParams.put("salt",URLEncoder.encode(OCRSalt,"utf-8"));
        httpParams.put("docType",URLEncoder.encode(OCRDocType,"utf-8"));
        OCRSign = md5(OCRAppKey + OCRImg + OCRSalt + OCRAppPassWord);
        httpParams.put("sign",URLEncoder.encode(OCRSign,"utf-8"));
        return httpParams;
    }

    public void setHttpParams(HttpParams httpParams) {
        this.httpParams = httpParams;
    }

    public String getOCRUrl() {
        return OCRUrl;
    }

    public String getTranslateURL(String query, int fromInt, int toInt) throws Exception {
        //初始语言
        switch (fromInt) {
            case 0:
                this.from = "EN";
                break;
            case 1:
                this.from = "zh-CHS";
                break;
        }
        //目标语言
        switch (toInt) {
            case 0:
                this.to = "EN";
                break;
            case 1:
                this.to = "zh-CHS";
                break;
        }
        this.query = query;
        //签名生成方式
        sign = md5(appKey + query + salt + appPassWord);
        return "https://openapi.youdao.com/api?q=" + query + "&from=" + from + "&to=" + to + "&appKey=" + appKey + "&salt=" + salt + "&sign=" + sign;
    }

    /**
     * 生成32位MD5摘要
     *
     * @param string
     * @return
     */
    public String md5(String string) {
        if (string == null) {
            return null;
        }
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F'};

        try {
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
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * 根据api地址和参数生成请求URL
     *
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
     *
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
    //OCR识别后的文本翻译API
    public String getOCRTranslate(String query){
        this.query = query;
        sign = md5(appKey + query + salt + appPassWord);
        return "https://openapi.youdao.com/api?q=" + query + "&from=auto" + "&to=auto" + "&appKey=" + appKey + "&salt=" + salt + "&sign=" + sign;
    }
}
