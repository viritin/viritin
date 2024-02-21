/*
 * Copyright 2016 Matti Tahvonen.
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

import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.ui.MNotification;

import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;

/**
 *
 * @author Max Schuster
 */
@Theme("valo")
public class MNotificationExapmle extends AbstractTest {

    private static final long serialVersionUID = 1298981554296094891L;

    @Override
    public Component getTestComponent() {
        MVerticalLayout layout = new MVerticalLayout(
                new MLabel("MNotification Examples").withStyleName("h1"),
                new MHorizontalLayout(
                        new MButton(VaadinIcons.COMMENT, "Humanized",
                                new Button.ClickListener() {
                            private static final long serialVersionUID = 5019806363620874205L;
                            
                            @Override
                            public void buttonClick(Button.ClickEvent event) {
                                MNotification.humanized("Humanized", "This is a humanized notification!")
                                                .withIcon(VaadinIcons.COMMENT);
                            }
                        }).withStyleName("primary"),
                        new MButton(VaadinIcons.CLOSE, "Error",
                                new Button.ClickListener() {
                            private static final long serialVersionUID = 5019806363620874205L;
                            
                            @Override
                            public void buttonClick(Button.ClickEvent event) {
                                MNotification.error("Error", "This is an error notification!")
                                                .withIcon(VaadinIcons.CLOSE);
                            }
                        }).withStyleName("danger"),
                        new MButton(VaadinIcons.EXCLAMATION, "Warning",
                                new Button.ClickListener() {
                            private static final long serialVersionUID = 5019806363620874205L;
                            
                            @Override
                            public void buttonClick(Button.ClickEvent event) {
                                MNotification.warning("Warning", "This is a warning notification!")
                                                .withIcon(
                                                        VaadinIcons.EXCLAMATION);
                            }
                        }),
                        new MButton(VaadinIcons.DOWNLOAD, "Tray",
                                new Button.ClickListener() {
                            private static final long serialVersionUID = 5019806363620874205L;
                            
                            @Override
                            public void buttonClick(Button.ClickEvent event) {
                                MNotification.tray("Tray", "This is a tray notification!")
                                                .withIcon(VaadinIcons.DOWNLOAD);
                            }
                        }).withStyleName("friendly"),
                        new MButton(VaadinIcons.ACCESSIBILITY, "Assistive",
                                new Button.ClickListener() {
                            private static final long serialVersionUID = 5019806363620874205L;
                            
                            @Override
                            public void buttonClick(Button.ClickEvent event) {
                                MNotification.assistive("Assistive", "This is an assistive notification!");
                            }
                        }).withStyleName("quiet")
                )
        ).withFullWidth();

        return layout;
    }

}
