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

package org.esa.snap.complexoperator.rcp;

import com.bc.ceres.binding.PropertySet;
import org.esa.snap.core.datamodel.Product;
import org.esa.snap.core.gpf.annotations.ParameterDescriptorFactory;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

// TODO - Rename this class
// TODO - Adapt variables/methods
// TODO - Adapt javadoc

public class ComplexOperatorFormModel {

    public static final String REFERENCE_BANDS_PROPERTY = "referenceBands";
    public static final String SPECTRA_PROPERTY = "spectra";
    public static final String HIDDEN_SPECTRA_PROPERTY = "hiddenSpectra";
    public static final String THRESHOLDS_PROPERTY = "thresholds";
    public static final String RESAMPLE_TYPE_PROPERTY = "resampleType";
    public static final String UPSAMPLING_PROPERTY = "upsamplingMethod";
    public static final String DOWNSAMPLING_PROPERTY = "downsamplingMethod";
    private final PropertySet container;
    private final Map<String, Object> parameterMap;
    private final Map<File, Product> sourceProductMap = Collections.synchronizedMap(new HashMap<>());
    private ComplexOperatorForm parentForm;

    ComplexOperatorFormModel(ComplexOperatorForm parentForm) {
        this.parentForm = parentForm;
        parameterMap = new HashMap<>();
        container = ParameterDescriptorFactory.createMapBackedOperatorPropertyContainer("ComplexOperator", parameterMap);
    }

    ComplexOperatorFormModel(PropertySet propertySet, Map<String, Object> paramMap) {
        this.container = propertySet;
        this.parameterMap = paramMap;
    }

    public PropertySet getPropertySet() {
        return container;
    }

    Map<String, Object> getParameterMap() {
        return parameterMap;
    }
}
