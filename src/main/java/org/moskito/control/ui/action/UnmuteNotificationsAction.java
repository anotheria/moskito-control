package org.moskito.control.ui.action;

import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.bean.FormBean;
import org.moskito.control.core.notification.StatusChangeMailNotifier;
import org.moskito.control.core.notification.StatusChangeOpsgenieNotifier;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The action for unmuting status change notification.
 *
 * @author vkazhdan
 */
public class UnmuteNotificationsAction extends BaseMoSKitoControlAction {
    @Override
    public ActionCommand execute(ActionMapping mapping, FormBean formBean, HttpServletRequest req, HttpServletResponse res) {
        StatusChangeMailNotifier.getInstance().unmute();
        StatusChangeOpsgenieNotifier.getInstance().unmute();
        return mapping.redirect();
    }
}
