package org.moskito.control.ui.action;

import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;

import org.moskito.control.core.ComponentRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * The action for unmuting status change notification.
 *
 * @author vkazhdan
 */
public class UnmuteNotificationsAction extends BaseMoSKitoControlAction {
    @Override
    public ActionCommand execute(ActionMapping mapping, HttpServletRequest req, HttpServletResponse res) {
        ComponentRepository.getInstance().getEventsDispatcher().unmute();
        return mapping.redirect();
    }
}
