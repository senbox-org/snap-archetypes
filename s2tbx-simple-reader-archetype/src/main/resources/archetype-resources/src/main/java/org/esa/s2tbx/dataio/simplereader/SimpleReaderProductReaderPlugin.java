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

package org.esa.s2tbx.dataio.simplereader;

import org.esa.s2tbx.dataio.simplereader.dimap.SimpleReaderConstants;
import org.esa.s2tbx.dataio.readers.BaseProductReaderPlugIn;
import org.esa.snap.core.dataio.DecodeQualification;
import org.esa.snap.core.metadata.MetadataInspector;
import org.esa.snap.core.dataio.ProductReader;
import org.esa.snap.core.datamodel.RGBImageProfile;
import org.esa.snap.core.datamodel.RGBImageProfileManager;

import java.util.Locale;

/**
 * Plugin for reading DEIMOS-1 files.
 * The files are GeoTIFF with DIMAP metadata.
 *
 * @author  Cosmin Cara
 */
public class SimpleReaderProductReaderPlugin extends BaseProductReaderPlugIn {
    private static final String COLOR_PALETTE_FILE_NAME = "Sample_color_palette.cpd";

    public SimpleReaderProductReaderPlugin() {
        super("org/esa/s2tbx/dataio/simplereader/" + SimpleReaderProductReaderPlugin.COLOR_PALETTE_FILE_NAME);
    }

    @Override
    public MetadataInspector getMetadataInspector() {
        return new SimpleReaderMetadataInspector();
    }

    @Override
    public Class[] getInputTypes() {
        return SimpleReaderConstants.DIMAP_READER_INPUT_TYPES;
    }

    @Override
    public ProductReader createReaderInstance() {
        return new SimpleReaderProductReader(this, getColorPaletteFilePath());
    }

    @Override
    public String[] getFormatNames() {
        return SimpleReaderConstants.DIMAP_FORMAT_NAMES;
    }

    @Override
    public String[] getDefaultFileExtensions() {
        return SimpleReaderConstants.DIMAP_DEFAULT_EXTENSIONS;
    }

    @Override
    public String getDescription(Locale locale) {
        return SimpleReaderConstants.DIMAP_DESCRIPTION;
    }

    @Override
    protected String[] getMinimalPatternList() { return SimpleReaderConstants.MINIMAL_PRODUCT_PATTERNS; }

    @Override
    protected String[] getExclusionPatternList() { return new String[0]; }

    @Override
    protected void registerRGBProfile() {
        RGBImageProfileManager.getInstance().addProfile(new RGBImageProfile("DEIMOS-1", new String[] { "Red", "Green", "NIR" }));
    }

    @Override
    public DecodeQualification getDecodeQualification(Object input) {
        return super.getDecodeQualification(input);
    }
}
