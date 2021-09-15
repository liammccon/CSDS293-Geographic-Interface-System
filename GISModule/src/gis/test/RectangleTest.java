package gis.test;

import gis.Coordinate;
import gis.Rectangle;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class RectangleTest {

    private static Coordinate[][] grid = CoordinateTest.coordinateGrid2x2;

    @Test
    void testValidate() {
        Rectangle [] invalidRectangles = { //todo take this approach earlier
                new Rectangle(grid[0][1], null),
                new Rectangle(grid[1][0], CoordinateTest.nullY),
                new Rectangle(grid[1][0], grid[0][0]),
                new Rectangle(grid[0][0], grid[0][0]),
                new Rectangle(grid[1][1], grid[1][0]),

        };

        for (Rectangle rectangle : invalidRectangles) {
            assertThrows(Exception.class, () -> rectangle.validate());
            assertThrows(Exception.class, () -> Rectangle.validate(rectangle));
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
}