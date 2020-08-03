package org.moskito.control.core.util;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Muter, designed for muting some repeatable tasks for given period of time.
 *
 * @author vkazhdan
 * @since 07.10.2013
 */
public class Muter {

    /**
     * Timer.
     */
    private Timer timer;

    /**
     * Timer stop time.
     */
    private Long stopTime;

    /**
     * Mute.
     *
     * @param delay delay in mills, positive.
     */
    public void mute(long delay) {
        if (delay <= 0) {
            throw new IllegalArgumentException("delay should be positive");
        }

        // Stop previous timer
        unmute();

        // Start new timer
        stopTime = System.currentTimeMillis() + delay;
        timer = new Timer(false);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timer = null;
                stopTime = null;
            }
        }, new Date(stopTime));
    }

    /**
     * Unmute if muted.
     */
    public void unmute() {
        if (timer != null) {
            timer.cancel();
            timer = null;
            stopTime = null;
        }
    }

    /**
     * Is muted.
     *
     * @return true if muted currently, false if not.
     */
    public boolean isMuted() {
        return timer != null;
    }

    /**
     * Get remaining muting time.
     *
     * @return remaining muting time, or 0 if not muted.
     */
    public long getRemainingTime() {
        return stopTime != null ? stopTime - System.currentTimeMillis(): 0;
    }
}
