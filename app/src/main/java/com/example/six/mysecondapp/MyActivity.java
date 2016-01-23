package com.example.six.mysecondapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MyActivity extends Activity {
    private CameraPreview camPreview;
    private FrameLayout mainLayout;
    private int PreviewSizeWidth = 160;
    private int PreviewSizeHeight= 120;
    TextView textView;

    private String message = "";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        // set up layout xml files
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_my);

        // create camera preview
        SurfaceView camView = new SurfaceView(this);
        SurfaceHolder camHolder = camView.getHolder();
        camPreview = new CameraPreview(PreviewSizeWidth, PreviewSizeHeight, this);
        camHolder.addCallback(camPreview);
        camHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mainLayout = (FrameLayout) findViewById(R.id.frameLayout1);
        mainLayout.addView(camView, new LayoutParams(PreviewSizeWidth, PreviewSizeHeight));

        textView = (TextView) findViewById(R.id.text);
    }

    // executes every time the preview gets a frame
    public void onPreviewFrame(byte[] array) {
        int yValue = array[0];
        if (yValue > 30) {
            message += "0 ";
        } else {
            message += "1 ";
        }
        if (message.length() > 80) {
            message = message.substring(2);
        }
        textView.setText(message);
    }
}
