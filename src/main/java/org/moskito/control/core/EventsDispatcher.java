package org.moskito.control.core;

import org.moskito.control.core.status.MuteEventListener;
import org.moskito.control.core.status.StatusChangeEvent;
import org.moskito.control.core.status.StatusChangeListener;
import org.moskito.control.core.util.Muter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Stores and triggers event listeners
 * Also contains listeners mute mechanism
 */
public final class EventsDispatcher {

    private Logger log = LoggerFactory.getLogger(EventsDispatcher.class);

    /**
     * Status change mail notifications muter.
     */
    private Muter muter = new Muter();

    /**
     * Listeners for status updates.
     */
    private List<StatusChangeListener> statusChangeListeners = new CopyOnWriteArrayList<StatusChangeListener>();

    public void addStatusChangeListener(StatusChangeListener statusChangeListener) {
        statusChangeListeners.add(statusChangeListener);
    }

    public void removeStatusChangeListener(StatusChangeListener statusChangeListener) {
        statusChangeListeners.remove(statusChangeListener);
    }

    /**
     * Called whenever a status change is detected. Propagates the change to attached listeners.
     *
     * @param event status change event.
     */
    public void addStatusChange(StatusChangeEvent event){
        log.debug("addStatusChange(" + event + ")");

        if(isMuted())
            log.warn("Status change event is triggered, but event listeners is still muted for " +
             getRemainingMutingTime() + " mills");

        for (StatusChangeListener listener: statusChangeListeners){
            try{
                // skip triggering listeners with mute interface if mute is on
                if(!isMuted() || !(listener instanceof MuteEventListener))
                    listener.notifyStatusChange(event);
            }catch(Exception e){
                log.warn("Status change listener "+listener+" couldn't update status",e);
            }
        }
    }

    /**
     * Mute status change notifications changes.
     *
     * @param delay delay in mills, positive.
     */
    public void mute(long delay) {
        muter.mute(delay);
        log.debug("Status change notifications muted for delay: " + getRemainingMutingTime());
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
