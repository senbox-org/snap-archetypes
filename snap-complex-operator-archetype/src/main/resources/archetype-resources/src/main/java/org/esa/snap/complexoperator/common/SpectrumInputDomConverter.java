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

package org.esa.snap.complexoperator.common;

import com.bc.ceres.binding.ConversionException;
import com.bc.ceres.binding.ValidationException;
import com.bc.ceres.binding.dom.DomConverter;
import com.bc.ceres.binding.dom.DomElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

// TODO - Rename this class
// TODO - Adapt variables/methods
// TODO - Adapt javadoc

/**
 * Sample - Dom converter for spectra elements
 */
public class SpectrumInputDomConverter implements DomConverter {

    @Override
    public Class<?> getValueType() {
        return SpectrumInput[].class;
    }

    @Override
    public SpectrumInput[] convertDomToValue(DomElement parentElement, Object value) throws ConversionException, ValidationException {

        final List<SpectrumInput> spectrumList = new ArrayList<>();

        final DomElement[] spectraElements = parentElement.getChildren();
        for(DomElement spectrumElem : spectraElements) {

            String nameStr = null;
            String xPosStr = null;
            String yPosStr = null;
            String isShapeDefinedStr = null;
            int [] xintArr = null;
            int [] yintArr = null;

            DomElement name = spectrumElem.getChild("name");
            if (name != null) {
                nameStr = name.getValue();
            }

            DomElement xPositions = spectrumElem.getChild("xPixelPolygonPositions");
            if (xPositions != null) {
                xPosStr = xPositions.getValue();
            }

            DomElement yPositions = spectrumElem.getChild("yPixelPolygonPositions");
            if (yPositions != null) {
                yPosStr = yPositions.getValue();
            }

            DomElement isShapeDefined = spectrumElem.getChild("isShapeDefined");
            if (isShapeDefined != null) {
                isShapeDefinedStr = isShapeDefined.getValue();
            }

            if (xPosStr != null)
            {
                xPosStr = xPosStr.replaceAll("\\[|]|\\s", "");
                String [] stringTokens = xPosStr.split(",");
                xintArr = Stream.of(stringTokens).mapToInt(Integer::parseInt).toArray();
            }
            if (yPosStr != null)
            {
                yPosStr = yPosStr.replaceAll("\\[|]|\\s", "");
                String [] stringTokens = yPosStr.split(",");
                yintArr = Stream.of(stringTokens).mapToInt(Integer::parseInt).toArray();
            }

            final SpectrumInput spectrum = new SpectrumInput(nameStr, xintArr, yintArr);
            if (isShapeDefinedStr != null)
            {
                spectrum.setIsShapeDefined(Boolean.parseBoolean(isShapeDefinedStr));
            }
            spectrumList.add(spectrum);
        }

        return spectrumList.toArray(new SpectrumInput[0]);
    }


    @Override
    public void convertValueToDom(Object value, DomElement parentElement) throws ConversionException {
        final SpectrumInput[] spectrumInputs = (SpectrumInput[])value;

        if(spectrumInputs != null) {
            for (SpectrumInput spectrumInput : spectrumInputs) {
                DomElement spectrum = parentElement.createChild("spectrum");

                final DomElement name = spectrum.createChild("name");
                name.setValue(spectrumInput.getName());

                DomElement xPixelPolygonPositions = spectrum.createChild("xPixelPolygonPositions");
                xPixelPolygonPositions.setValue(Arrays.toString(spectrumInput.getXPixelPolygonPositions()));

                DomElement yPixelPolygonPositions = spectrum.createChild("yPixelPolygonPositions");
                yPixelPolygonPositions.setValue(Arrays.toString(spectrumInput.getYPixelPolygonPositions()));

                DomElement isShapeDefined = spectrum.createChild("isShapeDefined");
                isShapeDefined.setValue(Boolean.toString(spectrumInput.getIsShapeDefined()));
            }
        }
    }
}
