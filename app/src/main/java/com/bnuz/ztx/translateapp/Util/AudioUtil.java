package com.bnuz.ztx.translateapp.Util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;

import com.orhanobut.logger.Logger;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import Decoder.BASE64Encoder;


public class AudioUtil {

    private static AudioUtil mInstance;
    private AudioRecord recorder;
    //录音源
    private static int audioSource = MediaRecorder.AudioSource.MIC;
    //录音的采样频率
    private static int audioRate = 16000;
    //录音的声道，单声道
    private static int audioChannel = AudioFormat.CHANNEL_IN_MONO;
    //量化的深度
    private static int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    //缓存的大小
    private static int bufferSize = AudioRecord.getMinBufferSize(audioRate, audioChannel, audioFormat);
    //记录播放状态
    private boolean isRecording = false;
    //数字信号数组
    public byte[] noteArray;
    //PCM文件
    public File pcmFile;
    //WAV文件
    public File wavFile;
    //文件输出流
    public OutputStream os;
    //文件根目录
    public String basePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/translateVoice/";
    //wav文件目录
    public String outFileName = basePath + "/voice.wav";
    //pcm文件目录
    public String inFileName = basePath + "/voice.pcm";
    public Context context;

    public String getInFileName() {
        return inFileName;
    }

    public void setInFileName(String inFileName) {
        this.inFileName = inFileName;
    }

    public String getOutFileName() {
        return outFileName;
    }

    public void setOutFileName(String outFileName) {
        this.outFileName = outFileName;
    }

    public AudioRecord getRecorder() {
        return recorder;
    }

    public void setRecorder(AudioRecord recorder) {
        this.recorder = recorder;
    }


    public AudioUtil(Context context) {
        this.context = context;
        createFile();
        recorder = new AudioRecord(audioSource, audioRate, audioChannel, audioFormat, bufferSize);
    }

    public synchronized AudioUtil getInstance() {
        if (mInstance == null) {
            mInstance = new AudioUtil(context);
        }
        return mInstance;
    }

    //读取录音数字数据线程
    class WriteThread implements Runnable {
        public void run() {
            writeData();
        }
    }

    //开始录音
    public void startRecord() {
        isRecording = true;
        recorder.startRecording();
    }

    //停止录音
    public void stopRecord() {
        isRecording = false;
        recorder.stop();
        pcmToWav(inFileName, outFileName,audioRate,1,16);
    }

