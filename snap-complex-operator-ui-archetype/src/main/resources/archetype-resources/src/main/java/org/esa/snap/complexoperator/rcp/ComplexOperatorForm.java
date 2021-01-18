/*
 * Copyright (C) 2010 Brockmann Consult GmbH (info@brockmann-consult.de)
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

import com.bc.ceres.swing.selection.SelectionChangeEvent;
import com.bc.ceres.swing.selection.SelectionChangeListener;
import org.esa.snap.core.datamodel.Product;
import org.esa.snap.core.gpf.descriptor.OperatorDescriptor;
import org.esa.snap.core.gpf.ui.DefaultIOParametersPanel;
import org.esa.snap.core.gpf.ui.SourceProductSelector;
import org.esa.snap.core.gpf.ui.TargetProductSelector;
import org.esa.snap.ui.AppContext;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Map;

/**
 * Form for complex example dialog.
 *
 * @author Ralf Quast
 * @version $Revision$ $Date$
 */
public class ComplexOperatorForm extends JTabbedPane {

    private static final int CURRENT_PRODUCT = 0;
    private final AppContext appContext;
    private final ComplexOperatorFormModel samModel;
    private final OperatorDescriptor operatorDescriptor;
    private final TargetProductSelector targetProductSelector;
    private DefaultIOParametersPanel ioParametersPanel;
    private ComplexOperatorParametersPanel parametersPanel;
    private ComplexOperatorThresholdPanel thresholdPanel;

    ComplexOperatorForm(OperatorDescriptor operatorDescriptor, AppContext appContext, TargetProductSelector targetProductSelector) {
        this.appContext = appContext;
        this.samModel = new ComplexOperatorFormModel(this);
        this.operatorDescriptor = operatorDescriptor;
        this.targetProductSelector = targetProductSelector;
        init();
        createUI();
    }

    private void init() {
        ioParametersPanel = new DefaultIOParametersPanel(appContext, operatorDescriptor, targetProductSelector, true);
        ArrayList<SourceProductSelector> sourceProductSelectorList = this.ioParametersPanel.getSourceProductSelectorList();
        parametersPanel = new ComplexOperatorParametersPanel(this, appContext, samModel);
        thresholdPanel = new ComplexOperatorThresholdPanel(samModel);
        SelectionChangeListener currentListenerProduct = new SelectionChangeListener() {
            public void selectionChanged(SelectionChangeEvent event) {
                Product product = sourceProductSelectorList.get(CURRENT_PRODUCT).getSelectedProduct();
                parametersPanel.updateBands(product);
            }

            public void selectionContextChanged(SelectionChangeEvent event) {
            }
        };
        sourceProductSelectorList.get(CURRENT_PRODUCT).addSelectionChangeListener(currentListenerProduct);
    }

    private void createUI() {
        addTab("I/O Parameters", ioParametersPanel);
        addTab("SAM Parameters", parametersPanel);
        addTab("Thresholds ", thresholdPanel);
    }

    ComplexOperatorThresholdPanel getThresholdPanelInstance() {
        return this.thresholdPanel;
    }

    ComplexOperatorFormModel getFormModel() {
        return samModel;
    }

    void prepareShow() {
        ioParametersPanel.initSourceProductSelectors();
    }

    void prepareHide() {
        ioParametersPanel.releaseSourceProductSelectors();
    }

    Map<String, Product> getSourceProductMap() {
        return ioParametersPanel.createSourceProductsMap();
    }
}
