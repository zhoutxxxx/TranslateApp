package com.bnuz.ztx.translateapp.Util;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;

/**
 * Created by ZTX on 2018/4/24.
 * 音频播放类
 */

public class MediaPlayerUtil {
    public void Paly(final Context context , final String url){
        //由于文件未知，且访问网络，使用线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                //获取已经存好的音频链接
                Uri uri = Uri.parse(url);
                //实例化一个音频播放器
                MediaPlayer mediaPlayer = new MediaPlayer();
                try {
                    //将数据放入播放器
                    mediaPlayer.setDataSource(context,uri);
                    //播放器准备
                    mediaPlayer.prepare();
                    //播放
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
