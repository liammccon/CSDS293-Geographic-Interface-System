package gis.test;

import gis.Coordinate;
import gis.Rectangle;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CoordinateTest {
    //Used in several tests
    static BigDecimal d0 = new BigDecimal(0);
    static BigDecimal d1 = new BigDecimal(1);

    static Coordinate c0x0 = Coordinate.ORIGIN;
    static Coordinate c0x0b = new Coordinate(d0, d0);
    static Coordinate c0x1 = new Coordinate(d0, d1);
    static Coordinate c1x0 = new Coordinate(d1, d0);
    static Coordinate c1x1 = new Coordinate(d1, d1);

    //invalid points
    static Coordinate nullPoint = null;
    static Coordinate nullY = new Coordinate(new BigDecimal(3), null);
    static Coordinate nullXandY = new Coordinate(null, null);

    static Coordinate makeCoord(int x, int y){
        return new Coordinate(new BigDecimal(x), new BigDecimal(y));
    }

    @Test
    public void testValidate(){ //tests both validate methods
        Coordinate [] invalidCoordinates = {
                nullPoint,
                nullY,
                nullXandY
        };

        for (Coordinate coordinate : invalidCoordinates) {
            assertThrows(NullPointerException.class, () -> coordinate.validate());
            assertThrows(NullPointerException.class, () -> Coordinate.validate(coordinate));
        }
    }

    @Test
    public void testCompareWithNull () {
        Coordinate c1 = null;
        assertThrows(NullPointerException.class, ()->{
            c0x0.compareTo(c1);
        });
        assertThrows(NullPointerException.class, ()->{
            c1.compareTo(c0x0);
        });
    }

    @Test
    public void testSimpleStringWithNull(){
        Coordinate c1 = null;
        assertThrows(NullPointerException.class, ()->{
            c1.toSimpleString();
        });
    }

    @Test
    public void testCompareTo(){

        assertTrue(c0x0.compareTo(c0x0b) == 0);
        assertTrue(c0x0.compareTo(c0x1)<0);
        assertTrue(c0x0.compareTo(c1x0)<0);
        assertTrue(c0x0.compareTo(c1x1) < 0);
        assertTrue(c1x1.compareTo(c1x0) > 0);
        assertTrue(c1x0.compareTo(c0x0) > 0);
    }


}