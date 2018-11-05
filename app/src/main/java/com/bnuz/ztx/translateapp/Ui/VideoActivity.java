package com.bnuz.ztx.translateapp.Ui;


import android.Manifest;
import android.bluetooth.BluetoothHeadset;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bnuz.ztx.translateapp.R;
import com.bnuz.ztx.translateapp.Util.EventMessage;
import com.bnuz.ztx.translateapp.Util.FontManager;
import com.bnuz.ztx.translateapp.Util.HeadSetReceiver;
import com.bnuz.ztx.translateapp.net.MyMQTT;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.AudioSource;
import org.webrtc.AudioTrack;
import org.webrtc.DataChannel;
import org.webrtc.IceCandidate;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoCapturerAndroid;
import org.webrtc.VideoRenderer;
import org.webrtc.VideoRendererGui;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class VideoActivity extends AppCompatActivity implements View.OnClickListener {
    //渲染界面
    GLSurfaceView myGLSurfaceView;
    //视频连接
    PeerConnection pc;
    final SDPObserve sdpObserver = new SDPObserve();
    final PCObserve pcObserve = new PCObserve();
    MyMQTT myMQTT;
    PeerConnectionFactory pcFactory;
    IceCandidate remoteIceCandidate;
    AudioManager audioManager;
    VideoRenderer remoteVideoRenderer;
    MediaConstraints sdpVideoConstraints;
    MediaConstraints pcConstraints;
    VideoCapturer videoCapturer;
    VideoCapturer videoCapturerBack;
    VideoRenderer localRenderer;
    VideoSource videoSource;
    HeadSetReceiver myReceiver;
    String CameraName;
    VideoTrack videoTrack;
    AudioTrack audioTrack;
    AudioSource audioSource;
    MediaStream mediaStream;
    List<PeerConnection.IceServer> iceServers;
    boolean isBack = false;

    TextView cameraChange, waitting, endLabel;
    Button phoneEnd;
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        myGLSurfaceView = findViewById(R.id.myView);
        EventBus.getDefault().register(this);
        init();
        // 初始化PeerConnectionFactory类（核心类）
        PeerConnectionFactory.initializeAndroidGlobals(getApplicationContext(), true, true, true, true);
        pcFactory = new PeerConnectionFactory();
        //获取摄像头设备的数量和名字等属性
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
            }
        }
        CameraName = VideoCapturerAndroid.getNameOfFrontFacingDevice();
        videoCapturer = VideoCapturerAndroid.create(CameraName);
        //对界面进行一个属性设置
        sdpVideoConstraints = new MediaConstraints();
        //视频源
        videoSource = pcFactory.createVideoSource(videoCapturer, sdpVideoConstraints);
        //将视频转换成stream
        videoTrack = pcFactory.createVideoTrack("VIDEO", videoSource);
        //音频源
        audioSource = pcFactory.createAudioSource(new MediaConstraints());
        //将音频转换成stream
        audioTrack = pcFactory.createAudioTrack("AUDIO", audioSource);
        //自己界面的实例化
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.d("zxaaa", "界面初始化完毕");
            }
        };
        VideoRendererGui.setView(myGLSurfaceView, runnable);
        try {
            VideoRendererGui.ScalingType scalingType = VideoRendererGui.ScalingType.SCALE_ASPECT_FILL;
            remoteVideoRenderer = VideoRendererGui.createGui(0, 0, 100, 100, scalingType, false);
            localRenderer = VideoRendererGui.createGui(4, 2, 30, 26, VideoRendererGui.ScalingType.SCALE_ASPECT_BALANCED, false);
            videoTrack.addRenderer(localRenderer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mediaStream = pcFactory.createLocalMediaStream("ZTX");
        mediaStream.addTrack(videoTrack);
        mediaStream.addTrack(audioTrack);
        iceServers = getIceServers();
        pcConstraints = new MediaConstraints();
        pcConstraints.optional.add(new MediaConstraints.KeyValuePair("DtlsSrtpKeyAgreement", "true"));
        pcConstraints.mandatory.add(new MediaConstraints.KeyValuePair("VoiceActivityDetection", "false"));
        //接受远程音频
        pcConstraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"));
        //接受远程视频
        pcConstraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveVideo", "true"));
        pc = pcFactory.createPeerConnection(iceServers, pcConstraints, pcObserve);
        pc.addStream(mediaStream);
        pc.createOffer(sdpObserver, sdpVideoConstraints);
    }


    //初始化
    private void init() {
        //初始化信令传输工具MQTT，方便后面调用
        myMQTT = new MyMQTT(getApplicationContext());
        myMQTT.init();

        //耳机监听
        myReceiver = new HeadSetReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_HEADSET_PLUG);
        intentFilter.addAction(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED);
        registerReceiver(myReceiver, intentFilter);

        //打开扬声器或者耳机
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioManager.isWiredHeadsetOn()) {
            audioManager.setBluetoothScoOn(true);
        } else {
            audioManager.setSpeakerphoneOn(true);
        }

        cameraChange = findViewById(R.id.camera_change);
        cameraChange.setTypeface(new FontManager().getALiType(getApplicationContext()));
        cameraChange.setOnClickListener(this);
        waitting = findViewById(R.id.waiting_tv);
        endLabel = findViewById(R.id.phone_end_label);
        phoneEnd = findViewById(R.id.phone_end);
        phoneEnd.setTypeface(new FontManager().getALiType(getApplicationContext()));
        phoneEnd.setOnClickListener(this);
        frameLayout = findViewById(R.id.myFrame);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.camera_change:
                pc.removeStream(mediaStream);
                mediaStream.removeTrack(videoTrack);
                videoSource.stop();
                videoCapturer.dispose();
                if (isBack) {
                    CameraName = VideoCapturerAndroid.getNameOfFrontFacingDevice();
                    isBack = false;
                } else {
                    CameraName = VideoCapturerAndroid.getNameOfBackFacingDevice();
                    isBack = true;
                }
                videoCapturer = VideoCapturerAndroid.create(CameraName);
                sdpVideoConstraints = new MediaConstraints();
                videoSource = pcFactory.createVideoSource(videoCapturer, sdpVideoConstraints);
                videoSource.restart();
                videoTrack = pcFactory.createVideoTrack("VIDEO", videoSource);
                videoTrack.addRenderer(localRenderer);
                mediaStream.addTrack(videoTrack);
                pc.addStream(mediaStream);
                Logger.d("this isBack is ---->" + isBack);
                break;
            case R.id.phone_end:
