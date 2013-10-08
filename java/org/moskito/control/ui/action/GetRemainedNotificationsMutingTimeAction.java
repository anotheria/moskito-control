package org.moskito.control.ui.action;

import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.bean.FormBean;
import org.moskito.control.core.notification.StatusChangeMailNotifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * The action for requesting remained notifications muting time in minutes.
 *
 * @author vkazhdan
 */
public class GetRemainedNotificationsMutingTimeAction extends BaseMoSKitoControlAction {
    /**
     * Logger.
     */
    private static Logger log = LoggerFactory.getLogger(GetRemainedNotificationsMutingTimeAction.class);

    @Override
    public ActionCommand execute(ActionMapping mapping, FormBean formBean, HttpServletRequest req, HttpServletResponse res) {
        try {
            long remainedTime = StatusChangeMailNotifier.getInstance().getRemainedMutingTime();
            res.getOutputStream().print(remainedTime <= 0 ? "0" : BigDecimal.valueOf((float) remainedTime / 60000).setScale(1, RoundingMode.UP).toString());
        } catch (IOException e) {
            log.error("Error on printing to the response output stream", e);
        }
        return mapping.redirect();
    }
}
