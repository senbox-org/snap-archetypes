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

package org.esa.snap.simpleoperator.gpf.ui;

import org.esa.snap.core.datamodel.Band;
import org.esa.snap.engine_utilities.gpf.OperatorUtils;
import org.esa.snap.graphbuilder.gpf.ui.BaseOperatorUI;
import org.esa.snap.graphbuilder.gpf.ui.OperatorUIUtils;
import org.esa.snap.graphbuilder.gpf.ui.UIValidation;
import org.esa.snap.graphbuilder.rcp.utils.DialogUtils;
import org.esa.snap.ui.AppContext;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This is a sample UI.
 */
public class SimpleOperatorUI extends BaseOperatorUI {

    private final JList<String> polList = new JList<>();
    private final JList bandList = new JList();
    private final JTextField bandNamePattern = new JTextField();

    @Override
    public JComponent CreateOpTab(String operatorName, Map<String, Object> parameterMap, AppContext appContext) {

        initializeOperatorUI(operatorName, parameterMap);
        final JComponent panel = createPanel();
        initParameters();
        return new JScrollPane(panel);
    }

    @Override
    public void initParameters() {

        if (sourceProducts != null && sourceProducts.length > 0) {
            final Set<String> pols = new HashSet<>(4);
            for (Band srcBand : sourceProducts[0].getBands()) {
                final String pol = OperatorUtils.getPolarizationFromBandName(srcBand.getName());
                if (pol != null)
                    pols.add(pol.toUpperCase());
            }

            OperatorUIUtils.initParamList(polList, pols.toArray(new String[pols.size()]),
                                          (String[]) paramMap.get("selectedPolarisations"));
        }

        OperatorUIUtils.initParamList(bandList, getBandNames());

        String bandNamePatternStr = (String) paramMap.get("bandNamePattern");
        if (bandNamePattern != null) {
            bandNamePattern.setText(bandNamePatternStr);
        }
    }

    @Override
    public UIValidation validateParameters() {
        return new UIValidation(UIValidation.State.OK, "");
    }

    @Override
    public void updateParameters() {

        OperatorUIUtils.updateParamList(polList, paramMap, "selectedPolarisations");

        OperatorUIUtils.updateParamList(bandList, paramMap, OperatorUIUtils.SOURCE_BAND_NAMES);

        paramMap.put("bandNamePattern", bandNamePattern.getText());
    }

    private JComponent createPanel() {

        final JPanel contentPane = new JPanel(new GridBagLayout());
        final GridBagConstraints gbc = DialogUtils.createGridBagConstraints();

        DialogUtils.addComponent(contentPane, gbc, "Polarisations:", polList);

        gbc.gridy++;
        DialogUtils.addComponent(contentPane, gbc, "Source Bands:", new JScrollPane(bandList));

        gbc.gridy++;
        DialogUtils.addComponent(contentPane, gbc, "Band Name Pattern:", bandNamePattern);

        DialogUtils.fillPanel(contentPane, gbc);

        return contentPane;
    }
}
