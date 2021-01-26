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
import org.esa.snap.graphbuilder.gpf.ui.OperatorUIUtils;
import org.esa.snap.ui.AppContext;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Core UI class for radiometric indices.
 *
 * @author Adrian Drăghici
 */
public class ComplexOperatorPanel {

    private final ComplexOperatorFormModel samModel;
    private ComplexOperatorParametersPanel parametersPanel;
    private Product currentProduct;
    private Map<String, Object> paramMap;

    private JScrollPane operatorPanel;
    private Callable<Product> sourceProductAccessor;

    public ComplexOperatorPanel(AppContext appContext, PropertySet propertySet, Map<String, Object> paramMap, Callable<Product> productAccessor) {
        this.samModel = new ComplexOperatorFormModel(propertySet, paramMap);
        this.paramMap = paramMap;
        this.sourceProductAccessor = productAccessor;
        ComplexOperatorThresholdPanel thresholdPanel = new ComplexOperatorThresholdPanel(this.samModel);
        this.parametersPanel = new ComplexOperatorParametersPanel(appContext, this.samModel, productAccessor, thresholdPanel);
        thresholdPanel.setPreferredSize(new Dimension(640, 100));
        this.parametersPanel.add(thresholdPanel);
        this.operatorPanel = new JScrollPane(this.parametersPanel);
    }

    public ComplexOperatorFormModel getFormModel() {
        return this.samModel;
    }

    public JComponent createPanel() {
        return this.operatorPanel;
    }

    public void reactOnChange() {
        if (isInputProductChanged() && this.currentProduct != null) {
            this.parametersPanel.updateBands(this.currentProduct);
        }
        OperatorUIUtils.updateParamList(this.parametersPanel.getSourceBandNames(), this.paramMap, ComplexOperatorFormModel.REFERENCE_BANDS_PROPERTY);
    }

    private Product getSourceProduct() {
        try {
            return this.sourceProductAccessor.call();
        } catch (Exception e) {
            return null;
        }
    }

    private boolean isInputProductChanged() {
        Product sourceProduct = getSourceProduct();
        if (sourceProduct != this.currentProduct) {
            this.currentProduct = sourceProduct;
            return true;
        } else {
            return false;
        }
    }
}
