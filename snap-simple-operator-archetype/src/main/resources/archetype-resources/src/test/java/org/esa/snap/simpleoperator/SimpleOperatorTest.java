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

package org.esa.snap.simpleoperator;

import org.esa.snap.core.datamodel.Band;
import org.esa.snap.core.datamodel.FlagCoding;
import org.esa.snap.core.datamodel.Product;
import org.esa.snap.core.datamodel.ProductData;
import org.esa.snap.core.datamodel.VirtualBand;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SimpleOperatorTest {


    @Test
    public void basicTest() {
        SimpleOperatorOp selectOp = new SimpleOperatorOp();
        selectOp.setParameterDefaultValues();
        selectOp.setSourceProduct("source", createSourceProduct());
        selectOp.setParameter("sourceBands", "b1");
        Product targetProduct = selectOp.getTargetProduct();
        assertTrue(targetProduct.containsBand("b1"));
        assertFalse(targetProduct.containsBand("v1"));
        assertTrue(targetProduct.getFlagCodingGroup().contains("fcoding"));
        assertTrue(targetProduct.containsBand("fband"));
    }

    @Test
    public void testFlagBandSpecified() {
        SimpleOperatorOp selectOp = new SimpleOperatorOp();
        selectOp.setParameterDefaultValues();
        selectOp.setSourceProduct("source", createSourceProduct());
        selectOp.setParameter("sourceBands", "fband,v1");
        Product targetProduct = selectOp.getTargetProduct();
        assertFalse(targetProduct.containsBand("b1"));
        assertTrue(targetProduct.containsBand("v1"));
        assertTrue(targetProduct.getFlagCodingGroup().contains("fcoding"));
        assertTrue(targetProduct.containsBand("fband"));
    }

    private Product createSourceProduct() {
        Product product = new Product("test", "type", 10, 10);
        product.addBand("b1", ProductData.TYPE_INT16);
        product.addBand(new VirtualBand("v1", ProductData.TYPE_FLOAT32, 10, 10, "x+y"));
        FlagCoding fcoding = new FlagCoding("fcoding");
        fcoding.addFlag("flag1", 0x01, "description");
        fcoding.addFlag("flag2", 0x02, "description");
        product.getFlagCodingGroup().add(fcoding);
        Band fband = product.addBand("fband", ProductData.TYPE_INT8);
        fband.setSampleCoding(fcoding);
        return product;
    }
}
