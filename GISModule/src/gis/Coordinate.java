package gis;

import org.junit.jupiter.params.shadow.com.univocity.parsers.common.DataValidationException;

import java.math.BigDecimal;
/**
 *Creates a coordinate with longitude and latitude.
 *Used to set a location in the Geographic Information System.
 *@param x the longitude (horizontal position) of the coordinate
 *@param y the latitude (vertical position) of the coordinate
 */

public record Coordinate(BigDecimal x, BigDecimal y) implements Comparable<Coordinate>{


    public final Coordinate validate(){
        if (x == null || y == null) {
            throw new NullPointerException("Horizontal and Vertical components of the coordinate can not be null");
        }
        return this;
    }


    public static final Coordinate validate(Coordinate coordinate){
        if (coordinate == null) {
            throw new NullPointerException("Coordinate can not be null");
        }
        coordinate.validate();
        return coordinate;
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

        boolean thisLessThanOtherTest1 = this.x.compareTo(other.x) < 0;
        boolean thisLessThanOtherTest2 = (this.x.compareTo(other.x)==0) && (this.y.compareTo(other.y) < 0);

        if (thisLessThanOtherTest1 || thisLessThanOtherTest2){
            return -1;
        } else if ((this.x.compareTo(other.x) == 0) && (this.y.compareTo(other.y) == 0)) {
            return 0;
        } else {
            return 1;
        }
    }

    public String toSimpleString(){
        this.validate();
        return "(" + x + ", " + y + ")";
    }
}
