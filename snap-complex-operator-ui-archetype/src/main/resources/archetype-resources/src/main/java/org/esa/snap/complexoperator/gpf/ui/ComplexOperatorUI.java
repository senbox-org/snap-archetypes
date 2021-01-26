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

package org.esa.snap.complexoperator.gpf.ui;

import org.esa.snap.core.datamodel.Product;
import org.esa.snap.core.gpf.GPF;
import org.esa.snap.core.gpf.OperatorSpi;
import org.esa.snap.complexoperator.common.SpectrumInput;
import org.esa.snap.complexoperator.rcp.ComplexOperatorFormModel;
import org.esa.snap.complexoperator.rcp.ComplexOperatorPanel;
import org.esa.snap.graphbuilder.gpf.ui.BaseOperatorUI;
import org.esa.snap.graphbuilder.gpf.ui.UIValidation;
import org.esa.snap.ui.AppContext;

import javax.swing.*;
import java.util.Map;

// TODO - Rename this class
// TODO - Adapt variables/methods
// TODO - Adapt javadoc
/**
 * This is a sample UI.
 */
public class ComplexOperatorUI extends BaseOperatorUI {

    private ComplexOperatorPanel baseUI;
    private String msg = "";
    private boolean uiReady = false;

    @Override
    public JComponent CreateOpTab(String operatorName, Map<String, Object> parameterMap, AppContext appContext) {
        initializeOperatorUI(operatorName, parameterMap);
        OperatorSpi operatorSpi = GPF.getDefaultInstance().getOperatorSpiRegistry().getOperatorSpi(operatorName);
        if (operatorSpi == null) {
            throw new IllegalArgumentException("No SPI found for operator name '" + operatorName + "'");
        }
        this.baseUI = new ComplexOperatorPanel(appContext, this.propertySet, this.paramMap, this::getCurrentProduct);
        return this.baseUI.createPanel();
    }

    @Override
    public void initParameters() {
        this.uiReady = true;
    }

    @Override
    public UIValidation validateParameters() {
        UIValidation.State state = this.uiReady ? UIValidation.State.ERROR : UIValidation.State.WARNING;
        final ComplexOperatorFormModel formModel = this.baseUI.getFormModel();
        if (!validateNumberOfBands(formModel)) {
            return new UIValidation(state, "classification has to be done on at least 2 source product bands");
        }
        if (!validateSpectrumClassInput(formModel)) {
            return new UIValidation(state, "No Spectrum Class set" + (this.msg.isEmpty() ? "" : ", " + this.msg + ""));
        }
        if (!validateThresholds(formModel)) {
            return new UIValidation(state, "No thresholds set");
        }
        return new UIValidation(UIValidation.State.OK, "");
    }

    @Override
    public void updateParameters() {
        this.baseUI.reactOnChange();

    }

    private boolean validateNumberOfBands(ComplexOperatorFormModel formModel) {
        String[] referenceBands = formModel.getPropertySet().getProperty(ComplexOperatorFormModel.REFERENCE_BANDS_PROPERTY).getValue();
        return referenceBands != null && referenceBands.length >= 2;
    }

    private boolean validateSpectrumClassInput(ComplexOperatorFormModel formModel) {
        SpectrumInput[] spectra = formModel.getPropertySet().getProperty(ComplexOperatorFormModel.SPECTRA_PROPERTY).getValue();
        SpectrumInput[] hiddenSpectra = formModel.getPropertySet().getProperty(ComplexOperatorFormModel.HIDDEN_SPECTRA_PROPERTY).getValue();
        if (spectra != null && hiddenSpectra != null && spectra.length != hiddenSpectra.length) {
            StringBuilder message = new StringBuilder("Selection different than selected classes to be used. Spectrum classes to be used ");
            for (int spectrumIndex = 0; spectrumIndex < hiddenSpectra.length - 1; spectrumIndex++) {
                message.append(hiddenSpectra[spectrumIndex].getName());
                message.append(", ");
            }
            message.append((hiddenSpectra[hiddenSpectra.length - 1].getName()));
            this.msg = message.toString();
        }
        return spectra != null && spectra.length != 0;

    }

    private boolean validateThresholds(ComplexOperatorFormModel formModel) {
        String thresholds = formModel.getPropertySet().getProperty(ComplexOperatorFormModel.THRESHOLDS_PROPERTY).getValue();
        SpectrumInput[] spectra = formModel.getPropertySet().getProperty(ComplexOperatorFormModel.SPECTRA_PROPERTY).getValue();
        return thresholds != null && spectra != null && thresholds.split(",").length - 1 > 0 && thresholds.split(",").length - 1 == spectra.length;
    }

    private Product getCurrentProduct() {
        return this.sourceProducts != null && this.sourceProducts.length > 0 ? this.sourceProducts[0] : null;
    }
}
