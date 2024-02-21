/*
 * Copyright 2019 Matti Tahvonen.
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
package org.vaadin.viritin.it;

import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.vaadin.viritin.components.UploadFileHandler;

/**
 *
 * @author mstahv
 */
public class UploadFileHandlerExample extends AbstractTest {

    @Override
    public Component getTestComponent() {
        UploadFileHandler uploadFileHandler = new UploadFileHandler(
                (InputStream content, String fileName, String mimeType) -> {
                    try {
                        int b = 0;
                        int count = 0;
                        while ((b = content.read()) != -1) {
                            if (b == "\n".getBytes()[0]) {
                                count++;
                            }
                        }
                        String msg = "Counted " + count + " lines";
                        getUI().access(() -> Notification.show(msg));
                    } catch (IOException ex) {
                        Logger.getLogger(UploadFileHandlerExample.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
        return uploadFileHandler;
    }

}
