package gis;

import org.junit.jupiter.params.shadow.com.univocity.parsers.common.DataValidationException;

import java.math.BigDecimal;

/**
 *<h3>Creates a coordinate</h3> todo more?
 * @param x the longitude (horizontal position) of the coordinate
 * @param y the latitude (vertical position) of the coordinate
 * @throws NullPointerException
 * @author Liam McConlogue
 */

public record Coordinate(BigDecimal x, BigDecimal y) implements Comparable<Coordinate>{
    //TODO! Format my error messages better
    //TODO! do I need to auto validate on initialization? Or ok to initialize null Coor. w/o exception

    public final Coordinate validate() throws DataValidationException {
        //todo: correct? throws a NullPointerException if either x or y are null, and otherwise returns this Coordinate.
        if (x == null || y == null) {
            throw new DataValidationException("Horizontal and Vertical components of the coordinate can not be null");
        }
        return this;
    }

    public static final Coordinate validate(Coordinate coordinate){
        //todo: correct? throws a NullPointerException if either the coordinate or its x or y values are null, and otherwise returns the argument.
        if (coordinate == null) {
            throw new NullPointerException("Coordinate can not be null");
        }
        coordinate.validate();
        return coordinate;
    }

    public static final Coordinate ORIGIN = new Coordinate(new BigDecimal(0),new BigDecimal(0));

    @Override
    public int compareTo(Coordinate other) {
        //Todo: correct?
        // A Coordinate (x1, y1) is less than (x2, y2) if x1 < x2 or x1 = x2 and y1 < y2
        this.validate();
        other.validate();
        if (this.x.compareTo(other.x) < 0)
            return -1;
        else if ((this.x.compareTo(other.x)==0) && (this.y.compareTo(other.y) < 0))
            return -1;
        else if ((this.x.compareTo(other.x) == 0) && (this.y.compareTo(other.y) == 0))
            return 0;
        else return 1; //(this.x > other.x) or (this.x == other.x and this.y > other.y)
    }

    public String toSimpleString(){
        //todo: correct? returns a textual representation that is as informative as that from toString but less needlessly verbose
        this.validate();
        return "(" + x + ", " + y + ")";
    }
}
