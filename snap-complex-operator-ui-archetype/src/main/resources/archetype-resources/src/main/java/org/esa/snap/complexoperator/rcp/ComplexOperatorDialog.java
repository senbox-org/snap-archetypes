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

import org.esa.snap.core.datamodel.Product;
import org.esa.snap.core.gpf.GPF;
import org.esa.snap.core.gpf.OperatorSpi;
import org.esa.snap.core.gpf.descriptor.OperatorDescriptor;
import org.esa.snap.core.gpf.ui.OperatorMenu;
import org.esa.snap.core.gpf.ui.OperatorParameterSupport;
import org.esa.snap.core.gpf.ui.SingleTargetProductDialog;
import org.esa.snap.core.gpf.ui.TargetProductSelector;
import org.esa.snap.complexoperator.common.SpectrumInput;
import org.esa.snap.ui.AppContext;

import java.util.Map;

// TODO - Rename this class
// TODO - Adapt variables/methods
// TODO - Adapt javadoc

class ComplexOperatorDialog extends SingleTargetProductDialog {

    private static final String DEFAULT_TARGET_PRODUCT_NAME = "complex_operator_target_product";

    private final ComplexOperatorForm form;

    ComplexOperatorDialog(String title, String helpID, AppContext appContext) {
        super(appContext, title, helpID);

        final TargetProductSelector selector = getTargetProductSelector();
        selector.getModel().setSaveToFileSelected(true);
        selector.getModel().setProductName(DEFAULT_TARGET_PRODUCT_NAME);
        selector.getSaveToFileCheckBox().setEnabled(false);

        final OperatorSpi operatorSpi = GPF.getDefaultInstance().getOperatorSpiRegistry().getOperatorSpi("ComplexOperator");
        if (operatorSpi == null) {
            throw new IllegalArgumentException("No SPI found for operator name '" + "ComplexOperator" + "'");
        }
        OperatorDescriptor operatorDescriptor = operatorSpi.getOperatorDescriptor();

        form = new ComplexOperatorForm(operatorDescriptor, appContext, getTargetProductSelector());
        ComplexOperatorFormModel formModel = form.getFormModel();
        OperatorParameterSupport parameterSupport = new OperatorParameterSupport(operatorSpi.getOperatorDescriptor(),
                                                                                 formModel.getPropertySet(),
                                                                                 formModel.getParameterMap(),
                                                                                 null);
        OperatorMenu operatorMenu = new OperatorMenu(this.getJDialog(),
                                                     operatorSpi.getOperatorDescriptor(),
                                                     parameterSupport,
                                                     appContext,
                                                     helpID);
        getJDialog().setJMenuBar(operatorMenu.createDefaultMenu());
    }

    @Override
    protected boolean verifyUserInput() {
        final ComplexOperatorFormModel formModel = form.getFormModel();
        if (!validateNumberOfBands(formModel)) {
            showErrorDialog("classification has to be done on at least 2 source product bans");
            return false;
        }
        if (!validateSpectrumClassInput(formModel)) {
            showErrorDialog("No Spectrum Class set");
            return false;
        }
        if (!validateThresholds(formModel)) {
            showErrorDialog("No thresholds set");
            return false;
        }
        return true;
    }

    private boolean validateSpectrumClassInput(ComplexOperatorFormModel formModel) {
        SpectrumInput[] spectra =  formModel.getPropertySet().getProperty(ComplexOperatorFormModel.SPECTRA_PROPERTY).getValue();
        SpectrumInput[] hiddenSpectra =  formModel.getPropertySet().getProperty(ComplexOperatorFormModel.HIDDEN_SPECTRA_PROPERTY).getValue();
        if(spectra.length != hiddenSpectra.length) {
            StringBuffer message  = new StringBuffer("Selection different than selected classes to be used. Spectrum classes to be used ");
            for (int spectrumIndex = 0; spectrumIndex < hiddenSpectra.length-1; spectrumIndex++) {
                message.append(hiddenSpectra[spectrumIndex].getName());
                message.append(", ");
            }
            message.append((hiddenSpectra[hiddenSpectra.length-1].getName()));
            showInformationDialog(message.toString());
        }
        return hiddenSpectra != null && hiddenSpectra.length != 0;

    }

    private boolean validateThresholds(ComplexOperatorFormModel formModel) {
        String thresholds = formModel.getPropertySet().getProperty(ComplexOperatorFormModel.THRESHOLDS_PROPERTY).getValue();
        if(thresholds != null) {
            if(!thresholds.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private boolean validateNumberOfBands(ComplexOperatorFormModel formModel) {
        String[] referenceBands = formModel.getPropertySet().getProperty(ComplexOperatorFormModel.REFERENCE_BANDS_PROPERTY).getValue();
        return referenceBands != null && referenceBands.length >= 2;
    }

    @Override
    public int show() {
        form.prepareShow();
        setContent(form);
        return super.show();
    }

    @Override
    public void hide() {
        form.prepareHide();
        super.hide();
    }
    @Override
    protected Product createTargetProduct() throws Exception {
        final ComplexOperatorFormModel formModel = form.getFormModel();
        final Map<String, Object> parameterMap = formModel.getParameterMap();
        final Map<String, Product>sourceProducts = form.getSourceProductMap();
        return GPF.createProduct("ComplexOperator", parameterMap, sourceProducts);
    }
}
