package gis.test;

import gis.BiDimensionalMap;
import gis.Rectangle;
import gis.RectilinearRegion;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import static gis.test.RectangleTest.rectMaker;
import static gis.test.CoordinateTest.makeCoord;
import static org.junit.jupiter.api.Assertions.*;

class RectilinearRegionTest {

    static Set<Rectangle> validSeparatedSet = new HashSet<>(Arrays.asList(
            rectMaker(0, 0, 3, 2),
            rectMaker(1, 3, 4, 5)
    ));

    static Set<Rectangle> overlappingSet = new HashSet<>(Arrays.asList(
            rectMaker(0,0,3,3),
            rectMaker(2,2,4,4)
    ));

    static RectilinearRegion regionOfSeparatedSet = RectilinearRegion.of(validSeparatedSet);

    @Test
    void testMakeRectRegion(){
        assertEquals(regionOfSeparatedSet.getRectangles(), validSeparatedSet);
    }

    @Test
    void testRectangleMap() {
        /*Using these rectangles
         (R1): (0, 0) to (3, 2). (R2): (1, 2) to (4, 5)
        The rectangleMap() should contain R1 at points [(0, 0), (1, 0)]
        and R2 at points [(1, 2), (3, 2)]
         */
        Rectangle r1 = rectMaker(0, 0, 3, 2);
        Rectangle r2 = rectMaker(1, 2, 4, 5);

        Set<Rectangle> validContiguousSet = new HashSet<>(Arrays.asList(r1, r2));
        BiDimensionalMap<Rectangle> rectangleMap = RectilinearRegion.of(validContiguousSet).rectangleMap();

        assertTrue(rectangleMap.get(makeCoord(0, 0)).contains(r1));
        assertTrue(rectangleMap.get(makeCoord(1, 0)).contains(r1));
        assertTrue(rectangleMap.get(makeCoord(1, 2)).contains(r2));
        assertTrue(rectangleMap.get(makeCoord(3, 2)).contains(r2));

    }

    @Test
    void testIsOverlapping() {
        assertThrows(Exception.class, ()->{
            RectilinearRegion.of(overlappingSet);
        });
    }
}