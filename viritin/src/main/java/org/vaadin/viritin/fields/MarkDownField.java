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
package org.vaadin.viritin.fields;

import org.vaadin.viritin.fluency.ui.FluentTextArea;
import org.vaadin.viritin.label.RichText;
import com.vaadin.shared.Registration;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Composite;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.TextArea;

public class MarkDownField extends Composite implements FluentTextArea<MarkDownField> {
  private final HorizontalLayout mainLayout = new HorizontalLayout();
  private final TabSheet tabSheet = new TabSheet();
  private final TextArea textArea = new TextArea();
  private final RichText previewLabel = new RichText();
  private Tab edit;
  private Tab preview;

  private String editCaption = "Edit";
  private String previewCaption = "Preview";

  public MarkDownField() {
    mainLayout.setSizeFull();
    mainLayout.setMargin(false);
    mainLayout.setSpacing(false);
    textArea.setSizeFull();
    previewLabel.setSizeFull();
    tabSheet.setSizeFull();

    buildTabs();

    tabSheet.addSelectedTabChangeListener(e -> {
      if (tabSheet.getSelectedTab().equals(previewLabel)) {
        previewLabel.withMarkDown(textArea.getValue());
      }
    });
    mainLayout.addComponent(tabSheet);

    setCompositionRoot(mainLayout);
  }

  public MarkDownField(String caption) {
    this();
    this.setCaption(caption);
  }

  public MarkDownField(String capition, String value) {
    this(capition);
    this.setValue(value);
  }

  public MarkDownField(String caption, String value,
      ValueChangeListener<String> valueChangeListener) {
    this(caption, value);
    this.addValueChangeListener(valueChangeListener);
  }

  public MarkDownField(ValueChangeListener<String> valueChangeListener) {
    this();
    this.addValueChangeListener(valueChangeListener);
  }

  private void buildTabs() {
    tabSheet.removeAllComponents();
    edit = tabSheet.addTab(textArea, editCaption);
    preview = tabSheet.addTab(previewLabel, previewCaption);
  }

  @Override
  public void setReadOnly(boolean readOnly) {
    textArea.setReadOnly(readOnly);
    mainLayout.removeAllComponents();
    if (readOnly) {
      mainLayout.addComponent(previewLabel);
    } else {
      buildTabs();
      mainLayout.addComponent(tabSheet);
    }
  }

  @Override
  public boolean isReadOnly() {
    return textArea.isReadOnly();
  }

  @Override
  public boolean isRequiredIndicatorVisible() {
    return super.isRequiredIndicatorVisible();
  }

  @Override
  public void setRequiredIndicatorVisible(boolean visible) {
    super.setRequiredIndicatorVisible(visible);
  }

  @Override
  public String getValue() {
    return textArea.getValue();
  }

  @Override
  public void setValue(String value) {
    textArea.setValue(value);
    previewLabel.withMarkDown(value);
  }

  @Override
  public Registration addValueChangeListener(ValueChangeListener<String> listener) {
    return textArea.addValueChangeListener(listener);
  }

  public void setPreviewCaption(String previewCaption) {
    this.previewCaption = previewCaption;
    preview.setCaption(previewCaption);
  }

  public MarkDownField withPreviewCaption(String previewCaption) {
    setPreviewCaption(previewCaption);
    return this;
  }

  public void setEditCaption(String editCaption) {
    this.editCaption = editCaption;
    edit.setCaption(editCaption);
  }

  public MarkDownField withEditCaption(String editCaption) {
    setEditCaption(editCaption);
    return this;
  }

  public String getPreviewCaption() {
    return previewCaption;
  }

  public String getEditCaption() {
    return editCaption;
  }

  @Override
  public int getTabIndex() {
    return textArea.getTabIndex();
  }

  @Override
  public void setTabIndex(int tabIndex) {
    textArea.setTabIndex(tabIndex);
  }

  @Override
  public void setValueChangeMode(ValueChangeMode valueChangeMode) {
    textArea.setValueChangeMode(valueChangeMode);
  }

  @Override
  public ValueChangeMode getValueChangeMode() {
    return textArea.getValueChangeMode();
  }

  @Override
  public void setValueChangeTimeout(int valueChangeTimeout) {
    textArea.setValueChangeTimeout(valueChangeTimeout);
  }

  @Override
  public int getValueChangeTimeout() {
    return textArea.getValueChangeTimeout();
  }

  @Override
  public void focus() {
    textArea.focus();
  }
}
