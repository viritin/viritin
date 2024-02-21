package org.vaadin.viritin.it;

import org.junit.*;

/**
 * This abstract class can be used if one e.g. cannot afford TestBench license.
 *
 */
public class AbstractWebDriverCase extends AbstractBaseWebDriverCase {

    protected static final int TESTPORT = 5678;
    protected static final String BASEURL = "http://localhost:" + TESTPORT
            + "/";
    public AbstractWebDriverCase() {
        super();
    }

    @BeforeClass
    public static void startServer() {
    	System.out.println("AUTOMATIC SERVER START/STOP NOT SUPPORTED");
//        try {
//            server = new TServer().startServer(TESTPORT);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
    }

    @AfterClass
    public static void stopServer() {
    	System.out.println("AUTOMATIC SERVER START/STOP NOT SUPPORTED");
//        if (server != null) {
//            try {
//                server.stop();
//            } catch (Exception e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
    }
}