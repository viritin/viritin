package org.vaadin.viritin.v7.fluency.ui;

import com.vaadin.v7.ui.TextArea;

public interface FluentTextArea<S extends FluentTextArea<S>>
        extends FluentAbstractTextField<S> {
    /**
     * Sets the text area's word-wrap mode on or off.
     *
     * @param wordwrap
     *            the boolean value specifying if the text area should be in
     *            word-wrap mode.
     * @return this for method chaining
     * @see TextArea#setWordwrap(boolean)
     * 
     */
    public default S withWordwrap(boolean wordwrap) {
        ((TextArea) this).setWordwrap(wordwrap);
        return (S) this;
    }

    /**
     * Sets the number of rows in the text area.
     *
     * @param rows
     *            the number of rows for this text area.
     * @see TextArea#setRows(int)
     * @return this for method chaining
     */
    public default S withRows(int rows) {
        ((TextArea) this).setRows(rows);
        return (S) this;
    }
}
