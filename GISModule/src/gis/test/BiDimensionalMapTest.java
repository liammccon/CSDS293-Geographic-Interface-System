package gis.test;

import gis.BiDimensionalMap;
import gis.Coordinate;
import gis.InterestPoint;
import gis.Rectangle;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class BiDimensionalMapTest {

    //used in multiple tests
    BiDimensionalMap<InterestPoint<String>> testMap = makeTestMapWithPoints(interestPointArray);

    static InterestPoint<String> [] interestPointArray = makeInterestPointArrayWithDuplicate();

    static InterestPoint<String> [] makeInterestPointArrayWithDuplicate(){
        final int UPPER_BOUND = 4;
        assert (UPPER_BOUND > 2);
        //will make an array with points in a square this large, starting at (0,0)
        InterestPoint<String> [] interestPointArray = new InterestPoint [UPPER_BOUND * UPPER_BOUND];
        int placeInArray = 0;

        X_COORDINATE_LOOP:
        for (int x = 0; x < UPPER_BOUND; x++ ){

            Y_COORDINATE_LOOP:
            for (int y = 0; y < UPPER_BOUND; y++){
                interestPointArray[placeInArray] = newPoint(x, y);
                placeInArray++;
            }
        }

        //creating a duplicate for testing
        InterestPoint<String> interestPointAt0x0 = interestPointArray[0];
        interestPointArray[1] = new InterestPoint<String>(Coordinate.ORIGIN, interestPointAt0x0.marker() + "Copy");
        return interestPointArray;

    }

    //helper with making interestPoints
    private static InterestPoint newPoint(int x, int y) {
        Coordinate c = new Coordinate(new BigDecimal(x), new BigDecimal(y));
        c.validate();
        return new InterestPoint(c, "pointAt" + x + "x" + y);
    }


    @Test
    public void testSet(){
        InterestPoint<String> pointAt1x1 = newPoint(1,1);
        BiDimensionalMap<InterestPoint<String>> map = new BiDimensionalMap<>();
        addInterestPointToMap(map, pointAt1x1);
        InterestPoint<String> demolitionZone = new InterestPoint<>(pointAt1x1.coordinate(), "DEMOLITION ZONE");
        BiDimensionalMap.Updater updater = map.getUpdater();
        updater.setCoordinate(demolitionZone.coordinate());
        updater.addValue(demolitionZone);
        Collection<String> previousValues = updater.set();
        /*Passes if:
          1. map contains the demolitionZone marker
          2. does not contain the originPoint that was previously at that location
          because set() overrides previously stored values
          3. Correctly returned the previousValue originPoint
         */
        assertTrue(map.get(demolitionZone.coordinate()).contains(demolitionZone));
        assertFalse(map.get(pointAt1x1.coordinate()).contains(pointAt1x1));
        assertTrue(previousValues.contains(pointAt1x1));
    }

    private static boolean addInterestPointToMap (BiDimensionalMap<InterestPoint<String>> map, InterestPoint<String> point){
        //Helper method for testing add()
        BiDimensionalMap<InterestPoint<String>>.Updater updater = map.getUpdater();
        updater.setCoordinate(point.coordinate());
        updater.addValue(point);
        return updater.add();
    }

    private static BiDimensionalMap<InterestPoint<String>> makeTestMapWithPoints(InterestPoint<String> [] interestPoints){
        BiDimensionalMap<InterestPoint<String>> testMap = new BiDimensionalMap<>();
        for (InterestPoint<String> point : interestPoints) {
            addInterestPointToMap(testMap, point);
        }
        return testMap;
    }

    @Test
    public void testAdd(){
        BiDimensionalMap<InterestPoint<String>> mapToAddTo = makeTestMapWithPoints(interestPointArray);
        Arrays.stream(interestPointArray)
                .forEach(interestPoint -> {
                    //Passes if map contains the points we added, at the location we added them
                    assertTrue(mapToAddTo.get(interestPoint.coordinate()).contains(interestPoint));
                });

        BiDimensionalMap<InterestPoint<String>>.Updater updater = mapToAddTo.getUpdater();
        boolean addedSomething = updater.add();
        //passes if using add() without calling addValue(...) returns false
        assertFalse(addedSomething);
    }



    @Test
    public void testNullHandling(){
        Coordinate nullC = null;
        Coordinate nullX = new Coordinate(null, new BigDecimal(1));
        InterestPoint<String> nullXCoordinateInterest = new InterestPoint<>(nullX, "SCHOOL");
        InterestPoint<String> nullCoordinateInterest = new InterestPoint<>(nullX, "WORK");
        BiDimensionalMap<InterestPoint<String>> map = new BiDimensionalMap<>();

        assertNull(map.get(new BigDecimal(-1), new BigDecimal(-1)));

        assertThrows(NullPointerException.class, () -> {
            BiDimensionalMap<InterestPoint<String>> nullMap = null;
            nullMap.getUpdater();
        });

        assertThrows(NullPointerException.class, () -> {
            addInterestPointToMap(map, nullXCoordinateInterest);
        });

        assertThrows(NullPointerException.class, () -> {
            addInterestPointToMap(map, nullCoordinateInterest);
        });
    }




    //Assignment 3 tests

    @Test
    public void testxSet(){
        BiDimensionalMap<InterestPoint<String>> testXSetMap = new BiDimensionalMap<>();
        assertTrue(testXSetMap.xSet().isEmpty()); //verifies that an empty set is created when no points exist
        testXSetMap = makeTestMapWithPoints(interestPointArray);

        Set<BigDecimal> myXSet = Arrays.stream(interestPointArray)
                .map(InterestPoint::coordinate)
                .map(Coordinate::x)
                .collect(Collectors.toSet());

        assertEquals(testXSetMap.xSet(), myXSet);
    }

    @Test
    public void testYSet(){

        for (InterestPoint<String> point : interestPointArray) {

            BigDecimal pointX = point.coordinate().x();
            BigDecimal pointY = point.coordinate().y();

            assertTrue(testMap.ySet(pointX).contains(pointY));
        }
    }

    @Test
    public void testCoordinateSet(){
        List<Coordinate> myCoordinateSet = Arrays.stream(interestPointArray)
                .map(InterestPoint::coordinate)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        assertEquals(testMap.coordinateSet(), myCoordinateSet);
    }

    @Test
    public void testCollectionList(){
        List<Collection<InterestPoint<String>>> testMapCollectionList = testMap.collectionList();

        int numOfInterestPoints = 0;
        for(Collection<InterestPoint<String>> collection : testMapCollectionList){

            for(InterestPoint<String> interestPoint : collection) {
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
    public void testCollectionSize(){
        assertEquals(testMap.collectionSize(), interestPointArray.length);

        BigDecimal bD0 = new BigDecimal(0);
        Predicate <? super InterestPoint<String>> filter = interestPoint -> interestPoint.coordinate().x().compareTo(bD0) == 0;
        long filteredSize = testMap.collectionSize(filter);
        //should return a collection with all points having x = 0

        long compareSize = Arrays.stream(interestPointArray)
                .filter(filter)
                .count();

        assertEquals(filteredSize, compareSize);
    }

    @Test
    public void testSlice(){
        //(Could add another test to make sure no coordinates inside the bounds were missed)
        BigDecimal bD2 = new BigDecimal(2);
        Coordinate topRight = new Coordinate(bD2, bD2);

        Rectangle rectangle = new Rectangle(Coordinate.ORIGIN, topRight);
        BiDimensionalMap<InterestPoint<String>> newMap = testMap.slice(rectangle);

        List<Coordinate> newMapCoordinates = newMap.coordinateSet();

        //ensures that the coordinates in the sliced map are within the bounds
        for (Coordinate coordinate : newMapCoordinates) {
            assertTrue(coordinate.compareTo(topRight) < 0);
        }
    }


    @Test
    public void testToString(){
        System.out.println("Calling toString for map: " + makeTestMapWithPoints(interestPointArray));
    }
}