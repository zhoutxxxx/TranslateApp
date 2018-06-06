package com.bnuz.ztx.translateapp.Util;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StatFs;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import com.bnuz.ztx.translateapp.R;
import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by ZTX on 2018/5/29.
 */

public class ImageCacheUtil {
    //Handler相关常量
    public static final int MESSAGE_POST_RESULT = 1;
    //线程池相关常量
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final long KEEP_ALIVE = 10L;

    private LruCache<String,Bitmap> mMemoryCache;
    private Context mContext;
    private DiskLruCache mDiskLruCache;
    private ImageResizer mImageResizer;
    private boolean mIsDiskLruCacheCreated = false;
    private int appVersion = 1;//app的版本号（默认值）
    private int valueCount = 1;//一个key对应的值（默认值）
    private static final int TAG_KEY_URI = R.id.img_tag;
    private static final long DISK_CACHE_SIZE = 1024 * 1024 * 50;//缓存总大小 50MB
    private static final int DISK_CACHE_INDEX = 0;
    private static final int IO_BUFFER_SIZE = 8 * 1024;

    //线程池
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r,"ImageCacheUtil#"+mCount.getAndIncrement());
        }
    };
    public static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(
            CORE_POOL_SIZE,MAXIMUM_POOL_SIZE,
            KEEP_ALIVE, TimeUnit.SECONDS,
            new LinkedBlockingDeque<Runnable>(),
            sThreadFactory
    );
    private Handler mMainHandler = new Handler((Looper.getMainLooper())){
        @Override
        public void handleMessage(Message msg) {
            LoaderResult result = (LoaderResult)msg.obj;
            ImageView imageView = result.imageView;
            String uri = (String)imageView.getTag();
            Log.d("TAG",uri);
            Log.d("TAG",result.uri);
            imageView.setImageBitmap(result.bitmap);
//            if(uri.equals(result.uri)){
//            }
//            else{
//                Log.w("TAG","set bitmap ,but url has changed,ignored");
//            }
        }
    };
/**
*关于内存缓存以及存储设备缓存的初始化
*/
    public ImageCacheUtil(Context context) {
        mContext = context.getApplicationContext();
        //内存缓存容量为该程序最大内存大小的1/8;
        int maxMemory = (int)(Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
            }
        };
        File diskCacheDir = getDiskCacheDir(mContext,"bitmap");
        if(!diskCacheDir.exists()){
            diskCacheDir.mkdirs();
        }
        //判断磁盘空间是否有足够空间创建磁盘缓存 ·
        if (getUsableSpace(diskCacheDir)>DISK_CACHE_SIZE){
            try{
                mDiskLruCache = DiskLruCache.open(diskCacheDir,appVersion, valueCount, DISK_CACHE_SIZE);
                mIsDiskLruCacheCreated = true;
            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }

    /*
    *内存缓存的相关操作
     */
    //把Bitmap加入到内存缓存中
    public void addBitmapToMemory(String key,Bitmap bitmap){
        if(getBitmapFromMemCache(key) == null){
            mMemoryCache.put(key,bitmap);
        }
    }
    //从内存缓存中获取Bitmap
    public Bitmap getBitmapFromMemCache(String key){
        return mMemoryCache.get(key);
    }
    //从内存缓存中删除Bitmap
    public void removeBitmapFromMemory(String key){
        mMemoryCache.remove(key);
    }
    /*
     *存储设备缓存相关操作
     */
    //获取缓存路径
    public File getDiskCacheDir(Context context,String uniqueName) {

        boolean externalStorageAvailable = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);
        final String cachePath;
        //当SD卡存在或者SD卡不可被移除的时候，就调用getExternalCacheDir()方法来获取缓存路径
        //否则就调用getCacheDir()方法来获取缓存路径。
        if(externalStorageAvailable){
             cachePath = context.getExternalCacheDir().getPath();
        }else{
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator +uniqueName);
    }

    /*
     *绑定资源
     */
    public void bindBitmap(final String uri,final ImageView imageView){
        bindBitmap(uri,imageView,0,0);
    }
    public void bindBitmap(final String uri,final ImageView imageView,final int reqWidth,final int reqHeight){
        imageView.setTag(TAG_KEY_URI,uri);
        Bitmap bitmap =loadBitmapFromMemCache(uri);
        if(bitmap!=null){
            imageView.setImageBitmap(bitmap);
            return;
        }
        Runnable loadBitmapTask = new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = loadBitmap(uri,reqWidth,reqHeight);
                if(bitmap!=null){
                    LoaderResult result = new LoaderResult(imageView,uri,bitmap);
                    mMainHandler.obtainMessage(MESSAGE_POST_RESULT,result).sendToTarget();
                }
            }
        };
        THREAD_POOL_EXECUTOR.execute(loadBitmapTask);
    }
   /*
    *从内存或磁盘或网络中加载图片
    */
   public Bitmap loadBitmap(String uri,int reqWidth,int reqHeight){
       Bitmap bitmap = loadBitmapFromMemCache(uri);
       if(bitmap != null){
           Log.d("TAG","loadBitmapFromMemCache,url:"+uri);
           return bitmap;
       }
       try{
           bitmap = loadBitmapFromDiskCache(uri,reqWidth,reqHeight);
           if(bitmap!=null){
               Log.d("TAG","loadBitmapFromDisk,url:"+uri);
               return bitmap;
           }
           bitmap = loadBitmapFromHttp(uri,reqWidth,reqHeight);
           Log.d("TAG","loadBitmapFromHttp,url:"+uri);
       }catch (IOException e){
           e.printStackTrace();
       }
       if(bitmap==null && !mIsDiskLruCacheCreated){
           Log.d("TAG","encounter error,DiskLruCache is not created");
           bitmap = downloadBitmapFromUrl(uri);
       }
       return bitmap;
   }
   //从内存中获取Bitmap
   private Bitmap loadBitmapFromMemCache(String url){
       final String key = hashKeyFormUrl(url);
       Bitmap bitmap = getBitmapFromMemCache(key);
       return bitmap;
   }
   //从磁盘中获取Bitmap
    private Bitmap loadBitmapFromDiskCache(String url,int reqWidth,int reqHeight)throws IOException{
       mImageResizer = new ImageResizer();
        if(Looper.myLooper() == Looper.getMainLooper()){
            Log.d("TAG","load bitmap from UI Thread, it's not recommended!");
        }
        if(mDiskLruCache==null){
            return  null;
        }
        Bitmap bitmap = null;
        String key = hashKeyFormUrl(url);
        DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
        if(snapshot!=null){
            FileInputStream fileInputStream = (FileInputStream)snapshot.getInputStream(DISK_CACHE_INDEX);
            FileDescriptor fileDescriptor = fileInputStream.getFD();
            bitmap = mImageResizer.decodeFormStream(fileDescriptor,reqWidth,reqHeight);
            if(bitmap!=null){
                addBitmapToMemory(key,bitmap);
            }
        }
        return bitmap;
    }
    private Bitmap loadBitmapFromHttp(String url,int reqWidth,int reqHeight)throws IOException{
       if(Looper.myLooper() == Looper.getMainLooper()){
           throw  new RuntimeException("Can not visit network from UI THREAD");
       }
       if(mDiskLruCache ==null){
           return null;
       }
       String key = hashKeyFormUrl(url);
       DiskLruCache.Editor editor = mDiskLruCache.edit(key);
       if(editor !=null){
           OutputStream outputStream = editor.newOutputStream(DISK_CACHE_INDEX);
           if(downloadUrlToStream(url,outputStream)){
               editor.commit();
           }
           else{
               editor.abort();
           }
           mDiskLruCache.flush();
       }
       return loadBitmapFromDiskCache(url,reqWidth,reqHeight);
    }


