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
 * @param <T> the type for the items stored at each coordinate point.
 */
public class BiDimensionalMap<T> {

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
        return get(coordinate.x(), coordinate.y()); //validation handled by first get method
    }

    private void validateBigDecimal(BigDecimal d){ //todo this specific enough?
        if (d == null) {
            throw new NullPointerException("Coordinate component can not be null");
        }
    }

    private boolean collectionExistAtX(BigDecimal x){
        return points.containsKey(x);
    }

    private boolean collectionExistsAtXY(BigDecimal x, BigDecimal y){
        if (collectionExistAtX(x)){
            return points.get(x).containsKey(y);
        } else {
            return false;
        }
    }

    public Updater getUpdater(){
        return new Updater();
    }

    public final class Updater{

        private BigDecimal x =  new BigDecimal(0);
        private BigDecimal y = new BigDecimal(0);

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


        //supplies the initial instance of the collection stored at the (x,y) coordinates todo change text?
        private Supplier<Collection<T>> collectionFactory = HashSet::new;
        public final Updater setCollectionFactory(Supplier<Collection<T>> collectionFactory){
            if (collectionFactory == null) {//todo this a good check?
                throw new NullPointerException("CollectionFactory can not be null");
            } else {
                this.collectionFactory = collectionFactory;
                return this;
            }
        }

        private Collection<T> values = collectionFactory.get();//todo is this right?
        public final Updater setValues(Collection<T> values){
            if (values == null){
                throw new NullPointerException("Values can not be null");
            } else {
                this.values = values;
                return this;
            }
        }

        public final Updater setCoordinate(Coordinate coordinate) {
            Coordinate.validate(coordinate);
            setX(coordinate.x());
            setY(coordinate.y());
            return this;
        }

        public final Updater addValue(T value){
            if (value == null){
                throw new NullPointerException("Value can not be null");
            } else {
                values.add(value);
                return this;
            }
        }


        public final Collection<T> set(){
            //todo what do i gotta do with the factory?! what checks do i do? whats this doing
            HashSet<T> previousValues = null;
            if (collectionExistsAtXY(x, y)) {
                previousValues = new HashSet<>(get(x, y));
            } else {
                addCollection();
            }
            get(x,y).clear();
            //todo assert values!= null?
            get(x,y).addAll(values);
            return previousValues;
        }

        public final boolean add() {
            //if no interestPoints at get(x,y)
            if (!collectionExistsAtXY(x, y)){//todo not needed, but looks nicer
                addCollection();
            }
            //todo assert values != null?
            if (values.isEmpty()) {
                return false;
            } else {
                get(x,y).addAll(values);
                return true;
            }
        }


        private Collection<T> addCollection(){
            if (!collectionExistAtX(x)){
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
                //Collection exists at (x, y)! so do nothing
                //todo throw some kinda error because they should check first?
            }
            return get(x,y);
        }

    }

}
