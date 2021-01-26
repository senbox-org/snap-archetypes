/*
 * Copyright (C) 2021 by ?
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option)
 * any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, see http://www.gnu.org/licenses/
 */

package org.esa.s2tbx.dataio.simplereader.dimap;

import org.esa.snap.core.metadata.XmlMetadata;
import org.esa.snap.core.datamodel.ProductData;

import java.awt.*;

import static org.esa.snap.utils.DateHelper.parseDate;

/**
 * Holder for DIMAP metadata file.
 *
 * @author Cosmin Cara
 */
public class SimpleReaderMetadata extends XmlMetadata {

    private float[] bandGains;
    private float[] bandBiases;

    public SimpleReaderMetadata(String name) {
        super(name);
    }

    @Override
    public String getFileName() {
        return name;
    }

    @Override
    public String getProductName() {
        String name = getAttributeValue(SimpleReaderConstants.PATH_SOURCE_ID, SimpleReaderConstants.VALUE_NOT_AVAILABLE);
        rootElement.setDescription(name);
        return name;
    }

    public String getProductDescription() {
        String descr = getAttributeValue(SimpleReaderConstants.PATH_SOURCE_DESCRIPTION, SimpleReaderConstants.VALUE_NOT_AVAILABLE);
        if (SimpleReaderConstants.VALUE_NOT_AVAILABLE.equals(descr)) {
            descr = getAttributeValue(SimpleReaderConstants.PATH_SOURCE_ID, SimpleReaderConstants.VALUE_NOT_AVAILABLE);
        }
        rootElement.setDescription(descr);
        return descr;
    }

    @Override
    public String getFormatName() {
        return getAttributeValue(SimpleReaderConstants.PATH_METADATA_FORMAT, SimpleReaderConstants.DIMAP);
    }

    @Override
    public String getMetadataProfile() {
        return getAttributeValue(SimpleReaderConstants.PATH_METADATA_PROFILE, SimpleReaderConstants.DEIMOS);
    }

    @Override
    public int getRasterWidth() {
        if (width == 0) {
            try {
                width = Integer.parseInt(getAttributeValue(SimpleReaderConstants.PATH_NCOLS, SimpleReaderConstants.STRING_ZERO));
            } catch (NumberFormatException e) {
                warn(MISSING_ELEMENT_WARNING, SimpleReaderConstants.PATH_NCOLS);
            }
        }
        return width;
    }

    @Override
    public int getRasterHeight() {
        if (height == 0) {
            try {
                height = Integer.parseInt(getAttributeValue(SimpleReaderConstants.PATH_NROWS, SimpleReaderConstants.STRING_ZERO));
            } catch (NumberFormatException e) {
                warn(MISSING_ELEMENT_WARNING, SimpleReaderConstants.PATH_NROWS);
            }
        }
        return height;
    }

    @Override
    public String[] getRasterFileNames() {
        String path = getAttributeValue(SimpleReaderConstants.PATH_DATA_FILE_PATH, null);
        return (path != null ? new String[] { path.toLowerCase() } : null);
    }

    @Override
    public ProductData.UTC getProductStartTime() {
        return null;
    }

    @Override
    public ProductData.UTC getProductEndTime() {
        return null;
    }

    /**
     * Returns the names of the bands found in the metadata file.
     * If the expected metadata nodes are not present, then the default band names
     * are returned (i.e. band_n).
     *
     * @return an array of band names
     */
    public String[] getBandNames() {
        int nBands = getNumBands();
        String[] names = new String[nBands];
        for (int i = 0; i < nBands; i++) {
            names[i] = getAttributeValue(SimpleReaderConstants.PATH_BAND_DESCRIPTION, i, SimpleReaderConstants.DEFAULT_BAND_NAMES[i]);
            if (names[i].contains(" ")) {
                names[i] = names[i].replace(" ", "_");
            }
        }
        return names;
    }

    public String[] getBandUnits() {
        int nBands = getNumBands();
        String[] units = new String[nBands];
        for (int i = 0; i < nBands; i++) {
            units[i] = getAttributeValue(SimpleReaderConstants.PATH_PHYSICAL_UNIT, i, SimpleReaderConstants.DEFAULT_UNIT);
        }
        return units;
    }

