package gis.test;

import gis.Coordinate;
import gis.InterestPoint;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InterestPointTest {
    //used in multiple tests
    static InterestPoint<String> originPoint = new InterestPoint<>(Coordinate.ORIGIN, "OriginPoint");
    static InterestPoint<String> pointAt1x1 = new InterestPoint<>(CoordinateTest.c1x1, "PointAt1x1");


    @Test
    void validate() {
        Coordinate nullXYCoordinate = new Coordinate(null, null);
        InterestPoint<String> nullPoint = null;
        String nullString = null;

        InterestPoint<String> nullCoordinatePoint = new InterestPoint<>(null, "Atlantis");
        InterestPoint<String> nullXYCoordinatePoint = new InterestPoint<>(nullXYCoordinate, "Mars");
        InterestPoint<String> nullMarkerPoint = new InterestPoint<>(Coordinate.ORIGIN, nullString);

        assertThrows(NullPointerException.class, nullCoordinatePoint::validate);

        assertThrows(NullPointerException.class, nullXYCoordinatePoint::validate);

        assertThrows(NullPointerException.class, nullMarkerPoint::validate);
    }


    @Test
    void hasMarker() {
        final String HOME = "Home";
        final String NEW_STRING_HOME = "Home";
        final String HOMETOWN = "Hometown";
        InterestPoint<String> point = new InterestPoint<>(Coordinate.ORIGIN, HOME);
        assertTrue(point.hasMarker(NEW_STRING_HOME));
        assertFalse(point.hasMarker(HOMETOWN));
    }

    @Test
    void testToString() {
        //Verify the printed interestPoint makes sense
        System.out.println("Testing toString for Coordinate: " + originPoint);
    }
}