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

    public String toString() {
        if (isNA) {
            return "NA";
        } else {
            return Double.toString(val);
        }
    }
}
