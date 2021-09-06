package gis;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class CoordinateTest {

    @Test
    public void testValidate(){
        assertThrows(NullPointerException.class, ()->{
            Coordinate nullY = new Coordinate(new BigDecimal(3), null);
            nullY.validate();
        } );
    }

    @Test
    public void testValidateWithNullCordinate(){
        assertThrows(NullPointerException.class, ()->{
            Coordinate.validate(null);
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

        //todo: make the test
        assertTrue(c0x0a.compareTo(c0x0b) == 0);
        assertTrue(c0x0a.compareTo(c0x1)<0);
        assertTrue(c0x0a.compareTo(c1x0)<0);
        assertTrue(c0x0a.compareTo(c1x1) < 0);
        assertTrue(c1x0.compareTo(c0x0a) > 0);
    }


}