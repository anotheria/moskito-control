package org.moskito.control.core.notification;

import org.moskito.control.core.ApplicationRepository;
import org.moskito.control.core.StatusChangeEvent;
import org.moskito.control.core.StatusChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Status changes mail notifier. Sends emails on any component status changes.
 *
 * @author vkazhdan
 */
public final class StatusChangeMailNotifier implements StatusChangeListener {

    /**
     * Logger.
     */
    private static Logger log = LoggerFactory.getLogger(StatusChangeMailNotifier.class);

    /**
     * Constructor. Registers itself as the status change listener.
     */
    private StatusChangeMailNotifier() {
        ApplicationRepository.getInstance().addStatusChangeListener(this);
    }

    public static StatusChangeMailNotifier getInstance() {
        return StatusChangeMailNotifierInstanceHolder.INSTANCE;
    }

    @Override
    public void notifyStatusChange(StatusChangeEvent statusChangeEvent) {
        log.debug("Processing status change event: " + statusChangeEvent);
        // TODO support mute mail notifications here
        // TODO integrate with mail service. Something like: mailService.sendStatusChangeMail(statusChangeEvent)
        log.debug("Notification mail was send for event: " + statusChangeEvent);
    }

    /**
     * Singleton instance holder class.
     */
    private static class StatusChangeMailNotifierInstanceHolder {
        /**
         * Singleton instance.
         */
        private static final StatusChangeMailNotifier INSTANCE = new StatusChangeMailNotifier();
    }
}
