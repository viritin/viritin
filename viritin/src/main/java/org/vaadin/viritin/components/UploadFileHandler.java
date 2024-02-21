/*
 * Copyright 2018 Viritin.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.vaadin.viritin.components;

import com.vaadin.server.UserError;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * An upload implementation that just pass the input stream (and name and mime
 * type) of the uploaded file for developer to handle.
 * <p>
 * Note, then FileHandler you pass in is not executed in the UI thread. If you
 * want to modify the UI from it, by sure to use UI.access to handle locking
 * properly.
 * <p>
 * Note, all Upload features are not supported (but the lazy developer is not
 * throwing exceptions on all those methods).
 *
 * @author mstahv
 */
public class UploadFileHandler extends Upload implements Receiver {

    @FunctionalInterface
    public interface FileHandler {

        /**
         * This method is called by the framework when a new file is being
         * received.
         * <p>
         * You can read the file contents from the given InputStream.
         * <p>
         * Note, that this method is not executed in the UI thread. If you want
         * to modify the UI from it, by sure to use UI.access (and possibly 
         * polling) to handle locking properly.
         *
         * @param content the file content
         * @param fileName the name of the file in users device
         * @param mimeType the mime type parsed from the file name
         */
        public void handleFile(InputStream content, String fileName, String mimeType);
    }

    protected final FileHandler fileHandler;

    public UploadFileHandler(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
        setReceiver(this);
    }

    @Override
    public OutputStream receiveUpload(String filename, String mimeType) {
        try {
            final PipedInputStream in = new PipedInputStream();
            final PipedOutputStream out = new PipedOutputStream(in);
            writeResponce(in, filename, mimeType);
            return out;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * By default just spans a new raw thread to get the input. For strict Java
     * EE fellows, this might not suite, so override and use executor service.
     *
     * @param in the input stream where file content can be handled
     * @param filename the file name on the senders machine
     * @param mimeType the mimeType interpreted from the file name
     */
    protected void writeResponce(final PipedInputStream in, String filename, String mimeType) {
        new Thread() {
            @Override
            public void run() {
                try {
                    fileHandler.handleFile(in, filename, mimeType);
                    in.close();
                } catch (Exception e) {
                    getUI().access(() -> {
                        setComponentError(new UserError("Handling file failed"));
                        throw new RuntimeException(e);
                    });
                }
            }
        }.start();
    }

}
