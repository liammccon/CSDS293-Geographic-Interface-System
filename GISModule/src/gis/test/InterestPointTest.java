package gis.test;

import gis.Coordinate;
import gis.InterestPoint;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class InterestPointTest {
    //used in multiple tests
    static InterestPoint<Marker> originPoint = newPoint(0, 0);

    //helper with making interestPoints
    static InterestPoint newPoint(int x, int y) {
        Coordinate c = new Coordinate(new BigDecimal(x), new BigDecimal(y));
        c.validate();
        return new InterestPoint(c, randomMarker());
    }

    private static Enum randomMarker() {
        Random random = new Random();
        Marker[] values = Marker.values();
        return values[random.nextInt(values.length)];
    }

    @Test
    void validate() {
        Coordinate nullXYCoordinate = new Coordinate(null, null);

        InterestPoint<Marker> nullCoordinatePoint = new InterestPoint<>(null, Marker.CLASSROOM);
        InterestPoint<Marker> nullXYCoordinatePoint = new InterestPoint<>(nullXYCoordinate, Marker.SCHOOL);
        InterestPoint<Marker> nullMarkerPoint = new InterestPoint<>(Coordinate.ORIGIN, null);

        assertThrows(NullPointerException.class, nullCoordinatePoint::validate);

        assertThrows(NullPointerException.class, nullXYCoordinatePoint::validate);

        assertThrows(NullPointerException.class, nullMarkerPoint::validate);
    }


    @Test
    void hasMarker() {
        InterestPoint<Marker> point = new InterestPoint<>(Coordinate.ORIGIN, Marker.HOME);
        assertTrue(point.hasMarker(Marker.HOME));
        assertFalse(point.hasMarker(Marker.SCHOOL));
    }

    @Test
    void testToString() {
        //Verify the printed interestPoint makes sense
        System.out.println("Testing toString for Coordinate: " + originPoint);
    }
}