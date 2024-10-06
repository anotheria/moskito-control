package org.moskito.control.core;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MuteStatus {
    private boolean isMuted;
    private long remainingMutingTime;

    public boolean isMuted() {
        return isMuted;
    }

    public void setMuted(boolean muted) {
        isMuted = muted;
    }

    public long getRemainingMutingTime() {
        return remainingMutingTime;
    }

    public void setRemainingMutingTime(long remainingMutingTime) {
        this.remainingMutingTime = remainingMutingTime;
    }

    public String getRemainingMutingTimeAsString(){
        return remainingMutingTime <= 0 ? "0" :
                BigDecimal.valueOf((float) remainingMutingTime / 60000).setScale(1,
                        RoundingMode.UP).toString();
    }

    public String toString() {
        return "MuteStatus{" +
                "isMuted=" + isMuted +
                ", remainingMutingTime=" + remainingMutingTime +
                '}';
    }


}
