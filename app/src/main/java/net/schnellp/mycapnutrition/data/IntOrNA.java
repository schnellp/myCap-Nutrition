package net.schnellp.mycapnutrition.data;

public class IntOrNA {
    public final int val;
    public final boolean isNA;

    public IntOrNA(int val) {
        this.val = val;
        this.isNA = false;
    }

    public IntOrNA(String string) {
        if (string.equals("") || string.equals("null") || string.equals("NA")) {
            isNA = true;
            val = 0;
        } else {
            isNA = false;
            val = Integer.parseInt(string);
        }
    }

    public IntOrNA(int val, boolean isNA) {
        this.val = val;
        this.isNA = isNA;
    }

    public IntOrNA(DoubleOrNA doubleOrNA) {
        val = (int) Math.round(doubleOrNA.val);
        isNA = doubleOrNA.isNA;
    }

    public IntOrNA add(IntOrNA n) {
        return new IntOrNA(val + n.val, isNA | n.isNA);
    }

    public IntOrNA multiply(IntOrNA n) {
        return new IntOrNA(val * n.val, isNA | n.isNA);
    }

    public String toString() {
        if (isNA) {
            return "NA";
        } else {
            return Integer.toString(val);
        }
    }

    public String toDBString() {
        if (isNA) {
            return "null";
        } else {
            return Integer.toString(val);
        }
    }
}
