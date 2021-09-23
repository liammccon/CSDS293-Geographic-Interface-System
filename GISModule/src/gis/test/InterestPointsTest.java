package gis.test;

import gis.Coordinate;
import gis.InterestPoint;
import gis.InterestPoints;
import gis.RectilinearRegion;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static gis.test.CoordinateTest.makeCoord;
import static org.junit.jupiter.api.Assertions.*;

class InterestPointsTest {

    static InterestPoint<Marker> [] interestPointArray = BiDimensionalMapTest.makeInterestPointArrayWithDuplicate();
    static InterestPoints interestPoints = interestPointsFromArray(interestPointArray);

    private static InterestPoints interestPointsFromArray(InterestPoint<Marker> [] interestPointArray){
        InterestPoints.Builder interestPointBuilder = new InterestPoints.Builder();
        Arrays.stream(interestPointArray)
                .forEach(interestPoint -> {
                    interestPointBuilder.add(interestPoint);
                });
        return interestPointBuilder.build();
    }

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
    @Test
    void testCount(){
        /*
        Using the RectilinearRegion regionOfSeparatedSet, which uses these rectangles:
            (0, 0) to (3, 2),
            (1, 3) to (4, 5).
        And adding the following interestPoints inside the rectalinearRegion: */
        InterestPoint<Marker> homeInside1 = new InterestPoint<>(makeCoord(1,1), Marker.HOME);
        InterestPoint<Marker> homeInside2 = new InterestPoint<>(makeCoord(3,3), Marker.HOME);
        InterestPoint<Marker> schoolInside = new InterestPoint<>(makeCoord(1,1), Marker.SCHOOL);

        //and adding the following home point outside the bounds
        InterestPoint<Marker> homeOutside = new InterestPoint<>(makeCoord(3, 0), Marker.HOME);
        InterestPoint<Marker> [] pointsArray = new InterestPoint[] {homeInside2, homeInside1, schoolInside, homeOutside};

        InterestPoints.Builder pointsBuilder = new InterestPoints.Builder();
        for (InterestPoint<Marker> point : pointsArray) {
            pointsBuilder.add(point);
        }
        InterestPoints points = pointsBuilder.build();
        RectilinearRegion region = RectilinearRegionTest.regionOfSeparatedSet;

        //Passes if two HOME markers (homeInside1 and homeInside2) are in the region
        assertEquals(points.count(region, Marker.HOME), 2);
        //Passes if one School marker (schoolInside1) is in the region
        assertEquals(points.count(region, Marker.SCHOOL), 1);
    }


}