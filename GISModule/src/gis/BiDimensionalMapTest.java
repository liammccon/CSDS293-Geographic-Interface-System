package gis;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class BiDimensionalMapTest {
    //used in multiple tests
    Coordinate c1x1 = new Coordinate(new BigDecimal(1), new BigDecimal(1));
    InterestPoint<String> home = new InterestPoint<>(c1x1, "HOME");
    InterestPoint<String> originIsland = new InterestPoint<>(Coordinate.ORIGIN, "ORIGIN ISLAND");

    @Test
    public void testSet(){
        BiDimensionalMap<InterestPoint<String>> map = new BiDimensionalMap<>();
        addInterestPointToMap(map, home);
        InterestPoint<String> demolitionZone = new InterestPoint<>(home.coordinate(), "DEMOLITION ZONE");
        BiDimensionalMap.Updater updater = map.getUpdater();
        updater.setCoordinate(demolitionZone.coordinate());
        updater.addValue(demolitionZone);
        Collection<String> previousValues = updater.set();
        /*Passes if:
          1. map contains the demolitionZone marker
          2. does not contain the home that was previously at that location
          because set() overrides previously stored values
          3. Correctly returned the previousValue home
         */
        assertTrue(map.get(demolitionZone.coordinate()).contains(demolitionZone));
        assertFalse(map.get(home.coordinate()).contains(home));
        assertTrue(previousValues.contains(home));
    }

    private static boolean addInterestPointToMap (BiDimensionalMap<InterestPoint<String>> map, InterestPoint<String> point){
        //Helper method for testing add()
        BiDimensionalMap<InterestPoint<String>>.Updater updater = map.getUpdater();
        updater.setCoordinate(point.coordinate());
        updater.addValue(point);
        return updater.add();
    }

    @Test
    public void testAdd(){
        BiDimensionalMap<InterestPoint<String>> map = new BiDimensionalMap<>();
        boolean addedHome = addInterestPointToMap(map, home);
        boolean addedOriginIsland = addInterestPointToMap(map, originIsland);

        BiDimensionalMap<InterestPoint<String>>.Updater updater = map.getUpdater();
        boolean addedSomething = updater.add();

        //Passes if map contains the points we added, at the location we added them
        assertTrue(map.get(c1x1).contains(home));
        assertTrue(map.get(Coordinate.ORIGIN).contains(originIsland));
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
}