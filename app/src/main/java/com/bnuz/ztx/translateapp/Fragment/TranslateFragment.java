package com.bnuz.ztx.translateapp.Fragment;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bnuz.ztx.translateapp.Adapter.HistoryAdapter;
import com.bnuz.ztx.translateapp.Adapter.MyRecyclerViewAdapter;
import com.bnuz.ztx.translateapp.Entity.TranslateInformation;
import com.bnuz.ztx.translateapp.Interface.OnItemClickListener;
import com.bnuz.ztx.translateapp.R;
import com.bnuz.ztx.translateapp.Util.AudioUtil;
import com.bnuz.ztx.translateapp.Util.FontManager;
import com.bnuz.ztx.translateapp.Util.HistoryUtil;
import com.bnuz.ztx.translateapp.Util.ImageUtil;
import com.bnuz.ztx.translateapp.Util.MediaPlayerUtil;
import com.bnuz.ztx.translateapp.Util.ShareUtils;
import com.bnuz.ztx.translateapp.Util.URLUtil;
import com.bnuz.ztx.translateapp.View.CustomDialog;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;
import com.orhanobut.logger.Logger;

import org.angmarch.views.NiceSpinner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by ZTX on 2018/3/25.
 */

public class TranslateFragment extends Fragment implements View.OnClickListener, OnItemClickListener {
    TextView enter, microphone, photo, exchange, queryTv, phoneticTv, phoneticTv2, voiceTv1, voiceTv2;
    NiceSpinner niceSpinner1, niceSpinner2;
    EditText input;
    String url, OCRUrl;
    Uri imageUri;
    RecyclerView recyclerView , historyView;
    MyRecyclerViewAdapter mAdapter;
    HistoryAdapter historyAdapter;
    List<TranslateInformation> mList = new ArrayList<>();
    List<String> explainsLists = new ArrayList<>() ,nullList = new ArrayList<>();
    ImageView iv;
    Bitmap bitmap;
    Handler mHandler;
    HttpParams httpParams;
    CustomDialog voiceDialog, selectDialog , loadingDialog;
    AudioUtil audioUtil;
    String selectLanguage;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_translate, null);
        findView(view);
        mHandler = new Handler();
        return view;
    }

    //实例化数据
    private void findView(View view) {
        //优化图片显示
        iv = (ImageView) view.findViewById(R.id.iv_image);
        //左下拉列表
        niceSpinner1 = (NiceSpinner) view.findViewById(R.id.nice_spinner_1);
        List<String> dataset1 = new LinkedList<>();
        dataset1.add(getResources().getString(R.string.English_language));
        dataset1.add(getResources().getString(R.string.Chinese_language));
        niceSpinner1.attachDataSource(dataset1);
        //右下拉列表
        niceSpinner2 = (NiceSpinner) view.findViewById(R.id.nice_spinner_2);
        List<String> dataset2 = new LinkedList<>();
        dataset2.add(getResources().getString(R.string.English_language));
        dataset2.add(getResources().getString(R.string.Chinese_language));
        niceSpinner2.attachDataSource(dataset2);
        niceSpinner2.setSelectedIndex(1);
        //顶部交换按钮
        exchange = (TextView) view.findViewById(R.id.exchange_tv);
        exchange.setText(getResources().getString(R.string.exchange_Icon));
        exchange.setTypeface(new FontManager().getType(getActivity()));
        exchange.setOnClickListener(this);
        //拍照按钮
        photo = (TextView) view.findViewById(R.id.photo_tv);
        photo.setText(getResources().getString(R.string.camera_Icon));
        photo.setTypeface(new FontManager().getType(getActivity()));
        photo.setOnClickListener(this);
        //麦克风按钮
        microphone = (TextView) view.findViewById(R.id.voice_tv);
        microphone.setText(getResources().getString(R.string.microphone_Icon));
        microphone.setTypeface(new FontManager().getType(getActivity()));
        microphone.setOnClickListener(this);
        //翻译按钮
        enter = (TextView) view.findViewById(R.id.enter_tv);
        enter.setText(getResources().getString(R.string.enter_Icon));
        enter.setTypeface(new FontManager().getType(getActivity()));
        enter.setOnClickListener(this);
        //输入框
        input = (EditText) view.findViewById(R.id.input_et);
        //Json数据显示框
        recyclerView = view.findViewById(R.id.myRecycleView);
        //设置RecyclerView管理器
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        historyView = view.findViewById(R.id.historyView);
        //设置RecyclerView管理器
        historyView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        //初始化适配器
        historyAdapter = new HistoryAdapter(new HistoryUtil().getHistoryList(ShareUtils.getString(getContext(),"history",".1.2.3.4.5.")));
//设置添加或删除item时的动画，这里使用默认动画
        historyView.setItemAnimator(new DefaultItemAnimator());
//设置适配器
        historyView.setAdapter(historyAdapter);
        historyAdapter.setItemClickListener(this);
        //音标显示
        phoneticTv = (TextView) view.findViewById(R.id.phonetic_tv1);
        phoneticTv2 = (TextView) view.findViewById(R.id.phonetic_tv2);
        //发音图标显示 事件监听
        voiceTv1 = (TextView) view.findViewById(R.id.voice_tv1);
        voiceTv1.setText(getResources().getString(R.string.voice_Icon));
        voiceTv1.setTypeface(new FontManager().getType(getActivity()));
        voiceTv2 = (TextView) view.findViewById(R.id.voice_tv2);
        voiceTv2.setText(getResources().getString(R.string.voice_Icon));
        voiceTv2.setTypeface(new FontManager().getType(getActivity()));
        voiceTv1.setOnClickListener(this);
        voiceTv2.setOnClickListener(this);
        //默认发音图标可见性为  不可见
        voiceTv1.setVisibility(View.INVISIBLE);
        voiceTv2.setVisibility(View.INVISIBLE);
        //查询的文本显示框
        queryTv = (TextView) view.findViewById(R.id.query_tv);
        //录音dialog
        voiceDialog = new CustomDialog(getActivity(), 100, 100, R.layout.dialog_voice, R.style.Theme_dialog, Gravity.CENTER, R.style.pop_anim_style);
        voiceDialog.setCancelable(true);
        //初始化dialog
        TextView textView = (TextView) voiceDialog.findViewById(R.id.voice_dialog);
        textView.setText(getResources().getString(R.string.microphone_Icon));
        textView.setTypeface(new FontManager().getType(getContext()));
        Button finish = (Button) voiceDialog.findViewById(R.id.cancel_bt);
        finish.setOnClickListener(this);
        Button cancel = (Button) voiceDialog.findViewById(R.id.finish_bt);
        cancel.setOnClickListener(this);
        //选择语音dialog
        selectDialog = new CustomDialog(getActivity(), 220, 100, R.layout.dialog_select, R.style.Theme_dialog, Gravity.CENTER, R.style.pop_anim_style);
        selectDialog.setCancelable(true);
        //初始化dialog
        Button EnButton = (Button) selectDialog.findViewById(R.id.en_bt);
        EnButton.setOnClickListener(this);
        Button CnButton = (Button) selectDialog.findViewById(R.id.cn_bt);
        CnButton.setOnClickListener(this);
        //初始化dialog
        loadingDialog = new CustomDialog(getActivity(),80,80,R.layout.dialog_query,R.style.Theme_dialog,Gravity.CENTER,R.style.pop_anim_style);
        loadingDialog.setCancelable(true);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //翻译按钮
            case R.id.enter_tv:
                loadingDialog.show();
                TXTRequest();
                break;
            //照相按钮
            case R.id.photo_tv:
                OCRRequest();
                break;
            //交换两个下拉列表的数据
            case R.id.exchange_tv:
                int temp = niceSpinner2.getSelectedIndex();
                niceSpinner2.setSelectedIndex(niceSpinner1.getSelectedIndex());
                niceSpinner1.setSelectedIndex(temp);
                break;
            //调用函数，播放音频
            case R.id.voice_tv1:
                new MediaPlayerUtil().Paly(getActivity(), mList.get(0).getSpeakUrl());
                break;
            case R.id.voice_tv2:
                new MediaPlayerUtil().Paly(getActivity(), mList.get(0).getUs_speech());
                break;
            //语音按钮
            case R.id.voice_tv:
                //将dialog展现出来
                voiceDialog.show();
                //实例化一个计时器
                final Chronometer chronometer = (Chronometer) voiceDialog.findViewById(R.id.voice_ch);
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.start();
                //实例化一个语音工具类
                audioUtil = new AudioUtil(getContext());
                //开始录音
                audioUtil.startRecord();
                //同步记录数据到本地
                audioUtil.recordData();
                break;
            case R.id.finish_bt:
                //停止录音
                audioUtil.stopRecord();
                //dialog消失
                voiceDialog.dismiss();
                //选择语言dialog出现
                selectDialog.show();
                //释放资源
                audioUtil.getRecorder().release();
                break;
            case R.id.cancel_bt:
                //释放资源
                audioUtil.getRecorder().release();
                voiceDialog.dismiss();
                break;
            case R.id.cn_bt:
                selectLanguage = "zh-CHS";
                selectDialog.dismiss();
                loadingDialog.show();
                ASRRequest();
                break;
            case R.id.en_bt:
                selectLanguage = "en";
                selectDialog.dismiss();
                loadingDialog.show();
                ASRRequest();
                break;
        }
    }

    //文本请求识别
    public void TXTRequest() {
        //获取到输入的文本
        String s = input.getText().toString();
        int index = ShareUtils.getInt(getContext(),"wordIndex",0);
        new HistoryUtil().putWord(getActivity(), index,s);
        //清除ImageView的图片
        queryTv.setText(null);
        iv.setImageDrawable(null);
        //获取翻译状态，由什么语言翻译到什么语言
        int fromInt = niceSpinner1.getSelectedIndex();
        int toInt = niceSpinner2.getSelectedIndex();
        try {
            //将数据传入，返回生成的url
            url = new URLUtil().getTranslateURL(s, fromInt, toInt).toString();
            //将带有空格的url转化成Android可以识别的url才可进行访问获取接送数据
            url = url.replace(" ", "%20");
            Logger.t("ztx").d("url-------->" + url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //网络请求接送数据
        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                parsingJson(t);
            }
        });
    }

    //图片识别请求
    public void OCRRequest() {
        final String[] items = {getResources().getString(R.string.Take_photo)
                , getResources().getString(R.string.Choose_from_the_album)
                , getResources().getString(R.string.Cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getResources().getString(R.string.Please_choose_the_following_way));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //拍照
                if (which == 0) {
                    //调用摄像头
                    File outputImage = new File(getContext().getExternalCacheDir(), "output_image.jpg");
                    try {
                        if (outputImage.exists()) {
                            outputImage.delete();
                        }
                        outputImage.createNewFile();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //根据SDK版本获取imageUri
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                        imageUri = FileProvider.getUriForFile(getActivity(), "com.gyq.cameraalbumtest.fileprovider", outputImage);
                        imageUri = FileProvider.getUriForFile(getActivity(),"com.example.cameraalbumtest.fileprovider",outputImage);
                    } else {
                        imageUri = Uri.fromFile(outputImage);
                    }
                    //启动相机程序
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, 1);
                }
                //从相册中选择
                if (which == 1) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");//相片类型
                    startActivityForResult(intent, 2);
                }
                Toast.makeText(getActivity(), items[which],
                        Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();
    }

    //语音识别请求
    public void ASRRequest() {
        //请求网络
        HttpParams ASRHttpParams = null;
        Logger.d(audioUtil.getOutFileName());
        try {
            ASRHttpParams = new URLUtil().getASRHttpParams(audioUtil.getVoiceStr(audioUtil.getOutFileName()), selectLanguage);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        HttpCallback callback = new HttpCallback() {
            @Override
            public void onSuccessInAsync(byte[] t) {
            }

            @Override
            public void onSuccess(String t) {
                parsingASRJson(t);
                Logger.json(t);
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
            }
        };
        new RxVolley.Builder()
                .url(new URLUtil().getASRUrl())//访问地址
                .httpMethod(RxVolley.Method.POST)//访问方式POST
                .params(ASRHttpParams)//参数
                .encoding("UTF-8")//UTF-8编码
                .callback(callback)//响应
                .doTask();//执行请求
    }
    //语音识别json解析
    private void parsingASRJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            JSONArray result = jsonObject.getJSONArray("result");
            String ASRString = result.get(0).toString();
            RxVolley.get(new URLUtil().getOCRTranslate(ASRString).replace(" ", "%20"), new HttpCallback() {
                @Override
                public void onSuccess(String t) {
                    parsingJson(t);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //事件处理
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            //1 即拍照， 2 即从相册中选择
            case 1:
                if (resultCode == getActivity().RESULT_OK) {
                    try {
                        //拍照后的照片bitmap
                        bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver()
                                .openInputStream(imageUri));
                        new Thread() {//对照片的处理过程放进线程中
                            public void run() {
                                //压缩图片
                                bitmap = new ImageUtil().comp(bitmap);
                                //图片灰度化
                                bitmap = new ImageUtil().convertGreyImg(bitmap);
                                //执行UI更新操作
                                mHandler.post(runnableUI);
                            }
                        }.start();
                    } catch (FileNotFoundException e) {
                        Logger.d(e.getMessage().toString());
                    }
                }
                break;
            case 2:
                if (resultCode == getActivity().RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        handleImageOnKitKat(data);
                    } else {
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    /**
     * 4.4及以上的系统使用这个方法处理图片
     * API>=19
     */
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(getActivity(), uri)) {
            //如果document类型的Uri,则通过document来处理
            String docID = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docID.split(":")[1];     //解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/piblic_downloads"), Long.valueOf(docID));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的uri，则使用普通方式使用
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是file类型的uri，直接获取路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过Uri和selection来获取真实的图片路径
        Cursor cursor = getActivity().getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        } else {
            Toast.makeText(getActivity(), "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

    //解析Json
    private void parsingJson(String t) {
        try {
            Logger.t("ztx").json(t);
            JSONObject jsonObject = new JSONObject(t);
            //数据的初始化，每次点击先清除之前查询的数据
            loadingDialog.dismiss();
            historyAdapter.setList(nullList);
            historyView.setAdapter(historyAdapter);
            voiceTv1.setVisibility(View.INVISIBLE);
            voiceTv2.setVisibility(View.INVISIBLE);
            phoneticTv.setText(null);
            phoneticTv2.setText(null);
            iv.setImageDrawable(null);
            explainsLists.clear();
            mList.clear();
            //实例化一个翻译对象
            TranslateInformation translateInformation = new TranslateInformation();
            //查询的文本
            String query = jsonObject.getString("query");
            translateInformation.setQuery(query);
            queryTv.setText(translateInformation.getQuery());
            //翻译的文本
            String translations = jsonObject.getJSONArray("translation").get(0).toString();
            translateInformation.setTranslations(translations);
            phoneticTv.setText(translateInformation.getTranslations());
            //翻译的详细内容
            try {
                JSONObject json = jsonObject.getJSONObject("basic");
                JSONArray explainsList = json.getJSONArray("explains");
                for (int i = 0; i < explainsList.length(); i++) {
                    String changeString = explainsList.get(i).toString();
                    StringBuffer sb = new StringBuffer();
                    //符号开关，只选取第一个.
                    boolean SYMBOL = false;
                    for (int k = 0; k < changeString.length(); k++) {
                        if (changeString.charAt(k) == '.') {
                            SYMBOL = true;
                        }
                    }
                    //搜索第一个.的位置
                    int index = -1;
                    if (SYMBOL) {
                        //在android中添加html语言，改变风格
                        sb.append(changeString).insert(0, "<font color='#FF0000'><i>");
                        changeString = sb.toString();
                    }
                    for (int j = 0; j < changeString.length(); j++) {
                        if (SYMBOL && changeString.charAt(j) == '.') {
                            SYMBOL = false;
                            index = j + 1;
                        }
                    }
                    if (index != -1) {
                        sb.insert(index, "</i></font> ");
                        changeString = sb.toString();
                    }
                    explainsLists.add(changeString);
                }
                //英语音标的判定条件
                if (niceSpinner1.getSelectedIndex() == 0) {
                    try {
                        String us_phonetic = json.getString("us-phonetic");
                        String uk_phonetic = json.getString("uk-phonetic");
                        String us_speech = json.getString("us-speech");
                        String uk_speech = jsonObject.getString("speakUrl");
                        String phonetic = json.getString("phonetic");
                        translateInformation.setPhonetic(phonetic);
                        translateInformation.setUk_phonetic(uk_phonetic);
                        translateInformation.setUs_phonetic(us_phonetic);
                        translateInformation.setSpeakUrl(uk_speech);
                        translateInformation.setUs_speech(us_speech);
                        phoneticTv.setText(translateInformation.getUKPhonetictoString());
                        phoneticTv2.setText(translateInformation.getUSPhonetictoString());
                        voiceTv1.setVisibility(View.VISIBLE);
                        voiceTv2.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                        Logger.d("无json对象，因此不显示");
                    }
                } else if (niceSpinner1.getSelectedIndex() == 1) {//汉语拼音的判定条件
                    try {
                        String phonetic = json.getString("phonetic");
                        String speakUrl = jsonObject.getString("speakUrl");
                        translateInformation.setSpeakUrl(speakUrl);
                        translateInformation.setPhonetic(phonetic);
                        phoneticTv.setText(translateInformation.getPhonetictoString());
                        voiceTv1.setVisibility(View.VISIBLE);
                        voiceTv2.setVisibility(View.INVISIBLE);
                    } catch (Exception e) {
                        Logger.d("无json对象，因此不显示");
                    }
                }
            } catch (Exception e) {
                Logger.d("无json对象，因此不显示");
            }
            //添加ListView的适配器，是得到的文本都显示出来
            translateInformation.setExplains(explainsLists);
            mList.add(translateInformation);
            //初始化适配器
            mAdapter = new MyRecyclerViewAdapter(explainsLists);
            //设置添加或删除item时的动画，这里使用默认动画
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            //设置适配器
            recyclerView.setAdapter(mAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parsingOCRJson(String t) {
        //控件初始化
        voiceTv1.setVisibility(View.INVISIBLE);
        voiceTv2.setVisibility(View.INVISIBLE);
        phoneticTv.setText(null);
        phoneticTv2.setText(null);
        explainsLists.clear();
        mList.clear();
        Logger.json(t);
        try {
            JSONObject jsonObject = new JSONObject(t);
            JSONObject result = jsonObject.getJSONObject("Result");
            JSONArray regions = result.getJSONArray("regions");
            String OCRQuery = "";
            for (int i = 0; i < regions.length(); i++) {
                JSONObject regionsNumber = regions.getJSONObject(i);
                JSONArray lines = regionsNumber.getJSONArray("lines");
                for (int j = 0; j < lines.length(); j++) {
                    JSONObject linesNumber = lines.getJSONObject(j);
                    OCRQuery = OCRQuery + linesNumber.getString("text");
                }
            }
            //get请求，将识别的整行字调用API进行翻译
            RxVolley.get(new URLUtil().getOCRTranslate(OCRQuery).replace(" ", "%20"), new HttpCallback() {
                @Override
                public void onSuccess(String t) {
                    //解析json
                    parsingJson(t);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //线程UI更新
    Runnable runnableUI = new Runnable() {
        @Override
        public void run() {
            loadingDialog.show();
            //将优化好的照片放在ImageView里
            iv.setImageBitmap(bitmap);
            //获取OCR的API
            OCRUrl = new URLUtil().getOCRUrl().toString();
            try {
                //调用函数，将图片通过base64编码传参
                httpParams = new URLUtil().getHttpParams(new ImageUtil().bitmapToBase64(bitmap));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            HttpCallback callback = new HttpCallback() {
                @Override
                public void onSuccessInAsync(byte[] t) {
                }

                @Override
                public void onSuccess(String t) {
                    parsingOCRJson(t);
                }

                @Override
                public void onFailure(int errorNo, String strMsg) {
                }
            };
            new RxVolley.Builder()
                    .url(OCRUrl)//访问地址
                    .httpMethod(RxVolley.Method.POST)//访问方式POST
                    .params(httpParams)//参数
                    .encoding("UTF-8")//UTF-8编码
                    .callback(callback)//响应
                    .doTask();//执行请求
        }
    };

    public void onItemClick(int position) {
        String query = new HistoryUtil().getHistoryList(ShareUtils.getString(getContext(),"history",".1.2.3.4.5.")).get(position);
        RxVolley.get(new URLUtil().getOCRTranslate(query), new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                parsingJson(t);
            }
        });
    }
}
