package gis;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class BiDimensionalMapTest {
    Coordinate c1x1 = new Coordinate(new BigDecimal(1), new BigDecimal(1));
    InterestPoint<String> home = new InterestPoint<>(c1x1, "HOME");
    InterestPoint<String> originIsland = new InterestPoint<>(Coordinate.ORIGIN, "ORIGIN ISLAND");

    private static void addInterestPointToMap (BiDimensionalMap<InterestPoint<String>> map, InterestPoint<String> point){
        //Helper method for testing add()
        BiDimensionalMap<InterestPoint<String>>.Updater updater = map.getUpdater();
        updater.setCoordinate(point.coordinate());
        updater.addValue(point);
        updater.add();
    }

    @Test
    public void testAdd(){
        BiDimensionalMap<InterestPoint<String>> map = new BiDimensionalMap<>();
        addInterestPointToMap(map, home);
        addInterestPointToMap(map, originIsland);
        //Passes if map contains the points we added, at the location we added them
        assertTrue(map.get(c1x1).contains(home));
        assertTrue(map.get(Coordinate.ORIGIN).contains(originIsland));
    }

    @Test
    public void testSet(){
        BiDimensionalMap<InterestPoint<String>> map = new BiDimensionalMap<>();
        addInterestPointToMap(map, home);
        InterestPoint<String> demolitionZone = new InterestPoint<>(home.coordinate(), "DEMOLITION ZONE");
        BiDimensionalMap.Updater updater = map.getUpdater();
        updater.setCoordinate(demolitionZone.coordinate());
        updater.addValue(demolitionZone);
        updater.set();
        /*Passes if map contains the demolitionZone marker
          and does not contain the home that was previously at that location
          because set() overrides previously stored values
         */
        assertTrue(map.get(demolitionZone.coordinate()).contains(demolitionZone));
        assertFalse(map.get(home.coordinate()).contains(home));
    }

    @Test
    public void testNullHandling(){
        Coordinate nullC = null;
        Coordinate nullX = new Coordinate(null, new BigDecimal(1));
        InterestPoint<String> nullXCoordinateInterest = new InterestPoint<>(nullX, "Home");
        BiDimensionalMap<InterestPoint<String>> map = new BiDimensionalMap<>();

        assertThrows(NullPointerException.class, () -> {
            BiDimensionalMap<InterestPoint<String>> nullMap = null;
            nullMap.getUpdater();
        });

        assertThrows(NullPointerException.class, () -> {
            addInterestPointToMap(map, nullXCoordinateInterest);
        });
    }
}