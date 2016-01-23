package com.example.six.mysecondapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.view.SurfaceHolder;

import java.io.FileOutputStream;
import java.io.IOException;

public class CameraPreview implements SurfaceHolder.Callback, Camera.PreviewCallback
{
    private Camera mCamera = null;
    private int PreviewSizeWidth;
    private int PreviewSizeHeight;
    private String NowPictureFileName;
    private Boolean TakePicture = false;
    int i = 0;
    private MyActivity activity;
    Parameters parameters;

    int sec = 2;

    public CameraPreview(int PreviewlayoutWidth, int PreviewlayoutHeight, MyActivity activity)
    {
        this.activity = activity;
        PreviewSizeWidth = PreviewlayoutWidth;
        PreviewSizeHeight = PreviewlayoutHeight;
    }

    @Override
    public void onPreviewFrame(byte[] arg0, Camera arg1)
    {
        // At preview mode, the frame data will push to here.
        // But we do not want these data.
        // 10 fps
        if (sec == 2) {
            activity.sayHi(arg0);
            sec = 0;
        } else {
            ++sec;
        }
        /*
        YuvImage yuvimage=new YuvImage(arg0, ImageFormat.NV21, PreviewSizeWidth, PreviewSizeHeight, null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        yuvimage.compressToJpeg(new Rect(0, 0, PreviewSizeWidth, PreviewSizeHeight), 80, baos);
        byte[] jdata = baos.toByteArray();
        Bitmap bmp = BitmapFactory.decodeByteArray(jdata, 0, jdata.length);
        activity.sayHi(bmp);
        sec = 0;*/
    }


    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3)
    {
        parameters = mCamera.getParameters();
        // Set the camera preview size
        parameters.setPreviewSize(PreviewSizeWidth, PreviewSizeHeight);
        // Set the take picture size, you can set the large size of the camera supported.
        parameters.setPictureSize(PreviewSizeWidth, PreviewSizeHeight);

        // Turn on the camera flash.
        String NowFlashMode = parameters.getFlashMode();
        if ( NowFlashMode != null )
            parameters.setFlashMode(Parameters.FLASH_MODE_ON);
        // Set the auto-focus.
        String NowFocusMode = parameters.getFocusMode ();
        if ( NowFocusMode != null )
            parameters.setFocusMode("auto");

        mCamera.setParameters(parameters);

        mCamera.startPreview();
    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0)
    {
        mCamera = Camera.open();
        try
        {
            // If did not set the SurfaceHolder, the preview area will be black.
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

    // Take picture interface
    public void CameraTakePicture(String FileName)
    {
        TakePicture = true;
        NowPictureFileName = FileName;
        mCamera.autoFocus(myAutoFocusCallback);
    }

    // Set auto-focus interface
    public void CameraStartAutoFocus()
    {
        TakePicture = false;
        mCamera.autoFocus(myAutoFocusCallback);
    }


    //=================================
    //
    // AutoFocusCallback
    //
    //=================================
    AutoFocusCallback myAutoFocusCallback = new AutoFocusCallback()
    {
        public void onAutoFocus(boolean arg0, Camera NowCamera)
        {
            if ( TakePicture )
            {
                NowCamera.stopPreview();//fixed for Samsung S2
                NowCamera.takePicture(shutterCallback, rawPictureCallback, jpegPictureCallback);
                TakePicture = false;
            }
        }
    };
    ShutterCallback shutterCallback = new ShutterCallback()
    {
        public void onShutter()
        {
            // Just do nothing.
        }
    };

    PictureCallback rawPictureCallback = new PictureCallback()
    {
        public void onPictureTaken(byte[] arg0, Camera arg1)
        {
            // Just do nothing.
        }
    };

    PictureCallback jpegPictureCallback = new PictureCallback()
    {
        public void onPictureTaken(byte[] data, Camera arg1)
        {
            // Save the picture.
            try {
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0,data.length);
                FileOutputStream out = new FileOutputStream(NowPictureFileName);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    };

}


/*
import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/ A basic Camera preview class
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera mCamera;
    int i = 0;

    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        if(mCamera!=null){
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);

            mCamera.release();
            mCamera = null;
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null){
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}*/