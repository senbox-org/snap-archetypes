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

import org.esa.snap.core.gpf.annotations.Parameter;

// TODO - Rename this class
// TODO - Adapt variables/methods
// TODO - Adapt javadoc

public class SpectrumInput {
    @Parameter(pattern = "[a-zA-Z_0-9]*")
    private String name;
    @Parameter
    private int[] xPixelPolygonPositions;
    @Parameter
    private int[] yPixelPolygonPositions;
    @Parameter
    private boolean isShapeDefined;


    public SpectrumInput(String name, int[] xPixelPolygonPositions, int[] yPixelPolygonPositions) {
        assert name != null;

        this.name = name;
        this.xPixelPolygonPositions = xPixelPolygonPositions;
        this.yPixelPolygonPositions = yPixelPolygonPositions;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }

    public int[] getXPixelPolygonPositions() {
        return this.xPixelPolygonPositions;
    }

    public int[] getYPixelPolygonPositions() {
        return this.yPixelPolygonPositions;
    }

    public void setIsShapeDefined(boolean isShapeDefined) {
        this.isShapeDefined = isShapeDefined;
    }

    public void setXPixelPolygonPositionIndex(int index, int value){
        this.xPixelPolygonPositions[index] = value;
    }

    public void setYPixelPolygonPositionIndex(int index, int value){
        this.yPixelPolygonPositions[index] = value;
    }

    public boolean getIsShapeDefined(){
        return this.isShapeDefined;
    }
}
