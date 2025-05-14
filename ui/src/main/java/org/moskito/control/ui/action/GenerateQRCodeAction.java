package org.moskito.control.ui.action;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.anotheria.maf.action.Action;
import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;

import java.io.ByteArrayOutputStream;
import java.util.Hashtable;

public class GenerateQRCodeAction implements Action {
    @Override
    public void preProcess(ActionMapping mapping, HttpServletRequest req, HttpServletResponse res) throws Exception {
        // No pre-processing needed
    }

    @Override
    public ActionCommand execute(ActionMapping mapping, HttpServletRequest req, HttpServletResponse res) throws Exception {
        String text = getQRCodeText(req);

        byte[] qrCodeImage = generateQRCodeImage(text);
        res.setContentType("image/png");
        res.setContentLength(qrCodeImage.length);
        res.getOutputStream().write(qrCodeImage);
        res.getOutputStream().flush();
        res.getOutputStream().close();

        return null;
    }

    @Override
    public void postProcess(ActionMapping mapping, HttpServletRequest req, HttpServletResponse res) throws Exception {
        // No post-processing needed
    }

    public static final String getQRCodeText(HttpServletRequest request){
        String scheme = request.getHeader("X-Forwarded-Proto");
        if (scheme == null) {
            scheme = request.getScheme();
        }
        String host = request.getHeader("X-Forwarded-Host");
        if (host == null) {
            host = request.getServerName();
        }else{
            if (host.contains(":")) {
                host = host.substring(0, host.indexOf(":")).trim();
            }
            if (host.contains(",")) {
                host = host.substring(0, host.indexOf(",")).trim();
            }
        }

        String port = request.getHeader("X-Forwarded-Port");
        if (port == null) {
            port = String.valueOf(request.getServerPort());

        }

        System.out.println("requestScheme:" +request.getScheme());
        System.out.println("requestHost:" +request.getServerName());
        System.out.println("requestPort:" +request.getServerPort());
        System.out.println("X-Forwarded-Proto:" +request.getHeader("X-Forwarded-Proto"));
        System.out.println("X-Forwarded-Host:" +request.getHeader("X-Forwarded-Host"));
        System.out.println("X-Forwarded-Port:" +request.getHeader("X-Forwarded-Port"));

        String portString = ":" + port;
        if (port.equals("80") || port.equals("443")) {
            portString = "";
        }

        String text = scheme + "://" + host + portString + "/api/v2";
        return text;
    }

    private static byte[] generateQRCodeImage(String text) throws Exception {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        BitMatrix bitMatrix = new MultiFormatWriter().encode(
                text, BarcodeFormat.QR_CODE, 250, 250, hints);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }
}
