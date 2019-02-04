package frc.lib.util;

import java.util.List;

public class CSVLogger {
    String out;
    public CSVLogger(String... cols){
        out = "";
        appendRow(cols);
    }
    public <T> void appendRow(List<T> row){
        for(T obj : row){
            out += obj + ",";
        }
        out = out.substring(0, out.length()-1)+"\n";
    }
    @SuppressWarnings("unchecked")
    public <T> void appendRow(T... row){
        for(T obj : row){
            out += obj + ",";
        }
        out = out.substring(0, out.length()-1)+"\n";
    }
    public String toString(){
        return out;
    }
    public void print(){
        System.out.println(out);
    }
}