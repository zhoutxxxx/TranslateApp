package com.bnuz.ztx.translateapp.net;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.bnuz.ztx.translateapp.Fragment.ShoppingFragment;
import com.bnuz.ztx.translateapp.R;
import com.bnuz.ztx.translateapp.Ui.SettingActivity;
import com.bnuz.ztx.translateapp.Ui.TabLayoutViewPager_Activity;
import com.bnuz.ztx.translateapp.Ui.VideoActivity;
import com.bnuz.ztx.translateapp.Util.EventMessage;
import com.orhanobut.logger.Logger;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by ZTX on 2018/6/1.
 * 即时通讯类
 */

public class MyMQTT extends Service {
    //测试Log的Tag
    private static final String TAG = "MqttTest";
    //商城推送的订阅主题
    final String subscriptionTopic = "exampleAndroidTopic";
    //接受视频通话请求的主题
    final String translateTopic = "TranslateAnswer";
    //接受视频通话ICE的主题
    final String offerICETopic = "AnswerICE";
    private ScheduledExecutorService scheduler;
    private String userName = "guest"; // 连接的用户名
    private String passWord = "guest"; //连接的密码
    private String mDeviceId = "exampleAndroidTopic";       // Device ID, Secure.ANDROID_ID
    String url = "tcp://120.79.146.91:1883";
    //连接配置
    private MqttConnectOptions options;
    //上下文
    private Context context;
    //MQTT连接客户端
    private MqttAndroidClient mqttAndroidClient;


    //构造器
    public MyMQTT(Context context) {
        this.context = context;
    }


    /**
     * 初始化相关数据
     */
    public void init() {
        try {
            mqttAndroidClient = new MqttAndroidClient(context, url, mDeviceId);
            //MQTT的连接设置
            options = new MqttConnectOptions();
            //设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
            options.setCleanSession(true);
            //设置连接的用户名
            options.setUserName(userName);
            //设置连接的密码
            options.setPassword(passWord.toCharArray());
            // 设置超时时间 单位为秒
            options.setConnectionTimeout(10);
            // 设置会话心跳时间 单位为秒 服务器会每隔1.5*60秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
            options.setKeepAliveInterval(60);
            //断线重新连接
            options.setAutomaticReconnect(true);
            //客户端连接
            mqttAndroidClient.connect(options);
            //监听事件
            mqttAndroidClient.setCallback(new MqttCallbackExtended() {
                @Override
                public void connectComplete(boolean reconnect, String serverURI) {
                    subscribeToTopic();
                    Logger.d("正在订阅主题");
                }

                @Override
                public void connectionLost(Throwable cause) {
                    //连接丢失后，一般在这里面进行重连
                    if (isNetworkAvailable()) {
                        reconnectIfNecessary();
                    }
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    //subscribe后得到的消息会执行到这里面
                    if (topic.equals(subscriptionTopic)){
                        Log.e(TAG, "message=:" + message.toString());
                        startNotification(message);
                    }else{
                        EventBus.getDefault().post(new EventMessage(true,topic,message.toString()));
                    }

                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    //publish后会执行到这里
                    long messageId = token.getMessageId();
                    Log.e(TAG, "messageId=:" + messageId);

                }

            });
            Logger.d("链接成功，等待消息中。。。。。");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //接收到消息后显示状态栏
    private void startNotification(MqttMessage message) {
        //实例化一个状态栏管理器
        NotificationManager notificationManager =(NotificationManager) context.getSystemService(
                Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(this.context,this.context.getClass());
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        //点击跳转的Intent
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(context)
                .setSmallIcon(R.mipmap.start)//推送图标
                .setContentTitle("翻译app")//推送主题
                .setContentIntent(pendingIntent)//跳转选项
                .setContentText(message.toString())//推送内容
                .setAutoCancel(true)//点击后消失
                .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                .build();
        notificationManager.notify(1, notification);
    }

    public void subscribeToTopic() {
        try {
            //默认订阅商城主题
            mqttAndroidClient.subscribe(subscriptionTopic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.e(TAG, "onSuccess ---> " + asyncActionToken);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.e(TAG, "onFailure ---> " + exception);
                }
            });
            //订阅接受视频请求
            mqttAndroidClient.subscribe(translateTopic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.e(TAG, "onSuccess ---> " + asyncActionToken);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.e(TAG, "onFailure ---> " + asyncActionToken);
                }
            });
            //订阅 ICE交换请求
            mqttAndroidClient.subscribe(offerICETopic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.e(TAG, "onSuccess ---> " + asyncActionToken);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.e(TAG, "onFailure ---> " + asyncActionToken);
                }
            });
        } catch (MqttException e) {
            Log.e(TAG, "subscribeToTopic is error");
            e.printStackTrace();
        }
    }


    //发布消息
    public void publish(String topic,String msg,boolean isRetained,int qos) {
        /*
        topic 发送的主题
        msg 发送的消息
        isRetained 是否保存消息
        qos 消息质量
         */
        try {
            if (mqttAndroidClient!=null) {
                MqttMessage message = new MqttMessage();
                message.setQos(qos);
                message.setRetained(isRetained);
                message.setPayload(msg.getBytes());
                mqttAndroidClient.publish(topic, message);
                Logger.d("topic="+topic+"--msg="+msg+"--isRetained"+isRetained);
            }
        } catch (MqttPersistenceException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
    /**
     * Query's the NetworkInfo via ConnectivityManager
     * to return the current connected state
     * 通过ConnectivityManager查询网络连接状态
     *
     * @return boolean true if we are connected false otherwise
     * 如果网络状态正常则返回true反之flase
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info =mConnectivityManager.getActiveNetworkInfo();
        return (info == null) ? false : info.isConnected();
    }

    /**
     * Checkes the current connectivity
     * and reconnects if it is required.
     * 重新连接如果他是必须的
     */
    public synchronized void reconnectIfNecessary() {
        if (mqttAndroidClient == null || !mqttAndroidClient.isConnected()) {
            connect();
        }
    }

    private void connect() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    mqttAndroidClient.connect(options);
                    Logger.d("成功链接服务端~~~~~");
                    // 连接成功之后，处理相关逻辑
                } catch (Exception e) {
                    e.printStackTrace();
                    // 连接出错，可以设置重新连接
                }
            }
        }).start();
    }

    /**
     * 调用init() 方法之后，调用此方法。
     */
    public void startReconnect() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                if (!mqttAndroidClient.isConnected() && isNetworkAvailable()) {
                    connect();
                }
            }
        }, 0 * 1000, 10 * 1000, TimeUnit.MILLISECONDS);
    }

    /**
     * Receiver that listens for connectivity chanes
     * via ConnectivityManager
     * 网络状态发生变化接收器
     */
    private final BroadcastReceiver mConnectivityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("BroadcastReceiver", "Connectivity Changed...");
            if (!isNetworkAvailable()) {
                Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show();
                scheduler.shutdownNow();
            } else {
                startReconnect();
            }
        }
    };


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
