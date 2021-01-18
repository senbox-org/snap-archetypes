package org.esa.snap.complexoperator.rcp;

import com.bc.ceres.binding.PropertySet;
import org.esa.snap.core.datamodel.Product;
import org.esa.snap.core.gpf.annotations.ParameterDescriptorFactory;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Dumitrascu Razvan.
 */
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
