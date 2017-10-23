package com.noxag.newnox.textanalyzer.util;

import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;

/**
 * This class provides {@link PDColor} constants
 * 
 * @author Tobias.Schmidt@de.ibm.com
 *
 */
public class PDFColors {
    public static final PDColor RED = new PDColor(new float[] { 1, 0, 0 }, PDDeviceRGB.INSTANCE);
    public static final PDColor MAROON = new PDColor(new float[] { 0.502f, 0, 0 }, PDDeviceRGB.INSTANCE);
    public static final PDColor CRIMSON = new PDColor(new float[] { 0.863f, 0.078f, 0.235f }, PDDeviceRGB.INSTANCE);
    public static final PDColor DARK_ORANGE = new PDColor(new float[] { 1, 0.549f, 0 }, PDDeviceRGB.INSTANCE);
    public static final PDColor ORANGE = new PDColor(new float[] { 1, 0.502f, 0 }, PDDeviceRGB.INSTANCE);

    public static final PDColor GOLD = new PDColor(new float[] { 1, 0.843f, 0 }, PDDeviceRGB.INSTANCE);
    public static final PDColor DARK_GOLDEN_ROD = new PDColor(new float[] { 0.722f, 0.525f, 0.043f },
            PDDeviceRGB.INSTANCE);
    public static final PDColor DARK_OLIVE_GREEN = new PDColor(new float[] { .502f, .502f, 0 }, PDDeviceRGB.INSTANCE);
    public static final PDColor OLIVE = new PDColor(new float[] { .502f, .502f, 0 }, PDDeviceRGB.INSTANCE);
    public static final PDColor LIGHT_OLIVE = new PDColor(new float[] { 0.753f, 0.753f, 0 }, PDDeviceRGB.INSTANCE);
    public static final PDColor DARK_GREEN = new PDColor(new float[] { 0, 0.392f, 0 }, PDDeviceRGB.INSTANCE);
    public static final PDColor GREEN = new PDColor(new float[] { 0, .502f, 0 }, PDDeviceRGB.INSTANCE);
    public static final PDColor GREY = new PDColor(new float[] { 0.627f, 0.627f, 0.627f }, PDDeviceRGB.INSTANCE);
    public static final PDColor CHOCOLATE = new PDColor(new float[] { 0.824f, 0.412f, 0.004f }, PDDeviceRGB.INSTANCE);
    public static final PDColor BROWN = new PDColor(new float[] { 0.545f, 0.271f, 0.075f }, PDDeviceRGB.INSTANCE);
    public static final PDColor MOCCASIN = new PDColor(new float[] { 1, 0.894f, 0.71f }, PDDeviceRGB.INSTANCE);

    public static final PDColor DARK_GREY = new PDColor(new float[] { 0.376f, 0.376f, 0.376f }, PDDeviceRGB.INSTANCE);
    public static final PDColor BLACK = new PDColor(new float[] { 0, 0, 0 }, PDDeviceRGB.INSTANCE);
    public static final PDColor WHITE = new PDColor(new float[] { 1, 1, 1 }, PDDeviceRGB.INSTANCE);
    public static final PDColor LIGHT_GREY = new PDColor(new float[] { 0.753f, 0.753f, 0.753f }, PDDeviceRGB.INSTANCE);
    public static final PDColor VERY_LIGHT_GREY = new PDColor(new float[] { 0.9f, 0.9f, 0.9f }, PDDeviceRGB.INSTANCE);
    public static final PDColor SKY_BLUE = new PDColor(new float[] { 0.529f, 0.808f, 1 }, PDDeviceRGB.INSTANCE);

    private PDFColors() {
        // hide constructor, because this is a completely static class
    }

}
