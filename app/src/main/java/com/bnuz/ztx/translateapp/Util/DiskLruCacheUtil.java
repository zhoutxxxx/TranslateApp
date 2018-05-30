package com.bnuz.ztx.translateapp.Util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by ZTX on 2018/5/29.
 */

public class DiskLruCacheUtil {
    int appVersion = 1;//app的版本号（默认值）
    int valueCount = 1;//一个key对应的值（默认值）
    static final long cacheSize = 1024 * 1024 * 50;//缓存总大小 50MB
    Bitmap bitmap = null;
    boolean result = false;
    //存储设备缓存
    public DiskLruCache getDiskLruCache(Context context) {
        DiskLruCache diskLruCache = null;
        try {
            diskLruCache = DiskLruCache.open(getDiskCacheDir(context), appVersion, valueCount, cacheSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return diskLruCache;
    }

    public File getDiskCacheDir(Context context) {
        String cachePath;
        cachePath = context.getExternalCacheDir().getPath();
        return new File(cachePath + File.separator + "bitmap");
    }

    public int getAppVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public Bitmap getCacheBitmap(final String imageUrl, final DiskLruCache diskLruCache) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String key = hashKeyForDisk(imageUrl);// 把Url转换成Key
                try {
                    DiskLruCache.Snapshot snapShot = diskLruCache.get(key);// 通过key获取Snapshot对象
                    if (snapShot != null) {
                        InputStream is = snapShot.getInputStream(0);// 通过Snapshot对象获取缓存文件的输入流
                        bitmap = BitmapFactory.decodeStream(is);// 把输入流转换成Bitmap对象
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return bitmap;
    }

    public Bitmap getCache(final String imageUrl, final DiskLruCache diskLruCache) {
        try {
            String key = hashKeyForDisk(imageUrl);
            DiskLruCache.Snapshot snapshot = diskLruCache.get(key);
            if (snapshot != null) {
                InputStream in = snapshot.getInputStream(0);
                return BitmapFactory.decodeStream(in);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    //将URL转换成KEY，采用url的MD5值作为key
    public String hashKeyForDisk(String url) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(url.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(url.hashCode());
        }
        return cacheKey;
    }

    private String bytesToHexString(byte[] bytes) {
        // http://stackoverflow.com/questions/332079
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public boolean addBitmapToDiskLruCache(final String imageUrl, final DiskLruCache diskLruCache) {
        final String key = hashKeyForDisk(imageUrl); // 通过md5加密了这个URL，生成一个key
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DiskLruCache.Editor editor = diskLruCache.edit(key);// 产生一个editor对象
                    if (editor != null) {
                        // 创建一个新的输出流 ，创建DiskLruCache时设置一个节点只有一个数据，所以这里的index直接设为0即可
                        OutputStream outputStream = editor.newOutputStream(0);
                        // 通过地址获取图片数据写入到输出流
                        if (downloadUrlToStream(imageUrl,
                                outputStream)) {
                            // 写入成功，提交
                            editor.commit();
                            result = true;
                        } else {
                            // 写入失败，中止
                            editor.abort();
                            result = false;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return result;
    }

    public boolean downloadUrlToStream(String urlString,
                                       OutputStream outputStream) {
        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;
        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(),
                    8 * 1024);
            out = new BufferedOutputStream(outputStream, 8 * 1024);
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            return true;
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}
