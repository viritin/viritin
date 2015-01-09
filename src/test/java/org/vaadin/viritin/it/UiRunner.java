
package org.vaadin.viritin.it;

import java.io.File;
import org.vaadin.addonhelpers.TServer;

/**
 *
 * @author Matti Tahvonen
 */
public class UiRunner extends TServer {
    
    public static void main(String... args) throws Exception {
        new File("target/testwebapp").mkdir();
        new TServer().startServer();
    }

}
