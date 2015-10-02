package org.vaadin.viritin;

import com.vaadin.server.SizeWithUnit;
import com.vaadin.server.Sizeable;

import java.io.Serializable;

/**
 * Created on 23/09/2015.
 *
 * @author Panos Bariamis
 */
public class MSize implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final MSize FULL_WIDTH = MSize.width(100, Sizeable.Unit.PERCENTAGE);
    public static final MSize FULL_HEIGHT = MSize.height(100, Sizeable.Unit.PERCENTAGE);
    public static final MSize FULL_SIZE = MSize.size(100, Sizeable.Unit.PERCENTAGE, 100, Sizeable.Unit.PERCENTAGE);
    public static final MSize HALF_WIDTH = MSize.width(50, Sizeable.Unit.PERCENTAGE);
    public static final MSize HALF_HEIGHT = MSize.height(50, Sizeable.Unit.PERCENTAGE);
    public static final MSize HALF_SIZE = MSize.size(50, Sizeable.Unit.PERCENTAGE, 50, Sizeable.Unit.PERCENTAGE);

    private float width = -1;
    private float height = -1;
    private Sizeable.Unit widthUnit = Sizeable.Unit.PIXELS;
    private Sizeable.Unit heightUnit = Sizeable.Unit.PIXELS;

    private MSize(float width, Sizeable.Unit widthUnit, float height, Sizeable.Unit heightUnit) {
        this.width = width;
        this.widthUnit = widthUnit;
        this.height = height;
        this.heightUnit = heightUnit;
    }

    public float getWidth() {
        return width;
    }

    public Sizeable.Unit getWidthUnit() {
        return widthUnit;
    }

    public float getHeight() {
        return height;
    }

    public Sizeable.Unit getHeightUnit() {
        return heightUnit;
    }

    public static MSize width(float width, Sizeable.Unit widthUnit) {
        return new MSize(width, widthUnit, -1, Sizeable.Unit.PIXELS);
    }

    public static MSize width(String width) {
        SizeWithUnit size = SizeWithUnit.parseStringSize(width);
        return size != null
                ? new MSize(size.getSize(), size.getUnit(), -1, Sizeable.Unit.PIXELS)
                : new MSize(-1, Sizeable.Unit.PIXELS, -1, Sizeable.Unit.PIXELS);
    }

    public static MSize height(float height, Sizeable.Unit heightUnit) {
        return new MSize(-1, Sizeable.Unit.PIXELS, height, heightUnit);
    }

    public static MSize height(String height) {
        SizeWithUnit size = SizeWithUnit.parseStringSize(height);
        return size != null
                ? new MSize(-1, Sizeable.Unit.PIXELS, size.getSize(), size.getUnit())
                : new MSize(-1, Sizeable.Unit.PIXELS, -1, Sizeable.Unit.PIXELS);
    }

    public static MSize size(float width, Sizeable.Unit widthUnit, float height, Sizeable.Unit heightUnit) {
        return new MSize(width, widthUnit, height, heightUnit);
    }

    public static MSize size(String width, String height) {
        float w = -1, h = -1;
        Sizeable.Unit wu = Sizeable.Unit.PIXELS, hu = Sizeable.Unit.PIXELS;

        SizeWithUnit size = SizeWithUnit.parseStringSize(width);
        if (size != null) {
            w = size.getSize();
            wu = size.getUnit();
        }

        size = SizeWithUnit.parseStringSize(height);
        if (size != null) {
            h = size.getSize();
            hu = size.getUnit();
        }

        return new MSize(w, wu, h, hu);
    }
}