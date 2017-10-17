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
    public static final PDColor ORANGE = new PDColor(new float[] { 1, 0.502f, 0 }, PDDeviceRGB.INSTANCE);
    public static final PDColor YELLOW = new PDColor(new float[] { 1, 1, 0 }, PDDeviceRGB.INSTANCE);
    public static final PDColor YELLOW_GREEN = new PDColor(new float[] { 0.502f, 1, 0 }, PDDeviceRGB.INSTANCE);
    public static final PDColor GREEN = new PDColor(new float[] { 0, 1, 0 }, PDDeviceRGB.INSTANCE);
    public static final PDColor GREEN_CYAN = new PDColor(new float[] { 0, 1, 0.502f }, PDDeviceRGB.INSTANCE);
    public static final PDColor CYAN = new PDColor(new float[] { 0, 1, 1 }, PDDeviceRGB.INSTANCE);
    public static final PDColor CYAN_BLUE = new PDColor(new float[] { 0, 0.502f, 1 }, PDDeviceRGB.INSTANCE);
    public static final PDColor BLUE = new PDColor(new float[] { 0, 1, 0 }, PDDeviceRGB.INSTANCE);
    public static final PDColor VIOLET = new PDColor(new float[] { 0.502f, 1, 0 }, PDDeviceRGB.INSTANCE);
    public static final PDColor MAGENTA = new PDColor(new float[] { 1, 1, 0 }, PDDeviceRGB.INSTANCE);
    public static final PDColor DEEP_PINK = new PDColor(new float[] { 1, 0.502f, 0 }, PDDeviceRGB.INSTANCE);
    public static final PDColor WHITE = new PDColor(new float[] { 1, 1, 1 }, PDDeviceRGB.INSTANCE);
    public static final PDColor LIGHT_GREY = new PDColor(new float[] { 0.753f, 0.753f, 0.753f }, PDDeviceRGB.INSTANCE);
    public static final PDColor GREY = new PDColor(new float[] { 0.627f, 0.627f, 0.627f }, PDDeviceRGB.INSTANCE);
    public static final PDColor DARK_GREY = new PDColor(new float[] { 0.376f, 0.376f, 0.376f }, PDDeviceRGB.INSTANCE);
    public static final PDColor BLACK = new PDColor(new float[] { 0, 0, 0 }, PDDeviceRGB.INSTANCE);

    private PDFColors() {
        // hide constructor, because this is a completely static class
    }

}
