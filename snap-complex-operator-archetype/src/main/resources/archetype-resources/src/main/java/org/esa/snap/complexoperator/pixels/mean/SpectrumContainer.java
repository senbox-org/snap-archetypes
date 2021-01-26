package org.esa.snap.complexoperator.pixels.mean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// TODO - Rename this class
// TODO - Adapt variables/methods
// TODO - Adapt javadoc

/**
 * A container for all the Spectrum objects
 */
public class SpectrumContainer {

    private List<Spectrum> specs = new ArrayList<>();

    public  SpectrumContainer(){}

    public synchronized void addElements(Spectrum spectrum){
        specs.add(spectrum);
    }

    public synchronized List<Spectrum> getElements(){
        return Collections.unmodifiableList(this.specs);
    }
}
