package gis;

import java.math.BigDecimal;

/**
 * Here's a green comment!
 */
public record Coordinate(BigDecimal x, BigDecimal y) implements Comparable<Coordinate>{


    public final Coordinate validate(){
        //todo: did i do this right?
        // throws a NullPointerException if either x or y are null, and otherwise returns this Coordinate.
        if (x == null || y == null)
            throw new NullPointerException("Horizontal and Vertical components of the coordinate can not be null");
        return this;
    }

    public static final Coordinate validate(Coordinate coordinate){
        //todo: is this right?
        // throws a NullPointerException if either the coordinate or its x or y values are null, and otherwise returns the argument.
        if (coordinate == null)
            throw new NullPointerException("Coordinate can not be null");
        coordinate.validate();
        return coordinate;
    }

    public static final Coordinate ORIGIN = new Coordinate(new BigDecimal(0),new BigDecimal(0));

    @Override
    public int compareTo(Coordinate other) {
        //Todo: correct?
        // A Coordinate (x1, y1) is less than (x2, y2) if x1 < x2 or x1 = x2 and y1 < y2
        if ((this.x.compareTo(other.x) <= 0) && (this.y.compareTo(other.y) < 0))
            return -1;
        if ((this.x.compareTo(other.x) == 0) && (this.y.compareTo(other.y) == 0))
            return 0;
        return 1;
    }
}
