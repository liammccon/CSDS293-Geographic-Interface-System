package gis;

import org.junit.jupiter.params.shadow.com.univocity.parsers.common.DataValidationException;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Objects;

/**
 *Creates a coordinate with longitude and latitude.
 *Used to set a location in the Geographic Information System.
 *@param x the longitude (horizontal position) of the coordinate
 *@param y the latitude (vertical position) of the coordinate
 */
public record Coordinate(BigDecimal x, BigDecimal y) implements Comparable<Coordinate>{

    public static final Coordinate validate(Coordinate coordinate){
        Objects.requireNonNull(coordinate);
        Objects.requireNonNull(coordinate.x);
        Objects.requireNonNull(coordinate.y);
        return coordinate;
    }

    public final Coordinate validate(){
        return Coordinate.validate(this);
    }

    public static final Coordinate ORIGIN = new Coordinate(new BigDecimal(0),new BigDecimal(0));

    /** Given the coordinate it is called on (x1, y1) and another coordinate passed as {@code other}(x2, y2):
     * @returns -1 when x1 < x2, or if x1 = x2 and y1 < y2
     * <p>0 when x1 = x2 and y1 = y2</p>
     * <p>1 otherwise (if x1 > x2, or if x1 = x2 and y1 >= y2)</p>
     */
    @Override
    public int compareTo(Coordinate other) {
        this.validate();
        other.validate();

        Comparator<Coordinate> compareX = Comparator.comparing(Coordinate::x);
        Comparator<Coordinate> compareY = Comparator.comparing(Coordinate::y);

        return compareX.thenComparing(compareY).compare(this, other);
    }

    public String toSimpleString(){
        this.validate();
        return "(" + x + ", " + y + ")";
    }
}
