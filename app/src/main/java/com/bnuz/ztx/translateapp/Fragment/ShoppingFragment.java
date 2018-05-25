package com.bnuz.ztx.translateapp.Fragment;

import android.annotation.SuppressLint;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import com.bnuz.ztx.translateapp.R;
import com.bnuz.ztx.translateapp.Util.FontManager;
import com.bnuz.ztx.translateapp.Util.HistoryUtil;
import com.bnuz.ztx.translateapp.Util.PicassoImageLoader;
import com.bnuz.ztx.translateapp.Util.URLUtil;
import com.bnuz.ztx.translateapp.View.LooperTextView;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.orhanobut.logger.Logger;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZTX on 2018/5/2.
 */

public class ShoppingFragment extends Fragment {
    TextView scanningTextView, enterTextView, cameraTextView;
    Banner banner;
    LooperTextView looperTextView;
    List<String> bannerList, newsList, chunkTitleList, descriptionList, imageList, topIdList,loveImageList,loveStoriesList,lovemoneyList;
    List<Integer> loveStateList,mipmapList;
    ViewStub view1,view2,view3,view4,viewLove;
    TextView more_bt;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping, null);
        findView(view);
        return view;
    }

    private void findView(View view) {
        more_bt = view.findViewById(R.id.more);
        more_bt.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        scanningTextView = view.findViewById(R.id.sao_tv);
        scanningTextView.setTypeface(new FontManager().getALiType(getContext()));
        enterTextView = view.findViewById(R.id.enter_tv);
        enterTextView.setTypeface(new FontManager().getALiType(getContext()));
        cameraTextView = view.findViewById(R.id.camera_tv);
        cameraTextView.setTypeface(new FontManager().getALiType(getContext()));
        banner = view.findViewById(R.id.myBanner);
        RxVolley.get(new URLUtil().getIP() + "/Carousel", new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                Logger.json(t);
                parsingJson(t);
                banner.setBannerAnimation(Transformer.Default);
                banner.updateBannerStyle(BannerConfig.CIRCLE_INDICATOR);
                banner.setImageLoader(new PicassoImageLoader());
                banner.setImages(bannerList);
                banner.start();
            }
        });
        looperTextView = view.findViewById(R.id.myLooper);
        RxVolley.get(new URLUtil().getIP() + "/NewsCarousel", new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                Logger.json(t);
                parsingNewsJson(t);
                looperTextView.setTipList(newsList);
            }
        });
        view1 = view.findViewById(R.id.viewOne);
        view2 = view.findViewById(R.id.viewTwo);
        view3 = view.findViewById(R.id.viewThree);
        view4 = view.findViewById(R.id.viewFour);
        viewLove = view.findViewById(R.id.viewLove);
        RxVolley.get(new URLUtil().getIP() + "/Promotion?Type=subPromotion&subTitle=null", new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                Logger.json(t);
                parsingTopTitleJson(t);
                String chunkUrl = new URLUtil().getIP() + "/Promotion?Type=subChunk&subTitle=";
                RxVolley.get(chunkUrl + topIdList.get(0), new HttpCallback() {
                    @Override
                    public void onSuccess(String t) {
                        Logger.json(t);
                        parsingChunkOneJson(t);
                    }
                });
                RxVolley.get(chunkUrl + topIdList.get(1), new HttpCallback() {
                    @Override
                    public void onSuccess(String t) {
                        Logger.json(t);
                        parsingChunkTwoJson(t);
                    }
                });
                RxVolley.get(chunkUrl + topIdList.get(2), new HttpCallback() {
                    @Override
                    public void onSuccess(String t) {
                        Logger.json(t);
                        parsingChunkThreeJson(t);
                    }
                });
                RxVolley.get(chunkUrl + topIdList.get(3), new HttpCallback() {
                    @Override
                    public void onSuccess(String t) {
                        Logger.json(t);
                        parsingChunkFourJson(t);
                    }
                });
                RxVolley.get(new URLUtil().getIP() + "/marketLove", new HttpCallback() {
                    @Override
                    public void onSuccess(String t) {
                        super.onSuccess(t);
                        Logger.json(t);
                        parsingLoveJson(t);
                    }
                });
            }
        });
    }

    @SuppressLint("ResourceType")
    private void parsingLoveJson(String t) {
        View a = viewLove.inflate();
        TextView tv = a.findViewById(R.id.topTitle);
        LinearGradient mLinearGradient = new LinearGradient(0, 0, tv.getPaint().getTextSize() * 1, 0, getResources().getColor(R.color.chunkTitle_blue_start), getResources().getColor(R.color.chunkTitle_purple_end), Shader.TileMode.MIRROR);
        tv.getPaint().setShader(mLinearGradient);
        try {
            JSONObject jsonObject = new JSONObject(t);
            int resultCode = jsonObject.getInt("resultCode");
            if (resultCode == 100) {
                JSONArray array = jsonObject.getJSONArray("data");
                loveImageList = new ArrayList<>();
                lovemoneyList = new ArrayList<>();
                loveStoriesList = new ArrayList<>();
                loveStateList = new ArrayList<>();
                String topTitle = "";
                String imageUrl;
                String stories;
                String money;
                int state;
                for (int i = 0; i < array.length(); i++) {
                    JSONObject json = array.getJSONObject(i);
                    topTitle = json.getString("topTitle");
                    imageUrl = json.getString("imageUrl");
                    stories = json.getString("stories");
                    money = json.getString("money");
                    state = json.getInt("state");
                    loveImageList.add(imageUrl);
                    loveStoriesList.add(stories);
                    lovemoneyList.add(money);
                    loveStateList.add(state);
                }
                tv.setText(new HistoryUtil().getPointString(topTitle));
                TextView stories1,stories2,stories3,stories4,stories5,stories6,stories7,stories8,stories9,stories10;
                int startTitle = 0;
                stories1 = a.findViewById(R.id.stories1);
                stories1.setText(loveStoriesList.get(startTitle++));
                stories2 = a.findViewById(R.id.stories2);
                stories2.setText(loveStoriesList.get(startTitle++));
                stories3 = a.findViewById(R.id.stories3);
                stories3.setText(loveStoriesList.get(startTitle++));
                stories4 = a.findViewById(R.id.stories4);
                stories4.setText(loveStoriesList.get(startTitle++));
                stories5 = a.findViewById(R.id.stories5);
                stories5.setText(loveStoriesList.get(startTitle++));
                stories6 = a.findViewById(R.id.stories6);
                stories6.setText(loveStoriesList.get(startTitle++));
                stories7 = a.findViewById(R.id.stories7);
                stories7.setText(loveStoriesList.get(startTitle++));
                stories8 = a.findViewById(R.id.stories8);
                stories8.setText(loveStoriesList.get(startTitle++));
                stories9 = a.findViewById(R.id.stories9);
                stories9.setText(loveStoriesList.get(startTitle++));
                stories10 = a.findViewById(R.id.stories10);
                stories10.setText(loveStoriesList.get(startTitle++));
                TextView money1,money2,money3,money4,money5,money6,money7,money8,money9,money10;
                int startDescription = 0;
                money1 = a.findViewById(R.id.money1);
                money1.setText(lovemoneyList.get(startDescription++));
                money2 = a.findViewById(R.id.money2);
                money2.setText(lovemoneyList.get(startDescription++));
                money3 = a.findViewById(R.id.money3);
                money3.setText(lovemoneyList.get(startDescription++));
                money4 = a.findViewById(R.id.money4);
                money4.setText(lovemoneyList.get(startDescription++));
                money5 = a.findViewById(R.id.money5);
                money5.setText(lovemoneyList.get(startDescription++));
                money6 = a.findViewById(R.id.money6);
                money6.setText(lovemoneyList.get(startDescription++));
                money7 = a.findViewById(R.id.money7);
                money7.setText(lovemoneyList.get(startDescription++));
                money8 = a.findViewById(R.id.money8);
                money8.setText(lovemoneyList.get(startDescription++));
                money9 = a.findViewById(R.id.money9);
                money9.setText(lovemoneyList.get(startDescription++));
                money10 = a.findViewById(R.id.money10);
                money10.setText(lovemoneyList.get(startDescription++));
                ImageView image1, image2,image3,image4,image5,image6,image7,image8,image9,image10;
                int startImage = 0;
                image1 = a.findViewById(R.id.image1);
                new PicassoImageLoader().displayImage(getContext(), loveImageList.get(startImage++).toString(), image1);
                image2 = a.findViewById(R.id.image2);
                new PicassoImageLoader().displayImage(getContext(), loveImageList.get(startImage++).toString(), image2);
                image3 = a.findViewById(R.id.image3);
                new PicassoImageLoader().displayImage(getContext(), loveImageList.get(startImage++).toString(), image3);
                image4 = a.findViewById(R.id.image4);
                new PicassoImageLoader().displayImage(getContext(), loveImageList.get(startImage++).toString(), image4);
                image5 = a.findViewById(R.id.image5);
                new PicassoImageLoader().displayImage(getContext(), loveImageList.get(startImage++).toString(), image5);
                image6 = a.findViewById(R.id.image6);
                new PicassoImageLoader().displayImage(getContext(), loveImageList.get(startImage++).toString(), image6);
                image7 = a.findViewById(R.id.image7);
                new PicassoImageLoader().displayImage(getContext(), loveImageList.get(startImage++).toString(), image7);
                image8 = a.findViewById(R.id.image8);
                new PicassoImageLoader().displayImage(getContext(), loveImageList.get(startImage++).toString(), image8);
                image9 = a.findViewById(R.id.image9);
                new PicassoImageLoader().displayImage(getContext(), loveImageList.get(startImage++).toString(), image9);
                image10 = a.findViewById(R.id.image10);
                new PicassoImageLoader().displayImage(getContext(), loveImageList.get(startImage++).toString(), image10);
                mipmapList = new ArrayList<>();
                for (int z = 0 ; z < loveStateList.size() ; z++){
                    switch (loveStateList.get(z)){
                        case 0:
                            break;
                        case 1:
                            mipmapList.add(R.mipmap.recommed_icon);
                            break;
                        case 2:
                            mipmapList.add(R.mipmap.new_icon);
                            break;
                        case 3:
                            mipmapList.add(z,R.mipmap.manjian);
                            break;
                        case 4:
                            mipmapList.add(z,R.mipmap.procket);
                            break;
                        default:
                            break;
                    }
                }
                ImageView state1, state2,state3,state4,state5,state6,state7,state8,state9,state10;
                int startstate = 0;
                state1 = a.findViewById(R.id.state1);
                state1.setImageResource(mipmapList.get(startstate++));
                state2 = a.findViewById(R.id.state2);
                state2.setImageResource(mipmapList.get(startstate++));
                state3 = a.findViewById(R.id.state3);
                state3.setImageResource(mipmapList.get(startstate++));
                state4 = a.findViewById(R.id.state4);
                state4.setImageResource(mipmapList.get(startstate++));
                state5 = a.findViewById(R.id.state5);
                state5.setImageResource(mipmapList.get(startstate++));
                state6 = a.findViewById(R.id.state6);
                state6.setImageResource(mipmapList.get(startstate++));
                state7 = a.findViewById(R.id.state7);
                state7.setImageResource(mipmapList.get(startstate++));
                state8 = a.findViewById(R.id.state8);
                state8.setImageResource(mipmapList.get(startstate++));
                state9 = a.findViewById(R.id.state9);
                state9.setImageResource(mipmapList.get(startstate++));
                state10 = a.findViewById(R.id.state10);
                state10.setImageResource(mipmapList.get(startstate++));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parsingChunkFourJson(String t) {
        View a = view4.inflate();
        TextView tv = a.findViewById(R.id.topTitle);
        LinearGradient mLinearGradient = new LinearGradient(0, 0, tv.getPaint().getTextSize() * 1, 0, getResources().getColor(R.color.chunkTitle_blue_start), getResources().getColor(R.color.chunkTitle_purple_end), Shader.TileMode.MIRROR);
        tv.getPaint().setShader(mLinearGradient);
        try {
            JSONObject jsonObject = new JSONObject(t);
            int resultCode = jsonObject.getInt("resultCode");
            if (resultCode == 100) {
                JSONArray array = jsonObject.getJSONArray("data");
                chunkTitleList = new ArrayList<>();
                descriptionList = new ArrayList<>();
                imageList = new ArrayList<>();
                String topTitle = "";
                String chunkTitle;
                String description;
                String date;
                for (int i = 0; i < array.length(); i++) {
                    JSONObject json = array.getJSONObject(i);
                    topTitle = json.getString("topTitle");
                    chunkTitle = json.getString("chunkTitle");
                    description = json.getString("description");
                    date = json.getString("date");
                    JSONArray url = json.getJSONArray("imageUrl");
                    for (int j = 0; j < url.length(); j++) {
                        String imageUrl = url.get(j).toString();
                        if (!imageUrl.equals("null")) {
                            imageList.add(imageUrl);
                        }
                    }
                    chunkTitleList.add(chunkTitle);
                    descriptionList.add(description);
                }
                tv.setText(new HistoryUtil().getPointString(topTitle));
                TextView subTitle1, subTitle2, subTitle3, subTitle4, subTitle5, subTitle6, subTitle7, subTitle8;
                int startTitle = 0;
                subTitle1 = a.findViewById(R.id.subTitle1);
                subTitle1.setText(chunkTitleList.get(startTitle++));
                subTitle2 = a.findViewById(R.id.subTitle2);
                subTitle2.setText(chunkTitleList.get(startTitle++));
                subTitle3 = a.findViewById(R.id.subTitle3);
                subTitle3.setText(chunkTitleList.get(startTitle++));
                subTitle4 = a.findViewById(R.id.subTitle4);
                subTitle4.setText(chunkTitleList.get(startTitle++));
                subTitle5 = a.findViewById(R.id.subTitle5);
                subTitle5.setText(chunkTitleList.get(startTitle++));
                subTitle6 = a.findViewById(R.id.subTitle6);
                subTitle6.setText(chunkTitleList.get(startTitle++));
                subTitle7 = a.findViewById(R.id.subTitle7);
                subTitle7.setText(chunkTitleList.get(startTitle++));
                subTitle8 = a.findViewById(R.id.subTitle8);
                subTitle8.setText(chunkTitleList.get(startTitle++));
                TextView des1, des2, des3, des4, des5, des6, des7, des8;
                int startDescription = 0;
                des1 = a.findViewById(R.id.description1);
                des1.setText(descriptionList.get(startDescription++));
                des2 = a.findViewById(R.id.description2);
                des2.setText(descriptionList.get(startDescription++));
                des3 = a.findViewById(R.id.description3);
                des3.setText(descriptionList.get(startDescription++));
                des4 = a.findViewById(R.id.description4);
                des4.setText(descriptionList.get(startDescription++));
                des5 = a.findViewById(R.id.description5);
                des5.setText(descriptionList.get(startDescription++));
                des6 = a.findViewById(R.id.description6);
                des6.setText(descriptionList.get(startDescription++));
                des7 = a.findViewById(R.id.description7);
                des7.setText(descriptionList.get(startDescription++));
                des8 = a.findViewById(R.id.description8);
                des8.setText(descriptionList.get(startDescription++));
                ImageView img1, img2, img3, img4, img5, img6, img7, img8;
                int startImage = 0;
                img1 = a.findViewById(R.id.image1);
                new PicassoImageLoader().displayImage(getContext(), imageList.get(startImage++).toString(), img1);
                img2 = a.findViewById(R.id.image2);
                new PicassoImageLoader().displayImage(getContext(), imageList.get(startImage++).toString(), img2);
                img3 = a.findViewById(R.id.image3);
                new PicassoImageLoader().displayImage(getContext(), imageList.get(startImage++).toString(), img3);
                img4 = a.findViewById(R.id.image4);
                new PicassoImageLoader().displayImage(getContext(), imageList.get(startImage++).toString(), img4);
                img5 = a.findViewById(R.id.image5);
                new PicassoImageLoader().displayImage(getContext(), imageList.get(startImage++).toString(), img5);
                img6 = a.findViewById(R.id.image6);
                new PicassoImageLoader().displayImage(getContext(), imageList.get(startImage++).toString(), img6);
                img7 = a.findViewById(R.id.image7);
                new PicassoImageLoader().displayImage(getContext(), imageList.get(startImage++).toString(), img7);
                img8 = a.findViewById(R.id.image8);
                new PicassoImageLoader().displayImage(getContext(), imageList.get(startImage++).toString(), img8);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parsingChunkThreeJson(String t) {
        View a = view3.inflate();
        TextView tv = a.findViewById(R.id.topTitle);
        LinearGradient mLinearGradient = new LinearGradient(0, 0, tv.getPaint().getTextSize() * 1, 0, getResources().getColor(R.color.chunkTitle_green_start), getResources().getColor(R.color.chunkTitle_cyan_end), Shader.TileMode.MIRROR);
        tv.getPaint().setShader(mLinearGradient);
        try {
            JSONObject jsonObject = new JSONObject(t);
            int resultCode = jsonObject.getInt("resultCode");
            if (resultCode == 100) {
                JSONArray array = jsonObject.getJSONArray("data");
                chunkTitleList = new ArrayList<>();
                descriptionList = new ArrayList<>();
                imageList = new ArrayList<>();
                String topTitle = "";
                String chunkTitle = "";
                String description = "";
                String date;
                for (int i = 0; i < array.length(); i++) {
                    JSONObject json = array.getJSONObject(i);
                    topTitle = json.getString("topTitle");
                    chunkTitle = json.getString("chunkTitle");
                    description = json.getString("description");
                    date = json.getString("date");
                    JSONArray url = json.getJSONArray("imageUrl");
                    for (int j = 0; j < url.length(); j++) {
                        String imageUrl = url.get(j).toString();
                        if (!imageUrl.equals("null")) {
                            imageList.add(imageUrl);
                        }
                    }
                    chunkTitleList.add(chunkTitle);
                    descriptionList.add(description);
                }
                tv.setText(new HistoryUtil().getPointString(topTitle));
                TextView subTitle1, subTitle2, subTitle3, subTitle4,subTitle5, subTitle6;
                int startTitle = 0;
                subTitle1 = a.findViewById(R.id.subTitle1);
                subTitle1.setText(chunkTitleList.get(startTitle++));
                subTitle2 = a.findViewById(R.id.subTitle2);
                subTitle2.setText(chunkTitleList.get(startTitle++));
                subTitle3 = a.findViewById(R.id.subTitle3);
                subTitle3.setText(chunkTitleList.get(startTitle++));
                subTitle4 = a.findViewById(R.id.subTitle4);
                subTitle4.setText(chunkTitleList.get(startTitle++));
                subTitle5 = a.findViewById(R.id.subTitle5);
                subTitle5.setText(chunkTitleList.get(startTitle++));
                subTitle6 = a.findViewById(R.id.subTitle6);
                subTitle6.setText(chunkTitleList.get(startTitle++));
                TextView des1, des2, des3, des4,des5,des6;
                int startDescription = 0;
                des1 = a.findViewById(R.id.description1);
                des1.setText(descriptionList.get(startDescription++));
                des2 = a.findViewById(R.id.description2);
                des2.setText(descriptionList.get(startDescription++));
                des3 = a.findViewById(R.id.description3);
                des3.setText(descriptionList.get(startDescription++));
                des4 = a.findViewById(R.id.description4);
                des4.setText(descriptionList.get(startDescription++));
                des5 = a.findViewById(R.id.description5);
                des5.setText(descriptionList.get(startDescription++));
                des6 = a.findViewById(R.id.description6);
                des6.setText(descriptionList.get(startDescription++));
                ImageView img1, img2, img3, img4, img5, img6, img7, img8;
                int startImage = 0;
                img1 = a.findViewById(R.id.image1);
                new PicassoImageLoader().displayImage(getContext(), imageList.get(startImage++).toString(), img1);
                img2 = a.findViewById(R.id.image2);
                new PicassoImageLoader().displayImage(getContext(), imageList.get(startImage++).toString(), img2);
                img3 = a.findViewById(R.id.image3);
                new PicassoImageLoader().displayImage(getContext(), imageList.get(startImage++).toString(), img3);
                img4 = a.findViewById(R.id.image4);
                new PicassoImageLoader().displayImage(getContext(), imageList.get(startImage++).toString(), img4);
                img5 = a.findViewById(R.id.image5);
                new PicassoImageLoader().displayImage(getContext(), imageList.get(startImage++).toString(), img5);
                img6 = a.findViewById(R.id.image6);
                new PicassoImageLoader().displayImage(getContext(), imageList.get(startImage++).toString(), img6);
                img7 = a.findViewById(R.id.image7);
                new PicassoImageLoader().displayImage(getContext(), imageList.get(startImage++).toString(), img7);
                img8 = a.findViewById(R.id.image8);
                new PicassoImageLoader().displayImage(getContext(), imageList.get(startImage++).toString(), img8);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parsingChunkTwoJson(String t) {
        View a = view2.inflate();
        TextView tv = a.findViewById(R.id.topTitle);
        LinearGradient mLinearGradient = new LinearGradient(0, 0, tv.getPaint().getTextSize() * 1, 0, getResources().getColor(R.color.chunkTitle_orange_start), getResources().getColor(R.color.chunkTitle_red_end), Shader.TileMode.MIRROR);
        tv.getPaint().setShader(mLinearGradient);
        try {
            JSONObject jsonObject = new JSONObject(t);
            int resultCode = jsonObject.getInt("resultCode");
            if (resultCode == 100) {
                JSONArray array = jsonObject.getJSONArray("data");
                chunkTitleList = new ArrayList<>();
                descriptionList = new ArrayList<>();
                imageList = new ArrayList<>();
                String topTitle = "";
                String chunkTitle = "";
                String description = "";
                String date;
                for (int i = 0; i < array.length(); i++) {
                    JSONObject json = array.getJSONObject(i);
                    topTitle = json.getString("topTitle");
                    chunkTitle = json.getString("chunkTitle");
                    description = json.getString("description");
                    date = json.getString("date");
                    JSONArray url = json.getJSONArray("imageUrl");
                    for (int j = 0; j < url.length(); j++) {
                        String imageUrl = url.get(j).toString();
                        if (!imageUrl.equals("null")) {
                            imageList.add(imageUrl);
                        }
                    }
                    chunkTitleList.add(chunkTitle);
                    descriptionList.add(description);
                }
                tv.setText(new HistoryUtil().getPointString(topTitle));
                TextView subTitle1, subTitle2, subTitle3, subTitle4;
                int startTitle = 0;
                subTitle1 = a.findViewById(R.id.subTitle1);
                subTitle1.setText(chunkTitleList.get(startTitle++));
                subTitle2 = a.findViewById(R.id.subTitle2);
                subTitle2.setText(chunkTitleList.get(startTitle++));
                subTitle3 = a.findViewById(R.id.subTitle3);
                subTitle3.setText(chunkTitleList.get(startTitle++));
                subTitle4 = a.findViewById(R.id.subTitle4);
                subTitle4.setText(chunkTitleList.get(startTitle++));
                TextView des1, des2, des3, des4;
                int startDescription = 0;
                des1 = a.findViewById(R.id.description1);
                des1.setText(descriptionList.get(startDescription++));
                des2 = a.findViewById(R.id.description2);
                des2.setText(descriptionList.get(startDescription++));
                des3 = a.findViewById(R.id.description3);
                des3.setText(descriptionList.get(startDescription++));
                des4 = a.findViewById(R.id.description4);
                des4.setText(descriptionList.get(startDescription++));
                ImageView img1, img2, img3, img4, img5, img6, img7, img8;
                int startImage = 0;
                img1 = a.findViewById(R.id.image1);
                new PicassoImageLoader().displayImage(getContext(), imageList.get(startImage++).toString(), img1);
                img2 = a.findViewById(R.id.image2);
                new PicassoImageLoader().displayImage(getContext(), imageList.get(startImage++).toString(), img2);
                img3 = a.findViewById(R.id.image3);
                new PicassoImageLoader().displayImage(getContext(), imageList.get(startImage++).toString(), img3);
                img4 = a.findViewById(R.id.image4);
                new PicassoImageLoader().displayImage(getContext(), imageList.get(startImage++).toString(), img4);
                img5 = a.findViewById(R.id.image5);
                new PicassoImageLoader().displayImage(getContext(), imageList.get(startImage++).toString(), img5);
                img6 = a.findViewById(R.id.image6);
                new PicassoImageLoader().displayImage(getContext(), imageList.get(startImage++).toString(), img6);
                img7 = a.findViewById(R.id.image7);
                new PicassoImageLoader().displayImage(getContext(), imageList.get(startImage++).toString(), img7);
                img8 = a.findViewById(R.id.image8);
                new PicassoImageLoader().displayImage(getContext(), imageList.get(startImage++).toString(), img8);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parsingTopTitleJson(String t) {
        topIdList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(t);
            int resultCode = jsonObject.getInt("resultCode");
            if (resultCode == 100) {
                JSONArray array = jsonObject.getJSONArray("data");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject json = array.getJSONObject(i);
                    String idTitle = json.getString("idTitle");
                    String subTitle = json.getString("subTitle");
                    String date = json.getString("date");
                    topIdList.add(idTitle);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parsingChunkOneJson(String t) {
        View a = view1.inflate();
        TextView tv = a.findViewById(R.id.topTitle);
        LinearGradient mLinearGradient = new LinearGradient(0, 0, tv.getPaint().getTextSize() * 1, 0, getResources().getColor(R.color.chunkTitle_blue_start), getResources().getColor(R.color.chunkTitle_purple_end), Shader.TileMode.MIRROR);
        tv.getPaint().setShader(mLinearGradient);
        try {
            JSONObject jsonObject = new JSONObject(t);
            int resultCode = jsonObject.getInt("resultCode");
            if (resultCode == 100) {
                JSONArray array = jsonObject.getJSONArray("data");
                chunkTitleList = new ArrayList<>();
                descriptionList = new ArrayList<>();
                imageList = new ArrayList<>();
                String topTitle = "";
                String chunkTitle;
                String description;
                String date;
                for (int i = 0; i < array.length(); i++) {
                    JSONObject json = array.getJSONObject(i);
                    topTitle = json.getString("topTitle");
                    chunkTitle = json.getString("chunkTitle");
                    description = json.getString("description");
                    date = json.getString("date");
                    JSONArray url = json.getJSONArray("imageUrl");
                    for (int j = 0; j < url.length(); j++) {
                        String imageUrl = url.get(j).toString();
                        if (!imageUrl.equals("null")) {
                            imageList.add(imageUrl);
                        }
                    }
                    chunkTitleList.add(chunkTitle);
                    descriptionList.add(description);
                }
                tv.setText(new HistoryUtil().getPointString(topTitle));
                TextView subTitle1, subTitle2, subTitle3, subTitle4, subTitle5, subTitle6, subTitle7, subTitle8;
                int startTitle = 0;
                subTitle1 = a.findViewById(R.id.subTitle1);
                subTitle1.setText(chunkTitleList.get(startTitle++));
                subTitle2 = a.findViewById(R.id.subTitle2);
                subTitle2.setText(chunkTitleList.get(startTitle++));
                subTitle3 = a.findViewById(R.id.subTitle3);
                subTitle3.setText(chunkTitleList.get(startTitle++));
                subTitle4 = a.findViewById(R.id.subTitle4);
                subTitle4.setText(chunkTitleList.get(startTitle++));
                subTitle5 = a.findViewById(R.id.subTitle5);
                subTitle5.setText(chunkTitleList.get(startTitle++));
                subTitle6 = a.findViewById(R.id.subTitle6);
                subTitle6.setText(chunkTitleList.get(startTitle++));
                subTitle7 = a.findViewById(R.id.subTitle7);
                subTitle7.setText(chunkTitleList.get(startTitle++));
                subTitle8 = a.findViewById(R.id.subTitle8);
                subTitle8.setText(chunkTitleList.get(startTitle++));
                TextView des1, des2, des3, des4, des5, des6, des7, des8;
                int startDescription = 0;
                des1 = a.findViewById(R.id.description1);
                des1.setText(descriptionList.get(startDescription++));
                des2 = a.findViewById(R.id.description2);
                des2.setText(descriptionList.get(startDescription++));
                des3 = a.findViewById(R.id.description3);
                des3.setText(descriptionList.get(startDescription++));
                des4 = a.findViewById(R.id.description4);
                des4.setText(descriptionList.get(startDescription++));
                des5 = a.findViewById(R.id.description5);
                des5.setText(descriptionList.get(startDescription++));
                des6 = a.findViewById(R.id.description6);
                des6.setText(descriptionList.get(startDescription++));
                des7 = a.findViewById(R.id.description7);
                des7.setText(descriptionList.get(startDescription++));
                des8 = a.findViewById(R.id.description8);
                des8.setText(descriptionList.get(startDescription++));
                ImageView img1, img2, img3, img4, img5, img6, img7, img8, img9, img10, img11, img12;
                int startImage = 0;
                img1 = a.findViewById(R.id.image1);
                new PicassoImageLoader().displayImage(getContext(), imageList.get(startImage++).toString(), img1);
                img2 = a.findViewById(R.id.image2);
                new PicassoImageLoader().displayImage(getContext(), imageList.get(startImage++).toString(), img2);
                img3 = a.findViewById(R.id.image3);
                new PicassoImageLoader().displayImage(getContext(), imageList.get(startImage++).toString(), img3);
                img4 = a.findViewById(R.id.image4);
                new PicassoImageLoader().displayImage(getContext(), imageList.get(startImage++).toString(), img4);
                img5 = a.findViewById(R.id.image5);
                new PicassoImageLoader().displayImage(getContext(), imageList.get(startImage++).toString(), img5);
                img6 = a.findViewById(R.id.image6);
                new PicassoImageLoader().displayImage(getContext(), imageList.get(startImage++).toString(), img6);
                img7 = a.findViewById(R.id.image7);
                new PicassoImageLoader().displayImage(getContext(), imageList.get(startImage++).toString(), img7);
                img8 = a.findViewById(R.id.image8);
                new PicassoImageLoader().displayImage(getContext(), imageList.get(startImage++).toString(), img8);
                img9 = a.findViewById(R.id.image9);
                new PicassoImageLoader().displayImage(getContext(), imageList.get(startImage++).toString(), img9);
                img10 = a.findViewById(R.id.image10);
                new PicassoImageLoader().displayImage(getContext(), imageList.get(startImage++).toString(), img10);
                img11 = a.findViewById(R.id.image11);
                new PicassoImageLoader().displayImage(getContext(), imageList.get(startImage++).toString(), img11);
                img12 = a.findViewById(R.id.image12);
                new PicassoImageLoader().displayImage(getContext(), imageList.get(startImage++).toString(), img12);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parsingNewsJson(String t) {
        newsList = new ArrayList<>();
        String start = "<font color='#FF0000'>";
        String end = "</font>";
        String s;
        try {
            JSONObject jsonObject = new JSONObject(t);
            int resultCode = jsonObject.getInt("resultCode");
            if (resultCode == 100) {
                JSONArray array = jsonObject.getJSONArray("data");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject json = array.getJSONObject(i);
                    String title = json.getString("title");
                    if (title.length() > 15) {
                        title = title.substring(0, 15) + "...";
                    }
                    String date = json.getString("date");
                    Integer state = json.getInt("state");
                    switch (state) {
                        case 1:
                            s = start + "NEW " + end + title;
                            break;
                        case 2:
                            s = start + "推荐 " + end + title;
                            break;
                        case 3:
                            s = start + "HOT " + end + title;
                            break;
                        default:
                            s = "...";
                            break;
                    }
                    newsList.add(s);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parsingJson(String t) {
        bannerList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(t);
            int resultCode = jsonObject.getInt("resultCode");
            if (resultCode == 100) {
                JSONArray array = jsonObject.getJSONArray("data");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject json = array.getJSONObject(i);
                    String imageUrl = json.getString("imageUrl");
                    String targetUrl = json.getString("targetUrl");
                    String date = json.getString("date");
                    Integer imageType = json.getInt("imageType");
                    bannerList.add(imageUrl);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}