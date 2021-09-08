package gis;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Supplier;

/**
 * A 2D map used to store information about landmarks in a geographical area.
 * <p>Add landmarks with the {@code Updater}</p>
 * @param <T> the type for markers stored at each coordinate point.
 */
public class BiDimensionalMap <T> {

    /*Markers stored in a Sorted Map of Sorted Maps
      Works by accessing the first sorted map with the x value as key ( with points.get(x)) to
      get a map of all collections of markers ( with points.get(x).get(y) ) at y locations for that x */
    private final SortedMap<BigDecimal, SortedMap<BigDecimal, Collection<T> > > points = new TreeMap<>();

    public final Collection<T> get(BigDecimal x, BigDecimal y){
        validateBigDecimal(x);
        validateBigDecimal(y);
        if (collectionExistsAtXY(x, y)){
            return points.get(x).get(y);
        }
        else {
            throw new NullPointerException("No collection exists at coordinates (x = " + x + ", y = " + y);
        }
    }

    public final Collection<T> get(Coordinate coordinate) {
        return get(coordinate.x(), coordinate.y());
    }


    private void validateBigDecimal(BigDecimal d){
        if (d == null) {
            throw new NullPointerException("Coordinate component can not be null");
        }
    }

    /*True if points has a SortedMap at x (that can store a collection of markers at a certain y coordinate)
    no markers need to exist to return true, just the Map */
    private boolean mapExistAtX(BigDecimal x){
        return points.containsKey(x);
    }

    /*True if points has a SortedMap at x and a Collection at that SortedMap's y location
    no markers need to exist to return true, just the Map
    if true, markers can be safely added to points.get(x).get(y) */
    private boolean collectionExistsAtXY(BigDecimal x, BigDecimal y){
        if (mapExistAtX(x)){
            return points.get(x).containsKey(y);
        } else {
            return false;
        }
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
            Coordinate.validate(coordinate);
            setX(coordinate.x());
            setY(coordinate.y());
            return this;
        }
        public final Updater setX(BigDecimal x){
            validateBigDecimal(x);
            this.x = x;
            return this;
        }
        public final Updater setY(BigDecimal y){
            validateBigDecimal(y);
            this.y = y;
            return this;
        }


        //Gives the initial instance of the collection stored at the (x,y) coordinates
        private Supplier<Collection<T>> collectionFactory = HashSet::new;

        public final Updater setCollectionFactory(Supplier<Collection<T>> collectionFactory){
            if (collectionFactory == null) {
                throw new NullPointerException("CollectionFactory can not be null");
            } else {
                this.collectionFactory = collectionFactory;
                return this;
            }
        }

        /*Stores the markers to be added to the Map with add() or set()
          Can not delete markers from values, instead make a new Updater*/
        private Collection<T> values = collectionFactory.get();

        public final Updater setValues(Collection<T> values){
            if (values == null){
                throw new NullPointerException("Values can not be null");
            } else {
                this.values = values;
                return this;
            }
        }

        //add a marker to values. Need to use add() or set() to add to the BiDimensionalMap
        public final Updater addValue(T value){
            if (value == null){
                throw new NullPointerException("Value can not be null");
            } else {
                values.add(value);
                return this;
            }
        }

        /**Replace the markers at (x, y) in the {@code BiDimensionalMap} with the markers added with {@code addValue(T value)}
         * @return the previous Collection at the (x, y) location in the {@code BiDimensionalMap}
         * if one exists, otherwise returns null
         */
        public final Collection<T> set(){
            HashSet<T> previousValues = null;
            if (collectionExistsAtXY(x, y)) {
                previousValues = new HashSet<>(get(x, y));
            } else {
                addCollectionToMapAtXY();
            }
            get(x,y).clear();
            get(x,y).addAll(values);
            return previousValues;
        }

        /**Add the markers at (x, y) in the {@code BiDimensionalMap} with the markers added with {@code addValue(T value)}
         * @return true if the markers at (x, y) in the {@code BiDimensionalMap} changed because of this call
         */
        public final boolean add() {
            if (!collectionExistsAtXY(x, y)){
                addCollectionToMapAtXY();
            }
            if (values.isEmpty()) {
                return false;
            } else {
                get(x,y).addAll(values);
                return true;
            }
        }

        /*Adds a Collection <T> to the BiDimensionalMap at (x, y) if none exists.
          Can safely add markers to points.get(x).get(y) or get(x,y) after running this method.
        */
        private Collection<T> addCollectionToMapAtXY(){
            //This method will do nothing if Collection already exists at (x, y) in the map
            //So check collectionExistsAtXY() before calling this method
            assert (!collectionExistsAtXY(x, y));

            if (!mapExistAtX(x)){
                //add the map that will store all of the y maps
                SortedMap<BigDecimal, Collection<T> >  newMap = new TreeMap<>();
                points.put(x, newMap);
            } else {
                //Map exists at x! so do nothing
                //can move on to checking for a y at this location
            }
            if (!collectionExistsAtXY(x, y)) {
                Supplier<Collection<T>> newCollectionFactory = HashSet::new;
                points.get(x).put(y, newCollectionFactory.get());
            } else {
                //Collection exists at (x, y)! do nothing
            }
            return get(x,y);
        }

    }

}