package gis;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CoordinateTest {

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
        Coordinate c0 = Coordinate.ORIGIN;
        Coordinate c1 = null;
        assertThrows(NullPointerException.class, ()->{
            c0.compareTo(c1);
        });
        assertThrows(NullPointerException.class, ()->{
            c1.compareTo(c0);
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
        BigDecimal d0 = new BigDecimal(0);
        BigDecimal d1 = new BigDecimal(1);
        BigDecimal dNeg1 = new BigDecimal(-1);

        Coordinate c0x0a = new Coordinate(d0, d0);
        Coordinate c0x0b = new Coordinate(d0, d0);
        Coordinate c0x1 = new Coordinate(d0, d1);
        Coordinate c1x0 = new Coordinate(d1, d0);
        Coordinate c1x1 = new Coordinate(d1, d1);

        assertTrue(c0x0a.compareTo(c0x0b) == 0);
        assertTrue(c0x0a.compareTo(c0x1)<0);
        assertTrue(c0x0a.compareTo(c1x0)<0);
        assertTrue(c0x0a.compareTo(c1x1) < 0);
        assertTrue(c1x0.compareTo(c0x0a) > 0);
    }


}