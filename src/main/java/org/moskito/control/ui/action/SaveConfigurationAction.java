package org.moskito.control.ui.action;

import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.bean.FormBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.PrintWriter;
import java.net.URLDecoder;

public class SaveConfigurationAction extends BaseMoSKitoControlAction {

    @Override
    public ActionCommand execute(ActionMapping mapping, FormBean formBean, HttpServletRequest req, HttpServletResponse res) throws Exception {
        String newConfig = URLDecoder.decode(req.getParameter("newConfig"),"UTF-8");
        String folderPath = System.getProperty("catalina.home") + "/moskito-control-configuration";
        PrintWriter writer = null;

        try {
            File file = new File(folderPath);
            if (!file.exists()) {
                file.mkdir();
            }

            writer = new PrintWriter(folderPath + "/moskitocontrol.json");
            new JsonParser().parse(newConfig);  //validation

            writer.write(newConfig);
            req.getSession().setAttribute("saveConfigurationSuccess", Boolean.TRUE);

        } catch (SecurityException se) {
            req.getSession().setAttribute("saveConfigurationSuccess", "Could not create folder \"moskito-control-configuration\" " + se.getMessage());
        } catch (JsonSyntaxException je) {
            req.getSession().setAttribute("saveConfigurationSuccess", "Not a valid JSON\n " + je.getMessage());
        } catch (Exception e) {
            req.getSession().setAttribute("saveConfigurationSuccess", e.getMessage());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }

        setEditConfigOff(req);
        return mapping.redirect();
    }
}
