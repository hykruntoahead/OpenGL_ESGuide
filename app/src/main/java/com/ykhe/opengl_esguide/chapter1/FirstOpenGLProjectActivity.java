package com.ykhe.opengl_esguide.chapter1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class FirstOpenGLProjectActivity extends AppCompatActivity {
    private GLSurfaceView glSurfaceView;
    private boolean rendererSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        glSurfaceView = new GLSurfaceView(this);
        boolean supportsEs2 = checkSupportEs2Version();
        if (supportsEs2){
            glSurfaceView.setEGLContextClientVersion(2);

            glSurfaceView.setRenderer(new FirstOpenGLProjectRenderer());
            rendererSet = true;
        }else {
            Toast.makeText(this,"not support OpenGL ES 2.0.",Toast.LENGTH_LONG)
                    .show();
            return;
        }
        setContentView(glSurfaceView);
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