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
    public void testCompareTrue(){
        BigDecimal d0 = new BigDecimal(0);
        BigDecimal d1 = new BigDecimal(1);
        BigDecimal dNeg1 = new BigDecimal(-1);

        Coordinate c0x0a = new Coordinate(d0, d0);
        Coordinate c0x0b = new Coordinate(d0, d0);
        Coordinate c0x1 = new Coordinate(d0, d1);
        Coordinate c1x0 = new Coordinate(d1, d0);

        //todo: make the test
    }


}