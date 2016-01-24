package com.example.six.mysecondapp;

import android.hardware.Sensor;
import android.widget.TextView;

public class SensorThread implements Runnable {
    private TextView textView;
    private Sensor lightSensor;
    private String bits = "";

    public SensorThread(TextView textView, Sensor lightSensor) {
        this.textView = textView;
        this.lightSensor = lightSensor;
    }

    @Override
    public void run() {
        try {
            while (true) {
                bits += "1 ";
                bits = bits.length() > 200 ? bits.substring(2) : bits;
                textView.setText(bits);
                Thread.sleep(100);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
            System.err.println("Thread sleep interrupted.");
        }

    }
}