//    public Bitmap getCacheBitmap(final String imageUrl, final DiskLruCache diskLruCache) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String key = hashKeyForDisk(imageUrl);// 把Url转换成Key
//                try {
//                    DiskLruCache.Snapshot snapShot = diskLruCache.get(key);// 通过key获取Snapshot对象
//                    if (snapShot != null) {
//                        InputStream is = snapShot.getInputStream(0);// 通过Snapshot对象获取缓存文件的输入流
//                        bitmap = BitmapFactory.decodeStream(is);// 把输入流转换成Bitmap对象
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//        return bitmap;
//    }
//
//    public Bitmap getCache(final String imageUrl, final DiskLruCache diskLruCache) {
//        try {
//            String key = hashKeyForDisk(imageUrl);
//            DiskLruCache.Snapshot snapshot = diskLruCache.get(key);
//            if (snapshot != null) {
//                InputStream in = snapshot.getInputStream(0);
//                return BitmapFactory.decodeStream(in);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public boolean addBitmapToDiskLruCache(final String imageUrl, final DiskLruCache diskLruCache) {
//        final String key = hashKeyForDisk(imageUrl); // 通过md5加密了这个URL，生成一个key
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    DiskLruCache.Editor editor = diskLruCache.edit(key);// 产生一个editor对象
//                    if (editor != null) {
//                        // 创建一个新的输出流 ，创建DiskLruCache时设置一个节点只有一个数据，所以这里的index直接设为0即可
//                        OutputStream outputStream = editor.newOutputStream(0);
//                        // 通过地址获取图片数据写入到输出流
//                        if (downloadUrlToStream(imageUrl,
//                                outputStream)) {
//                            // 写入成功，提交
//                            editor.commit();
//                            result = true;
//                        } else {
//                            // 写入失败，中止
//                            editor.abort();
//                            result = false;
//                        }
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//        return result;
//    }

    public boolean downloadUrlToStream(String urlString,
                                       OutputStream outputStream) {
        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;
        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(),
                    IO_BUFFER_SIZE);
            out = new BufferedOutputStream(outputStream, IO_BUFFER_SIZE);
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            return true;
        } catch (final IOException e) {
            Log.e("TAG","downloadBitmap failed"+e);
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
    private  Bitmap downloadBitmapFromUrl(String urlString){
       Bitmap bitmap = null;
       HttpURLConnection urlConnection = null;
        BufferedInputStream in =null;
        try{
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection)url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(),IO_BUFFER_SIZE);
            bitmap = BitmapFactory.decodeStream(in);
        }catch (final IOException e){
            Log.d("TAG","Error in downloadBitmap:" + e);
        }finally {
            if(urlConnection !=null){
                urlConnection.disconnect();
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    //将URL转换成KEY，采用url的MD5值作为key
    private String hashKeyFormUrl(String url) {
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

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private long getUsableSpace(File path){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD){
            return path.getUsableSpace();
        }
        final StatFs stats = new StatFs(path.getPath());
        return (long)stats.getBlockSize() * (long)stats.getAvailableBlocks();
    }
    private static class LoaderResult{
       public ImageView imageView;
       public String uri;
       public Bitmap bitmap;
       public LoaderResult(ImageView imageView,String uri,Bitmap bitmap){
           this.imageView = imageView;
           this.uri = uri;
           this.bitmap = bitmap;
       }
    }
}
