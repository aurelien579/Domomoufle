package domomoufle.arduino;

public class Mesure {

    public final double x;
    public final double y;
    public final double z;
    public final int flex1;
    public final int flex2;

    public Mesure(double x, double y, double z, int flex1, int flex2) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.flex1 = flex1;
        this.flex2 = flex2;
    }

    public static Mesure parse(String line) {
        String[] tokens = line.split(",");
        try {
            return new Mesure(
                    Double.parseDouble(tokens[0]),
                    Double.parseDouble(tokens[1]),
                    Double.parseDouble(tokens[2]),
                    Integer.parseInt(tokens[3]),
                    Integer.parseInt(tokens[4])
            );
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
