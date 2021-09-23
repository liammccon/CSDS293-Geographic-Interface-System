package gis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Stores InterestPoints in a BiDimensionalMap.
 * To use, create a {@code Builder}, then {@code add( interestPoint)} to the builder,
 * then put it in the  BiDimensionalMap with {@code build()}.
 */
public final class InterestPoints <M extends Enum<M>> {

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

    /**
     * @return the number of interest points within the non-overlapping region
     * with the given marker
     */
    public final long count(RectilinearRegion region, M marker){
        if (region.isOverlapping()) throw new IllegalArgumentException("Region can not be overlapping");

        Collection<InterestPoint> filteredPoints = getFilteredPoints(marker);

        long count = 0;
        for (InterestPoint point : filteredPoints) {

            if (regionContainsCoordinate(region, point.coordinate())){
                //true if the point.coordinate() appears in the region
                count ++;
            }
        }
        return count;
    }

    private Collection<InterestPoint> getFilteredPoints(M marker) {
        Collection<InterestPoint> filteredPoints = new ArrayList<>();

        for (Collection<InterestPoint> pointCollection : points.collectionList()) {
            filteredPoints.addAll(pointCollection.stream()
                    .filter(interestPoint -> interestPoint.hasMarker(marker))
                    .collect(Collectors.toList())
            );
        }
        return filteredPoints;
    }

    private boolean regionContainsCoordinate(RectilinearRegion region, Coordinate coordinate) {
        for (Rectangle rectangle : region.getRectangles()) {
            //Checks each rectangle slice of points for the given coordinate
            if (points.slice(rectangle).coordinateSet().contains(coordinate)){
                return true;
            } else {
                //keep checking!
            }
        }
        //Coordinate does not exist in the rectangle set
        return false;
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
