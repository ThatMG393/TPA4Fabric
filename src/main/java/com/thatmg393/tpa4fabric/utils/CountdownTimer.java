package com.thatmg393.tpa4fabric.utils;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * CountdownTimer class manages a countdown timer that triggers a callback on each tick and when the timer finishes.
 */
public class CountdownTimer extends TimerTask {
    private Timer scheduler;
    private TimerCallback callback;

    private long now = 0;
    private long remaining;

    private int tickRate = 500;

    private AtomicBoolean running;

    /**
     * Constructs a CountdownTimer with a specified duration.
     * 
     * @param callback The callback interface for timer events.
     * @param duration The duration of the timer in milliseconds.
     */
    public CountdownTimer(
        TimerCallback callback,
        long duration
    ) {
        this.scheduler = new Timer();
        this.callback = callback;
        
        this.remaining = duration;
    }

    /**
     * Constructs a CountdownTimer with a specified duration and tick rate.
     * 
     * @param callback The callback interface for timer events.
     * @param duration The duration of the timer in milliseconds.
     * @param tickRate The frequency at which the timer ticks, in milliseconds.
     */
    public CountdownTimer(
        TimerCallback callback,
        long duration,
        int tickRate
    ) {
        this(callback, duration);
        this.tickRate = tickRate;
    }

    /** 
     * Starts the countdown timer. It schedules a task to run at fixed intervals based on the tick rate.
     */
    public synchronized void start() {
        if (isRunning()) return;
        if (remaining <= 0 || tickRate >= remaining) {
            internalStop();
            callback.onFinished(this);

            return;
        }

        this.running.set(true);
        scheduler.scheduleAtFixedRate(this, 0, tickRate);
    }

    /** 
     * Stops the countdown timer and triggers the callback to notify that the timer was stopped early.
     */
    public synchronized void stop() {
        internalStop();
        callback.onStop(this, remaining);

        // Optionally reset now if desired, depending on use case
        now = 0;  
    }

    /**
     * Checks if the timer is running.
     * 
     * @return true if the timer is running, false otherwise.
     */
    public boolean isRunning() {
        return this.running.get();
    }

    /**
     * Internal method to stop the timer without triggering the stop callback. It cancels the scheduler.
     */
    private synchronized void internalStop() {
        this.running.set(false);
        this.cancel();
        scheduler.cancel();
    }

    /*
     * Main logic/implementation of this class 
     */
    @Override
    public void run() {
        now += tickRate;
        remaining -= tickRate; // Subtract tickRate from remaining instead of now
        
        if (remaining <= 0) {
            internalStop();
            callback.onFinished(CountdownTimer.this);
        } else callback.onTick(CountdownTimer.this, now);
    }

    /**
     * TimerCallback interface defines the methods that should be implemented to receive timer events.
     */
    public static interface TimerCallback {
        /**
         * Called on each tick of the timer.
         * 
         * @param myself The current instance of the CountdownTimer.
         * @param delta The time elapsed since the timer started, in milliseconds.
         */
        public void onTick(CountdownTimer myself, long delta);
        
        /**
         * Called when the timer is stopped manually before completion.
         * 
         * @param myself The current instance of the CountdownTimer.
         * @param remaining The remaining time in milliseconds when the timer was stopped.
         */
        public void onStop(CountdownTimer myself, long remaining);
        
        /**
         * Called when the timer finishes naturally (reaches zero).
         * 
         * @param myself The current instance of the CountdownTimer.
         */
        public void onFinished(CountdownTimer myself);
    }
}
