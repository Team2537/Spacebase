package frc.lib.util;

public class LookupTable {
    private final double[] table;
    public final double firstIndex, lastIndex, delta;
    public LookupTable(double[] table, double firstIndex, double delta){
        this.table = table;
        this.firstIndex = firstIndex;
        this.delta = delta;
        this.lastIndex = firstIndex + delta*(table.length - 1);
    }
    public double get(double x){
        x = (x - firstIndex)/delta;
        int index = (int)x;

        if(index <= 0) return table[0];
        if(index >= table.length - 1) return table[table.length - 1];
        return Util.linearInterp(table[index], table[index+1], x - index);
    }
}