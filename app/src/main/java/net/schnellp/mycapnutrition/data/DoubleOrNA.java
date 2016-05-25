package net.schnellp.mycapnutrition.data;

public class DoubleOrNA {
    public final double val;
    public final boolean isNA;

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

    public IntOrNA round() {
        return new IntOrNA((int) Math.round(val), isNA);
    }
}
