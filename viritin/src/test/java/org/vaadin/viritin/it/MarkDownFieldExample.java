/*
 * Copyright 2020 Daniel Nordhoff-Vergien.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.vaadin.viritin.it;

import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MarkDownField;
import org.vaadin.viritin.layouts.MVerticalLayout;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

public class MarkDownFieldExample extends AbstractTest {

  @Override
  public Component getTestComponent() {
    MarkDownField markDownField1 =
        new MarkDownField("MarkDownField 1").withValue("This is **Markdown** formatted *text*");
    MarkDownField markDownField2 = new MarkDownField("With TextChangeListener").withValue(
        "This has a textChangeListener attached")
        .addTextChangeListener(e -> Notification.show(e.getValue(), Type.HUMANIZED_MESSAGE));

    return new MVerticalLayout(markDownField1, //
        new MButton("Toggle ReadOnly",
            e -> markDownField1.setReadOnly(!markDownField1.isReadOnly())), //
        markDownField2);
  }

}
