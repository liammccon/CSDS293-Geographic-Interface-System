package gis.test;

import gis.BiDimensionalMap;
import gis.Coordinate;
import gis.InterestPoint;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class BiDimensionalMapTest {
    //used in multiple tests
    private static Coordinate[][] grid = CoordinateTest.coordinateGrid2x2;
    static InterestPoint<String> pointAt1x1 = InterestPointTest.pointAt1x1;
    static InterestPoint<String> pointAt1x1b = new InterestPoint<>(grid[1][1], "PointAt1x1b");
    static InterestPoint<String> originPoint = InterestPointTest.originPoint;
    static InterestPoint<String> pointAt1x0 = new InterestPoint<>(grid[1][0], "PointAt1x0");
    static InterestPoint<String> pointAt0x1 = new InterestPoint<>(grid[0][1], "PointAt0x1");
    static InterestPoint<String> [] interestPointArray2x2 = new InterestPoint[] {
            pointAt1x1, originPoint, pointAt1x0, pointAt0x1, pointAt1x1b
    };




    @Test
    public void testSet(){
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
        BiDimensionalMap<InterestPoint<String>> map = makeTestMapWithPoints(interestPointArray2x2);
        Arrays.stream(interestPointArray2x2)
                .forEach(interestPoint -> {
                    //Passes if map contains the points we added, at the location we added them
                    assertTrue(map.get(interestPoint.coordinate()).contains(interestPoint));
                });

        BiDimensionalMap<InterestPoint<String>>.Updater updater = map.getUpdater();
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

    //todo: assignment 3 tests. refactor this into old tests?



    @Test
    public void testxSet(){
        //todo!
        BiDimensionalMap<InterestPoint<String>> testMap = new BiDimensionalMap<>();
        assertTrue(testMap.xSet().isEmpty()); //verifies that an empty set is created when no points exist
        testMap = makeTestMapWithPoints(interestPointArray2x2);

        Set<BigDecimal> myXSet = Arrays.stream(interestPointArray2x2)
                .map(InterestPoint::coordinate)
                .map(Coordinate::x)
                .collect(Collectors.toSet());

        assertEquals(testMap.xSet(), myXSet);
    }

    @Test
    public void testYSet(){
        //todo!
        BiDimensionalMap<InterestPoint<String>> testMap = makeTestMapWithPoints(interestPointArray2x2);


        for (InterestPoint<String> point : interestPointArray2x2) {
            Set<BigDecimal> myYSet = Arrays.stream(interestPointArray2x2)
                    .map(InterestPoint::coordinate)
                    .map(Coordinate::y)
                    .collect(Collectors.toSet());

            assertEquals(testMap.ySet(point.coordinate().x()), myYSet);
        }

    }

    @Test
    public void testCoordinateSet(){
        //todo!
        BiDimensionalMap<InterestPoint<String>> testMap = makeTestMapWithPoints(interestPointArray2x2);
        List<Coordinate> myCoordinateSet = Arrays.stream(interestPointArray2x2)
                .map(InterestPoint::coordinate)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        assertEquals(testMap.coordinateSet(), myCoordinateSet);
    }

    @Test
    public void testCollectionList(){
        //todo!
        BiDimensionalMap<InterestPoint<String>> testMap = makeTestMapWithPoints(interestPointArray2x2);

        System.out.println("TODO: MAKE TESTCOLLECTIONLIST: " + testMap.collectionList());
    }

    @Test
    public void testSlice(){

    }

    @Test
    public void testToString(){
        //todo this ok?
        System.out.println("Calling toString for map: " + makeTestMapWithPoints(interestPointArray2x2));
    }




}