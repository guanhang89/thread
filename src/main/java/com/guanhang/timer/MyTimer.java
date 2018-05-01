package com.guanhang.timer;

import java.util.Timer;

public class MyTimer {

    public static void main(String[] args) {
        Timer timer = new Timer();
        MyTimerTask myTimerTask = new MyTimerTask("1");

        timer.schedule(myTimerTask, 2000L, 1000L);

    }
}
