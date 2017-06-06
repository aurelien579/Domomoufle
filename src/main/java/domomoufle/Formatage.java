package domomoufle;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Formatage {

    public Formatage(String dossier) {
        ArrayList<double[]> t = new ArrayList<double[]>();
        for (int i = 0; i < 11; i++) {
            //t.add(createTuple(dossier, "01", ((i+1) >= 10) ? ""+(i+1) : "0"+(i+1)));
        }
        write(t);
    }

    public static void main(String[] args) {
        new Formatage("H:\\Analyse\\Accelerometer\\Shimmer01\\");
    }

    public static ArrayList createTuple(ArrayList<Double[]> donnees) throws Exception {
        int n = (donnees.size()-donnees.size()%30)/30;
        if (n == 0) {
            throw new Exception("Acquisition trop courte");
        }
        
        ArrayList<ArrayList<Double[]>> subgroup = new ArrayList<ArrayList<Double[]>>();
        for (int i = 0; i < 30; i++) {
            subgroup.add(new ArrayList<Double[]>(donnees.subList(i*n, (i+1)*n)));
        }

        ArrayList tuple = traitementSubGroup(subgroup);
        
        return tuple;
    }
    
    public static ArrayList traitementSubGroup(ArrayList<ArrayList<Double[]>> subgroup) {
        ArrayList tuple = new ArrayList();
        for (int i = 0; i < subgroup.size(); i++) {
            tuple.add(variance(getDonnees(subgroup.get(i), 0)));
            tuple.add(variance(getDonnees(subgroup.get(i), 1)));
            tuple.add(variance(getDonnees(subgroup.get(i), 2)));
            tuple.add((int)Math.round(moyenne(getDonnees(subgroup.get(i), 3))));
            tuple.add((int)Math.round(moyenne(getDonnees(subgroup.get(i), 4))));
        }
        
        return tuple;
    }
    
    public static double variance(double[] d) {
        double m = moyenne(d);
        double s = 0;
        for (int i = 0; i < d.length; i++) {
            s += (d[i]-m)*(d[i]*m);
        }
        return s/d.length;
    }
    
    public static double moyenne(double[] d) {
        double s = 0;
        for (int i = 0; i < d.length; i++) {
            s += d[i];
        }
        return s/d.length;
    }
    
    public static double[] getDonnees(ArrayList<Double[]> t, int index) {
        double[] d = new double[t.size()];
        for (int i = 0; i < t.size(); i++) {
            d[i] = t.get(i)[index];
        }
        return d;
    }

    public static ArrayList<Double[]> parseFile(String file) {
        ArrayList<Double[]> t = new ArrayList<Double[]>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(file)));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] val = line.split(" ");
                Double[] v = new Double[4];
                for (int i = 0; i < v.length; i++) {
                    v[i] = Double.parseDouble(val[i]);
                }
                t.add(v);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        double t0 = t.get(0)[3];
        for (int i = 0; i < t.size(); i++) {
            Double[] val = new Double[4];
            for (int j = 0; j < 4; j++) {
                val[j] = t.get(i)[j];
            }
            val[3] = val[3] - t0;
            t.set(i, val);
        }

        return t;
    }
    
    public static void write(ArrayList<double[]> t) {
        try {
            PrintWriter w = new PrintWriter(new OutputStreamWriter(new FileOutputStream("output.txt")));
            
            for (int i = 0; i < t.size(); i++) {
                String s = "";
                for (int j = 0; j < t.get(0).length; j++) {
                    s += t.get(i)[j] + ";";
                }
                w.println(s);
            }
            
            w.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
