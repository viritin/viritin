/*
 * Copyright 2012 Vaadin Community.
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

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.HtmlRenderer;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mattitahvonenitmill
 */
@Theme("valo")
@Widgetset("com.vaadin.v7.Vaadin7WidgetSet")
public class TListUi extends UI {

    public static class TestDetails {

        private Class clazz;
        private String name;
        private String description;

        public TestDetails(Class clazz, String name) {
            this.clazz = clazz;
            this.name = name;
        }

        public Class getClazz() {
            return clazz;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getName() {
            return name;
        }

    }

    private List<TestDetails> testClassess;

    @Override
    protected void init(VaadinRequest request) {
        loadTestClasses(this);
    }

    private void loadTestClasses(TListUi aThis) {
        if (testClassess != null) {
            return;
        }
        testClassess = listTestClasses();
        Grid<TestDetails> grid = new Grid();
        grid.addColumn(v -> String.format("<a href='/%s' target='_new'>%s</a>", v.getClazz().getName(),v.getName()), new HtmlRenderer());
        // TODO how to make this wrap/truncated by default???
        grid.addColumn(TestDetails::getDescription).setCaption("description").setExpandRatio(1);
        grid.setSizeFull();
        grid.setItems(testClassess);
        VerticalLayout verticalLayout = new VerticalLayout();
        TextField filter = new TextField();
        filter.setPlaceholder("Filter list");
        filter.addValueChangeListener(e -> {
            String f = e.getValue();
            grid.setItems(testClassess.stream().filter(
                    c -> (c.getName() + c.getDescription()).toLowerCase()
                            .contains(f.toLowerCase())
            ));
        });
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        verticalLayout.addComponent(filter);
        filter.focus();
        verticalLayout.addComponent(grid);
        verticalLayout.setSizeFull();
        verticalLayout.setExpandRatio(grid, 1);
        verticalLayout.setMargin(true);
        setContent(verticalLayout);
    }

    public static List<TestDetails> listTestClasses() {

        List<TestDetails> l = new ArrayList<>();

        final File testroot = getTestRoot();

        if (testroot.exists()) {
            try {
                Files.walkFileTree(testroot.toPath(),
                        new SimpleFileVisitor<Path>() {

                    @Override
                    public FileVisitResult visitFile(Path f,
                            BasicFileAttributes attrs) {
                        if (!f.toString().endsWith(".java")) {
                            return FileVisitResult.CONTINUE;
                        }
                        try {
                            String name = f.getFileName().toString().replace(".java", "");
                            Path packageDir = testroot.toPath().
                                    relativize(f.getParent());
                            String packageName = packageDir.toString().
                                    replaceAll("[/\\\\]", ".");
                            if (!packageName.isEmpty()) {
                                packageName += ".";
                            }

                            Class<?> forName = Class.forName(
                                    packageName + name);

                            addTest(l, name, forName);
                        } catch (Exception e) {
                            // e.printStackTrace();
                            // e.printStackTrace();
                        }
                        return FileVisitResult.CONTINUE;
                    }

                }
                );
            } catch (IOException ex) {
                Logger.getLogger(TListUi.class.getName()).
                        log(Level.SEVERE, null, ex);
            }
        }
        return l;
    }

    protected static File getTestRoot() {
        return new File("src/test/java/");
    }

    static void addTest(List<TestDetails> indexedContainer, String simpleName,
            Class<?> forName) throws InstantiationException, IllegalAccessException {
        if (UI.class.isAssignableFrom(forName)) {
            UI newInstance = (UI) forName.newInstance();
            TestDetails testDetails = new TestDetails(forName, simpleName);
            indexedContainer.add(testDetails);
            testDetails.setDescription(newInstance.getDescription());
        }
    }

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = TListUi.class)
    public static class Servlet extends VaadinServlet {
        final AbstractUIProviderImpl uiprovider = new AbstractUIProviderImpl();

        @Override
        public void init(ServletConfig servletConfig)
                throws ServletException {
    		super.init(servletConfig);
    		getService().addSessionInitListener(new SessionInitListener() {
                @Override
                public void sessionInit(SessionInitEvent event)
                        throws ServiceException {
                    event.getSession().addUIProvider(uiprovider);
                }
            });
    	}    	
    }
    
}