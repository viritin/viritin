package org.vaadin.viritin.button;

import com.vaadin.server.FileDownloader;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * A helper class to implement typical file downloads.
 * <p>
 * With this class you'll get rid of lots of boilerplate code from your
 * application. It also inverts the bit cumbersome input-output stream API in
 * Vaadin so, normally you just "wire" this button to your backend method that
 * writes your resource to OutputStream (instead of playing around with piped streams or storing
 * resources temporary in memory. Example of usage:
 * <pre><code>
 *  new DownloadButton(invoice::toPdf).setFileName("invoice.pdf")
 * </code></pre>
 * <p>
 * The button extension hooks FileDownloader extension internally and inverts
 * the cumbersome default Vaadin API.
 * <p>
 * The writing of response is also spawn to separate thread, so in case your
 * resource generation takes lots of time, the UI wont block.
 */
public class DownloadButton extends MButton {

    public interface ContentWriter {

        void write(OutputStream stream);
    }

    private ContentWriter writer;

    private final StreamResource streamResource = new StreamResource(
            new StreamResource.StreamSource() {

                @Override
                public InputStream getStream() {
                    try {
                        final PipedOutputStream out = new PipedOutputStream();
                        final PipedInputStream in = new PipedInputStream(out);
                        writeResponce(out);
                        return in;
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }

            }, "file");

    /**
     * Constructs a new Download button without ContentWriter. Be sure to set
     * the ContentWriter or override its getter, before instance is actually 
     * used.
     */
    public DownloadButton() {
        new FileDownloader(streamResource).extend(this);

    }

    public DownloadButton(ContentWriter writer) {
        this();
        this.writer = writer;
    }

    /**
     * By default just spans a new raw thread to get the input. For strict Java
     * EE fellows, this might not suite, so override and use executor service.
     *
     * @param out the output stream where the output is targeted
     */
    protected void writeResponce(final PipedOutputStream out) {
        new Thread() {
            @Override
            public void run() {
                try {
                    getWriter().write(out);
                    out.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }.start();
    }

    public ContentWriter getWriter() {
        return writer;
    }

    public String getMimeType() {
        return streamResource.getMIMEType();
    }

    public DownloadButton setMimeType(String mimeType) {
        streamResource.setMIMEType(mimeType);
        return this;
    }

    public DownloadButton setCacheTime(long cacheTime) {
        streamResource.setCacheTime(cacheTime);
        return this;
    }

    public DownloadButton setWriter(ContentWriter writer) {
        this.writer = writer;
        return this;
    }

    public String getFileName() {
        return streamResource.getFilename();
    }

    public DownloadButton setFileName(String fileName) {
        streamResource.setFilename(fileName);
        return this;
    }

    @Override
    public DownloadButton withIcon(Resource icon) {
        setIcon(icon);
        return this;
    }

    @Override
    public DownloadButton withClickShortcut(int keycode, int... modifiers) {
        setClickShortcut(keycode, modifiers);
        return this;
    }
}
