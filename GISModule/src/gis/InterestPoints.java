package gis;

import java.util.Collection;
import java.util.List;

/**
 * Stores InterestPoints in a BiDimensionalMap.
 * To use, create a {@code Builder}, then {@code add( interestPoint)} to the builder,
 * then put it in the  BiDimensionalMap with {@code build()}.
 */
public final class InterestPoints<M> {
    //todo added <M> here. Should i add it everywhere I use InterestPoint?

    private final BiDimensionalMap<InterestPoint> points;

    private InterestPoints(Builder builder) {
        assert(builder!=null);
        this.points = builder.points;
    }

    /**
     * @return the interest points at the given coordinate, or null if none exists there
     */
    public final Collection<InterestPoint> get (Coordinate coordinate) {
        coordinate.validate();
        return points.get(coordinate);
    }

    public final List<Collection<InterestPoint>> interestPoints(){
        return points.collectionList();
    }

    public final long count(RectilinearRegion region, M marker){
        //todo!!
        return 0;
    }

    public String toString(){
        return points.toString();
    }

    public static class Builder {

        private final BiDimensionalMap<InterestPoint> points = new BiDimensionalMap<>();

        /**
         * Adds an interestPoint to the Builder.
         * Create an InterestPoints instance with {@code build()} after adding one or more points.
         * @param interestPoint: The InterestPoint to be added.
         * @return false if interestPoint is not valid (is or contains a null pointer)
         * or true if successfully added
         */
        public final boolean add(InterestPoint interestPoint) {
            try {
                interestPoint.validate();
            } catch (NullPointerException e) {
                return false;
            }

            BiDimensionalMap.Updater updater = points.getUpdater();
            updater.setCoordinate(interestPoint.coordinate());
            updater.addValue(interestPoint);
            return updater.add();
        }

        /**
         * @return new InterestPoints instance with any points previously added.
         */
        public final InterestPoints build(){
            return new InterestPoints(this);
        }
    }
}
