
package org.vaadin.viritin.layouts;

public class MMarginInfo extends FMarginInfo {

    public MMarginInfo(boolean enabled) {
        super(enabled);
    }

    public MMarginInfo(int bitMask) {
        super(bitMask);
    }

    public MMarginInfo(boolean top, boolean right, boolean bottom, boolean left) {
        super(top, right, bottom, left);
    }
    
    public MMarginInfo(boolean vertical, boolean horizontal) {
        super(vertical, horizontal, vertical, horizontal);
    }

}
