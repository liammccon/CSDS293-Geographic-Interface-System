package gis;

import java.util.Collection;
import java.util.List;

/**
 * Stores InterestPoints in a BiDimensionalMap.
 * To use, create a {@code Builder}, then {@code add( interestPoint)} to the builder,
 * then put it in the  BiDimensionalMap with {@code build()}.
 */
public final class InterestPoints {

    private final BiDimensionalMap<InterestPoint> points;

    private InterestPoints(Builder builder) {
        assert(builder!=null);
        //todo deletes all the points when you do this?! or u just keep same builder?
        this.points = builder.points;
    }

    /**
     * @return the interest points at the given coordinate, or null if none exists there
     */
    public final Collection<InterestPoint> get (Coordinate coordinate) {
        //deleteme returns the interest points at the given valid coordinate, and deals with the error if the coordinate is invalid.
        coordinate.validate(); //todo deal with this with try catch? And should it throw error if no point exists
        return points.get(coordinate);
    }

    public final List<Collection<InterestPoint>> interestPoints(){
        //todo test
        return points.collectionList();
    }

    public String toString(){
        return points.toString();
    }

    public static class Builder {

        //todo why?!
        private final BiDimensionalMap<InterestPoint> points = new BiDimensionalMap<>();

        /**
         * Adds an interestPoint to the Builder.
         * Create an InterestPoints instance with {@code build()} after adding one or more points.
         * @param interestPoint: The InterestPoint to be added.
         * @return false if interestPoint is not valid (is or contains a null pointer)
         * or true if successfully added
         */
        public final boolean add(InterestPoint interestPoint) {
            //todo: is that what he meant by handled? and test and doc
            try {
                interestPoint.validate();
            } catch (NullPointerException e) {
                return false;
            }

            BiDimensionalMap.Updater updater = points.getUpdater();
            updater.setCoordinate(interestPoint.coordinate());
            updater.addValue(interestPoint);
            updater.add();
            return true;
        }

        /**
         * @return new InterestPoints instance with any points previously added.
         */
        public final InterestPoints build(){
            //todo this right?
            return new InterestPoints(this);
        }
    }
}