    @Override
    public int getNumBands() {
        if (numBands == 0) {
            try {
                numBands = Integer.parseInt(getAttributeValue(SimpleReaderConstants.PATH_NBANDS, "3"));
            } catch (NumberFormatException e) {
                warn(MISSING_ELEMENT_WARNING, SimpleReaderConstants.PATH_NBANDS);
            }
        }
        return numBands;
    }

    public int getNoDataValue() {
        int noData = Integer.MIN_VALUE;
        try {
            noData = Integer.parseInt(getAttributeSiblingValue(SimpleReaderConstants.PATH_SPECIAL_VALUE_TEXT, SimpleReaderConstants.NODATA_VALUE, SimpleReaderConstants.PATH_SPECIAL_VALUE_INDEX, Integer.toString(Integer.MIN_VALUE)));
        } catch (NumberFormatException e) {
            warn(MISSING_ELEMENT_WARNING, SimpleReaderConstants.NODATA_VALUE);
        }
        return noData;
    }

    public Color getNoDataColor() {
        Color color;
        try {
            int red = (int) (SimpleReaderConstants.MAX_LEVEL * Double.parseDouble(getAttributeSiblingValue(SimpleReaderConstants.PATH_SPECIAL_VALUE_TEXT, SimpleReaderConstants.NODATA_VALUE, SimpleReaderConstants.PATH_SPECIAL_VALUE_COLOR_RED_LEVEL, SimpleReaderConstants.STRING_ZERO)));
            int green = (int) (SimpleReaderConstants.MAX_LEVEL * Double.parseDouble(getAttributeSiblingValue(SimpleReaderConstants.PATH_SPECIAL_VALUE_TEXT, SimpleReaderConstants.NODATA_VALUE, SimpleReaderConstants.PATH_SPECIAL_VALUE_COLOR_GREEN_LEVEL, SimpleReaderConstants.STRING_ZERO)));
            int blue = (int) (SimpleReaderConstants.MAX_LEVEL * Double.parseDouble(getAttributeSiblingValue(SimpleReaderConstants.PATH_SPECIAL_VALUE_TEXT, SimpleReaderConstants.NODATA_VALUE, SimpleReaderConstants.PATH_SPECIAL_VALUE_COLOR_BLUE_LEVEL, SimpleReaderConstants.STRING_ZERO)));
            color = new Color(red, green, blue);
        } catch (NumberFormatException e) {
            color = Color.BLACK;
        }
        return color;
    }

    public int getSaturatedPixelValue() {
        int saturatedValue = Integer.MAX_VALUE;
        try {
            saturatedValue = Integer.parseInt(getAttributeSiblingValue(SimpleReaderConstants.PATH_SPECIAL_VALUE_TEXT, SimpleReaderConstants.SATURATED_VALUE, SimpleReaderConstants.PATH_SPECIAL_VALUE_INDEX, Integer.toString(Integer.MAX_VALUE)));
        } catch (NumberFormatException nfe) {
            warn(MISSING_ELEMENT_WARNING, SimpleReaderConstants.SATURATED_VALUE);
        }
        return saturatedValue;
    }

    public Color getSaturatedColor() {
        Color color;
        try {
            int red = (int) (SimpleReaderConstants.MAX_LEVEL * Double.parseDouble(getAttributeSiblingValue(SimpleReaderConstants.PATH_SPECIAL_VALUE_TEXT, SimpleReaderConstants.SATURATED_VALUE, SimpleReaderConstants.PATH_SPECIAL_VALUE_COLOR_RED_LEVEL, SimpleReaderConstants.STRING_ZERO)));
            int green = (int) (SimpleReaderConstants.MAX_LEVEL * Double.parseDouble(getAttributeSiblingValue(SimpleReaderConstants.PATH_SPECIAL_VALUE_TEXT, SimpleReaderConstants.SATURATED_VALUE, SimpleReaderConstants.PATH_SPECIAL_VALUE_COLOR_GREEN_LEVEL, SimpleReaderConstants.STRING_ZERO)));
            int blue = (int) (SimpleReaderConstants.MAX_LEVEL * Double.parseDouble(getAttributeSiblingValue(SimpleReaderConstants.PATH_SPECIAL_VALUE_TEXT, SimpleReaderConstants.SATURATED_VALUE, SimpleReaderConstants.PATH_SPECIAL_VALUE_COLOR_BLUE_LEVEL, SimpleReaderConstants.STRING_ZERO)));
            color = new Color(red, green, blue);
        } catch (NumberFormatException e) {
            color = Color.WHITE;
        }
        return color;
    }

