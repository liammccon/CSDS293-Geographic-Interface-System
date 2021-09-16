package gis.test;

import gis.Coordinate;
import gis.InterestPoint;
import gis.InterestPoints;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InterestPointsTest {
    static InterestPoint<String> [] interestPointArray = BiDimensionalMapTest.makeInterestPointArrayWithDuplicate();
    static InterestPoints interestPoints = interestPointsFromArray(interestPointArray);

    @Test
    public void testAddAndGet(){
        Arrays.stream(interestPointArray)
                .forEach(interestPoint -> {
                    assertTrue(interestPoints.get(interestPoint.coordinate()).contains(interestPoint));
                });

        //testing invalid interestPoint
        InterestPoints.Builder builder = new InterestPoints.Builder();
        assertFalse(builder.add(new InterestPoint<>(Coordinate.ORIGIN, null)));
    }

    @Test
    public void testInterestPoints(){
        List<Collection<InterestPoint>> interestPointsCollection =  interestPoints.interestPoints();

        int numOfInterestPoints = 0;
        for(Collection<InterestPoint> collection : interestPointsCollection){

            for(InterestPoint interestPoint : collection) {
                numOfInterestPoints++;
                //True if each interestPoint in the collectionList was
                //in the original interestPointArray which was added to the map
                assertTrue(List.of(interestPointArray).contains(interestPoint));
            }
        }

        //Assures that the same number of interestPoints in the collection list is in
        //the original interestPointArray which was added to the map
        assertEquals(interestPointArray.length , numOfInterestPoints);
    }

    private static InterestPoints interestPointsFromArray(InterestPoint<String> [] interestPointArray){
        InterestPoints.Builder interestPointBuilder = new InterestPoints.Builder();
        Arrays.stream(interestPointArray)
                .forEach(interestPoint -> {
                    interestPointBuilder.add(interestPoint);
                });
        return interestPointBuilder.build();
    }


}