//                myMQTT.publish("offerEND", "end", false, 0);
//                videoCapturer.dispose();
//                videoSource.stop();
//                videoTrack.dispose();
//                pc.close();
                onDestroy();
                Toast.makeText(getApplicationContext(), "通话结束", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    class PCObserve implements PeerConnection.Observer {

        @Override
        public void onSignalingChange(PeerConnection.SignalingState signalingState) {

        }

        @Override
        public void onIceConnectionChange(PeerConnection.IceConnectionState iceConnectionState) {

        }

        @Override
        public void onIceConnectionReceivingChange(boolean b) {

        }

        @Override
        public void onIceGatheringChange(PeerConnection.IceGatheringState iceGatheringState) {

        }

        @Override
        public void onIceCandidate(final IceCandidate iceCandidate) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String topic = "OfferICE";
                    JSONObject json = new JSONObject();
                    try {
                        json.put("sdpMid", iceCandidate.sdpMid.toString());
                        json.put("sdp", iceCandidate.sdp);
                        json.put("sdpLineIndex", iceCandidate.sdpMLineIndex);
                        String ice = json.toString();
                        myMQTT.publish(topic, ice, false, 0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public void onAddStream(final MediaStream mediaStream) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (pc == null) {
                        Log.e("onAddStream", "pc == null");
                        return;
                    }
                    if (mediaStream.videoTracks.size() > 1 || mediaStream.audioTracks.size() > 1) {
                        Log.e("onAddStream", "size > 1");
                        return;
                    }
                    if (mediaStream.videoTracks.size() == 1) {
                        waitting.setVisibility(View.GONE);
                        phoneEnd.setVisibility(View.INVISIBLE);
                        cameraChange.setVisibility(View.INVISIBLE);
                        endLabel.setVisibility(View.INVISIBLE);
                        VideoTrack videoTrack = mediaStream.videoTracks.get(0);
                        videoTrack.addRenderer(remoteVideoRenderer);
                    }
                    if (mediaStream.videoTracks.size() == 0) {
                        Logger.e("没有流媒体，请检查！");
                    }
                }
            });
        }

        @Override
        public void onRemoveStream(final MediaStream mediaStream) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mediaStream.videoTracks.get(0).dispose();
                }
            });
        }

        @Override
        public void onDataChannel(DataChannel dataChannel) {

        }

        @Override
        public void onRenegotiationNeeded() {

        }
    }

    class SDPObserve implements SdpObserver {


        @Override
        public void onCreateSuccess(final SessionDescription sessionDescription) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String topic = "TranslateOffer";
                    JSONObject json = new JSONObject();
                    try {
                        json.put("type", sessionDescription.type.canonicalForm().toString());
                        json.put("description", sessionDescription.description.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String information = json.toString();
                    myMQTT.publish(topic, information, false, 0);
                    pc.setLocalDescription(sdpObserver, sessionDescription);
                }
            });
        }

        @Override
        public void onSetSuccess() {
            Logger.e("set success!!");
        }

        @Override
        public void onCreateFailure(String s) {
            Logger.e("offer create fail  ----->" + s);
        }

        @Override
        public void onSetFailure(String s) {
            Logger.e("offer set fail  ----->" + s);
        }
    }

    private void drainRemoteCandidates() {
        if (remoteIceCandidate == null) {
            Log.e("SDPObserver", "remoteIceCandidate == null");
            return;
        }
        pc.addIceCandidate(remoteIceCandidate);
        Log.e("IceCanditate", "添加IceCandidate成功");
        remoteIceCandidate = null;
    }

    private void processExtraData(String sdpMessage) {
        if (sdpMessage != null) {
            String s = sdpMessage;
            try {
                JSONObject jsonObject = new JSONObject(s);
                String type = jsonObject.getString("type");
                Logger.d("This Type is ---->" + type);
                String description = jsonObject.get("description").toString();
                Logger.d("This Description is ------>" + description);
                SessionDescription.Type sdpType = SessionDescription.Type.fromCanonicalForm(type);
                SessionDescription sdp = new SessionDescription(sdpType, description);
                pc.setRemoteDescription(sdpObserver, sdp);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private List<PeerConnection.IceServer> getIceServers() {
        LinkedList<PeerConnection.IceServer> iceServers = new LinkedList<PeerConnection.IceServer>();
        iceServers.add(new PeerConnection.IceServer("stun:s1.voipstation.jp:3478"));
        iceServers.add(new PeerConnection.IceServer("stun:stun.l.google.com:19302"));
        iceServers.add(new PeerConnection.IceServer("stun:stun.xten.com:3478"));
        iceServers.add(new PeerConnection.IceServer("stun:stun.voipbuster.com:3478"));
        iceServers.add(new PeerConnection.IceServer("stun:stun.voxgratia.org:3478"));
        iceServers.add(new PeerConnection.IceServer("stun:stun.sipgate.net:10000"));
        iceServers.add(new PeerConnection.IceServer("stun:stun.ekiga.net:3478"));
        iceServers.add(new PeerConnection.IceServer("stun:stun.ideasip.com:3478"));
        iceServers.add(new PeerConnection.IceServer("stun:stun.schlund.de:3478"));
        iceServers.add(new PeerConnection.IceServer("stun:stun.voiparound.com:3478"));
        iceServers.add(new PeerConnection.IceServer("stun:stun.voipbuster.com:3478"));
        iceServers.add(new PeerConnection.IceServer("stun:stun.voipstunt.com:3478"));
        iceServers.add(new PeerConnection.IceServer("stun:numb.viagenie.ca:3478"));
        iceServers.add(new PeerConnection.IceServer("stun:stun.counterpath.com:3478"));
        iceServers.add(new PeerConnection.IceServer("stun:stun.1und1.de:3478"));
        iceServers.add(new PeerConnection.IceServer("stun:stun.gmx.net:3478"));
        iceServers.add(new PeerConnection.IceServer("stun:stun.bcs2005.net:3478"));
        iceServers.add(new PeerConnection.IceServer("stun:stun.callwithus.com:3478"));
        iceServers.add(new PeerConnection.IceServer("stun:stun.counterpath.net:3478"));
        iceServers.add(new PeerConnection.IceServer("stun:stun.internetcalls.com:3478"));
        iceServers.add(new PeerConnection.IceServer("stun:stun.voip.aebc.com:3478"));
        return iceServers;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventMessage messageEvent) {
        if (messageEvent.getTopic().equals("AnswerICE")) {
            processExtraDataICE(messageEvent.getMessage());
        } else if (messageEvent.getTopic().equals("TranslateAnswer")) {
            if (pc.getRemoteDescription() == null) {
                processExtraData(messageEvent.getMessage());
            }
        }
    }

    private void processExtraDataICE(String message) {
        try {
            JSONObject jsonObject = new JSONObject(message);
            String mid = jsonObject.getString("sdpMid");
            String sdp = jsonObject.getString("sdp");
            int sdpLineIndex = jsonObject.getInt("sdpLineIndex");
            Logger.d("Mid is ------->" + mid);
            Logger.d("SDP is ------->" + sdp);
            Logger.d("SDPLineIndex is ------->" + sdpLineIndex);
            IceCandidate iceCandidate = new IceCandidate(mid, sdpLineIndex, sdp);
            remoteIceCandidate = iceCandidate;
            drainRemoteCandidates();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //释放资源
        videoCapturer.dispose();
        videoSource.stop();
        if (pc != null) {
            pc.dispose();
            pc = null;
        }
        audioManager.setSpeakerphoneOn(false);

        if (myReceiver != null) {
            unregisterReceiver(myReceiver);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                phoneEnd.setVisibility(View.VISIBLE);
                cameraChange.setVisibility(View.VISIBLE);
                endLabel.setVisibility(View.VISIBLE);
                phoneEnd.bringToFront();
                cameraChange.bringToFront();
                endLabel.bringToFront();
                break;
            case MotionEvent.ACTION_UP:
                startTime();
                break;
        }
        return super.onTouchEvent(event);
    }

    private void startTime() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        phoneEnd.setVisibility(View.INVISIBLE);
                        cameraChange.setVisibility(View.INVISIBLE);
                        endLabel.setVisibility(View.INVISIBLE);
                    }
                });
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask, 2000);
    }
}