    public ProductData.UTC getCenterTime() {
        ProductData.UTC centerTime = null;
        String stringDate = getAttributeValue(SimpleReaderConstants.PATH_SCENE_CENTER_DATE, null);
        if (stringDate != null) {
            String stringTime = getAttributeValue(SimpleReaderConstants.PATH_SCENE_CENTER_TIME, null);
            if (stringTime == null) {
                warn(MISSING_ELEMENT_WARNING, SimpleReaderConstants.PATH_SCENE_CENTER_TIME);
                stringTime = "00:00:00";
            }
            centerTime = parseDate(stringDate + " " + stringTime, SimpleReaderConstants.DEIMOS_DATE_FORMAT);
        } else {
            warn(MISSING_ELEMENT_WARNING, SimpleReaderConstants.PATH_SCENE_CENTER_DATE);
        }
        return centerTime;
    }

    private void extractGainsAndBiases() {
        if (bandGains == null || bandBiases == null) {
            int nBands = getNumBands();
            bandGains = new float[nBands];
            // we extract both, because they are used in conjunction
            bandBiases = new float[nBands];
            try {
                for (int i = 0; i < nBands; i++) {
                    bandBiases[i] = Float.parseFloat(getAttributeValue(SimpleReaderConstants.PATH_PHYSICAL_BIAS, i, SimpleReaderConstants.STRING_ZERO)) * SimpleReaderConstants.UNIT_MULTIPLIER;
                    bandGains[i] = Float.parseFloat(getAttributeValue(SimpleReaderConstants.PATH_PHYSICAL_GAIN, i, SimpleReaderConstants.STRING_ZERO)) * SimpleReaderConstants.UNIT_MULTIPLIER;
                }
            } catch (NumberFormatException e) {
                warn(MISSING_ELEMENT_WARNING, SimpleReaderConstants.PATH_SPECTRAL_BAND_INFO);
            }
        }
    }

    public String getProcessingLevel() {
        String value = null;
        try {
            value = getAttributeValue(SimpleReaderConstants.PATH_GEOMETRIC_PROCESSING, null);
        } catch (Exception e) {
            warn(MISSING_ELEMENT_WARNING, SimpleReaderConstants.PATH_GEOMETRIC_PROCESSING);
        }
        if (value == null)
            value = SimpleReaderConstants.PROCESSING_2T;
        return value;
    }

    public InsertionPoint[] getGeopositionPoints() {
        InsertionPoint[] points = null;
        try {
            String[] dataX = getAttributeValues(SimpleReaderConstants.PATH_TIE_POINT_DATA_X);
            if (dataX != null) {
                String[] dataY = getAttributeValues(SimpleReaderConstants.PATH_TIE_POINT_DATA_Y);
                String[] crsX = getAttributeValues(SimpleReaderConstants.PATH_TIE_POINT_CRS_X);
                String[] crsY = getAttributeValues(SimpleReaderConstants.PATH_TIE_POINT_CRS_Y);
                points = new InsertionPoint[dataX.length];
                for (int i = 0; i < points.length; i++) {
                    points[i] = new InsertionPoint();
                    points[i].x = Float.parseFloat(crsX[i]);
                    points[i].y = Float.parseFloat(crsY[i]);
                    points[i].stepX = Float.parseFloat(dataX[i]);
                    points[i].stepY = Float.parseFloat(dataY[i]);
                }
            }
        } catch (Exception e) {
            warn(MISSING_ELEMENT_WARNING, SimpleReaderConstants.PATH_GEOMETRIC_PROCESSING);
        }
        return points;
    }

    public class InsertionPoint {
        public float x;
        public float y;
        public float stepX;
        public float stepY;
    }
}
