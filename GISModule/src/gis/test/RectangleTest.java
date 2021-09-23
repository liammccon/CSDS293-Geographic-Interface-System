package gis.test;

import gis.Coordinate;
import gis.Rectangle;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static gis.test.CoordinateTest.makeCoord;
import static org.junit.jupiter.api.Assertions.*;

class RectangleTest {

    static Rectangle [] validRectangles = {
            rectMaker(-1, 0, 2, 4), //normal rectangle
            rectMaker(1, 1, 3, 1), //horizontal line
            //rectMaker(1, 3, 1, 5) //vertical line
    };

    static Rectangle rectMaker(int left, int bottom, int right, int top){
        Coordinate bottomLeft = new Coordinate( new BigDecimal(left), new BigDecimal(bottom));
        Coordinate topRight = new Coordinate(new BigDecimal(right), new BigDecimal(top));
        return new Rectangle(bottomLeft, topRight);

    }

    @Test
    void testValidate() {
        Rectangle [] invalidRectangles = {
                new Rectangle(makeCoord(0,1), null),
                new Rectangle(makeCoord(1,0), CoordinateTest.nullY),
                rectMaker(1,0,0,0),
                rectMaker(0,0,0,0),
                rectMaker(1,1,1,0)
        };

        for (Rectangle rectangle : invalidRectangles) {
            assertThrows(Exception.class, rectangle::validate);
            assertThrows(Exception.class, () -> Rectangle.validate(rectangle));
        }

        for (Rectangle rectangle: validRectangles) {
            rectangle.validate();
        }
    }



    @Test
    void testLeftRightBottomTop() {
        BigDecimal [] decimals = {
                new BigDecimal(0),
                new BigDecimal(1),
                new BigDecimal(2),
                new BigDecimal(3),
        };

        Coordinate leftCoor = new Coordinate(decimals[0], decimals[1]);
        Coordinate rightCoor = new Coordinate(decimals[2], decimals[3]);

        Rectangle r = new Rectangle(leftCoor, rightCoor);
        r.validate();
        assertEquals(r.left(), decimals[0]);
        assertEquals(r.right(), decimals[2]);
        assertEquals(r.bottom(), decimals[1]);
        assertEquals(r.top(), decimals[3]);

    }

    @Test
    public void testToString (){
        Rectangle rectangle = new Rectangle(Coordinate.ORIGIN, makeCoord(1,1));
        System.out.println("Testing rectangle.toString " + rectangle);
    }
}