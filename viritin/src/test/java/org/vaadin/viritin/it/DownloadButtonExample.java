package org.vaadin.viritin.it;

import com.vaadin.annotations.Theme;
import com.vaadin.ui.Component;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.button.DownloadButton;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * An example how to use DownloadButton
 *
 * @author Matti Tahvonen
 */
@Theme("valo")
public class DownloadButtonExample extends AbstractTest {

    private static final long serialVersionUID = 2638100034569162593L;

    @Override
    public Component getTestComponent() {
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

        return new MVerticalLayout(d);

    }

}
