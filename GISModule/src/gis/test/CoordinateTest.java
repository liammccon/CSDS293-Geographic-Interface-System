package gis.test;

import gis.Coordinate;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CoordinateTest {
    //Used in several tests static
    static BigDecimal d0 = new BigDecimal(0);
    static BigDecimal d1 = new BigDecimal(1);

    static Coordinate c0x0 = Coordinate.ORIGIN;
    static Coordinate c0x0b = new Coordinate(d0, d0);
    static Coordinate c0x1 = new Coordinate(d0, d1);
    static Coordinate c1x0 = new Coordinate(d1, d0);
    static Coordinate c1x1 = new Coordinate(d1, d1);

    @Test
    public void testValidate(){ //tests both validate methods
        Coordinate nullPoint = null;
        Coordinate nullY = new Coordinate(new BigDecimal(3), null);
        Coordinate nullXandY = new Coordinate(null, null);

        assertThrows(NullPointerException.class, ()->{
            nullPoint.validate();
        } );

        assertThrows(NullPointerException.class, ()->{
            nullY.validate();
        } );

        assertThrows(NullPointerException.class, ()->{
            nullXandY.validate();
        } );

        assertThrows(NullPointerException.class, ()->{
            Coordinate.validate(null);
        });

        assertThrows(NullPointerException.class, ()->{
            Coordinate.validate(nullY);
        });
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
        assertTrue(c1x0.compareTo(c0x0) > 0);
    }


}