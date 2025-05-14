package org.moskito.control.ui.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.anotheria.maf.action.Action;
import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;

public class ShowQRCodeAction implements Action {
    @Override
    public void preProcess(ActionMapping mapping, HttpServletRequest req, HttpServletResponse res) throws Exception {

    }

    @Override
    public ActionCommand execute(ActionMapping mapping, HttpServletRequest req, HttpServletResponse res) throws Exception {
        String qrCodeText = GenerateQRCodeAction.getQRCodeText(req);
        req.setAttribute("qrCodeText", qrCodeText);
        return mapping.success();
    }

    @Override
    public void postProcess(ActionMapping mapping, HttpServletRequest req, HttpServletResponse res) throws Exception {

    }
}
