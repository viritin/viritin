
package org.vaadin.viritin.webjar;

import com.vaadin.server.BootstrapFragmentResponse;
import com.vaadin.server.BootstrapListener;
import com.vaadin.server.BootstrapPageResponse;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.VaadinServletService;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author Matti Tahvonen
 */

@WebListener
public class WebjarSupport implements javax.servlet.http.HttpSessionListener {
    private boolean inited = false;

    public WebjarSupport() { 
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        if(!inited) {
            VaadinServletService.getCurrent().addSessionInitListener(new SessionInitListener() {

                @Override
                public void sessionInit(SessionInitEvent event) throws ServiceException {
            VaadinSession.getCurrent().addBootstrapListener(new BootstrapListener() {

                @Override
                public void modifyBootstrapFragment(
                        BootstrapFragmentResponse response) {
                }

                @Override
                public void modifyBootstrapPage(BootstrapPageResponse response) {
                    Class<? extends UI> uiClass = response.getUiClass();
                    WebjarResource annotation = uiClass.getAnnotation(WebjarResource.class);
                    if(annotation == null) {
                        return;
                    }
                    // Update the bootstrap page
                    Document document = response.getDocument();
                    Element head = document.getElementsByTag("head").get(0);
                    for(String res : annotation.value()) {
                        if(res.endsWith(".js")) {
                            Element script = document.createElement("script");
                            script.attr("type", "text/javascript");
                            script.attr("src", "webjars/" + res);
                            head.appendChild(script);
                        } else if (res.endsWith(".css")) {
                            Element s = document.createElement("link");
                            s.attr("href", "webjars/"+res);
                            s.attr("rel", "stylesheet");
                            head.appendChild(s);
                        }
                    }
                    
                }
            });
            
                inited = true;
                }
            });
        }
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
    }
}