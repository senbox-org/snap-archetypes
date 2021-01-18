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

import org.esa.snap.rcp.actions.AbstractSnapAction;
import org.esa.snap.ui.ModelessDialog;

import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Geographic example action.
 *
 * @author Ralf Quast
 * @author Marco Peters
 */
public class ComplexOperatorAction extends AbstractSnapAction {
    private static final Set<String> KNOWN_KEYS = new HashSet<>(Arrays.asList("displayName", "operatorName", "dialogTitle", "helpId", "targetProductNameSuffix"));

    public ComplexOperatorAction() {
        super();
    }

    public static ComplexOperatorAction create(Map<String, Object> properties) {
        ComplexOperatorAction action = new ComplexOperatorAction();
        properties.entrySet().stream().filter(entry -> KNOWN_KEYS.contains(entry.getKey())).forEach(entry -> {
            action.putValue(entry.getKey(), entry.getValue());
        });
        return action;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        ModelessDialog dialog = new ComplexOperatorDialog((String)(getValue("dialogTitle")),
                                                          (String)(getValue("helpId")), getAppContext());
        dialog.show();
    }
}
