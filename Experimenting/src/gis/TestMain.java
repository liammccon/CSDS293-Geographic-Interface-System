package gis;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class TestMain<T> {

    public static void main(String[]args){
        Coordinate c = null;
        //Coordinate c = new Coordinate(new BigDecimal(1), new BigDecimal(1));
        BiDimensionalMap<InterestPoint<String>> map = new BiDimensionalMap<>();
        InterestPoint<String> home = new InterestPoint<>(c, "HOME");
        InterestPoint<String> nullIsland = new InterestPoint<>(Coordinate.ORIGIN, "Null Island");
        TestMain<String> main = new TestMain<>();
        main.addInterestPointToMap(map, home);
        main.addInterestPointToMap(map, nullIsland);
        //addInterestPointToMap();
        System.out.println(map.get(c) + ", " + map.get(Coordinate.ORIGIN));
    }

    private void addInterestPointToMap (BiDimensionalMap<InterestPoint<T>> map, InterestPoint<T> point){
        BiDimensionalMap<InterestPoint<T>>.Updater updater = map.getUpdater();
        updater.setCoordinate(point.coordinate());
        updater.addValue(point);
        updater.add();
    }

    public static void streamTest(){
        Person [] arr = {new Person("Joe", 13), new Person("Jane", 24), new Person("Xeno", 654)};
        List<Person> people = List.of(arr);
        Stream<Person> streamOfPeople = people.stream();

    }
}

record Person(String name, int age) implements Comparable<Person> {
    @Override
    public int compareTo(Person o) {
        return name.compareTo(o.name);
    }

    public int birthday (){
        assert age > 0 :"Age can not be 0 or negative";
        return age;
    }
}