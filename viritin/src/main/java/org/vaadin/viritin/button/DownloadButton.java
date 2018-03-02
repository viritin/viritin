package org.vaadin.viritin.button;

import com.vaadin.server.FileDownloader;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;
import com.vaadin.ui.Button;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;
import org.vaadin.viritin.fluency.ui.FluentAbstractComponent;

/**
 * A helper class to implement typical file downloads.
 * <p>
 * With this class you'll get rid of lots of boilerplate code from your
 * application. It also inverts the bit cumbersome input-output stream API in
 * Vaadin so, normally you just "wire" this button to your backend method that
 * writes your resource to OutputStream (instead of playing around with piped
 * streams or storing resources temporary in memory. Example of usage:
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
public class DownloadButton extends Button implements FluentAbstractComponent<DownloadButton> {

    private static final long serialVersionUID = 356223526447669958L;

    public interface ContentWriter extends Serializable {

        void write(OutputStream stream);
    }

    public interface DownloadCompletedListener extends Serializable {

        void onCompleted();
    }

    private Collection<DownloadCompletedListener> downloadCompletedListeners;

    private ContentWriter writer;
    private MimeTypeProvider mimeTypeProvider;
    private FileNameProvider fileNameProvider;

    private final StreamResource streamResource = new StreamResource(new StreamResource.StreamSource() {

        private static final long serialVersionUID = 3641967669172064511L;

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
    }, "") {

        private static final long serialVersionUID = -8221900203840804581L;

        @Override
        public String getFilename() {
            if (fileNameProvider != null) {
                return fileNameProvider.getFileName();
            }
            return DownloadButton.this.getFileName();
        }

        @Override
        public String getMIMEType() {
            if (mimeTypeProvider != null) {
                return mimeTypeProvider.getMimeType();
            }
            return super.getMIMEType();
        }

    };

    /**
     * Constructs a new Download button without ContentWriter. Be sure to set
     * the ContentWriter or override its getter, before instance is actually
     * used.
     */
    public DownloadButton() {
        new FileDownloader(streamResource) {
            @Override
            public boolean handleConnectorRequest(VaadinRequest request, VaadinResponse response, String path) throws IOException {
                final boolean handleConnectorRequest = super.handleConnectorRequest(request, response, path);
                if (handleConnectorRequest) {
                    afterResponseWritten();
                }
                return handleConnectorRequest;
            }

        }.extend(this);

    }

    public DownloadButton(ContentWriter writer) {
        this();
        this.writer = writer;
    }

    /**
     * Adds a listener that is notified when the download has been sent to the
     * client. Note that you need to enable push connection or use UI.setPollingInterval
     * to make UI modifications visible automatically.
     * 
     * @param listener the listener to be notified
     * @return the download button
     */
    public DownloadButton addDownloadCompletedListener(DownloadCompletedListener listener) {
        if (downloadCompletedListeners == null) {
            downloadCompletedListeners = new LinkedHashSet<>();
        }
        downloadCompletedListeners.add(listener);
        return this;
    }

    public void removeDownloadCompletedListener(DownloadCompletedListener listener) {
        downloadCompletedListeners.remove(listener);
    }

    /**
     * A hook for extension to do something after the response has been sent to
     * the client.
     */
    protected void afterResponseWritten() {
        if (downloadCompletedListeners != null) {
            getUI().access(() -> {
                downloadCompletedListeners.forEach(DownloadCompletedListener::onCompleted);
            });
        }

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
        return fileNameProvider != null ? fileNameProvider.getFileName() : "file";
    }

    public DownloadButton setFileName(final String fileName) {
        this.fileNameProvider = new FileNameProvider() {

            private static final long serialVersionUID = -3449552786114328636L;

            @Override
            public String getFileName() {
                return fileName;
            }
        };
        return this;
    }

    @Override
    public DownloadButton withIcon(Resource icon) {
        setIcon(icon);
        return this;
    }

    public MimeTypeProvider getMimeTypeProvider() {
        return mimeTypeProvider;
    }

    public FileNameProvider getFileNameProvider() {
        return fileNameProvider;
    }

    public DownloadButton setFileNameProvider(FileNameProvider fileNameProvider) {
        this.fileNameProvider = fileNameProvider;
        return this;
    }

    public DownloadButton setMimeTypeProvider(MimeTypeProvider mimeTypeProvider) {
        this.mimeTypeProvider = mimeTypeProvider;
        return this;
    }

    public interface FileNameProvider extends Serializable {

        String getFileName();
    }

    public interface MimeTypeProvider extends Serializable {

        String getMimeType();
    }

}
