package com.thatmg393.tpa4fabric.utils;

import java.util.Timer;
import java.util.TimerTask;

public class CountdownTimer {
    private Timer scheduler;
    private TimerCallback callback;

    private long now = 0;
    private long remaining;

    private int tickRate = 500;

    private boolean running;

    public CountdownTimer(
        TimerCallback callback,
        long duration
    ) {
        if (duration <= 0) {
            internalStop();
            callback.onFinished(this);
        }

        this.scheduler = new Timer();
        this.callback = callback;
        
        this.remaining = duration;
    }

    public CountdownTimer(
        TimerCallback callback,
        long duration,
        int tickRate
    ) {
        this(callback, duration);
        if (tickRate >= duration) {
            internalStop();
            callback.onFinished(this);
        }

        this.tickRate = tickRate;
    }

    
    /** start()
     * Starts the countdown timer
     */
    public void start() {
        this.running = true;
        scheduler.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                now += tickRate;
                remaining -= now;
                
                if (remaining <= 0) {
                    internalStop();
                    callback.onFinished(CountdownTimer.this);
                } else callback.onTick(CountdownTimer.this, tickRate);
            }
        }, 0, tickRate);
    }

    /** stop()
     * Stops the countdown timer
     */
    public void stop() {
        internalStop();
        callback.onStop(this, remaining - now);

        now = 0;
    }

    public boolean isRunning() {
        return this.running;
    }

    private void internalStop() {
        this.running = false;
        scheduler.cancel();
    }

    public static interface TimerCallback {
        public void onTick(CountdownTimer myself, long delta);
        public void onStop(CountdownTimer myself, long remaining);
        public void onFinished(CountdownTimer myself);
    }
}
