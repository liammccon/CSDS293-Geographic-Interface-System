package gis;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * A 2D map used to store information about landmarks in a geographical area.
 * <p>Add landmarks with the {@code Updater}</p>
 * @param <T> the type for markers stored at each coordinate point.
 */
public class BiDimensionalMap <T> {

    /**
     * Markers stored in a Sorted Map of Sorted Maps
     * Works by accessing the first sorted map with the x value as key ( with points.get(x)) to
     * get a map of all collections of markers ( with points.get(x).get(y) ) at y locations for that x
     */
    private final SortedMap<BigDecimal, SortedMap<BigDecimal, Collection<T> > > points = new TreeMap<>();

    public BiDimensionalMap() { }

    /**
     * Enters a new hashmap in all the coordinates made of (xCoord, yCoord)
     */
    BiDimensionalMap ( Collection<BigDecimal> xCoord, Collection<BigDecimal> yCoord) {

        for (BigDecimal x: xCoord) {
            for (BigDecimal y: yCoord) {
                //no value added to updater so empty map is added
                new Updater().setX(x).setY(y).add();
            }
        }
    }

    private static void validate(BigDecimal x, BigDecimal y){
        Objects.requireNonNull(x);
        Objects.requireNonNull(y);
    }

    /**
     * Adds a given value to every (x, y) location that exists in the map.
     * @param value
     */
    public final void addEverywhere(T value) {
        addEverywhere(coordinateSet(), value);
    }

    /**
     * Adds a given value at every coordinate location given.
     */
    public void addEverywhere(List<Coordinate> coordinateSet, T value) {
        for (Coordinate coordinate : coordinateSet){
            Updater updater = getUpdater();
            updater.setCoordinate(coordinate).addValue(value).add();
        }
    }

    /**
     * Gets the collection of markers at the given (x, y) coordinates.
     * @return The collection if it exists, or null if no collection exists there
     */
    public final Collection<T> get(BigDecimal x, BigDecimal y){
        validate(x, y);
        if (collectionExistsAt(x, y)){
            return points.get(x).get(y);
        }
        else {
            return null;
        }
    }

    /**
     * Gets the collection of markers at the given coordinate.
     * @return The collection if it exists, or null if no collection exists there
     */
    public final Collection<T> get(Coordinate coordinate) {
        return get(coordinate.x(), coordinate.y());
    }


    /**True if points has a SortedMap at x and a Collection at that SortedMap's y location.
     * No markers need to exist to return true, just the Map.
     * If true, markers can be safely added to points.get(x).get(y)*/
    private boolean collectionExistsAt(BigDecimal x, BigDecimal y){
        validate(x, y);
        if (points.containsKey(x)){
            return points.get(x).containsKey(y);
        } else {
            return false;
        }
    }

    /**
     *
     * @return the collection of x coordinates in the map, or an empty set if none exist.
     */
    public final Set<BigDecimal> xSet() {
        return points.keySet();
    }

    /**
     * @return the collection of y coordinates in the map corresponding to the given x,
     *or an empty set if none exist.
     */
    public final Set<BigDecimal> ySet(BigDecimal x) {
        Objects.requireNonNull(x);
        if (points.containsKey(x)) {
            return points.get(x).keySet();
        } else {
            throw new IllegalArgumentException("Given x value does not exist yet in the map");
        }
    }

    /**
     *
     * @return the list of sorted coordinates, or an empty list if none exist
     */
    public final List<Coordinate> coordinateSet(){
        List<Coordinate> coordinateList = new ArrayList<>();

        X_VALUES_IN_POINTS:
        for (BigDecimal x : xSet()) {

            Y_VALUES_FOR_EACH_X:
            for (BigDecimal y : ySet(x)) {
                coordinateList.add(new Coordinate(x, y));
            }
        }

        return coordinateList;
    }

    /**
     *
     * @return a list of contents from points, implicitly sorted by their coordinates.
     */
    public final List<Collection<T>> collectionList(){
        List<Coordinate> coordinateSet = coordinateSet();

        return coordinateSet.stream()
                .map(coordinate -> points.get(coordinate.x()).get(coordinate.y()))
                .collect(Collectors.toList());
    }

    /**
     * @return the number of markers in the map
     */
    public final long collectionSize() {
        return collectionListToMarkerList(collectionList()).size();
    }

    /**
     * @return the number of markers in the map that fit a filter
     */
    public final long collectionSize(Predicate <? super T> filter){
        Objects.requireNonNull(filter);
        return collectionListToMarkerList(collectionList()).stream()
                .filter(filter)
                .count();
    }

    public String toString() {
        return collectionListToMarkerList(collectionList()).toString();
    }

