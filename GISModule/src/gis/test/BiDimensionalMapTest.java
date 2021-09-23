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

import static gis.test.CoordinateTest.makeCoord;
import static gis.test.InterestPointTest.newPoint;
import static org.junit.jupiter.api.Assertions.*;

class BiDimensionalMapTest {

    //used in multiple tests
    BiDimensionalMap<InterestPoint> testMap = makeTestMapWithPoints(interestPointArray);

    static InterestPoint [] interestPointArray = makeInterestPointArrayWithDuplicate();

    static InterestPoint [] makeInterestPointArrayWithDuplicate(){
        final int UPPER_BOUND = 4;
        assert (UPPER_BOUND > 2);
        //will make an array with points in a square this large, starting at (0,0)
        InterestPoint [] interestPointArray = new InterestPoint [UPPER_BOUND * UPPER_BOUND];
        int placeInArray = 0;

        X_COORDINATE_LOOP:
        for (int x = 0; x < UPPER_BOUND; x++ ){

            Y_COORDINATE_LOOP:
            for (int y = 0; y < UPPER_BOUND; y++){
                interestPointArray[placeInArray] = newPoint(x, y);
                placeInArray++;
            }
        }

        //creating a duplicate for testing. They can not have the same marker since Sets do not allow duplicates
        interestPointArray[0] = new InterestPoint(Coordinate.ORIGIN, Marker.HOME);
        interestPointArray[1] = new InterestPoint(Coordinate.ORIGIN, Marker.DUPLICATE);
        return interestPointArray;

    }

    @Test
    public void testAddEverywhere(){
        BiDimensionalMap newMap = makeTestMapWithPoints(interestPointArray);
        final String ADDED_VAL = "add!";
        newMap.addEverywhere(ADDED_VAL);

        List<Coordinate> coordinates = newMap.coordinateSet();
        for (Coordinate c : coordinates){
            assertTrue(newMap.get(c).contains(ADDED_VAL));
        }
    }


    @Test
    public void testSet(){
        InterestPoint pointAt1x1 = new InterestPoint(makeCoord(1,1), Marker.HOME);
        BiDimensionalMap<InterestPoint> map = new BiDimensionalMap<>();
        addInterestPointToMap(map, pointAt1x1);
        InterestPoint demolitionZone = new InterestPoint<>(pointAt1x1.coordinate(), Marker.SCHOOL);
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

    private static boolean addInterestPointToMap (BiDimensionalMap<InterestPoint> map, InterestPoint point){
        //Helper method for testing add()
        BiDimensionalMap<InterestPoint>.Updater updater = map.getUpdater();
        updater.setCoordinate(point.coordinate());
        updater.addValue(point);
        return updater.add();
    }

    private static BiDimensionalMap<InterestPoint> makeTestMapWithPoints(InterestPoint [] interestPoints){
        BiDimensionalMap<InterestPoint> testMap = new BiDimensionalMap<>();
        for (InterestPoint point : interestPoints) {
            addInterestPointToMap(testMap, point);
        }
        return testMap;
    }

    @Test
    public void testAdd(){
        BiDimensionalMap<InterestPoint> mapToAddTo = makeTestMapWithPoints(interestPointArray);
        Arrays.stream(interestPointArray)
                .forEach(interestPoint -> {
                    //Passes if map contains the points we added, at the location we added them
                    assertTrue(mapToAddTo.get(interestPoint.coordinate()).contains(interestPoint));
                });

        BiDimensionalMap<InterestPoint>.Updater updater = mapToAddTo.getUpdater();
        boolean addedSomething = updater.add();
        //passes if using add() without calling addValue(...) returns false
        assertFalse(addedSomething);
    }



    @Test
    public void testNullHandling(){
        Coordinate nullC = null;
        Coordinate nullX = new Coordinate(null, new BigDecimal(1));
        InterestPoint nullXCoordinateInterest = new InterestPoint<>(nullX, Marker.SCHOOL);
        InterestPoint nullCoordinateInterest = new InterestPoint<>(nullX, Marker.WORK);
        BiDimensionalMap<InterestPoint> map = new BiDimensionalMap<>();

        assertNull(map.get(new BigDecimal(-1), new BigDecimal(-1)));

        assertThrows(NullPointerException.class, () -> {
            BiDimensionalMap<InterestPoint> nullMap = null;
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
        BiDimensionalMap<InterestPoint> testXSetMap = new BiDimensionalMap<>();
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

        for (InterestPoint point : interestPointArray) {

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
        List<Collection<InterestPoint>> testMapCollectionList = testMap.collectionList();

        int numOfInterestPoints = 0;
        for(Collection<InterestPoint> collection : testMapCollectionList){

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
    public void testCollectionSize(){
        assertEquals(testMap.collectionSize(), interestPointArray.length);

        BigDecimal bD0 = new BigDecimal(0);
        Predicate <? super InterestPoint> filter = interestPoint -> interestPoint.coordinate().x().compareTo(bD0) == 0;
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
        BiDimensionalMap<InterestPoint> newMap = testMap.slice(rectangle);

        List<Coordinate> newMapCoordinates = newMap.coordinateSet();

        //ensures that the coordinates in the sliced map are within the bounds
        assertFalse(newMapCoordinates.isEmpty());
        for (Coordinate coordinate : newMapCoordinates) {
            assertTrue(coordinate.compareTo(topRight) < 0);
        }
    }


    @Test
    public void testToString(){
        System.out.println("Calling toString for map: " + makeTestMapWithPoints(interestPointArray));
    }
}