package com.baselet.diagram.draw.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ColorOwn {
    private static final Logger log = LoggerFactory.getLogger(ColorOwn.class);

    public static enum Transparency {
        FOREGROUND(255), FULL_TRANSPARENT(0), DEPRECATED_WARNING(175), BACKGROUND(125), SELECTION_BACKGROUND(20);

        private int alpha;

        private Transparency(int alpha) {
            this.alpha = alpha;
        }

        public int getAlpha() {
            return alpha;
        }
    }

    /* fields should be final to avoid changing parts of existing color object (otherwise unexpected visible changes can happen) */
    protected final int red;
    protected final int green;
    protected final int blue;
    protected final int alpha;

    public ColorOwn(int red, int green, int blue, Transparency transparency) {
        this(red, green, blue, transparency.getAlpha());
    }

    public ColorOwn(int red, int green, int blue, int alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public ColorOwn(String hex) {
        int i = Integer.decode(hex);
        red = i >> 16 & 0xFF;
        green = i >> 8 & 0xFF;
        blue = i & 0xFF;
        alpha = Transparency.FOREGROUND.getAlpha();
    }

    public ColorOwn() {
        this(0, 0, 0, 0);
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    public int getAlpha() {
        return alpha;
    }

    public ColorOwn transparency(Transparency transparency) {
        return transparency(transparency.getAlpha());
    }

    public ColorOwn transparency(int alpha) {
        return new ColorOwn(getRed(), getGreen(), getBlue(), alpha);
    }

    public ColorOwn darken(int factor) {
        return new ColorOwn(Math.max(0, getRed() - factor), Math.max(0, getGreen() - factor), Math.max(0, getBlue() - factor), getAlpha());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + alpha;
        result = prime * result + blue;
        result = prime * result + green;
        result = prime * result + red;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ColorOwn other = (ColorOwn) obj;
        if (alpha != other.alpha) {
            return false;
        }
        if (blue != other.blue) {
            return false;
        }
        if (green != other.green) {
            return false;
        }
        if (red != other.red) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ColorOwn [red=" + red + ", green=" + green + ", blue=" + blue + ", alpha=" + alpha + "]";
    }
}
