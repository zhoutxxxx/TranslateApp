package com.bnuz.ztx.translateapp.net;

import android.content.Context;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import com.bnuz.ztx.translateapp.R;
import com.bnuz.ztx.translateapp.Util.ACache;
import com.bnuz.ztx.translateapp.Util.HistoryUtil;
import com.bnuz.ztx.translateapp.Util.ImageCacheUtil;
import com.bnuz.ztx.translateapp.Util.PicassoImageLoader;
import com.bnuz.ztx.translateapp.View.LooperTextView;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.http.VolleyError;
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
 * Created by ZTX on 2018/5/26.
 */

public class MyVolley {
    List<String> bannerList, looperTextViewList, topIdList, chunkTitleList, descriptionList, imageUrlList, loveImageUrlList, loveStoriesList, loveMoneyList;
    List<Integer> loveStateList, mipmapList;
    ImageCacheUtil mImageCacheUtil; //cachUtil ->
   // DiskLruCache diskLruCache;
    Context context;

    public MyVolley(Context context) {
        this.context = context;
        mImageCacheUtil = new ImageCacheUtil(context);
      //  diskLruCache = cacheUtil.getDiskLruCache(context);
    }

    public void getBanner(String url, final Banner banner) {
        bannerList = new ArrayList<>();
        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                com.orhanobut.logger.Logger.json(t);
                parsingBannerJson(t, banner);
                ACache.get(context).put("bannerStr", t);
            }
            @Override
            public void onFailure(VolleyError error) {
                parsingBannerJson(ACache.get(context).getAsString("bannerStr"), banner);
            }
        });
    }

    private void parsingBannerJson(String t, Banner banner) {
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

                  //  mImageCacheUtil.loadBitmap(imageUrl,100,100);
                 //   cacheUtil.addBitmapToDiskLruCache(imageUrl, diskLruCache);
                }
            }
            banner.setBannerAnimation(Transformer.Default);
            banner.updateBannerStyle(BannerConfig.CIRCLE_INDICATOR);
            banner.setImageLoader(new PicassoImageLoader());
            banner.setImages(bannerList);
            banner.start();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getLooperTextView(String url, final LooperTextView looperTextView) {
        looperTextViewList = new ArrayList<>();
        RxVolley.get(url, new HttpCallback() {

            @Override
            public void onSuccess(String t) {
                Logger.json(t);
                ACache.get(context).put("LooperJson", t);
                parsingLooperTextView(t, looperTextView);
            }

            @Override
            public void onFailure(VolleyError error) {
                parsingLooperTextView(ACache.get(context).getAsString("LooperJson"), looperTextView);
            }
        });
    }

    private void parsingLooperTextView(String t, LooperTextView looperTextView) {
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
                    looperTextViewList.add(s);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        looperTextView.setTipList(looperTextViewList);
    }

