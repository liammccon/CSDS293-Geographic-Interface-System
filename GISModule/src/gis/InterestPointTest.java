package gis;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class InterestPointTest {
    //todo: these ok?
    @Test
    void validate() {
        Coordinate nullXYCoordinate = new Coordinate(null, null);
        InterestPoint<String> nullPoint = null;
        String nullString = null;

        InterestPoint<String> nullCoordinatePoint = new InterestPoint<>(null, "Atlantis");
        InterestPoint<String> nullXYCoordinatePoint = new InterestPoint<>(nullXYCoordinate, "Mars");
        InterestPoint<String> nullMarkerPoint = new InterestPoint<>(Coordinate.ORIGIN, nullString);

        assertThrows(NullPointerException.class, ()->{
            nullCoordinatePoint.validate();
        } );

        assertThrows(NullPointerException.class, ()->{
            nullXYCoordinatePoint.validate();
        } );

        assertThrows(NullPointerException.class, ()->{
            nullMarkerPoint.validate();
        } );
    }


    @Test
    void hasMarker() {
        final String HOME = "HOME";
        final String NEW_STRING_HOME = "HOME";
        final String HOMETOWN = "HOMETOWN";
        InterestPoint<String> point = new InterestPoint<>(Coordinate.ORIGIN, HOME);
        assertTrue(point.hasMarker(NEW_STRING_HOME));
        assertFalse(point.hasMarker(HOMETOWN));
    }

    @Test
    void testToString() {
        //Verify the printed interestPoint makes sense
        InterestPoint<String> interestPoint = new InterestPoint<>(Coordinate.ORIGIN, "HOME");
        System.out.println(interestPoint);
    }
}