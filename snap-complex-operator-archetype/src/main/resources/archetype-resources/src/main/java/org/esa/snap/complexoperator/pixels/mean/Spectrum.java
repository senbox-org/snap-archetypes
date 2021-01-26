package org.esa.snap.complexoperator.pixels.mean;

// TODO - Rename this class
// TODO - Adapt variables/methods
// TODO - Adapt javadoc

/**
 * Contains the mean value for each source band for a specific region defined by the user
 */
public class Spectrum {

    private String className;
    //mean value for each band
    private float[] meanValue;
    //minimum value for each band

    Spectrum(String className, float[] meanValue){
        this.setClassName(className);
        this.setMeanValue(meanValue);
    }

    public String getClassName() {
        return className;
    }

    private void setClassName(String className) {
        this.className = className;
    }

    public float[] getMeanValue() {
        return meanValue;
    }

    private void setMeanValue(float[] meanValue) {
        this.meanValue = meanValue;
    }
}
