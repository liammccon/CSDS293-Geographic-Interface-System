package gis.test;

import gis.Coordinate;
import gis.InterestPoint;
import gis.InterestPoints;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class InterestPointsTest {
    static InterestPoint<String> [] interestPointArray = BiDimensionalMapTest.makeInterestPointArrayWithDuplicate();

    @Test
    public void testAddAndGet(){
        InterestPoints interestPoints = interestPointsFromArray(interestPointArray);
        Arrays.stream(interestPointArray)
                .forEach(interestPoint -> {
                    assertTrue(interestPoints.get(interestPoint.coordinate()).contains(interestPoint));
                });

        //testing invalid interestPoint
        InterestPoints.Builder builder = new InterestPoints.Builder();
        assertFalse(builder.add(new InterestPoint<>(Coordinate.ORIGIN, null)));
    }

    private InterestPoints interestPointsFromArray(InterestPoint<String> [] interestPointArray){
        InterestPoints.Builder interestPointBuilder = new InterestPoints.Builder();
        Arrays.stream(interestPointArray)
                .forEach(interestPoint -> {
                    interestPointBuilder.add(interestPoint);
                });
        return interestPointBuilder.build();
    }


}