package com.bnuz.ztx.translateapp.Fragment;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bnuz.ztx.translateapp.Adapter.TranslateInformationAdapter;
import com.bnuz.ztx.translateapp.Entity.TranslateInformation;
import com.bnuz.ztx.translateapp.R;
import com.bnuz.ztx.translateapp.Util.FontManager;
import com.bnuz.ztx.translateapp.Util.URLUtil;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.orhanobut.logger.Logger;

import org.angmarch.views.NiceSpinner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by ZTX on 2018/3/25.
 */

public class TranslateFragment extends Fragment implements View.OnClickListener {
    TextView enter,microphone,photo,exchange,queryTv,phoneticTv;
    NiceSpinner niceSpinner1,niceSpinner2;
    EditText input;
    String url;
    Uri imageUri;
    ListView informationListView;
    List<TranslateInformation> mList = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_translate, null);

        findView(view);
        return view;
    }
    //实例化数据
    private void findView(View view) {
        //左下拉列表
        niceSpinner1 = (NiceSpinner) view.findViewById(R.id.nice_spinner_1);
        List<String> dataset1 = new LinkedList<>();
        dataset1.add(getResources().getString(R.string.English_language));
        dataset1.add(getResources().getString(R.string.Chinese_language));
//        dataset1.add(getResources().getString(R.string.Japanese_language));
//        dataset1.add(getResources().getString(R.string.Korean_language));
//        dataset1.add(getResources().getString(R.string.Portugal_language));
        niceSpinner1.attachDataSource(dataset1);
        //右下拉列表
        niceSpinner2 = (NiceSpinner) view.findViewById(R.id.nice_spinner_2);
        List<String> dataset2 = new LinkedList<>();
        dataset2.add(getResources().getString(R.string.Chinese_language));
        dataset2.add(getResources().getString(R.string.English_language));
//        dataset2.add(getResources().getString(R.string.Portugal_language));
//        dataset2.add(getResources().getString(R.string.Japanese_language));
//        dataset2.add(getResources().getString(R.string.Korean_language));
        niceSpinner2.attachDataSource(dataset2);
        //顶部交换按钮
        exchange = (TextView)view.findViewById(R.id.exchange_tv);
        exchange.setText(getResources().getString(R.string.exchange_Icon));
        exchange.setTypeface(new FontManager().getType(getActivity()));
        //拍照按钮
        photo = (TextView)view.findViewById(R.id.photo_tv);
        photo.setText(getResources().getString(R.string.camera_Icon));
        photo.setTypeface(new FontManager().getType(getActivity()));
        photo.setOnClickListener(this);
        //麦克风按钮
        microphone = (TextView)view.findViewById(R.id.voice_tv);
        microphone.setText(getResources().getString(R.string.microphone_Icon));
        microphone.setTypeface(new FontManager().getType(getActivity()));
        //翻译按钮
        enter = (TextView)view.findViewById(R.id.enter_tv);
        enter.setText(getResources().getString(R.string.enter_Icon));
        enter .setTypeface(new FontManager().getType(getActivity()));
        enter.setOnClickListener(this);
        //输入框
        input = (EditText)view.findViewById(R.id.input_et);
        //Json数据显示框
        informationListView = (ListView)view.findViewById(R.id.myListView);
        phoneticTv = (TextView)view.findViewById(R.id.phonetic_tv);
        queryTv = (TextView)view.findViewById(R.id.query_tv);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //翻译按钮
            case R.id.enter_tv:
                String s = input.getText().toString();
                int fromInt = niceSpinner1.getSelectedIndex();
                int toInt = niceSpinner2.getSelectedIndex();
                try {
                    url = new URLUtil().getTranslateURL(s,fromInt,toInt);
                    Logger.t("ztx").d("url-------->" + url);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                RxVolley.get(url, new HttpCallback() {
                    @Override
                    public void onSuccess(String t) {
                        parsingJson(t);
                    }
                });
                break;
                //照相按钮
            case R.id.photo_tv:
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
                        if (which==0){
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
                            if (Build.VERSION.SDK_INT >= 24) {
                                imageUri = FileProvider.getUriForFile(getActivity(),
                                        "com.gyq.cameraalbumtest.fileprovider", outputImage);
                            } else {
                                imageUri = Uri.fromFile(outputImage);
                            }

                            //启动相机程序
                            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            startActivityForResult(intent, 1);
                        }
                        //从相册中选择
                        if(which==1){
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");//相片类型
                                startActivityForResult(intent, 2);
                        }
                        Toast.makeText(getActivity(), items[which],
                                Toast.LENGTH_SHORT).show();
                    }
                });
                builder.create().show();
                break;
        }
    }
    //事件处理
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            //1 即拍照， 2 即从相册中选择
            case 1:
              if(resultCode ==getActivity().RESULT_OK){
                  try{
                      Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver()
                      .openInputStream(imageUri));
                      ByteArrayOutputStream baos = new ByteArrayOutputStream();
                      bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
                      byte[] appicon = baos.toByteArray();
                      String img_string = Base64.encodeToString(appicon,Base64.DEFAULT);
                  }catch (FileNotFoundException e){
                      e.printStackTrace();
                  }
              }
                    break;
            case 2:
                if(resultCode ==getActivity().RESULT_OK){
                   if(Build.VERSION.SDK_INT >=19){
                       handleImageOnKitKat(data);
                   }else{
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
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
            byte[] appicon = baos.toByteArray();
            String img_string = Base64.encodeToString(appicon,Base64.DEFAULT);
        } else {
            Toast.makeText(getActivity(), "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }



//解析Json
    private void parsingJson(String t) {
        try {
            Logger.t("ztx").json(t);
            JSONObject jsonObject = new JSONObject(t);
            JSONObject json = jsonObject.getJSONObject("basic");
            JSONArray explainsList = json.getJSONArray("explains");
            TranslateInformation translateInformation = new TranslateInformation();
            List<String> explainsLists = new ArrayList<>();
            mList.clear();
            //解析JSon数组的数据
            for (int i = 0 ; i < explainsList.length(); i++){
                String changeString = explainsList.get(i).toString();
                StringBuffer sb = new StringBuffer();
                //符号开关，只选取第一个.
                boolean SYMBOL = false;
                for (int k = 0 ; k < changeString.length() ; k++){
                    if (changeString.charAt(k) == '.'){
                        SYMBOL = true;
                    }
                }
                //搜索第一个.的位置，并将词性颜色标红
                int index = -1;
                if (SYMBOL){
                    sb.append(changeString).insert(0,"<font color='#FF0000'><i>");
                    changeString = sb.toString();
                }
                for (int j = 0 ; j < changeString.length() ; j++){
                    if(SYMBOL && changeString.charAt(j) == '.'){
                        SYMBOL = false;
                        index = j + 1;
                    }
                }
                if (index != -1){
                    sb.insert(index,"</i></font> ");
                    changeString = sb.toString();
                }
                explainsLists.add(changeString);
            }
            if (niceSpinner1.getSelectedIndex() == 0){
                String query = jsonObject.getString("query");
                String us_phonetic = json.getString("us-phonetic");
                String uk_phonetic = json.getString("uk-phonetic");
                String phonetic = json.getString("phonetic");
                translateInformation.setPhonetic(phonetic);
                translateInformation.setUk_phonetic(uk_phonetic);
                translateInformation.setUs_phonetic(us_phonetic);
                translateInformation.setQuery(query);
                queryTv.setText(translateInformation.getQuery());
                phoneticTv.setText(translateInformation.getENPhonetictoString());
            }else{
                String query = jsonObject.getString("query");
                String phonetic = json.getString("phonetic");
                translateInformation.setPhonetic(phonetic);
                translateInformation.setQuery(query);
                queryTv.setText(translateInformation.getQuery());
                phoneticTv.setText(translateInformation.getPhonetictoString());
            }
            translateInformation.setExplains(explainsLists);
            mList.add(translateInformation);
            TranslateInformationAdapter adapter = new TranslateInformationAdapter(getActivity(), explainsLists);
            informationListView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
