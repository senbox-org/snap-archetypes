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

package eu.esa.opt.dataio.simplereader.dimap;

import org.esa.snap.core.metadata.GenericXmlMetadata;
import org.esa.snap.core.metadata.XmlMetadataParser;
import org.esa.snap.core.metadata.XmlMetadataParserFactory;
import org.esa.snap.engine_utilities.utils.TestUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assume.assumeTrue;

// TODO - Rename this class
// TODO - Adapt variables/methods
// TODO - Adapt javadoc

/**
 * Unit test class for reader Metadata
 *
 */
public class SimpleReaderMetadataTest {

    private SimpleReaderMetadata metadata;
    // TODO - Adapt folder name
    private String productsFolder = "_simplereader" + File.separator;

    @Before
    public void setup() {
        assumeTrue(TestUtil.testdataAvailable());

        XmlMetadataParserFactory.registerParser(SimpleReaderMetadata.class, new XmlMetadataParser<>(SimpleReaderMetadata.class));
        metadata = GenericXmlMetadata.create(SimpleReaderMetadata.class, TestUtil.getTestFile(productsFolder + "DE01_SL6_22P_1T_20120905T170604_20120905T170613_DMI_0_4502/DE01_SL6_22P_1T_20120905T170604_20120905T170613_DMI_0_4502.dim"));
    }

    @After
    public void teardown() {
        metadata = null;
        System.gc();
    }

    @Test
    public void testGetFileName() throws Exception {
        assertEquals("DEIMOS", metadata.getFileName());
    }

    @Test
    public void testGetProductName() throws Exception {
        assertEquals("DE004502p_023150_046799_042858_045602", metadata.getProductName());
    }

    @Test
    public void testGetProductDescription() throws Exception {
        assertEquals("DE004502p_023150_046799_042858_045602", metadata.getProductDescription());
    }

    @Test
    public void testGetFormatName() throws Exception {
        assertEquals(SimpleReaderConstants.DIMAP, metadata.getFormatName());
    }

    @Test
    public void testGetMetadataProfile() throws Exception {
        assertEquals(SimpleReaderConstants.DEIMOS, metadata.getMetadataProfile());
    }

    @Test
    public void testGetRasterWidth() throws Exception {
        assertEquals(4338, metadata.getRasterWidth());
    }

    @Test
    public void testGetRasterHeight() throws Exception {
        assertEquals(1889, metadata.getRasterHeight());
    }

    @Test
    public void testGetRasterFileNames() throws Exception {
        String[] fileNames = metadata.getRasterFileNames();
        assertNotNull(fileNames);
        assertEquals(fileNames.length, 1);
        assertEquals("de01_sl6_22p_1t_20120905t170604_20120905t170613_dmi_0_4502.tif", fileNames[0]);
    }

    @Test
    public void testGetBandNames() throws Exception {
        String[] bandNames = metadata.getBandNames();
        assertNotNull(bandNames);
        assertEquals(bandNames.length, 3);
        assertEquals(SimpleReaderConstants.DEFAULT_BAND_NAMES[0], bandNames[0]);
        assertEquals(SimpleReaderConstants.DEFAULT_BAND_NAMES[1], bandNames[1]);
        assertEquals(SimpleReaderConstants.DEFAULT_BAND_NAMES[2], bandNames[2]);
    }

    @Test
    public void testGetNumBands() throws Exception {
        assertEquals(3, metadata.getNumBands());
    }

    @Test
    public void testGetNoDataValue() throws Exception {
        assertEquals(0, metadata.getNoDataValue());
    }

    @Test
    public void testGetNoDataColor() throws Exception {
        assertEquals(Color.BLACK, metadata.getNoDataColor());
    }

    @Test
    public void testGetSaturatedPixelValue() throws Exception {
        assertEquals(Integer.MAX_VALUE, metadata.getSaturatedPixelValue());
    }

    @Test
    public void testGetSaturatedColor() throws Exception {
        assertEquals(Color.BLACK, metadata.getSaturatedColor());
    }

    @Test
    public void testGetCenterTime() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date time = sdf.parse("2012-09-05 17:06:09");
        assertEquals(time.getTime(), metadata.getCenterTime().getAsDate().getTime());
    }

    @Test
    public void testGetProcessingLevel() throws Exception {
        assertEquals(SimpleReaderConstants.PROCESSING_2T, metadata.getProcessingLevel());
    }
}
