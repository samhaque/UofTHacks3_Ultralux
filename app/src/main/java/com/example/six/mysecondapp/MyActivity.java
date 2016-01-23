package com.example.six.mysecondapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import edu.princeton.cs.introcs.BinaryStdIn;
import edu.princeton.cs.introcs.BinaryStdOut;

public class MyActivity extends Activity {
    private CameraPreview camPreview;
    private FrameLayout mainLayout;
    private int PreviewSizeWidth = 160;
    private int PreviewSizeHeight= 120;

    TextView textView;

    private static final int inputChars = 256;  // number of input chars
    private static final int L = 4096;          // number of codewords = 2^W
    private static final int W = 12;            // codeword width

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
        if (yValue > -20) {
            message += "0 ";
        } else {
            message += "1 ";
        }
        if (message.length() > 224) {
            message = message.substring(2);
        }
        textView.setText(message);
        decompressMessage(message);
    }

    public void decompressMessage(String message){

        /*char[] st = message.toCharArray();*/

        String[] st = message.split("");
        int i; // next available codeword value

        // initialize symbol table with all 1-character strings
        for (i = 0; i < inputChars; i++)
            st[i] = "" + (char) i;
        st[i++] = "";                        // (unused) lookahead for EOF

        int codeword = BinaryStdIn.readInt(W);
        if (codeword == inputChars) return;           // expanded message is empty string
        String val = st[codeword];

        while (true) {
            BinaryStdOut.write(val);
            codeword = BinaryStdIn.readInt(W);
            if (codeword == inputChars) break;
            String s = st[codeword];
            if (i == codeword) s = val + val.charAt(0);   // special case hack
            if (i < L) st[i++] = val + s.charAt(0);
            val = s;
        }
        BinaryStdOut.close();
    }

    public void onPreviewFrame(String exposure) {
        textView.setText(exposure);
    }
}
