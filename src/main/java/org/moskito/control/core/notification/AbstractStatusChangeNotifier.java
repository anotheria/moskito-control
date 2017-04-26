package org.moskito.control.core.notification;

import org.moskito.control.core.status.StatusChangeListener;
import org.moskito.control.core.util.Muter;

/**
 * Basic class for notifications.
 * Contains mute mechanism.
 */
abstract class AbstractStatusChangeNotifier implements StatusChangeListener {


    /**
     * Status change mail notifications muter.
     */
    Muter muter = new Muter();

    /**
     * Mute status change mail notifications changes.
     *
     * @param delay delay in mills, positive.
     */
    public void mute(long delay) {
        muter.mute(delay);
        // TODO : FIX LOGGING HERE
        //log.debug("Status change mail notifications muted for delay: " + delay);
    }

    /**
     * Unmute if muted.
     */
    public void unmute() {
        muter.unmute();
    }

    /**
     * Is muted.
     *
     * @return true if muted currently, false if not.
     */
    public boolean isMuted() {
        return muter.isMuted();
    }

    /**
     * Get remaining muting time.
     *
     * @return remaining muting time, or 0 if not muted.
     */
    public long getRemainingMutingTime() {
        return muter.getRemainingTime();
    }

}
