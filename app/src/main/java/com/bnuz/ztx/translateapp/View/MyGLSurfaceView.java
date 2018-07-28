package com.bnuz.ztx.translateapp.View;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by ZTX on 2018/7/15.
 */

public class MyGLSurfaceView extends GLSurfaceView {
    public MyGLSurfaceView(Context context) {
        super(context);
    }

    class MyRenderer implements GLSurfaceView.Renderer {

        public void onDrawFrame(GL10 unused) {
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        }

        @Override
        public void onSurfaceCreated(GL10 gl, javax.microedition.khronos.egl.EGLConfig eglConfig) {
            Log.i("ztx", "onSurfaceCreated...");
            //关闭抗抖动
            gl.glDisable(GL10.GL_DITHER);
            //设置系统对透视进行修正
            gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,
                    GL10.GL_FASTEST);
            gl.glClearColor(0, 0, 0, 0);
            //设置阴影平滑模式
            gl.glShadeModel(GL10.GL_SMOOTH);
            //启动深度测试
            gl.glEnable(GL10.GL_DEPTH_TEST);
            //设置深度测试的类型
            gl.glDepthFunc(GL10.GL_LEQUAL);
        }

        public void onSurfaceChanged(GL10 gl, int width, int height) {
            //设置3D视窗的大小及位置
            gl.glViewport(0, 0, 1000, 500);
            //将当前矩阵模式设为投影矩形
            gl.glMatrixMode(GL10.GL_PROJECTION);
            //初始化单位矩阵
            gl.glLoadIdentity();
            //计算透视窗口的宽度高度比
            float ratio = (float) 1000 / 500;
            //调用此方法设置透视窗口的空间大小
            gl.glFrustumf(-ratio,
                    ratio, -1, 1, 1, 10);
        }
    }
}