    /**
     * Converts the list of all collections of markers into a list of those markers
     */
    private Collection<T> collectionListToMarkerList(List<Collection<T>> collectionList) {
        assert collectionList!=null;

        return collectionList.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    /**
     * Returns a new BiDimensionalMap containing the points in the rectangle.
     * The points along the bottom and left borders of the rectangle are included,
     * but not those along the top and right borders.
     */
    public final BiDimensionalMap<T> slice(Rectangle rectangle){
        rectangle.validate();
        BiDimensionalMap<T> mapSlice = new BiDimensionalMap<>();

        coordinateSet().stream()
                .filter(coordinate -> {
                    boolean xInRectangle = isWithinBounds(rectangle.left(), coordinate.x(), rectangle.right()) ;
                    boolean yInRectangle = isWithinBounds(rectangle.bottom(), coordinate.y(), rectangle.top());
                    return xInRectangle && yInRectangle;
                })
                .forEach(coordinate -> copyToOtherMap(mapSlice, coordinate));

        return mapSlice;
    }

    /**
     * @return left <= middle < right
     */
    private boolean isWithinBounds(BigDecimal leftIncludedBoundary, BigDecimal middle, BigDecimal rightExcludedBoundary) {
        return ( leftIncludedBoundary.compareTo(middle) <= 0 ) && ( middle.compareTo(rightExcludedBoundary) < 0 );
    }

    /**
     * Copies the values from points.get(coordinate) to the map
     */
    private void copyToOtherMap(BiDimensionalMap<T> otherMap, Coordinate coordinate) {
        Updater updater = otherMap.getUpdater();
        updater.setCoordinate(coordinate);

        for (T value : get(coordinate)){
            updater.addValue(value);
        }
        updater.add();
    }

    public Updater getUpdater(){
        return new Updater();
    }


    /**
     * Updater class used to add markers to the {@code BiDimensionalMap}
     * <p>To add one or more markers to a coordinate location:</p>
     * <p>  - Use {@code setCoordinate(), setX()}, or {@code setY()} to set what coordinate point the markers will be added to, default is (0, 0).</p>
     * <p>  - Use {@code addValue} to add markers to a list that will be put into the {@code BiDimensionalMap}   </p>
     *<p>   - Use {@code add()} to add that list of markers to the map at the specified location
     * or {@code set()} to replace the map's  values at that location with the list of markers </p>
     */
    public final class Updater{

        private BigDecimal x =  new BigDecimal(0);
        private BigDecimal y = new BigDecimal(0);


        public final Updater setCoordinate(Coordinate coordinate) {
            coordinate.validate();
            setX(coordinate.x());
            setY(coordinate.y());
            return this;
        }

        public final Updater setX(BigDecimal x){
            Objects.requireNonNull(x);
            this.x = x;
            return this;
        }

        public final Updater setY(BigDecimal y){
            Objects.requireNonNull(y);
            this.y = y;
            return this;
        }

        //Gives the initial instance of the collection stored at the (x,y) coordinates
        private Supplier<Collection<T>> collectionFactory = HashSet::new;

        //Stores the markers to be added to the Map with add() or set()
        private Collection<T> values = collectionFactory.get();

        /**add a marker to values. Need to use add() or set() to add to the BiDimensionalMap
         *
         * @param value The (non-null) marker to be added a list of values
         * @return this Updater
         */
        public final Updater addValue(T value){
            Objects.requireNonNull(value);
            values.add(value);
            return this;
        }

        /**Replace the markers at (x, y) in the {@code BiDimensionalMap} with the markers added with {@code addValue(T value)}
         * @return the previous Collection at the (x, y) location in the {@code BiDimensionalMap}
         * if one exists, otherwise returns null
         */
        public final Collection<T> set(){
            Collection<T> previousValues = null;
            if (collectionExistsAt(x, y)) {
                previousValues = collectionFactory.get();
                previousValues.addAll(get(x, y));
            } else {
                addCollection(x, y);
            }
            get(x,y).clear();
            get(x,y).addAll(values);
            return previousValues;
        }

        /**Add the markers at (x, y) in the {@code BiDimensionalMap} with the markers added with {@code addValue(T value)}.
         * Adds an empty map if no values have been added.
         * @return true if the markers at (x, y) in the {@code BiDimensionalMap} changed because of this call
         */
        public final boolean add() {
            if (!collectionExistsAt(x, y)){
                addCollection(x, y);
            }
            if (values.isEmpty()) {
                return false;
            } else {
                return get(x,y).addAll(values);
            }
        }

        /**Adds a new collection to the BiDimensionalMap at (x, y) if none exists there.
         Can safely add markers to points.get(x).get(y) or get(x,y) after running this method.
         Used in the Updater and in the private BiDimensionalMap constructor.
         */
        private void addCollection(BigDecimal x, BigDecimal y){
            validate(x, y);

            if (!points.containsKey(x)){
                //add the map that will store all of the y maps
                SortedMap<BigDecimal, Collection<T> >  newMap = new TreeMap<>();
                points.put(x, newMap);
            } else {
                //Map exists at x! so do nothing
                //can move on to checking for a y at this location
            }
            if (!collectionExistsAt(x, y)) {
                points.get(x).put(y, collectionFactory.get());
            } else {
                //Collection exists at (x, y)! do nothing
            }
        }
    }

}