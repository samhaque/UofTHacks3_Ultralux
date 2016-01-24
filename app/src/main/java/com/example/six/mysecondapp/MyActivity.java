package com.example.six.mysecondapp;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.Display;
import android.widget.RelativeLayout;
import android.view.ViewGroup;

import java.util.ArrayList;


public class MyActivity extends Activity {
    private CameraPreview camPreview;
    private FrameLayout mainLayout;
    //private SurfaceView mainLayout;

    private int PreviewSizeWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int PreviewSizeHeight= Resources.getSystem().getDisplayMetrics().heightPixels;

    TextView textView;
    TextView resultView;

    private boolean listening = false;
    private boolean messageStarted = false;
    private boolean messageEnded = false;
    private ArrayList<Byte> message;
    private String scrollingBits = "";


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

        mainLayout = (FrameLayout) findViewById(R.id.frameLayout1);

        camPreview = new CameraPreview(PreviewSizeWidth, PreviewSizeHeight, this);
        camHolder.addCallback(camPreview);
        camHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        mainLayout.addView(camView, new LayoutParams(PreviewSizeWidth, PreviewSizeHeight));

        textView = (TextView) findViewById(R.id.text);
        message = new ArrayList<Byte>();

        LinearLayout rootLayout = (LinearLayout) findViewById(R.id.rootLayout);
        TextView newText;
        for (int j=0; j<5; j++) {
            newText = new TextView(this);
            newText.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            newText.setPadding(40, 15, 40, 15);
            newText.setText(" This is " + j + "th message.");
            rootLayout.addView(newText);
        }
    }


    // executes every time the preview gets a frame
    public void onPreviewFrame(byte[] array) {
        if (messageEnded) return;

        // when we hit the first 0 bit, we start listening for messages
        boolean oneBit = oneBit(array);
        boolean zeroBit = !oneBit;
        //showScrollingBits(zeroBit);

        listening = listening || zeroBit;
        if (!listening) return;

        // when we hit the first 1 bit, message started
        if (!messageStarted) {
            messageStarted = messageStarted || oneBit;

            // after message has started, save it in an arrayList
        } else if (messageStarted) {
            if (zeroBit) {
                message.add((byte) 0);
            } else {
                message.add((byte) 1);
            }
        }
        messageEnded = checkMessageEnd();
    }

    private void showScrollingBits(boolean zeroBit) {
        if (zeroBit) {
            scrollingBits += "0 ";
        } else {
            scrollingBits += "1 ";
        }
        if (scrollingBits.length() > 200) {
            scrollingBits = scrollingBits.substring(2);
        }
        textView.setText(scrollingBits);
    }

    private boolean oneBit(byte[] array) {
        for (int i = -8; i < 8; i += 2) {
            for (int j = -8; j < 8; j += 2) {
                if (array[((80 + i) * 160) + 60 + j] < -20) return true;
            }
        }
        return false;
    }

    private boolean checkMessageEnd() {
        if (message.size() < 8) return false;
            // if the last 8 bits are 0, then the message has ended
        else {
            for (int i = message.size() - 8; i < message.size(); ++i) {
                if (message.get(i) == 1) return false;
            }
            displayMessage();
            return true;
        }
    }

    private void displayMessage() {
        String result = "0";
        for (int i = 1; i < message.size(); ++i) {
            result += message.get(i) + "";
        }
        // make sure that length of message is divisible by 8
        // the last few remaining bits are 0 and can be discarded
        resultView.setText(result);
    }

    public void onPreviewFrame(String exposure) {
        textView.setText(exposure);
    }

}
