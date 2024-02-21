package org.vaadin.viritin.it;

import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.vaadin.viritin.button.DownloadButton;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * An example how to use DownloadButton
 *
 * @author Matti Tahvonen
 */
public class DownloadButtonExample extends AbstractTest {

    private static final long serialVersionUID = 2638100034569162593L;

    public DownloadButtonExample() {
        // Polling or Push needs to be enable to support response written hooks
        getUI().setPollInterval(1000);
    }

    @Override
    public Component getTestComponent() {
        
        final UI ui = getUI();
        DownloadButton d = new DownloadButton((OutputStream out) -> {
            try {
                // super easy to provide dynamic content
                out.write(("Hello there " + Instant.now().toString()).getBytes("UTF-8"));
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(DownloadButtonExample.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(DownloadButtonExample.class.getName()).log(Level.SEVERE, null, ex);
            }

        }).setFileNameProvider(() -> {
            // File name can be set with setter, but also dynamically
            // Note, that this is used for URL as well, so it is called
            // first time already when showing the button. The second 
            // call actually sets the content disposition header that 
            // affects the name browser uses for the downloaded file.
            return "file" + System.currentTimeMillis() + ".txt";
        }).setMimeTypeProvider(() -> {
            // Mime type can be set with setter, but also dynamically
            return "text/dynamically-set-odd-file-type";
        }).withCaption("Click to download");
        
        d.setDisableOnClick(true);
        d.addDownloadCompletedListener(() -> { 
            d.setEnabled(true);
            Notification.show("The response has been written to the client");
        });
        
        DownloadButton simple = new DownloadButton(out -> {
            try {
                out.write("Foobar".getBytes());
            } catch (IOException ex) {
                Logger.getLogger(DownloadButtonExample.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).withCaption("Simple Download");

        DownloadButton failing = new DownloadButton(out -> {
            throw new RuntimeException("Issue generating a file!");
        }).withCaption("Failing Download");

        return new MVerticalLayout(d, simple, failing);

    }

}
