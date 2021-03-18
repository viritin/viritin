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
package org.vaadin.viritin.fluency.ui;

import com.vaadin.data.HasValue;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.TextArea;

public interface FluentTextArea<S extends FluentTextArea<S>>
    extends FluentAbstractField<S, String>, FluentHasValueChangeMode<S> {
  /**
   * Sets the number of rows in the text area.
   *
   * @param rows the number of rows for this text area.
   * @return this object for further configuration 
   */
  public default S withRows(int rows) {
    ((TextArea) this).setRows(rows);
    return (S) this;
  }

  /**
   * Sets the text area's word-wrap mode on or off.
   *
   * @param wordWrap <code>true</code> to use word-wrap mode <code>false</code> otherwise.
   * @return this object for further configuration 
   */
  public default S withWordWrap(boolean wordWrap) {
    ((TextArea) this).setWordWrap(wordWrap);
    return (S) this;
  }

  public default S addTextChangeListener(HasValue.ValueChangeListener<String> l) {
    this.addValueChangeListener(l);
    this.setValueChangeMode(ValueChangeMode.LAZY);
    return (S) this;
  }
}
