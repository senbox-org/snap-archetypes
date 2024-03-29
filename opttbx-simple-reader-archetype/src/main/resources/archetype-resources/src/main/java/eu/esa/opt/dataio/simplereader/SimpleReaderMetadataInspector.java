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

package eu.esa.opt.dataio.simplereader;

import org.esa.snap.engine_utilities.commons.FilePathInputStream;
import org.esa.snap.engine_utilities.dataio.VirtualDirEx;
import eu.esa.opt.dataio.simplereader.dimap.SimpleReaderConstants;
import eu.esa.opt.dataio.simplereader.dimap.SimpleReaderMetadata;
import eu.esa.opt.dataio.readers.MetadataList;
import eu.esa.opt.dataio.readers.RastersMetadata;
import org.esa.snap.core.datamodel.GeoCoding;
import org.esa.snap.core.metadata.MetadataInspector;
import org.esa.snap.dataio.geotiff.GeoTiffImageReader;
import org.esa.snap.dataio.geotiff.GeoTiffProductReader;

import java.io.IOException;
import java.nio.file.Path;

// TODO - Rename this class
// TODO - Adapt variables/methods
// TODO - Adapt javadoc

/**
 * Sample class.
 */
public class SimpleReaderMetadataInspector implements MetadataInspector {

    public SimpleReaderMetadataInspector() {
    }

    @Override
    public Metadata getMetadata(Path productPath) throws IOException {
        try (VirtualDirEx productDirectory = VirtualDirEx.build(productPath, false, true)) {
            MetadataList<SimpleReaderMetadata> metadataList = SimpleReaderProductReader.readMetadata(productDirectory);

            RastersMetadata rastersMetadata = SimpleReaderProductReader.computeMaximumDefaultProductSize(metadataList, productDirectory);

            Metadata metadata = new Metadata(rastersMetadata.getMaximumWidh(), rastersMetadata.getMaximumHeight());

            GeoCoding productGeoCoding = addGeoCoding(metadataList, productDirectory);
            metadata.setGeoCoding(productGeoCoding);

            for (int i = 0; i < metadataList.getCount(); i++) {
                SimpleReaderMetadata currentMetadata = metadataList.getMetadataAt(i);
                String[] bandNames = currentMetadata.getBandNames();
                String bandPrefix = SimpleReaderProductReader.computeBandPrefix(metadataList.getCount(), i);
                int rasterBandCount = rastersMetadata.getRasterBandCount(currentMetadata);
                for (int bandIndex = 0; bandIndex < rasterBandCount; bandIndex++) {
                    String bandName = bandPrefix + ((bandIndex < bandNames.length) ? bandNames[bandIndex] : ("band_" + bandIndex));
                    metadata.addBandName(bandName);
                }
            }

            metadata.addMaskName(SimpleReaderConstants.NODATA_VALUE);
            metadata.addMaskName(SimpleReaderConstants.SATURATED_VALUE);

            return metadata;
        } catch (RuntimeException | IOException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new IOException(exception);
        }
    }

    private GeoCoding addGeoCoding(MetadataList<SimpleReaderMetadata> metadataList, VirtualDirEx productDirectory) throws Exception {
        GeoCoding productGeoCoding = SimpleReaderProductReader.buildProductTiePointGridGeoCoding(metadataList.getMetadataAt(0), metadataList, null);
        if(productGeoCoding == null) {
            boolean inputStreamSuccess = false;
            GeoTiffImageReader geoTiffImageReader;
            FilePathInputStream filePathInputStream = productDirectory.getInputStream(metadataList.getMetadataImageRelativePath(0));
            try {
                geoTiffImageReader = new GeoTiffImageReader(filePathInputStream, null);
                inputStreamSuccess = true;
            } finally {
                if (!inputStreamSuccess) {
                    filePathInputStream.close();
                }
            }
            if (productGeoCoding == null) {
                productGeoCoding = GeoTiffProductReader.readGeoCoding(geoTiffImageReader, null);
            }
        }
        return productGeoCoding;
    }
}
