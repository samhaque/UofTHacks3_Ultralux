package com.example.six.mysecondapp;

import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.view.SurfaceHolder;
import java.io.IOException;

public class CameraPreview implements SurfaceHolder.Callback, Camera.PreviewCallback
{
    private Camera mCamera = null;
    private int PreviewSizeWidth;
    private int PreviewSizeHeight;
    private MyActivity myActivity;
    Parameters parameters;

    private int frame = 2;
    private int taken = 0;
    private long sec = System.currentTimeMillis() / 1000;
    private int fps = 1;

    public CameraPreview(int PreviewlayoutWidth, int PreviewlayoutHeight, MyActivity activity)
    {
        this.myActivity = activity;
        PreviewSizeWidth = PreviewlayoutWidth;
        PreviewSizeHeight = PreviewlayoutHeight;
    }

    @Override
    public void onPreviewFrame(byte[] arg0, Camera arg1)
    {
        // call myActivity's onPreviewFrame every time we get a preview frame
        // restrict to 10 fps
        if (frame == 2) {
            myActivity.onPreviewFrame(arg0);
            frame = 0;
        } else {
            ++frame;
        }
        //myActivity.onPreviewFrame(Byte.toString(arg0[0]));
        /*
        long currentSec = System.currentTimeMillis() / 1000;
        if (currentSec == sec) {
            ++fps;
        } else {
            sec = currentSec;
            myActivity.onPreviewFrame(Long.toString(fps));
            fps = 1;
        }*/
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3)
    {
        parameters = mCamera.getParameters();
        parameters.setPreviewSize(PreviewSizeWidth, PreviewSizeHeight);
        parameters.setPictureSize(PreviewSizeWidth, PreviewSizeHeight);
        parameters.setAutoExposureLock(true);

        mCamera.setParameters(parameters);
        mCamera.startPreview();
    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0)
    {
        mCamera = Camera.open();
        try
        {
            mCamera.setPreviewDisplay(arg0);
            mCamera.setPreviewCallback(this);
        }
        catch (IOException e)
        {
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0)
    {
        mCamera.setPreviewCallback(null);
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }
}