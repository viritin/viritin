
package org.vaadin.viritin.webjar;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Matti Tahvonen
 */
@WebServlet(urlPatterns = "/webjars/*")
public class WebJarServlet extends HttpServlet {

        private RequestDispatcher defaultDispatcher;

        @Override
        public void init() throws ServletException {
            super.init();
            defaultDispatcher = getServletContext().
                    getNamedDispatcher("default");
        }

        @Override
        protected void service(HttpServletRequest request,
                HttpServletResponse response) throws ServletException, IOException {
            defaultDispatcher.forward(request, response);
        }

}
