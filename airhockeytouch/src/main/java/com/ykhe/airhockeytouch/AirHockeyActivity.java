package com.ykhe.airhockeytouch;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AirHockeyActivity extends AppCompatActivity {

    private GLSurfaceView glSurfaceView;
    private boolean rendererSet = false;
    private AirHockeyRenderer airHockeyRenderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        glSurfaceView = new GLSurfaceView(this);
        boolean supportsEs2 = checkSupportEs2Version();
        if (supportsEs2){
            glSurfaceView.setEGLContextClientVersion(2);
            airHockeyRenderer = new AirHockeyRenderer(this);
            glSurfaceView.setRenderer(airHockeyRenderer);
            rendererSet = true;
        }else {
            Toast.makeText(this,"not support OpenGL ES 2.0.",Toast.LENGTH_LONG)
                    .show();
            return;
        }
        initTouchListener();
        setContentView(glSurfaceView);
        
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initTouchListener() {
        glSurfaceView.setOnTouchListener((view, event) -> {
            if (event !=null){
                //将触控坐标转换为归一化设备坐标(y轴反转),并把每个坐标按比例映射到范围[-1,1]内
                final float normalizedX =
                        (event.getX()/(float) view.getWidth()) *2 -1;
                final float normalizedY =
                        -((event.getY()/(float) view.getHeight()) *2 -1);
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    glSurfaceView.queueEvent(new Runnable() {
                        @Override
                        public void run() {
                            //按压事件
                            airHockeyRenderer.handleTouchPress(normalizedX,
                                    normalizedY);
                        }
                    });
                }else if (event.getAction() == MotionEvent.ACTION_MOVE){
                    glSurfaceView.queueEvent(new Runnable() {
                        @Override
                        public void run() {
                            //拖拽事件
                            airHockeyRenderer.handleTouchDrag(
                                    normalizedX,normalizedY);
                        }
                    });
                }
                return true;
            }
            return false;
        });
    }

    /**
     * 检查支持OpenGL ES 2.0
     * @return
     */
    private boolean checkSupportEs2Version() {
        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();

        return configurationInfo.reqGlEsVersion >=0x20000 ||
                //以下适用模拟器
                ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1
                        && (Build.FINGERPRINT.startsWith("generic"))
                        || Build.FINGERPRINT.startsWith("unknown")
                        || Build.MODEL.contains("google_sdk")
                        || Build.MODEL.contains("Emulator")
                        || Build.MODEL.contains("Android SDK built for x86"));
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (rendererSet){
            glSurfaceView.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (rendererSet){
            glSurfaceView.onResume();
        }
    }
}