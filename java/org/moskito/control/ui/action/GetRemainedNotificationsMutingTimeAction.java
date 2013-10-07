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

/**
 * The action for requesting remained notifications muting time.
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
            res.getOutputStream().print(StatusChangeMailNotifier.getInstance().getRemainedMutingTime());
        } catch (IOException e) {
            log.error("Error on printing to the response output stream", e);
        }
        return mapping.redirect();
    }
}
