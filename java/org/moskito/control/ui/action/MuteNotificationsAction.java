package org.moskito.control.ui.action;

import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.bean.FormBean;
import org.moskito.control.core.notification.StatusChangeMailNotifier;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The action for muting status change notification mails.
 *
 * @author vkazhdan
 */
public class MuteNotificationsAction extends BaseMoSKitoControlAction {
    @Override
    public ActionCommand execute(ActionMapping mapping, FormBean formBean, HttpServletRequest req, HttpServletResponse res) {
        long delay = Long.valueOf(req.getParameter("delay"));
        StatusChangeMailNotifier.getInstance().mute(delay);
        return mapping.redirect();
    }
}
