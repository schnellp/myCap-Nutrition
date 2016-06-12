package net.schnellp.mycapnutrition.model;

public class DoubleOrNA {
    public final double val;
    public final boolean isNA;

    public DoubleOrNA(double d) {
        val = d;
        isNA = false;
    }

    public DoubleOrNA(String string) {
        if (string.equals("") || string.equals("null") || string.equals("NA")) {
            isNA = true;
            val = 0;
        } else {
            isNA = false;
            val = Double.parseDouble(string);
        }
    }

    public DoubleOrNA(double val, boolean isNA) {
        this.val = val;
        this.isNA = isNA;
    }

    public DoubleOrNA(IntOrNA intOrNA) {
        val = intOrNA.val;
        isNA = intOrNA.isNA;
    }

    public DoubleOrNA multiply(DoubleOrNA d) {
        return new DoubleOrNA(val * d.val, isNA || d.isNA);
    }

    public DoubleOrNA multiply(double d) {
        return new DoubleOrNA(val * d, isNA);
    }

    public DoubleOrNA divide(DoubleOrNA d) {
        return new DoubleOrNA(val / d.val, isNA || d.isNA);
    }

    public DoubleOrNA divide(double d) {
        return new DoubleOrNA(val / d, isNA);
    }

    public String toString() {
        if (isNA) {
            return "NA";
        } else {
            return Double.toString(val);
        }
    }

    public DoubleOrNA add(DoubleOrNA d) {
        return new DoubleOrNA(d.val + val, d.isNA | d.isNA);
    }

    public IntOrNA round() {
        return new IntOrNA((int) Math.round(val), isNA);
    }
}
