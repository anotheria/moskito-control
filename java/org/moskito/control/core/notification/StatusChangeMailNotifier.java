package org.moskito.control.core.notification;

import org.moskito.control.config.MoskitoControlConfiguration;
import org.moskito.control.core.ApplicationRepository;
import org.moskito.control.core.status.StatusChangeEvent;
import org.moskito.control.core.status.StatusChangeListener;
import org.moskito.control.core.util.Muter;
import org.moskito.control.mail.MailService;
import org.moskito.control.mail.MailServiceConfig;
import org.moskito.control.mail.message.MailMessageBuilder;
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
     * Status change mail notifications muter.
     */
    private Muter muter = new Muter();

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
    public void notifyStatusChange(StatusChangeEvent event) {
        log.debug("Processing status change event: " + event);

		if (!MoskitoControlConfiguration.getConfiguration().isMailNotificationEnabled()){
			log.debug("Mail notifications are disabled");
			return;
		}

        if (muter.isMuted()) {
            log.debug("Mail notifications are muted. Skipped notification mail sending for status change event " + event
                    + ". Remaining muting time: " + getRemainingMutingTime());
            return;
        }

        String[] notificationRecipients = MailServiceConfig.getInstance().getNotificationsMap().get(event.getStatus().getHealth());
		MailService.getInstance().send(MailMessageBuilder.buildStatusChangedMessage(event, notificationRecipients));
        log.warn("Notification mail was send for status change event: " + event);
    }

    /**
     * Mute status change mail notifications changes.
     *
     * @param delay delay in mills, positive.                   \
     */
    public void mute(long delay) {
        muter.mute(delay);
        log.debug("Status change mail notifications muted for delay: " + delay);
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