    //将数据写入文件夹,文件的写入没有做优化
    public void writeData() {
        noteArray = new byte[bufferSize];
        //建立文件输出流
        try {
            os = new BufferedOutputStream(new FileOutputStream(pcmFile));
        } catch (IOException e) {

        }
        while (isRecording == true) {
            int recordSize = recorder.read(noteArray, 0, bufferSize);
            if (recordSize > 0) {
                try {
                    os.write(noteArray);
                } catch (IOException e) {

                }
            }
        }
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {

            }
        }
    }

    // 这里得到可播放的音频文件
    public void convertWaveFile() {
        FileInputStream in = null;
        FileOutputStream out = null;
        long totalAudioLen = 0;
        long totalDataLen = totalAudioLen + 36;
        long longSampleRate = AudioUtil.audioRate;
        int channels = 1;
        long byteRate = 16 * AudioUtil.audioRate * channels / 8;
        byte[] data = new byte[bufferSize];
        try {
            in = new FileInputStream(inFileName);
            out = new FileOutputStream(outFileName);
            totalAudioLen = in.getChannel().size();
            //由于不包括RIFF和WAV
            totalDataLen = totalAudioLen + 36;
            WriteWaveFileHeader(out, totalAudioLen, totalDataLen, longSampleRate, channels, byteRate);
            while (in.read(data) != -1) {
                out.write(data);
            }
            in.close();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* 任何一种文件在头部添加相应的头文件才能够确定的表示这种文件的格式，wave是RIFF文件结构，每一部分为一个chunk，其中有RIFF WAVE chunk， FMT Chunk，Fact chunk,Data chunk,其中Fact chunk是可以选择的， */
    public void WriteWaveFileHeader(FileOutputStream out, long totalAudioLen, long totalDataLen, long longSampleRate,
                                    int channels, long byteRate) throws IOException {
        byte[] header = new byte[44];
        header[0] = 'R'; // RIFF
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte) (totalDataLen & 0xff);//数据大小
        header[5] = (byte) ((totalDataLen >> 8) & 0xff);
        header[6] = (byte) ((totalDataLen >> 16) & 0xff);
        header[7] = (byte) ((totalDataLen >> 24) & 0xff);
        header[8] = 'W';//WAVE
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        //FMT Chunk
        header[12] = 'f'; // 'fmt '
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';//过渡字节
        //数据大小
        header[16] = 16; // 4 bytes: size of 'fmt ' chunk
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        //编码方式 10H为PCM编码格式
        header[20] = 1; // format = 1
        header[21] = 0;
        //通道数
        header[22] = (byte) channels;
        header[23] = 0;
        //采样率，每个通道的播放速度
        header[24] = (byte) (longSampleRate & 0xff);
        header[25] = (byte) ((longSampleRate >> 8) & 0xff);
        header[26] = (byte) ((longSampleRate >> 16) & 0xff);
        header[27] = (byte) ((longSampleRate >> 24) & 0xff);
        //音频数据传送速率,采样率*通道数*采样深度/8
        header[28] = (byte) (byteRate & 0xff);
        header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[30] = (byte) ((byteRate >> 16) & 0xff);
        header[31] = (byte) ((byteRate >> 24) & 0xff);
        // 确定系统一次要处理多少个这样字节的数据，确定缓冲区，通道数*采样位数
        header[32] = (byte) (1 * 16 / 8);
        header[33] = 0;
        //每个样本的数据位数
        header[34] = 16;
        header[35] = 0;
        //Data chunk
        header[36] = 'd';//data
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte) (totalAudioLen & 0xff);
        header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
        header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
        header[43] = (byte) ((totalAudioLen >> 24) & 0xff);
        out.write(header, 0, 44);
    }

    //创建文件夹,首先创建目录，然后创建对应的文件
    public void createFile() {
//        File baseFile = new File(basePath);
        File baseFile = new File(context.getExternalCacheDir(), "translateVoice");
        if (!baseFile.exists()) {
            Logger.d("文件夹创建成功" + baseFile.getAbsolutePath());
            baseFile.mkdirs();
        }
        pcmFile = new File(context.getExternalCacheDir() + "/voice.pcm");
        wavFile = new File(context.getExternalCacheDir() + "/voice.wav");
        setOutFileName(context.getExternalCacheDir() + "/voice.wav");
        setInFileName(context.getExternalCacheDir() + "/voice.pcm");
        if (!pcmFile.exists()) {
            pcmFile.delete();
        }
        if (!wavFile.exists()) {
            wavFile.delete();
        }
        try {
            pcmFile.createNewFile();
            wavFile.createNewFile();
        } catch (IOException e) {
            Logger.e(e.getMessage());
        }
    }

    //记录数据
    public void recordData() {
        new Thread(new WriteThread()).start();
    }

    //将pcm文件转换成wav文件
    public void pcmToWav(String inPcmFilePath, String outWavFilePath, int sampleRate, int channels, int bitNum) {

        FileInputStream in = null;
        FileOutputStream out = null;
        byte[] data = new byte[1024];

        try {
            //采样字节byte率
            long byteRate = sampleRate * channels * bitNum / 8;
            in = new FileInputStream(inPcmFilePath);
            out = new FileOutputStream(outWavFilePath);
            //PCM文件大小
            long totalAudioLen = in.getChannel().size();
            //总大小，由于不包括RIFF和WAV，所以是44 - 8 = 36，在加上PCM文件大小
            long totalDataLen = totalAudioLen + 36;
            WriteWaveFileHeader(out, totalAudioLen, totalDataLen, sampleRate, channels, byteRate);
            int length = 0;
            while ((length = in.read(data)) > 0) {
                out.write(data, 0, length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //将音频文件转化为字节数组字符串，并对其进行Base64编码处理
    public String getVoiceStr(String voiceFile) {
        InputStream in = null;
        byte[] data = null;
        //读取音频字节数组
        try {
            in = new FileInputStream(voiceFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //对字节数组Base64编码
        return new BASE64Encoder().encode(data);//返回Base64编码过的字节数组字符串
    }
}