//    public void getPromotion(String s, final ViewStub view, final int index, final Context context) {
//        topIdList = new ArrayList<>();
//        RxVolley.get(s, new HttpCallback() {
//
//            @Override
//            public void onSuccess(String t) {
//                Logger.json(t);
//                parsingTopIdJson(t);
//                String chunkUrl = new URLUtil().getIP() + "/Promotion?Type=subChunk&subTitle=";
//                switch (index) {
//                    case 0:
//                        RxVolley.get(chunkUrl + topIdList.get(index), new HttpCallback() {
//                            @RequiresApi(api = Build.VERSION_CODES.M)
//                            @Override
//                            public void onSuccess(String t) {
//                                Logger.json(t);
//                                ACache.get(context).put("oneJson", t);
//                                parsingViewOneJson(t, view, context);
//                            }
//
//                            @RequiresApi(api = Build.VERSION_CODES.M)
//                            @Override
//                            public void onFailure(VolleyError error) {
//                                parsingViewOneJson(ACache.get(context).getAsString("oneJson"), view, context);
//                            }
//                        });
//                        break;
//                    case 1:
//                        RxVolley.get(chunkUrl + topIdList.get(index), new HttpCallback() {
//                            @RequiresApi(api = Build.VERSION_CODES.M)
//                            @Override
//                            public void onSuccess(String t) {
//                                Logger.json(t);
//                                ACache.get(context).put("twoJson", t);
//                                parsingViewTwoJson(t, view, context);
//                            }
//
//                            @RequiresApi(api = Build.VERSION_CODES.M)
//                            @Override
//                            public void onFailure(VolleyError error) {
//                                parsingViewTwoJson(ACache.get(context).getAsString("twoJson"), view, context);
//                            }
//                        });
//                        break;
//                    case 2:
//                        RxVolley.get(chunkUrl + topIdList.get(index), new HttpCallback() {
//                            @RequiresApi(api = Build.VERSION_CODES.M)
//                            @Override
//                            public void onSuccess(String t) {
//                                Logger.json(t);
//                                ACache.get(context).put("threeJson", t);
//                                parsingViewThreeJson(t, view, context);
//                            }
//
//                            @RequiresApi(api = Build.VERSION_CODES.M)
//                            @Override
//                            public void onFailure(VolleyError error) {
//                                parsingViewThreeJson(ACache.get(context).getAsString("threeJson"), view, context);
//                            }
//                        });
//                        break;
//                    case 3:
//                        RxVolley.get(chunkUrl + topIdList.get(index), new HttpCallback() {
//                            @RequiresApi(api = Build.VERSION_CODES.M)
//                            @Override
//                            public void onSuccess(String t) {
//                                Logger.json(t);
//                                ACache.get(context).put("fourJson", t);
//                                parsingViewFourJson(t, view, context);
//                            }
//
//                            @RequiresApi(api = Build.VERSION_CODES.M)
//                            @Override
//                            public void onFailure(VolleyError error) {
//                                parsingViewFourJson(ACache.get(context).getAsString("fourJson"), view, context);
//                            }
//                        });
//                        break;
//                    default:
//                        break;
//                }
//            }
//            @RequiresApi(api = Build.VERSION_CODES.M)
//            @Override
//            public void onFailure(VolleyError error) {
//                switch (index){
//                    case 0:
//                        parsingViewOneJson(ACache.get(context).getAsString("oneJson"), view, context);
//                        break;
//                    case 1:
//                        parsingViewTwoJson(ACache.get(context).getAsString("twoJson"), view, context);
//                        break;
//                    case 2:
//                        parsingViewThreeJson(ACache.get(context).getAsString("threeJson"), view, context);
//                        break;
//                    case 3:
//                        parsingViewFourJson(ACache.get(context).getAsString("fourJson"), view, context);
//                        break;
//                }
//            }
//        });
//
//    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void parsingViewLoveJson(String t, ViewStub view, Context context) {
        View a = view.inflate();
        TextView tv = a.findViewById(R.id.topTitle);
        LinearGradient mLinearGradient = new LinearGradient(0, 0, tv.getPaint().getTextSize() * 1, 0, context.getColor(R.color.chunkTitle_blue_start), context.getColor(R.color.chunkTitle_purple_end), Shader.TileMode.MIRROR);
        tv.getPaint().setShader(mLinearGradient);
        try {
            JSONObject jsonObject = new JSONObject(t);
            int resultCode = jsonObject.getInt("resultCode");
            if (resultCode == 100) {
                JSONArray array = jsonObject.getJSONArray("data");
                loveImageUrlList = new ArrayList<>();
                loveMoneyList = new ArrayList<>();
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
                    ACache.get(context).put("loveTitle", topTitle);
                    //mImageCacheUtil.loadBitmap(imageUrl,100,100);
                  //  cacheUtil.addBitmapToDiskLruCache(imageUrl, diskLruCache);
                    ACache.get(context).put("love" + i, imageUrl);

                    ACache.get(context).put("stories" + i, stories);
                    ACache.get(context).put("money" + i, money);
                    ACache.get(context).put("state" + i, String.valueOf(state));
                    loveImageUrlList.add(imageUrl);
                    loveStateList.add(state);
                }

                tv.setText(new HistoryUtil().getPointString(ACache.get(context).getAsString("loveTitle")));
                TextView stories1, stories2, stories3, stories4, stories5, stories6, stories7, stories8, stories9, stories10;
                int startTitle = 0;
                stories1 = a.findViewById(R.id.stories1);
                stories1.setText(ACache.get(context).getAsString("stories" + startTitle++));
                stories2 = a.findViewById(R.id.stories2);
                stories2.setText(ACache.get(context).getAsString("stories" + startTitle++));
                stories3 = a.findViewById(R.id.stories3);
                stories3.setText(ACache.get(context).getAsString("stories" + startTitle++));
                stories4 = a.findViewById(R.id.stories4);
                stories4.setText(ACache.get(context).getAsString("stories" + startTitle++));
                stories5 = a.findViewById(R.id.stories5);
                stories5.setText(ACache.get(context).getAsString("stories" + startTitle++));
                stories6 = a.findViewById(R.id.stories6);
                stories6.setText(ACache.get(context).getAsString("stories" + startTitle++));
                stories7 = a.findViewById(R.id.stories7);
                stories7.setText(ACache.get(context).getAsString("stories" + startTitle++));
                stories8 = a.findViewById(R.id.stories8);
                stories8.setText(ACache.get(context).getAsString("stories" + startTitle++));
                stories9 = a.findViewById(R.id.stories9);
                stories9.setText(ACache.get(context).getAsString("stories" + startTitle++));
                stories10 = a.findViewById(R.id.stories10);
                stories10.setText(ACache.get(context).getAsString("stories" + startTitle++));

                TextView money1, money2, money3, money4, money5, money6, money7, money8, money9, money10;
                int startDescription = 0;
                money1 = a.findViewById(R.id.money1);
                money1.setText(ACache.get(context).getAsString("money" + startDescription++));
                money2 = a.findViewById(R.id.money2);
                money2.setText(ACache.get(context).getAsString("money" + startDescription++));
                money3 = a.findViewById(R.id.money3);
                money3.setText(ACache.get(context).getAsString("money" + startDescription++));
                money4 = a.findViewById(R.id.money4);
                money4.setText(ACache.get(context).getAsString("money" + startDescription++));
                money5 = a.findViewById(R.id.money5);
                money5.setText(ACache.get(context).getAsString("money" + startDescription++));
                money6 = a.findViewById(R.id.money6);
                money6.setText(ACache.get(context).getAsString("money" + startDescription++));
                money7 = a.findViewById(R.id.money7);
                money7.setText(ACache.get(context).getAsString("money" + startDescription++));
                money8 = a.findViewById(R.id.money8);
                money8.setText(ACache.get(context).getAsString("money" + startDescription++));
                money9 = a.findViewById(R.id.money9);
                money9.setText(ACache.get(context).getAsString("money" + startDescription++));
                money10 = a.findViewById(R.id.money10);
                money10.setText(ACache.get(context).getAsString("money" + startDescription++));
                ImageView image1, image2, image3, image4, image5, image6, image7, image8, image9, image10;
                int startImage = 0;
                image1 = a.findViewById(R.id.image1);
                image1.setTag("love" + startImage++);
               // String image11 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1528872471&di=a141d98b71fbb4b90a8bdd1a21c9f398&imgtype=jpg&er=1&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F01e4ef59395005a8012193a37970d9.jpg%401280w_1l_2o_100sh.jpg";
                int Count = 0;
                mImageCacheUtil.bindBitmap(loveImageUrlList.get(Count++),image1,100,100);
                //image1.setImageBitmap(cacheUtil.getCache(ACache.get(context).getAsString("love" + startImage++), diskLruCache));
                image2 = a.findViewById(R.id.image2);
                image2.setTag("love" + startImage++);
                mImageCacheUtil.bindBitmap(loveImageUrlList.get(Count++),image2,100,100);
                //image2.setImageBitmap(cacheUtil.getCache(ACache.get(context).getAsString("love" + startImage++), diskLruCache));
                image3 = a.findViewById(R.id.image3);
                image3.setTag("love" + startImage++);
                mImageCacheUtil.bindBitmap(loveImageUrlList.get(Count++),image3,100,100);
                //image3.setImageBitmap(cacheUtil.getCache(ACache.get(context).getAsString("love" + startImage++), diskLruCache));
                image4 = a.findViewById(R.id.image4);
                image4.setTag("love" + startImage++);
                mImageCacheUtil.bindBitmap(loveImageUrlList.get(Count++),image4,100,100);
                //image4.setImageBitmap(cacheUtil.getCache(ACache.get(context).getAsString("love" + startImage++), diskLruCache));
                image5 = a.findViewById(R.id.image5);
                image5.setTag("love" + startImage++);
                mImageCacheUtil.bindBitmap(loveImageUrlList.get(Count++),image5,100,100);
                //image5.setImageBitmap(cacheUtil.getCache(ACache.get(context).getAsString("love" + startImage++), diskLruCache));
                image6 = a.findViewById(R.id.image6);
                image6.setTag("love" + startImage++);
                mImageCacheUtil.bindBitmap(loveImageUrlList.get(Count++),image6,100,100);
                //image6.setImageBitmap(cacheUtil.getCache(ACache.get(context).getAsString("love" + startImage++), diskLruCache));
                image7 = a.findViewById(R.id.image7);
                image7.setTag("love" + startImage++);
                mImageCacheUtil.bindBitmap(loveImageUrlList.get(Count++),image7,100,100);
                //image7.setImageBitmap(cacheUtil.getCache(ACache.get(context).getAsString("love" + startImage++), diskLruCache));
                image8 = a.findViewById(R.id.image8);
                image8.setTag("love" + startImage++);
                mImageCacheUtil.bindBitmap(loveImageUrlList.get(Count++),image8,100,100);
                //image8.setImageBitmap(cacheUtil.getCache(ACache.get(context).getAsString("love" + startImage++), diskLruCache));
                image9 = a.findViewById(R.id.image9);
                image9.setTag("love" + startImage++);
                mImageCacheUtil.bindBitmap(loveImageUrlList.get(Count++),image9,100,100);
                //image9.setImageBitmap(cacheUtil.getCache(ACache.get(context).getAsString("love" + startImage++), diskLruCache));
                image10 = a.findViewById(R.id.image10);
                image10.setTag("love" + startImage++);
                mImageCacheUtil.bindBitmap(loveImageUrlList.get(Count++),image10,100,100);
                //image10.setImageBitmap(cacheUtil.getCache(ACache.get(context).getAsString("love" + startImage++), diskLruCache));
                mipmapList = new ArrayList<>();
                for (int z = 0; z < loveStateList.size(); z++) {
                    switch (loveStateList.get(z)) {
                        case 0:
                            break;
                        case 1:
                            mipmapList.add(R.mipmap.recommed_icon);
                            break;
                        case 2:
                            mipmapList.add(R.mipmap.new_icon);
                            break;
                        case 3:
                            mipmapList.add(z, R.mipmap.manjian);
                            break;
                        case 4:
                            mipmapList.add(z, R.mipmap.procket);
                            break;
                        default:
                            break;
                    }
                }
                ImageView state1, state2, state3, state4, state5, state6, state7, state8, state9, state10;
                int startState = 0;
                state1 = a.findViewById(R.id.state1);
                state1.setImageResource(mipmapList.get(startState++));
                state2 = a.findViewById(R.id.state2);
                state2.setImageResource(mipmapList.get(startState++));
                state3 = a.findViewById(R.id.state3);
                state3.setImageResource(mipmapList.get(startState++));
                state4 = a.findViewById(R.id.state4);
                state4.setImageResource(mipmapList.get(startState++));
                state5 = a.findViewById(R.id.state5);
                state5.setImageResource(mipmapList.get(startState++));
                state6 = a.findViewById(R.id.state6);
                state6.setImageResource(mipmapList.get(startState++));
                state7 = a.findViewById(R.id.state7);
                state7.setImageResource(mipmapList.get(startState++));
                state8 = a.findViewById(R.id.state8);
                state8.setImageResource(mipmapList.get(startState++));
                state9 = a.findViewById(R.id.state9);
                state9.setImageResource(mipmapList.get(startState++));
                state10 = a.findViewById(R.id.state10);
                state10.setImageResource(mipmapList.get(startState++));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void parsingViewFourJson(String t, ViewStub view, Context context) {
        View a = view.inflate();
        TextView tv = a.findViewById(R.id.topTitle);
        LinearGradient mLinearGradient = new LinearGradient(0, 0, tv.getPaint().getTextSize() * 1, 0, context.getColor(R.color.chunkTitle_blue_start), context.getColor(R.color.chunkTitle_purple_end), Shader.TileMode.MIRROR);
        tv.getPaint().setShader(mLinearGradient);
        try {
            JSONObject jsonObject = new JSONObject(t);
            int resultCode = jsonObject.getInt("resultCode");
            if (resultCode == 100) {
                JSONArray array = jsonObject.getJSONArray("data");
                chunkTitleList = new ArrayList<>();
                descriptionList = new ArrayList<>();
                imageUrlList = new ArrayList<>();
                String topTitle = "";
                String chunkTitle;
                String description;
                String imageUrl="";
                String date;
                int z = 0;
                for (int i = 0; i < array.length(); i++) {
                    JSONObject json = array.getJSONObject(i);
                    topTitle = json.getString("topTitle");
                    chunkTitle = json.getString("chunkTitle");
                    description = json.getString("description");
                    date = json.getString("date");
                    JSONArray url = json.getJSONArray("imageUrl");
                    for (int j = 0; j < url.length(); j++) {
                        imageUrl = url.get(j).toString();
                        if (!imageUrl.equals("null")) {
                            ACache.get(context).put("four" + z++, imageUrl);
                           // cacheUtil.addBitmapToDiskLruCache(imageUrl,diskLruCache);
                            //mImageCacheUtil.loadBitmap(imageUrl,100,100);
                        }
                    }
                    imageUrlList.add(imageUrl);
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
                img1.setTag("four" + startImage++);
                mImageCacheUtil.bindBitmap(imageUrlList.get(startImage++),img1,100,100);
                //img1.setImageBitmap(cacheUtil.getCache(ACache.get(context).getAsString("four" + startImage++), diskLruCache));
                img2 = a.findViewById(R.id.image2);
                img2.setTag("four" + startImage++);
                mImageCacheUtil.bindBitmap(imageUrlList.get(startImage++),img2,100,100);
                //img2.setImageBitmap(cacheUtil.getCache(ACache.get(context).getAsString("four" + startImage++), diskLruCache));
                img3 = a.findViewById(R.id.image3);
                img3.setTag("four" + startImage++);
                mImageCacheUtil.bindBitmap(imageUrlList.get(startImage++),img3,100,100);
                //img3.setImageBitmap(cacheUtil.getCache(ACache.get(context).getAsString("four" + startImage++), diskLruCache));
                img4 = a.findViewById(R.id.image4);
                img4.setTag("four" + startImage++);
                mImageCacheUtil.bindBitmap(imageUrlList.get(startImage++),img4,100,100);
                //img4.setImageBitmap(cacheUtil.getCache(ACache.get(context).getAsString("four" + startImage++), diskLruCache));
                img5 = a.findViewById(R.id.image5);
                img5.setTag("four" + startImage++);
                mImageCacheUtil.bindBitmap(imageUrlList.get(startImage++),img5,100,100);
                //img5.setImageBitmap(cacheUtil.getCache(ACache.get(context).getAsString("four" + startImage++), diskLruCache));
                img6 = a.findViewById(R.id.image6);
                img6.setTag("four" + startImage++);
                mImageCacheUtil.bindBitmap(imageUrlList.get(startImage++),img6,100,100);
                //img6.setImageBitmap(cacheUtil.getCache(ACache.get(context).getAsString("four" + startImage++), diskLruCache));
                img7 = a.findViewById(R.id.image7);
                img7.setTag("four" + startImage++);
                mImageCacheUtil.bindBitmap(imageUrlList.get(startImage++),img7,100,100);
                //img7.setImageBitmap(cacheUtil.getCache(ACache.get(context).getAsString("four" + startImage++), diskLruCache));
                img8 = a.findViewById(R.id.image8);
                img8.setTag("four" + startImage++);
                mImageCacheUtil.bindBitmap(imageUrlList.get(startImage++),img8,100,100);
                //img8.setImageBitmap(cacheUtil.getCache(ACache.get(context).getAsString("four" + startImage++), diskLruCache));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void parsingViewThreeJson(String t, ViewStub view, Context context) {
        View a = view.inflate();
        TextView tv = a.findViewById(R.id.topTitle);
        LinearGradient mLinearGradient = new LinearGradient(0, 0, tv.getPaint().getTextSize() * 1, 0, context.getColor(R.color.chunkTitle_green_start), context.getColor(R.color.chunkTitle_cyan_end), Shader.TileMode.MIRROR);
        tv.getPaint().setShader(mLinearGradient);
        try {
            JSONObject jsonObject = new JSONObject(t);
            int resultCode = jsonObject.getInt("resultCode");
            if (resultCode == 100) {
                JSONArray array = jsonObject.getJSONArray("data");
                chunkTitleList = new ArrayList<>();
                descriptionList = new ArrayList<>();
                imageUrlList = new ArrayList<>();
                String topTitle = "";
                String chunkTitle = "";
                String description = "";
                String imageUrl ="";
                String date;
                int z = 0;
                for (int i = 0; i < array.length(); i++) {
                    JSONObject json = array.getJSONObject(i);
                    topTitle = json.getString("topTitle");
                    chunkTitle = json.getString("chunkTitle");
                    description = json.getString("description");
                    date = json.getString("date");
                    JSONArray url = json.getJSONArray("imageUrl");
                    for (int j = 0; j < url.length(); j++) {
                        imageUrl = url.get(j).toString();
                        if (!imageUrl.equals("null")) {
                            ACache.get(context).put("three" + z++, imageUrl);
                            //cacheUtil.addBitmapToDiskLruCache(imageUrl,diskLruCache);
                            //mImageCacheUtil.loadBitmap(imageUrl,100,100);
                        }
                    }
                    imageUrlList.add(imageUrl);
                    chunkTitleList.add(chunkTitle);
                    descriptionList.add(description);
                }
                tv.setText(new HistoryUtil().getPointString(topTitle));
                TextView subTitle1, subTitle2, subTitle3, subTitle4, subTitle5, subTitle6;
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
                TextView des1, des2, des3, des4, des5, des6;
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
                img1.setTag("three" + startImage++);
                mImageCacheUtil.bindBitmap(imageUrlList.get(startImage++),img1,100,100);
               // img1.setImageBitmap(cacheUtil.getCache(ACache.get(context).getAsString("three" + startImage++), diskLruCache));
                img2 = a.findViewById(R.id.image2);
                img2.setTag("three" + startImage++);
                mImageCacheUtil.bindBitmap(imageUrlList.get(startImage++),img2,100,100);
               // img2.setImageBitmap(cacheUtil.getCache(ACache.get(context).getAsString("three" + startImage++), diskLruCache));
                img3 = a.findViewById(R.id.image3);
                img3.setTag("three" + startImage++);
                mImageCacheUtil.bindBitmap(imageUrlList.get(startImage++),img3,100,100);
               // img3.setImageBitmap(cacheUtil.getCache(ACache.get(context).getAsString("three" + startImage++), diskLruCache));
                img4 = a.findViewById(R.id.image4);
                img4.setTag("three" + startImage++);
                mImageCacheUtil.bindBitmap(imageUrlList.get(startImage++),img4,100,100);
                //img4.setImageBitmap(cacheUtil.getCache(ACache.get(context).getAsString("three" + startImage++), diskLruCache));
                img5 = a.findViewById(R.id.image5);
                img5.setTag("three" + startImage++);
                mImageCacheUtil.bindBitmap(imageUrlList.get(startImage++),img5,100,100);
                //img5.setImageBitmap(cacheUtil.getCache(ACache.get(context).getAsString("three" + startImage++), diskLruCache));
                img6 = a.findViewById(R.id.image6);
                img6.setTag("three" + startImage++);
                mImageCacheUtil.bindBitmap(imageUrlList.get(startImage++),img6,100,100);
               // img6.setImageBitmap(cacheUtil.getCache(ACache.get(context).getAsString("three" + startImage++), diskLruCache));
                img7 = a.findViewById(R.id.image7);
                img7.setTag("three" + startImage++);
                mImageCacheUtil.bindBitmap(imageUrlList.get(startImage++),img7,100,100);
                //img7.setImageBitmap(cacheUtil.getCache(ACache.get(context).getAsString("three" + startImage++), diskLruCache));
                img8 = a.findViewById(R.id.image8);
                img8.setTag("three" + startImage++);
                mImageCacheUtil.bindBitmap(imageUrlList.get(startImage++),img8,100,100);
                //img8.setImageBitmap(cacheUtil.getCache(ACache.get(context).getAsString("three" + startImage++), diskLruCache));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void parsingViewTwoJson(String t, ViewStub view, Context context) {
        View a = view.inflate();
        TextView tv = a.findViewById(R.id.topTitle);
        LinearGradient mLinearGradient = new LinearGradient(0, 0, tv.getPaint().getTextSize() * 1, 0, context.getColor(R.color.chunkTitle_orange_start), context.getColor(R.color.chunkTitle_red_end), Shader.TileMode.MIRROR);
        tv.getPaint().setShader(mLinearGradient);
        try {
            JSONObject jsonObject = new JSONObject(t);
            int resultCode = jsonObject.getInt("resultCode");
            if (resultCode == 100) {
                JSONArray array = jsonObject.getJSONArray("data");
                chunkTitleList = new ArrayList<>();
                descriptionList = new ArrayList<>();
                imageUrlList = new ArrayList<>();
                String topTitle = "";
                String chunkTitle = "";
                String description = "";
                String imageUrl="";
                String date;
                int z = 0;
                for (int i = 0; i < array.length(); i++) {
                    JSONObject json = array.getJSONObject(i);
                    topTitle = json.getString("topTitle");
                    chunkTitle = json.getString("chunkTitle");
                    description = json.getString("description");
                    date = json.getString("date");
                    JSONArray url = json.getJSONArray("imageUrl");
                    for (int j = 0; j < url.length(); j++) {
                        imageUrl = url.get(j).toString();
                        if (!imageUrl.equals("null")) {
                            ACache.get(context).put("two" + z++, imageUrl);
                           // cacheUtil.addBitmapToDiskLruCache(imageUrl,diskLruCache);
                            //mImageCacheUtil.loadBitmap(imageUrl,100,100);
                        }
                    }
                    imageUrlList.add(imageUrl);
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
                img1.setTag("two" + startImage++);
                mImageCacheUtil.bindBitmap(imageUrlList.get(startImage++),img1,100,100);
                //img1.setImageBitmap(cacheUtil.getCache(ACache.get(context).getAsString("two" + startImage++), diskLruCache));
                img2 = a.findViewById(R.id.image2);
                img2.setTag("two" + startImage++);
                mImageCacheUtil.bindBitmap(imageUrlList.get(startImage++),img2,100,100);
               // img2.setImageBitmap(cacheUtil.getCache(ACache.get(context).getAsString("two" + startImage++), diskLruCache));
                img3 = a.findViewById(R.id.image3);
                img3.setTag("two" + startImage++);
                mImageCacheUtil.bindBitmap(imageUrlList.get(startImage++),img3,100,100);
               // img3.setImageBitmap(cacheUtil.getCache(ACache.get(context).getAsString("two" + startImage++), diskLruCache));
                img4 = a.findViewById(R.id.image4);
                img4.setTag("two" + startImage++);
                mImageCacheUtil.bindBitmap(imageUrlList.get(startImage++),img4,100,100);
                //img4.setImageBitmap(cacheUtil.getCache(ACache.get(context).getAsString("two" + startImage++), diskLruCache));
                img5 = a.findViewById(R.id.image5);
                img5.setTag("two" + startImage++);
                mImageCacheUtil.bindBitmap(imageUrlList.get(startImage++),img5,100,100);
                //img5.setImageBitmap(cacheUtil.getCache(ACache.get(context).getAsString("two" + startImage++), diskLruCache));
                img6 = a.findViewById(R.id.image6);
                img6.setTag("two" + startImage++);
                mImageCacheUtil.bindBitmap(imageUrlList.get(startImage++),img6,100,100);
                //img6.setImageBitmap(cacheUtil.getCache(ACache.get(context).getAsString("two" + startImage++), diskLruCache));
                img7 = a.findViewById(R.id.image7);
                img7.setTag("two" + startImage++);
                mImageCacheUtil.bindBitmap(imageUrlList.get(startImage++),img7,100,100);
                //img7.setImageBitmap(cacheUtil.getCache(ACache.get(context).getAsString("two" + startImage++), diskLruCache));
                img8 = a.findViewById(R.id.image8);
                img8.setTag("two" + startImage++);
                mImageCacheUtil.bindBitmap(imageUrlList.get(startImage++),img8,100,100);
                //img8.setImageBitmap(cacheUtil.getCache(ACache.get(context).getAsString("two" + startImage++), diskLruCache));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void parsingViewOneJson(String t, ViewStub view, final Context context) {
        View a = view.inflate();
        TextView tv = a.findViewById(R.id.topTitle);
        LinearGradient mLinearGradient = new LinearGradient(0, 0, tv.getPaint().getTextSize() * 1, 0, context.getColor(R.color.chunkTitle_blue_start), context.getColor(R.color.chunkTitle_purple_end), Shader.TileMode.MIRROR);
        tv.getPaint().setShader(mLinearGradient);
        try {
            JSONObject jsonObject = new JSONObject(t);
            int resultCode = jsonObject.getInt("resultCode");
            if (resultCode == 100) {
                JSONArray array = jsonObject.getJSONArray("data");
                chunkTitleList = new ArrayList<>();
                descriptionList = new ArrayList<>();
                imageUrlList = new ArrayList<>();
                String topTitle = "";
                String chunkTitle;
                String description;
                String imageUrl = "";
                String date;
                int z = 0;
                for (int i = 0; i < array.length(); i++) {
                    JSONObject json = array.getJSONObject(i);
                    topTitle = json.getString("topTitle");
                    chunkTitle = json.getString("chunkTitle");
                    description = json.getString("description");
                    date = json.getString("date");
                    JSONArray url = json.getJSONArray("imageUrl");
                    for (int j = 0; j < url.length(); j++) {
                        imageUrl = url.get(j).toString();
                        if (!imageUrl.equals("null")) {
                            ACache.get(context).put("one" + z++, imageUrl);
                            //cacheUtil.addBitmapToDiskLruCache(imageUrl,diskLruCache);
                           // mImageCacheUtil.loadBitmap(imageUrl,100,100);
                        }
                    }
                    imageUrlList.add(imageUrl);
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
                img1.setTag("one" + startImage++);
                mImageCacheUtil.bindBitmap(imageUrlList.get(startImage++),img1,100,100);
                //img1.setImageBitmap(cacheUtil.getCache(ACache.get(context).getAsString("one" + startImage++), diskLruCache));
                img2 = a.findViewById(R.id.image2);
                img2.setTag("one" + startImage++);
                mImageCacheUtil.bindBitmap(imageUrlList.get(startImage++),img2,100,100);
                //img2.setImageBitmap(cacheUtil.getCache(ACache.get(context).getAsString("one" + startImage++), diskLruCache));
                img3 = a.findViewById(R.id.image3);
                img3.setTag("one" + startImage++);
                mImageCacheUtil.bindBitmap(imageUrlList.get(startImage++),img3,100,100);
                //img3.setImageBitmap(cacheUtil.getCache(ACache.get(context).getAsString("one" + startImage++), diskLruCache));
                img4 = a.findViewById(R.id.image4);
                img4.setTag("one" + startImage++);
                mImageCacheUtil.bindBitmap(imageUrlList.get(startImage++),img4,100,100);
                //img4.setImageBitmap(cacheUtil.getCache(ACache.get(context).getAsString("one" + startImage++), diskLruCache));
                img5 = a.findViewById(R.id.image5);
                img5.setTag("one" + startImage++);
                mImageCacheUtil.bindBitmap(imageUrlList.get(startImage++),img5,100,100);
                //img5.setImageBitmap(cacheUtil.getCache(ACache.get(context).getAsString("one" + startImage++), diskLruCache));
                img6 = a.findViewById(R.id.image6);
                img6.setTag("one" + startImage++);
                mImageCacheUtil.bindBitmap(imageUrlList.get(startImage++),img6,100,100);
                //img6.setImageBitmap(cacheUtil.getCache(ACache.get(context).getAsString("one" + startImage++), diskLruCache));
                img7 = a.findViewById(R.id.image7);
                img7.setTag("one" + startImage++);
                mImageCacheUtil.bindBitmap(imageUrlList.get(startImage++),img7,100,100);
                //img7.setImageBitmap(cacheUtil.getCache(ACache.get(context).getAsString("one" + startImage++), diskLruCache));
                img8 = a.findViewById(R.id.image8);
                img8.setTag("one" + startImage++);
                mImageCacheUtil.bindBitmap(imageUrlList.get(startImage++),img8,100,100);
                //img8.setImageBitmap(cacheUtil.getCache(ACache.get(context).getAsString("one" + startImage++), diskLruCache));
                img9 = a.findViewById(R.id.image9);
                img9.setTag("one" + startImage++);
                mImageCacheUtil.bindBitmap(imageUrlList.get(startImage++),img9,100,100);
                //img9.setImageBitmap(cacheUtil.getCache(ACache.get(context).getAsString("one" + startImage++), diskLruCache));
                img10 = a.findViewById(R.id.image10);
                img10.setTag("one" + startImage++);
                mImageCacheUtil.bindBitmap(imageUrlList.get(startImage++),img10,100,100);
                //img10.setImageBitmap(cacheUtil.getCache(ACache.get(context).getAsString("one" + startImage++), diskLruCache));
                img11 = a.findViewById(R.id.image11);
                img11.setTag("one" + startImage++);
                mImageCacheUtil.bindBitmap(imageUrlList.get(startImage++),img11,100,100);
                //img11.setImageBitmap(cacheUtil.getCache(ACache.get(context).getAsString("one" + startImage++), diskLruCache));
                img12 = a.findViewById(R.id.image12);
                img12.setTag("one" + startImage++);
                mImageCacheUtil.bindBitmap(imageUrlList.get(startImage++),img12,100,100);
                //img12.setImageBitmap(cacheUtil.getCache(ACache.get(context).getAsString("one" + startImage++), diskLruCache));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parsingTopIdJson(String t) {
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

    public void getPromotion(String url, final ViewStub view, final Context context) {
        RxVolley.get(url, new HttpCallback() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSuccess(String t) {
                Logger.json(t);
                ACache.get(context).put("loveJson", t);
                parsingViewLoveJson(t, view, context);
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onFailure(VolleyError error) {
                parsingViewLoveJson(ACache.get(context).getAsString("loveJson"), view, context);
            }
        });
    }
}
